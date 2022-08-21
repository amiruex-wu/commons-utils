package org.wch.commons;


import lombok.Data;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import org.wch.commons.beans.RSASecretKeyPair;
import org.wch.commons.lang.*;

import javax.crypto.*;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

/**
 * @Description: 加密工具
 * @Author: wuchu
 * @CreateTime: 2022-08-05 11:28
 */
@Data
public class EncryptUtils {

    private int saltLength = 24;

    public EncryptUtils() {
    }

    public static EncryptUtils of(Integer saltLength) {
        final EncryptUtils encryptUtils = new EncryptUtils();
        if (Objects.nonNull(saltLength)) {
            encryptUtils.setSaltLength(saltLength);
        }
        return encryptUtils;
    }

    public static Optional<String> encryptBy(String source, DigestType dataType) {
        if (ObjectUtils.anyNull(source, dataType)) {
            return Optional.empty();
        }
        Optional<String> result = Optional.empty();
        switch (dataType) {
            case MD5:
                result = encryptByMD5(source);
                break;
            case SHA1:
                result = encryptBySha1(source);
                break;
        }
        return result;
    }

    public static Optional<String> encryptFileBy(String source, DigestType dataType) {
        if (ObjectUtils.anyNull(source, dataType)) {
            return Optional.empty();
        }
        Optional<String> result = Optional.empty();
        switch (dataType) {
            case MD5:
                result = encryptFileByMD5(source);
                break;
            case SHA1:
                result = encryptFileBySha1(source);
                break;
        }
        return result;
    }

    public static Optional<String> encryptBy(InputStream inputStream, DigestType dataType) {
        if (ObjectUtils.anyNull(inputStream, dataType)) {
            return Optional.empty();
        }
        Optional<String> result = Optional.empty();
        switch (dataType) {
            case MD5:
                result = encryptByMD5(inputStream);
                break;
            case SHA1:
                result = encryptBySha1(inputStream);
                break;
        }
        return result;
    }

    //region 非对称加密

