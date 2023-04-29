package org.wch.commons;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.wch.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Description: 文件压缩工具 其他参考：<a href="https://blog.csdn.net/tianynnb/article/details/121194687">java创建文件夹的4种方法及其优缺点（io基础）</a>
 * @Author: wuchu
 * @CreateTime: 2022-09-21 14:09
 */
public class ZipUtils {

//    private static final Logger log = LoggerFactory.getLogger(ZipUtils.class);

    /**
     * 压缩文件(自动关闭输出流)
     *
     * @param sourcePath
     * @param targetPath
     */
    public static Path zip(String sourcePath, String targetPath) {
        return zip(sourcePath, targetPath, true);
    }

    public static Path zip(String sourcePath, String targetPath, boolean containRootFolder) {
        final File file = new File(sourcePath);
        final File targetFile = new File(targetPath);
        Path result = null;
        if (Files.isDirectory(targetFile.toPath())) {
            try {
                final Path directories = Files.createDirectories(targetFile.toPath());
                result = directories.resolve(RandomUtils.uuid() + ".zip");
            } catch (IOException e) {
//                log.error("targetFile create error,{}", e.getMessage(), e);
                e.printStackTrace();
            }
        } else {
            result = targetFile.toPath();
        }
        if (Objects.isNull(result)) {
            return null;
        }

        try (final ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(result.toFile().toPath()))) {
            zip(file.toPath(), containRootFolder ? file.getName() : "", zipOutputStream);
        } catch (IOException e) {
            e.printStackTrace();

        }
        return result;
    }

    public static void zip(String sourcePath, ZipOutputStream zipOutputStream) {
        final File file = new File(sourcePath);
        zip(file.toPath(), file.getName(), zipOutputStream);
    }

    public static void zip(String sourcePath, ZipOutputStream zipOutputStream, boolean containRootFolder) {
        final File file = new File(sourcePath);
        zip(file.toPath(), containRootFolder ? file.getName() : "", zipOutputStream);
    }

    public static void zip(Path sourcePath, ZipOutputStream zipOutputStream) {
        zip(sourcePath, sourcePath.getFileName().toString(), zipOutputStream);
    }

    public static void zip(Path sourcePath, ZipOutputStream zipOutputStream, boolean containRootFolder) {
        zip(sourcePath, containRootFolder ? sourcePath.getFileName().toString() : "", zipOutputStream);
    }

    public static void upZip(String sourceZipPath, String targetFolderPath) {
        final Optional<String> optional = StringUtils.appendIfMissing(targetFolderPath, File.separator);
        if (!optional.isPresent()) {
//            log.error("targetFolderPath is not present");
            return;
        }
        // 开始解压
        try (ZipFile zipFile = new ZipFile(sourceZipPath)) {
            Enumeration<?> entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = (ZipArchiveEntry) entries.nextElement();
                final String dirPath = optional.get() + "/" + entry.getName();
                // 如果是文件夹，就创建个文件夹
                if (entry.isDirectory()) {
                    // 创建文件夹及其父文件夹
                    Files.createDirectories(new File(dirPath).toPath());
                } else {
                    // 如果是文件，就先创建一个文件，然后用io流把内容copy过去
                    File targetFile = new File(dirPath);
                    // 保证这个文件的父文件夹必须要存在
                    final Path directories = Files.createDirectories(targetFile.getParentFile().toPath());
                    final Path targetPath = directories.resolve(targetFile.getName());
                    Files.copy(zipFile.getInputStream(entry), targetPath);
                }
            }
        } catch (Exception e) {
//            log.error("unzip error from ZipUtils，", e);
            throw new RuntimeException("unzip error from ZipUtils," + e.getMessage());
        }
    }

    public static void unZip(Path sourceZipPath, Path targetPath) {
        // 开始解压
        try (ZipFile zipFile = new ZipFile(sourceZipPath.toFile())) {
            Enumeration<?> entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = (ZipArchiveEntry) entries.nextElement();
                // 如果是文件夹，就创建个文件夹
                final Path directories = Files.createDirectories(targetPath.getParent());
                final Path resolve = directories.resolve(entry.getName());
                if (entry.isDirectory()) {
                    Files.createDirectories(resolve);
                } else {
                    // 如果是文件，就先创建一个文件，然后用io流把内容copy过去
                    // 保证这个文件的父文件夹必须要存在
                    Files.copy(zipFile.getInputStream(entry), resolve);
                }
            }
        } catch (Exception e) {
//            log.error("unzip error from ZipUtils，", e);
            throw new RuntimeException("unzip error from ZipUtils," + e.getMessage());
        }
    }
/*
    // 其他方法
    public static boolean deleteFolderAndFiles(String source) {
        Path path = Paths.get(source);
        try (Stream<Path> walk = Files.walk(path)) {
            final List<Path> collect = walk.sorted(Comparator.reverseOrder())
                    .collect(Collectors.toList());
            for (Path path1 : collect) {
                Files.delete(path1);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }*/


    // region
    private static void zip(Path source, String fileName, ZipOutputStream zipOutputStream) {
        try {
            if (Files.isDirectory(source)) {
                final Optional<String> optional = StringUtils.appendIfMissing(fileName, File.separator);
                if (!optional.isPresent()) {
                    return;
                }
                try (Stream<Path> walk = Files.list(source)) {
                    walk.forEach(path -> zip(path, optional.get() + path.getFileName(), zipOutputStream));
                }
            } else {
                final ZipEntry zipEntry = new ZipEntry(fileName);
                zipOutputStream.putNextEntry(zipEntry);
                Files.copy(source, zipOutputStream);
                zipOutputStream.closeEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
