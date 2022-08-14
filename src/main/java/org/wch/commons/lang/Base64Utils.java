package org.wch.commons.lang;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Utils {

    public Base64Utils() {
    }

    public static byte[] encode(byte[] src) {
        return ObjectUtils.isEmpty(src) ? src : Base64.getEncoder().encode(src);
    }

    public static byte[] decode(byte[] src) {
        return ObjectUtils.isEmpty(src) ? src : Base64.getDecoder().decode(src);
    }

    public static byte[] encodeUrlSafe(byte[] src) {
        return ObjectUtils.isEmpty(src) ? src : Base64.getUrlEncoder().encode(src);
    }

    public static byte[] decodeUrlSafe(byte[] src) {
        return ObjectUtils.isEmpty(src) ? src : Base64.getUrlDecoder().decode(src);
    }

    public static String encodeToString(byte[] src) {
        return ObjectUtils.isEmpty(src) ? "" : new String(encode(src), StandardCharsets.UTF_8);
    }

    public static byte[] decodeFromString(String src) {
        return StringUtils.isBlank(src) ? new byte[0] : decode(src.getBytes(StandardCharsets.UTF_8));
    }

    public static String encodeToUrlSafeString(byte[] src) {
        return new String(encodeUrlSafe(src), StandardCharsets.UTF_8);
    }

    public static byte[] decodeFromUrlSafeString(String src) {
        return decodeUrlSafe(src.getBytes(StandardCharsets.UTF_8));
    }

}
