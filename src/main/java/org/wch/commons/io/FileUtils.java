package org.wch.commons.io;


import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.wch.commons.callableInterface.FileReaderCallable;
import org.wch.commons.callableInterface.FileWriterCallable;
import org.wch.commons.lang.ObjectUtils;
import org.wch.commons.lang.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * @Description: TODO
 * @Author: wuchu
 * @CreateTime: 2022-07-07 14:11
 */
public class FileUtils {
    
    private static final Logger log = LoggerFactory.getLogger(FileUtils.class);

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
            log.debug("文件读取完成...");
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
            log.debug("文件读取完成...");
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
            log.debug("文件写入开始...");
            String line;
            while (ObjectUtils.nonNull(line = bufferedReader.readLine())) {
                Object call = fileWriterCallable.call(line);
                bufferedWriter.write(call.toString());
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
            log.debug("文件写入完成...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 拷贝文件
     *
     * @param source
     * @param targetPath
     */
    public static void copy(InputStream source, String targetPath) {
        if (ObjectUtils.anyNull(source, targetPath)) {
            return;
        }
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(source));
             FileOutputStream out = new FileOutputStream(targetPath);
             BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(out))) {
            log.debug("文件写入开始...");
            String line;
            while (ObjectUtils.nonNull(line = bufferedReader.readLine())) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
            log.debug("文件写入完成...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * TODO 存在同文件覆盖风险
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
            log.debug("文件写入开始...");
            String line;
            while (ObjectUtils.nonNull(line = bufferedReader.readLine())) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
            log.debug("文件写入完成...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 拷贝文件夹及其子文件夹
     *
     * @param sourceFolder 源文件夹
     * @param targetFolder 目标文件夹
     * @throws Exception
     */
    public static void copyFolder(String sourceFolder, String targetFolder) throws Exception {

        File sourceFile = new File(sourceFolder);
        if (!sourceFile.exists()) {
            throw new Exception("源目标路径：[" + sourceFolder + "] 不存在...");
        }
        File targetFile = new File(targetFolder);
        if (!targetFile.exists()) {
            boolean mkdirs = targetFile.mkdirs();
            if (!mkdirs) {
                throw new Exception("存放的目标路径：[" + targetFolder + "] 不存在...");
            } else {
                log.debug("存放的目标路径：[" + targetFolder + "] 已创建...");
            }
        }

        // 获取源文件夹下的文件夹或文件
        File[] sourceFiles = sourceFile.listFiles();
        assert sourceFiles != null;
        for (File file : sourceFiles) {
            String targetFilePath = targetFolder + file.getName();
            if (file.isFile()) {
                copy(new FileInputStream(file), new FileOutputStream(targetFilePath));
            }
            if (file.isDirectory()) {
                copyFolder(file.getAbsolutePath() + File.separator, targetFilePath + File.separator);
            }
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
        File file = new File(dirPath);
        if (file.isFile()) {
            try {
                if (!file.exists()) {
                    boolean mkdirs = file.createNewFile();
                }
                return Optional.of(file.getParent());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (!file.exists()) {
                boolean mkdirs = file.mkdirs();
            }
            return Optional.of(file.getPath());
        }
        return Optional.empty();
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
