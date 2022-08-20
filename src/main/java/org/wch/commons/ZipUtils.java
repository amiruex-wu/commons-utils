package org.wch.commons;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.wch.commons.lang.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件或文件夹压缩工具，
 * 这个工具类的功能为：
 * （1）可以压缩文件，也可以压缩文件夹
 * （2）同时支持压缩多级文件夹，工具内部做了递归处理
 * （3）碰到空的文件夹，也可以压缩
 * （4）可以选择是否保留原来的目录结构，如果不保留，所有文件跑压缩包根目录去了，且空文件夹直接舍弃。注意：如果不保留文件原来目录结构，在碰到文件名相同的文件时，会压缩失败。
 * （5）代码中提供了2个压缩文件的方法，一个的输入参数为文件夹路径，一个为文件列表，可根据实际需求选择方法。
 */
public class ZipUtils {

    private static final int BUFFER_SIZE = 2 * 1024;

    private static final String SUFFIX = ".zip";

    /**
     * 压缩成ZIP 方法1（根据参数是否保留文件节目录结构）
     *
     * @param srcDir           压缩文件夹路径
     * @param out              压缩文件输出流
     * @param KeepDirStructure 是否保留原来的目录结构,true:保留目录结构;
     *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws RuntimeException 压缩失败会抛出运行时异常
     */
    public static void toZip(String srcDir, OutputStream out, boolean KeepDirStructure) {
        long start = System.currentTimeMillis();
        try (ZipOutputStream zos = new ZipOutputStream(out);) {
            File sourceFile = new File(srcDir);
            compress(sourceFile, zos, sourceFile.getName(), KeepDirStructure);
            long end = System.currentTimeMillis();
            System.out.println("压缩完成，耗时：" + (end - start) + " ms");
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils", e);
        }
    }

    /**
     * 压缩成ZIP 方法1（根据参数是否保留文件节目录结构）
     *
     * @param srcDir           压缩文件夹路径
     * @param targetDir        压缩文件输出路径
     * @param KeepDirStructure 是否保留原来的目录结构,true:保留目录结构;
     *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws RuntimeException 压缩失败会抛出运行时异常
     */
    public static void toZip(String srcDir, String targetDir, String fileName, boolean KeepDirStructure) {
        if (StringUtils.anyBlank(srcDir, targetDir, fileName)) {
            return;
        }

        long start = System.currentTimeMillis();

        String filePath = targetDir + File.separator + fileName + SUFFIX;
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(filePath));) {

            File sourceFile = new File(srcDir);
            compress(sourceFile, zos, sourceFile.getName(), KeepDirStructure);
            long end = System.currentTimeMillis();
            System.out.println("压缩完成，耗时：" + (end - start) + " ms");
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils", e);
        }
    }

    /**
     * 压缩成ZIP 方法2(不含层级)
     *
     * @param srcFiles 需要压缩的文件列表
     * @param out      压缩文件输出流
     * @throws RuntimeException 压缩失败会抛出运行时异常
     */
    public static void toZip(List<File> srcFiles, OutputStream out) {
        try (ZipOutputStream zos = new ZipOutputStream(out)) {
            compressFiles(srcFiles, zos);
        } catch (IOException e) {
            throw new RuntimeException("zip error from ZipUtils", e);
        }
    }

    /**
     * 压缩成ZIP 方法2(不含层级)
     *
     * @param srcFiles   需要压缩的文件列表
     * @param targetPath 压缩文件输出路径
     * @throws RuntimeException 压缩失败会抛出运行时异常
     */
    public static void toZip(List<File> srcFiles, String targetPath) {
        long start = System.currentTimeMillis();
        try (FileOutputStream fileOutputStream = new FileOutputStream(targetPath)) {
            toZip(srcFiles, fileOutputStream);
            long end = System.currentTimeMillis();
            System.out.println("压缩完成，耗时：" + (end - start) + " ms");
        } catch (IOException e) {
            throw new RuntimeException("zip error from ZipUtils", e);
        }
    }

    /**
     * 解压zip包至指定的文件夹下
     *
     * @param srcFile
     * @param destDirPath
     * @throws RuntimeException
     */
    public static void unZip(File srcFile, String destDirPath) {
        // 判断源文件是否存在
        if (!srcFile.exists()) {
            throw new RuntimeException("9999" + srcFile.getPath() + "所指文件不存在");
        }

        // 开始解压
        try (ZipFile zipFile = new ZipFile(srcFile)) {
            Enumeration<?> entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = (ZipArchiveEntry) entries.nextElement();
                // 如果是文件夹，就创建个文件夹
                if (entry.isDirectory()) {
                    String dirPath = destDirPath + "/" + entry.getName();
                    File dir = new File(dirPath);
                    dir.mkdirs();
                } else {
                    // 如果是文件，就先创建一个文件，然后用io流把内容copy过去
                    String targetFilePath = destDirPath + File.separator + entry.getName();
                    FileUtils.copy(zipFile.getInputStream(entry), targetFilePath);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("9999" + "unzip error from ZipUtils," + e.getMessage());
        }
    }

    /**
     * 解压文件获取实体
     *
     * @param srcFile
     * @return
     * @throws RuntimeException
     */
    public static List<ZipArchiveEntry> unZip(File srcFile) {
        // 判断源文件是否存在
        if (!srcFile.exists()) {
            throw new RuntimeException("9999" + srcFile.getPath() + "所指文件不存在");
        }
        List<ZipArchiveEntry> zipEntries = new ArrayList<>();
        // 开始解压
        try (ZipFile zipFile = new ZipFile(srcFile)) {
            Enumeration<?> entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = (ZipArchiveEntry) entries.nextElement();
                zipEntries.add(entry);
            }
        } catch (IOException e) {
            throw new RuntimeException("9999" + "unzip error from ZipUtils," + e.getMessage());
        }
        return zipEntries;
    }

    // reigon 私有方法

    private static void compressFiles(List<File> srcFiles, ZipOutputStream zos) throws IOException {
        for (File srcFile : srcFiles) {
            byte[] buf = new byte[BUFFER_SIZE];
            zos.putNextEntry(new ZipEntry(srcFile.getName()));
            int len;
            try (FileInputStream in = new FileInputStream(srcFile);) {
                while ((len = in.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
            }
        }
    }

    /**
     * 递归压缩方法
     *
     * @param sourceFile       源文件
     * @param zos              zip输出流
     * @param name             压缩后的名称
     * @param KeepDirStructure 是否保留原来的目录结构,true:保留目录结构;
     *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws IOException if file not exists
     */
    private static void compress(File sourceFile, ZipOutputStream zos, String name, boolean KeepDirStructure) throws IOException {
        byte[] buf = new byte[BUFFER_SIZE];
        if (sourceFile.isFile()) {
            try (FileInputStream in = new FileInputStream(sourceFile);) {
                // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
                zos.putNextEntry(new ZipEntry(name));
                // copy文件到zip输出流中
                int len;
                while ((len = in.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
                // Complete the entry
                zos.closeEntry();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if (KeepDirStructure) {
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                }
            } else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (KeepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, zos, name + "/" + file.getName(), KeepDirStructure);
                    } else {
                        compress(file, zos, file.getName(), KeepDirStructure);
                    }
                }
            }
        }
    }
}
