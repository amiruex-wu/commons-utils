package org.wch.commons.io;


import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

//@Slf4j
@Deprecated
public class ZipUtils1 {
//    private static Logger log = LogManager.getLogger(ZipUtils.class);
    /**
     * 获取zip下文件名称文件类型
     *
     * @param srcFile
     * @return
     * @throws RuntimeException
     */
    public static List<String> unZipGetFileType(File srcFile) {
        // 判断源文件是否存在
        if (!srcFile.exists()) {
            throw new RuntimeException("9999"+ srcFile.getPath() + "所指文件不存在");
        }
        List<String> fileSuffix = new ArrayList<>();
        // 开始解压
        try (ZipFile zipFile = new ZipFile(srcFile)) {
            Enumeration<?> entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = (ZipArchiveEntry) entries.nextElement();
                String fileName = entry.getName();
                String suffix = fileName.substring(fileName.lastIndexOf('.') + 1);
                fileSuffix.add(suffix);
            }
        } catch (Exception e) {
            throw new RuntimeException("9999"+"unzip error from ZipUtils," + e.getMessage());
        }
        return fileSuffix;
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
            throw new RuntimeException("9999"+srcFile.getPath() + "所指文件不存在");
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
                    File targetFile = new File(destDirPath + "/" + entry.getName());
                    // 保证这个文件的父文件夹必须要存在
                    if (!targetFile.getParentFile().exists()) {
                        targetFile.getParentFile().mkdirs();
                    }
                    if (!targetFile.createNewFile()) {

                    }
                    // 将压缩文件内容写入到这个文件中
                    try (InputStream is = zipFile.getInputStream(entry);
                         FileOutputStream fos = new FileOutputStream(targetFile)) {
                        int len;
                        byte[] buf = new byte[1024];
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("9999"+"unzip error from ZipUtils," + e.getMessage());
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
        List<ZipArchiveEntry> zipEntries = new ArrayList<>();
        // 判断源文件是否存在
        if (!srcFile.exists()) {
            throw new RuntimeException("9999"+srcFile.getPath() + "所指文件不存在");
        }
        // 开始解压
        try (ZipFile zipFile = new ZipFile(srcFile)) {
            Enumeration<?> entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = (ZipArchiveEntry) entries.nextElement();
                zipEntries.add(entry);
            }
        } catch (Exception e) {
            throw new RuntimeException("9999"+"unzip error from ZipUtils," + e.getMessage());
        }
        return zipEntries;
    }
}
