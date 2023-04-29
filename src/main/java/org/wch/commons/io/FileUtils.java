package org.wch.commons.io;


import org.wch.commons.callableInterface.FileReaderCallable;
import org.wch.commons.callableInterface.FileWriterCallable;
import org.wch.commons.lang.ObjectUtils;
import org.wch.commons.lang.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @Description: TODO
 * @Author: wuchu
 * @CreateTime: 2022-07-07 14:11
 */
public class FileUtils {

//    private static final Logger log = LoggerFactory.getLogger(FileUtils.class);

    private final String resourceRelativePath;

    public FileUtils(String resourceRelativePath) {
        this.resourceRelativePath = resourceRelativePath;
    }

    /**
     * 读取文件
     *
     * @param filePath
     * @return
     */
    public static Optional<String> readFile(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return Optional.empty();
        }
        try (FileInputStream in = new FileInputStream(filePath);) {
            return readFile(in);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * 读取文件
     *
     * @param file
     * @return
     */
    public static Optional<String> readFile(File file) {
        if (ObjectUtils.isNull(file)) {
            return Optional.empty();
        }

        try (FileInputStream in = new FileInputStream(file);) {
            return readFile(in);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * 读取文件
     *
     * @param inputStream
     * @return
     */
    public static Optional<String> readFile(InputStream inputStream) {
        if (ObjectUtils.isNull(inputStream)) {
            return Optional.empty();
        }
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder stringBuffer = new StringBuilder();
            String line;
            while (ObjectUtils.nonNull(line = bufferedReader.readLine())) {
                stringBuffer.append(line).append(StringUtils.LF);
            }
//            log.debug("文件读取完成...");
            return Optional.of(stringBuffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * 逐行读取文件
     *
     * @param filePath
     * @param fileReaderCallable
     */
    public static void readFileByOneLineStep(String filePath, FileReaderCallable fileReaderCallable) {
        if (ObjectUtils.anyNull(filePath, fileReaderCallable)) {
            return;
        }
        try (FileInputStream in = new FileInputStream(filePath);) {
            readFileByOneLineStep(in, fileReaderCallable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 逐行读取文件
     *
     * @param file
     * @param fileReaderCallable
     */
    public static void readFileByOneLineStep(File file, FileReaderCallable fileReaderCallable) {
        if (ObjectUtils.anyNull(file, fileReaderCallable)) {
            return;
        }

        try (FileInputStream in = new FileInputStream(file);) {
            readFileByOneLineStep(in, fileReaderCallable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // todo
    public static void readFileByOneLineStep(InputStream inputStream, FileReaderCallable fileReaderCallable) {
        if (ObjectUtils.anyNull(inputStream, fileReaderCallable)) {
            return;
        }
        // todo
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream), 10240)) {
            String line;
            while (ObjectUtils.nonNull(line = bufferedReader.readLine())) {
                fileReaderCallable.call(line);
            }
//            log.debug("文件读取完成...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * TODO Checks if any value in the given array is {@code null}.
     *
     * <p>
     * If any of the values are {@code null} or the array is {@code null},
     * then {@code true} is returned, otherwise {@code false} is returned.
     * </p>
     *
     * <pre>
     * ObjectUtils.anyNull(*)             = false
     * </pre>
     *
     * @param filePath the values to test, may be {@code null} or empty
     * @param content  the values to test, may be {@code null} or empty
     * @return {@code true} if there is at least one {@code null} value in the array,
     * {@code false} if all the values are non-null.
     * If the array is {@code null} or empty, {@code true} is also returned.
     */
    public static boolean write(String filePath, String content) {
        if (StringUtils.anyBlank(filePath, content)) {
            return false;
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write(content);
            bw.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //------------------------------------大文件逐行写入-------------------------------------
    public static void writeByOneLineStep(String sourceFilePath, String targetFilePath, FileWriterCallable fileWriterCallable) {
        if (ObjectUtils.anyNull(sourceFilePath, targetFilePath, fileWriterCallable)) {
            return;
        }
        try (FileInputStream in = new FileInputStream(sourceFilePath);
             FileOutputStream out = new FileOutputStream(targetFilePath);) {
            writeByOneLineStep(in, out, fileWriterCallable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeByOneLineStep(String sourceFilePath, File fileOut, FileWriterCallable fileWriterCallable) {
        if (ObjectUtils.anyNull(sourceFilePath, fileOut, fileWriterCallable)) {
            return;
        }
        try (FileInputStream in = new FileInputStream(sourceFilePath);) {
            writeByOneLineStep(in, fileOut, fileWriterCallable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeByOneLineStep(String sourceFilePath, OutputStream outputStream, FileWriterCallable fileWriterCallable) {
        if (ObjectUtils.anyNull(sourceFilePath, outputStream, fileWriterCallable)) {
            return;
        }
        try (FileInputStream in = new FileInputStream(sourceFilePath);) {
            writeByOneLineStep(in, outputStream, fileWriterCallable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeByOneLineStep(File source, String targetFilePath, FileWriterCallable fileWriterCallable) {
        if (ObjectUtils.anyNull(source, targetFilePath, fileWriterCallable)) {
            return;
        }
        try (FileInputStream in = new FileInputStream(source);
             FileOutputStream out = new FileOutputStream(targetFilePath);) {
            writeByOneLineStep(in, out, fileWriterCallable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeByOneLineStep(File source, File target, FileWriterCallable fileWriterCallable) {
        if (ObjectUtils.anyNull(source, target, fileWriterCallable)) {
            return;
        }
        try (FileInputStream in = new FileInputStream(source);) {
            writeByOneLineStep(in, target, fileWriterCallable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeByOneLineStep(File source, OutputStream outputStream, FileWriterCallable fileWriterCallable) {
        if (ObjectUtils.anyNull(source, outputStream, fileWriterCallable)) {
            return;
        }
        try (FileInputStream in = new FileInputStream(source);) {
            writeByOneLineStep(in, outputStream, fileWriterCallable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeByOneLineStep(InputStream inputStream, String targetFilePath, FileWriterCallable fileWriterCallable) {
        if (ObjectUtils.anyNull(inputStream, targetFilePath, fileWriterCallable)) {
            return;
        }
        try (FileOutputStream out = new FileOutputStream(targetFilePath);) {
            writeByOneLineStep(inputStream, out, fileWriterCallable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeByOneLineStep(InputStream inputStream, File target, FileWriterCallable fileWriterCallable) {
        if (ObjectUtils.anyNull(inputStream, target, fileWriterCallable)) {
            return;
        }
        try (FileOutputStream out = new FileOutputStream(target);) {
            writeByOneLineStep(inputStream, out, fileWriterCallable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeByOneLineStep(InputStream source, OutputStream target, FileWriterCallable fileWriterCallable) {
        if (ObjectUtils.anyNull(source, target, fileWriterCallable)) {
            return;
        }
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(source));
             BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(target))) {
//            log.debug("文件写入开始...");
            String line;
            while (ObjectUtils.nonNull(line = bufferedReader.readLine())) {
                Object call = fileWriterCallable.call(line);
                bufferedWriter.write(call.toString());
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
//            log.debug("文件写入完成...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copyFolder(String sourceFolder, String destinationFolder) {
        Path sourcePath = Paths.get(sourceFolder);
        Path destinationPath = Paths.get(destinationFolder);
        copyFolder(sourcePath, destinationPath);
    }

    /**
     * 复制源文件夹到目标文件夹
     *
     * @param sourcePath
     * @param destinationPath
     */
    public static void copyFolder(Path sourcePath, Path destinationPath) {
        try {
            Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    Path targetPath = destinationPath.resolve(sourcePath.relativize(dir));
                    Files.copy(dir, targetPath, StandardCopyOption.COPY_ATTRIBUTES);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Path targetPath = destinationPath.resolve(sourcePath.relativize(file));
                    Files.copy(file, targetPath, StandardCopyOption.COPY_ATTRIBUTES);
                    return FileVisitResult.CONTINUE;
                }
            });
            System.out.println("Folder copied successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除文件夹及其子文件
     *
     * @param file
     */
    public static void deleteFolder(File file) {
        try (Stream<Path> walk = Files.walk(file.toPath())) {
            walk.sorted(Comparator.reverseOrder()).forEach(item -> {
                try {
                    Files.delete(item);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteFolder(String folder) throws IOException {
        Files.walkFileTree(Paths.get(folder),
                new SimpleFileVisitor<Path>() {
                    // 先去遍历删除文件
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.delete(file);
//                        System.out.printf("文件被删除 : %s%n", file);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        Files.delete(dir);
//                        System.out.printf("文件夹被删除: %s%n", dir);
                        return FileVisitResult.CONTINUE;
                    }

                }
        );
        return true;
    }

    public static void copy(InputStream source, String targetPath) {
        if (ObjectUtils.anyNull(source, targetPath)) {
            return;
        }
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(source));
             FileOutputStream out = new FileOutputStream(targetPath);
             BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(out))) {
//            log.debug("文件写入开始...");
            String line;
            while (ObjectUtils.nonNull(line = bufferedReader.readLine())) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
//            log.debug("文件写入完成...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 输入流转输出流
     *
     * @param source
     * @param target
     */
    public static void copy(InputStream source, OutputStream target) {
        if (ObjectUtils.anyNull(source, target)) {
            return;
        }
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(source));
             BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(target))) {
//            log.debug("文件写入开始...");
            String line;
            while (ObjectUtils.nonNull(line = bufferedReader.readLine())) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
//            log.debug("文件写入完成...");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Optional<InputStream> covert(String source) {
        if (StringUtils.isBlank(source)) {
            return Optional.empty();
        }
        return Optional.of(new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8)));
    }

    public static Optional<String> createDirsIfNotExists(String dirPath) {
        if (StringUtils.isBlank(dirPath)) {
            return Optional.empty();
        }
        try {
            Path path = Paths.get(dirPath);
            if (Files.isDirectory(path)) {
                Files.createDirectories(path);
                return Optional.of(dirPath);
            } else {
                Path parent = path.getParent();
                Files.createDirectories(parent);
                return Optional.of(parent.toAbsolutePath().toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] copyToByteArray(InputStream inputStream, Integer size) {
        if (ObjectUtils.isNull(inputStream)) {
            return new byte[0];
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream(ObjectUtils.isNull(size) ? 4096 : size);
        copy(inputStream, out);
        return out.toByteArray();
    }

    public static byte[] copyToByteArray(InputStream inputStream) {
        return copyToByteArray(inputStream, null);
    }

    public static FileUtils of(String resourceRelativePath) {
        return new FileUtils(resourceRelativePath);
    }

    public Reader readResourceFile() {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(resourceRelativePath);
        byte[] bytes = copyToByteArray(resourceAsStream);
        return new InputStreamReader(new ByteArrayInputStream(bytes));
    }
}
