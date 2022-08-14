package org.wch.commons.io;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.wch.commons.EncryptUtils;

import java.util.Optional;

/**
 * @Description: TODO 新增20220805
 * @Author: wuchu
 * @CreateTime: 2022-08-05 14:38
 */
@RunWith(JUnit4.class)
public class EncryptUtilsTest {

    @Test
    public void test() {
        String test = "adfasdfasfwqeadfasdgasdadfasdfasdf";
        Optional<String> s = EncryptUtils.encryptBy(test, EncryptUtils.DigestType.SHA1);
        System.out.println("result is " + s.orElse(null));
    }

    @Test
    public void testOne() {
        String sourceStr = "adfasdfasfwqeadfasdgasdadfasdfasdf";
        String sourceStr1 = "adfasdfasfwqeadfasdgasdadfasdfasdf1";
        Optional<String> s = EncryptUtils.of(24).encryptByMD5WithSalt(sourceStr);
        if (s.isPresent()) {
            boolean equals = EncryptUtils.of(24).validPasswordByMD5(sourceStr1, s.get());
            System.out.println("result is " + equals);
        } else {
            System.out.println("result is false, because of empty data");
        }
    }


    @Test
    public void test2() {
        String test = "adfasdfasfwqeadfasdgasdadfasdfasdf";
//        Optional<String> s = EncryptUtils.e(test);
//        System.out.println("result is " + s.orElse(null));
    }

    @Test
    public void test3() {
        String test = "adfasdfasfwqeadfasdgasdadfasdfasdf";
//        EncryptUtils.encryptByAES(test);
//        System.out.println("result is " + s.orElse(null));
    }

    @Test
    public void test5() {
        String test = "adfasdfasfwqeadfasdgasdadfasdfasdf";
        final Optional<String> secretKey = EncryptUtils.generateSecretKey(EncryptUtils.DigestType.DES_EDE);
        if(!secretKey.isPresent()){
            return;
        }
        final Optional<String> optional = EncryptUtils.encryptBy3DES(test, secretKey.get());
        if(!optional.isPresent()){
            return;
        }
        System.out.println("encrypt result is " + optional.get());
        final Optional<String> optional1 = EncryptUtils.decryptBy3DES(optional.get(), secretKey.get());
        System.out.println("decrypt result is "+ optional1.orElse(null));
    }

    @Test
    public void test6() {
        String test = "adfasdfasfwqeadfasdgasdadfasdfasdf";
        final Optional<String> secretKey = EncryptUtils.generateSecretKey(EncryptUtils.DigestType.AES_ECB);
        if(!secretKey.isPresent()){
            return;
        }
//        final String key = Base64.getEncoder().encodeToString(secretKey.get());
//        System.out.println("Algorithm() is " + secretKey.get().getAlgorithm());
//        System.out.println("format is " + secretKey.get().getFormat());
//        final String key = new String(secretKey.get().getEncoded(), StandardCharsets.UTF_8);
        final Optional<String> optional = EncryptUtils.encryptByAES(test, secretKey.get());
        if(!optional.isPresent()){
            return;
        }
        System.out.println("encrypt result is " + optional.get());
        final Optional<String> optional1 = EncryptUtils.decryptByAES(optional.get(), secretKey.get());
        System.out.println("decrypt result is "+ optional1.orElse(null));
//        EncryptUtils.encryptByAES(test);
//        System.out.println("result is " + s.orElse(null));
    }

    @Test
    public void test4() {
        String content = "你好,我很喜欢加密算法";
//
//        long timeStart = System.currentTimeMillis();
//        final Optional<SecretKey> secretKey = EncryptUtils.generateKey();
//        if (!secretKey.isPresent()) {
//            return;
//        }
//        final Optional<String> optional = EncryptUtils.encryptByAES(content, secretKey.get());
//        if (!optional.isPresent()) {
//            return;
//        }
//        long timeEnd = System.currentTimeMillis();
//        System.out.println("加密后的结果为：" + optional.orElse(null));
//        final Optional<String> optional1 = EncryptUtils.decryptByAES1(optional.get(), secretKey.get());
//        if (optional1.isPresent()) {
//
//            System.out.println("解密后的结果为：" + optional1.orElse(null));
//        }
//        System.out.println("加密用时：" + (timeEnd - timeStart));

    }

}