    /**
     * 生成兼容ssl的秘钥对（pem类型）
     *
     * @param size 秘钥长度
     * @return 秘钥对
     */
    public static Optional<RSASecretKeyPair> generateKeyPairPem(Integer size) {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance(DigestType.RSA.getName());
            kpg.initialize(Objects.nonNull(size) ? size : 1024);
            KeyPair keyPair = kpg.generateKeyPair();

            String publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
            String privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
            String pem = privatePem(privateKey);
            return Optional.of(new RSASecretKeyPair(publicKey, pem));
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * 生成RSA密钥对（私钥不进行PEM转换）
     *
     * @param size 秘钥长度
     * @return 秘钥对
     */
    public static Optional<RSASecretKeyPair> generateKeyPair(Integer size) {

        try {
            // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(DigestType.RSA.getName());
            // 初始化密钥对生成器，密钥大小为96-1024位
            keyPairGen.initialize(Objects.nonNull(size) ? size : 1024, new SecureRandom());
            // 生成一个密钥对，保存在keyPair中
            KeyPair keyPair = keyPairGen.generateKeyPair();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
            String publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());
            // 得到私钥字符串
            String privateKeyString = Base64.getEncoder().encodeToString((privateKey.getEncoded()));

            final RSASecretKeyPair rsaSecretKeyPair = new RSASecretKeyPair();
            rsaSecretKeyPair.setPrivateKeyStr(privateKeyString)
                    .setPublicKeyStr(publicKeyString);
            return Optional.of(rsaSecretKeyPair);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * RSA公钥加密(非ssl pem格式)
     *
     * @param str       加密字符串
     * @param publicKey 公钥
     * @return 密文
     */
    public static Optional<String> encryptByPublicKey(String str, String publicKey) {
        if (StringUtils.anyBlank(str, publicKey)) {
            return Optional.empty();
        }
        try {
            // base64编码的公钥
            byte[] decoded = Base64.getDecoder().decode(publicKey);
            RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance(DigestType.RSA.getName())
                    .generatePublic(new X509EncodedKeySpec(decoded));
            // RSA加密
            Cipher cipher = Cipher.getInstance(DigestType.RSA.getName());
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            String outStr = Base64.getEncoder().encodeToString(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
            return Optional.of(outStr);

        } catch (InvalidKeySpecException | NoSuchAlgorithmException | NoSuchPaddingException
                | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * RSA私钥解密(非ssl pem格式)
     *
     * @param str        加密字符串
     * @param privateKey 私钥
     * @return 明文
     */
    public static Optional<String> decryptByPrivateKey(String str, String privateKey) {
        if (StringUtils.anyBlank(str, privateKey)) {
            return Optional.empty();
        }
        // 64位解码加密后的字符串
        byte[] inputByte = Base64.getDecoder().decode(str.getBytes(StandardCharsets.UTF_8));
        // base64编码的私钥
        byte[] decoded = Base64.getDecoder().decode(privateKey);
        try {
            RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance(DigestType.RSA.getName())
                    .generatePrivate(new PKCS8EncodedKeySpec(decoded));
            // RSA解密
            Cipher cipher = Cipher.getInstance(DigestType.RSA.getName());
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            String outStr = new String(cipher.doFinal(inputByte));
            return Optional.of(outStr);
        } catch (InvalidKeySpecException | InvalidKeyException | NoSuchAlgorithmException
                | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }


    //endregion

    // region 对称加密
    // ------------------------------------------- 对称加密 --------------------------------------------------

    /**
     * 生成AES/DES秘钥
     *
     * @param digestType
     * @param size
     * @return
     */
    public static Optional<SecretKey> generateSecretKey(DigestType digestType, Integer size) {
        // 返回生成指定算法密钥的KeyGenerator对象
        try {
            final List<DigestType> digestTypes = Arrays.asList(DigestType.AES_ECB, DigestType.DES_EDE);
            if (!digestTypes.contains(digestType)) {
                return Optional.empty();
            }
            KeyGenerator kgen = KeyGenerator.getInstance(digestType.getName());

            Integer sizeTemp = null;
            // 初始化此密钥生成器,使其具有确定的密钥大小
            switch (digestType) {
                case AES_ECB:
                    final boolean contains = Arrays.asList(128, 192, 256).contains(size);
                    sizeTemp = contains ? size : 128;
                    break;
                case DES_EDE:
                    final boolean contains1 = Arrays.asList(112, 168).contains(size);
                    sizeTemp = contains1 ? size : 168;
                    break;
            }
            if (ObjectUtils.isNull(sizeTemp)) {
                return Optional.empty();
            }
            kgen.init(sizeTemp, new SecureRandom());
            // 生成一个密钥
            SecretKey secretKey = kgen.generateKey();
            SecretKey resultKey = null;
            switch (digestType) {
                case AES_ECB:
                    // 获取密钥
                    resultKey = new SecretKeySpec(secretKey.getEncoded(), DigestType.AES_ECB.getName());
                    break;
                case DES_EDE:
                    // 实例化DES密钥规则
                    DESedeKeySpec desKeySpec = new DESedeKeySpec(secretKey.getEncoded());
                    // 实例化密钥工厂
                    SecretKeyFactory factory = SecretKeyFactory.getInstance(DigestType.DES_EDE.getName());
                    // 生成密钥
                    resultKey = factory.generateSecret(desKeySpec);
                    break;
                default:
                    break;
            }

            return Optional.ofNullable(resultKey);
        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * AES或DESede生成加密秘钥
     *
     * @param digestType 秘钥类型
     * @return 经过base64编码后的的字符串
     */
    public static Optional<String> generateSecretKey(DigestType digestType) {
        try {
            final List<DigestType> digestTypes = Arrays.asList(DigestType.AES_ECB, DigestType.DES_EDE);
            if (!digestTypes.contains(digestType)) {
                return Optional.empty();
            }
            // 返回生成指定算法密钥的KeyGenerator对象
            KeyGenerator kgen = KeyGenerator.getInstance(digestType.getName());

            // 初始化此密钥生成器,使其具有确定的密钥大小
//            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
//            random.setSeed(key.getBytes());
            switch (digestType) {
                case AES_ECB:
                    kgen.init(128, new SecureRandom());
                    break;
                case DES_EDE:
                    kgen.init(168, new SecureRandom());
                    break;
            }

            //生成一个密钥
            SecretKey secretKey = kgen.generateKey();
            SecretKey resultKey = new SecretKeySpec(secretKey.getEncoded(), digestType.getName());

            String resultKeyString = Base64.getEncoder().encodeToString(resultKey.getEncoded());
            return Optional.ofNullable(resultKeyString);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * AES加密（ECB填充模式）
     *
     * @param source 待加密明文
     * @param key    base64编码后的秘钥
     * @return
     */
    public static Optional<String> encryptByAES(String source, String key) {
        if (StringUtils.anyBlank(source, key)) {
            return Optional.empty();
        }
        try {
            // 默认模式
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            final byte[] bytes = Base64.getDecoder().decode(key);
            SecretKey secretKey = new SecretKeySpec(bytes, DigestType.AES_ECB.getName());
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            final byte[] encryptBytes = cipher.doFinal(source.getBytes(StandardCharsets.UTF_8));
            return Optional.of(Base64.getEncoder().encodeToString(encryptBytes));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * TODO AES加密（CBC填充模式）
     *
     * @param source 明文
     * @param key    base64编码的秘钥
     * @return base64编码的密文
     */
    public static Optional<String> encryptByAESCBC(String source, String key) {
        if (StringUtils.anyBlank(source, key)) {
            return Optional.empty();
        }
        try {
            // 默认模式
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            final byte[] bytes = Base64.getDecoder().decode(key);
            SecretKey secretKey = new SecretKeySpec(bytes, DigestType.AES_ECB.getName());
            IvParameterSpec iv = new IvParameterSpec(key.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            final byte[] encryptBytes = cipher.doFinal(source.getBytes(StandardCharsets.UTF_8));
            return Optional.of(Base64.getEncoder().encodeToString(encryptBytes));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | BadPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }


    /**
     * AES解密（ECB填充模式）
     *
     * @param source 加密字符串
     * @param key    base64编码后的秘钥
     * @return 明文
     */
    public static Optional<String> decryptByAES(String source, String key) {
        if (StringUtils.anyBlank(source, key)) {
            return Optional.empty();
        }
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            final byte[] keyBytes = Base64.getDecoder().decode(key);
            SecretKey secretKey = new SecretKeySpec(keyBytes, DigestType.AES_ECB.getName());
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            final byte[] decodeBytes = Base64.getDecoder().decode(source);
            final byte[] resultBytes = cipher.doFinal(decodeBytes);
            return Optional.of(new String(resultBytes, StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * TODO AES解密（CBC填充模式）
     *
     * @param source base64编码的密文
     * @param key    base64编码的秘钥
     * @return 明文
     */
    public static Optional<String> decryptByAESCBC(String source, String key) {
        if (StringUtils.anyBlank(source, key)) {
            return Optional.empty();
        }
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            final byte[] keyBytes = Base64.getDecoder().decode(key);
            SecretKey secretKey = new SecretKeySpec(keyBytes, DigestType.AES_ECB.getName());
            IvParameterSpec iv = new IvParameterSpec(key.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            final byte[] decodeBytes = Base64.getDecoder().decode(source);
            final byte[] resultBytes = cipher.doFinal(decodeBytes);
            return Optional.of(new String(resultBytes, StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | BadPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * 3DES加密，DES加密的升级版
     *
     * @param source 明文
     * @param key    base64编码的秘钥
     * @return 返回base64编码的密文
     */
    public static Optional<String> encryptBy3DES(String source, String key) {
        if (StringUtils.anyBlank(source, key)) {
            return Optional.empty();
        }
        try {
            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            final byte[] decodeKeyBytes = Base64.getDecoder().decode(key);
            final DESedeKeySpec deSedeKeySpec = new DESedeKeySpec(decodeKeyBytes);
            SecretKey secretKey = SecretKeyFactory.getInstance(DigestType.DES_EDE.getName()).generateSecret(deSedeKeySpec);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            final byte[] encryptBytes = cipher.doFinal(source.getBytes(StandardCharsets.UTF_8));
            final String value = Base64.getEncoder().encodeToString(encryptBytes);
            return Optional.of(value);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | BadPaddingException
                | IllegalBlockSizeException | InvalidKeyException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * 3DES解密密，DES加密的升级版
     *
     * @param source base64编码过的密文
     * @param key    base64编码的秘钥
     * @return 返回解密后的明文
     */
    public static Optional<String> decryptBy3DES(String source, String key) {
        if (StringUtils.anyBlank(source, key)) {
            return Optional.empty();
        }
        try {
            final byte[] sourceDecodeBytes = Base64.getDecoder().decode(source);
            final byte[] decodeKeyBytes = Base64.getDecoder().decode(key);
            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            final DESedeKeySpec deSedeKeySpec = new DESedeKeySpec(decodeKeyBytes);
            SecretKey secretKey = SecretKeyFactory.getInstance(DigestType.DES_EDE.getName()).generateSecret(deSedeKeySpec);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            final byte[] resultBytes = cipher.doFinal(sourceDecodeBytes);
            return Optional.of(new String(resultBytes, StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | BadPaddingException | IllegalBlockSizeException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // endregion

    // region sha1加密
    // --------------------------------- sha1加密 ----------------------------------

    /**
     * SHA1加密（不可逆）
     *
     * @param source 明文
     * @return 密文
     */
    public static Optional<String> encryptBySha1(String source) {
        if (StringUtils.isBlank(source)) {
            return Optional.empty();
        }
        // 拿到一个MD5转换器（如果想要SHA1参数换成”SHA1”）
        Optional<MessageDigest> messageDigest = getMessageDigest(DigestType.SHA1);
        return messageDigest.flatMap(digest -> getEncryptString(source, digest));
    }

    /**
     * SHA1加密（不可逆）
     *
     * @param inputStream 明文输入流
     * @return 密文
     */
    public static Optional<String> encryptBySha1(InputStream inputStream) {
        if (ObjectUtils.isNull(inputStream)) {
            return Optional.empty();
        }
        // 拿到一个MD5转换器（如果想要SHA1参数换成”SHA1”）
        Optional<MessageDigest> messageDigest = getMessageDigest(DigestType.SHA1);
        return messageDigest.flatMap(digest -> getEncryptFileString(inputStream, digest));
    }

    /**
     * SHA1加密（不可逆）
     *
     * @param sourcePath 文件路径
     * @return 密文
     */
    public static Optional<String> encryptFileBySha1(String sourcePath) {
        if (StringUtils.isBlank(sourcePath)) {
            return Optional.empty();
        }
        // 拿到一个MD5转换器（如果想要SHA1参数换成”SHA1”）
        Optional<MessageDigest> messageDigest = getMessageDigest(DigestType.SHA1);
        return messageDigest.flatMap(digest -> {
            try {
                return getEncryptFileString(new FileInputStream(sourcePath), digest);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return Optional.empty();
            }
        });
    }

    // endregion

    // region MD5加密

    /**
     * MD5加密
     *
     * @param source 明文
     * @return 密文
     */
    public static Optional<String> encryptByMD5(String source) {
        if (StringUtils.isBlank(source)) {
            return Optional.empty();
        }
        // 拿到一个MD5转换器（如果想要SHA1参数换成”SHA1”）
        Optional<MessageDigest> messageDigest = getMessageDigest(DigestType.MD5);
        return messageDigest.flatMap(digest -> getEncryptString(source, digest));
    }

    /**
     * MD5加密
     *
     * @param inputStream 明文输入流
     * @return 密文
     */
    public static Optional<String> encryptByMD5(InputStream inputStream) {
        // 拿到一个MD5转换器（同样，这里可以换成SHA1）
        Optional<MessageDigest> messageDigest = getMessageDigest(DigestType.MD5);
        if (!messageDigest.isPresent()) {
            return Optional.empty();
        }
        return getEncryptFileString(inputStream, messageDigest.get());
    }

    /**
     * MD5加密文件
     *
     * @param sourceFilePath 文件路径
     * @return 密文
     */
    public static Optional<String> encryptFileByMD5(String sourceFilePath) {
        if (ObjectUtils.isNull(sourceFilePath)) {
            return Optional.empty();
        }

        try {
            return encryptByMD5(new FileInputStream(sourceFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // MD5加盐处理

    /**
     * MD5加盐处理
     *
     * @param source 明文
     * @return 密文
     */
    public Optional<String> encryptByMD5WithSalt(String source) {
        if (StringUtils.isBlank(source)) {
            return Optional.empty();
        }

        // 拿到一个MD5转换器（如果想要SHA1参数换成”SHA1”）
        Optional<MessageDigest> messageDigest = getMessageDigest(DigestType.MD5);
        if (!messageDigest.isPresent()) {
            return Optional.empty();
        }

        // 随机数生成器
        SecureRandom random = new SecureRandom();
        // 声明盐数组变量
        byte[] salt = new byte[saltLength];
        //将随机数放入盐变量中
        random.nextBytes(salt);
        //将盐数据传入消息摘要对象
        messageDigest.get().update(salt);
        messageDigest.get().update(source.getBytes(StandardCharsets.UTF_8));
        // 转换并返回结果，也是字节数组，包含16个元素
        byte[] digest = messageDigest.get().digest();

        // 声明加密后的口令数组变量
        //因为要在口令的字节数组中存放盐，所以加上盐的字节长度
        byte[] pwd = new byte[digest.length + saltLength];
        //将盐的字节拷贝到生成的加密口令字节数组的前12个字节，以便在验证口令时取出盐
        System.arraycopy(salt, 0, pwd, 0, saltLength);
        //将消息摘要拷贝到加密口令字节数组从第13个字节开始的字节
        System.arraycopy(digest, 0, pwd, saltLength, digest.length);

        // 字符数组转换成字符串返回
        return ConvertUtils.byteArrayToHex(pwd);
    }

    /**
     * 校验
     *
     * @param source          明文
     * @param encryptedTarget 目标密文
     * @return 返回是否
     */
    // 参考地址： https://blog.csdn.net/weixin_46822367/article/details/120743992
    // 其他参考资料：https://blog.csdn.net/qq_41437844/article/details/121120227
    public boolean validPasswordByMD5(String source, String encryptedTarget) {
        if (StringUtils.anyBlank(source, encryptedTarget)) {
            return false;
        }
        // 创建消息摘要对象
        Optional<MessageDigest> messageDigest = getMessageDigest(DigestType.MD5);
        if (!messageDigest.isPresent()) {
            return false;
        }
        // 将16进制字符串格式口令转换成字节数组
        final Optional<Byte[]> bytes = ConvertUtils.hexToByteArray(encryptedTarget);
        if (!bytes.isPresent()) {
            return false;
        }
        final byte[] targetBytes = ArrayUtils.toWrap(bytes.get());
        // 声明盐变量
        byte[] salt = new byte[saltLength];
        // 将盐从数据库中保存的口令字节数组中提取出来
        System.arraycopy(targetBytes, 0, salt, 0, saltLength);

        // 将盐数据传入消息摘要对象
        messageDigest.get().update(salt);
        // 将口令的数据传给消息摘要对象
        messageDigest.get().update(source.getBytes(StandardCharsets.UTF_8));
        // 生成输入口令的消息摘要
        byte[] digest = messageDigest.get().digest();

        // 声明一个保存数据库中口令消息摘要的变量
        byte[] digestInDb = new byte[targetBytes.length - saltLength];
        // 取得数据库中口令的消息摘要
        System.arraycopy(targetBytes, saltLength, digestInDb, 0, digestInDb.length);
        // 比较根据输入口令生成的消息摘要和数据库中消息摘要是否相同
        return Arrays.equals(digest, digestInDb);
    }

    // endregion

    // region 私有方法区

    private static String privatePem(String privateKey) throws IOException {
        byte[] privateKBytes = Base64.getDecoder().decode(privateKey);

        PrivateKeyInfo pkInfo = PrivateKeyInfo.getInstance(privateKBytes);
        ASN1Encodable encodable = pkInfo.parsePrivateKey();
        ASN1Primitive primitive = encodable.toASN1Primitive();
        byte[] privateKeyPKCS1 = primitive.getEncoded();

        return pkcs1ToPem(privateKeyPKCS1, false);
    }

    private static String pkcs1ToPem(byte[] pcks1KeyBytes, boolean isPublic) throws IOException {
        String type;
        if (isPublic) {
            type = "RSA PUBLIC KEY";
        } else {
            type = "RSA PRIVATE KEY";
        }

        PemObject pemObject = new PemObject(type, pcks1KeyBytes);
        StringWriter stringWriter = new StringWriter();
        PemWriter pemWriter = new PemWriter(stringWriter);
        pemWriter.writeObject(pemObject);
        pemWriter.close();

        return stringWriter.toString();
    }

    /* *//*
     * 将16进制字符串转换成字节数组
     *
     * @param hexStr
     * @return
     *//*
    public static byte[] hexStringToByte(String hexStr) {
        int len = (hexStr.length() / 2);
        byte[] result = new byte[len];
        char[] hexChars = hexStr.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (HEX_DIGITS[hexChars[pos]] << 4 | HEX_DIGITS[hexChars[pos + 1]]);
        }
        return result;
    }
*/

    /**
     * 获取字符串加密并转换结果
     *
     * @param source
     * @param messageDigest
     * @return
     */
    private static Optional<String> getEncryptString(String source, MessageDigest messageDigest) {
        // 输入的字符串转换成字节数组
        byte[] inputByteArray = source.getBytes(StandardCharsets.UTF_8);

        // inputByteArray是输入字符串转换得到的字节数组
        messageDigest.update(inputByteArray);

        // 转换并返回结果，也是字节数组，包含16个元素
        byte[] resultByteArray = messageDigest.digest();

        // 字符数组转换成字符串返回
        return ConvertUtils.byteArrayToHex(resultByteArray);
    }

    /**
     * 获取文件加密并转换结果
     *
     * @param inputStream   输入流
     * @param messageDigest 加密消息摘要
     * @return 密文
     */
    private static Optional<String> getEncryptFileString(InputStream inputStream, MessageDigest messageDigest) {
        int bufferSize = 256 * 1024;
        try (DigestInputStream digestInputStream = new DigestInputStream(inputStream, messageDigest)) {
            // read的过程中进行MD5处理，直到读完文件
            byte[] buffer = new byte[bufferSize];
            while (digestInputStream.read(buffer) > 0) ;
            // 拿到结果，也是字节数组，包含16个元素
            byte[] resultByteArray = messageDigest.digest();

            // 同样，把字节数组转换成字符串
            return ConvertUtils.byteArrayToHex(resultByteArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /*
     * 下面这个函数用于将字节数组换成成16进制的字符串
     *
     * @param byteArray
     * @return
     *//*
    @Deprecated
    private static String byteArrayToHex(byte[] byteArray) {

        // 首先初始化一个字符数组，用来存放每个16进制字符
//        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

        // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））
        char[] resultCharArray = new char[byteArray.length * 2];

        // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = HEX_DIGITS[b >>> 4 & 0xf];
            resultCharArray[index++] = HEX_DIGITS[b & 0xf];
        }

        // 字符数组组合成字符串返回
        return new String(resultCharArray);
    }*/

    /**
     * 获取摘要实体
     *
     * @param digestType
     * @return
     */
    private static Optional<MessageDigest> getMessageDigest(DigestType digestType) {
        if (ObjectUtils.isNull(digestType)) {
            return Optional.empty();
        }
        try {
            MessageDigest instance = MessageDigest.getInstance(digestType.name());
            return Optional.of(instance);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // endregion

    //region enum定义区域
    public enum DataType {
        GENERAL_STRING,
        FILE;
    }

    public enum DigestType {
        MD5(1, "MD5"),
        SHA1(2, "SHA1"),
        //        DES(3, "DES"),
        AES_ECB(4, "AES"),
        AES_CBC(5, "AES"),
        DES_EDE(6, "DESede"),
        RSA(7, "RSA"),
        ;

        DigestType(int code, String name) {
            this.code = code;
            this.name = name;
        }

        private int code;

        private String name;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
