package org.wch.commons.lang;

import lombok.extern.slf4j.Slf4j;
import org.wch.commons.conversion.SimpleTypeConverter;

import java.util.Optional;

// todo
public class ConvertUtils {

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static <T> Optional<T> convertIfNecessary(Object obj, Class<T> tClass) {
        if (ObjectUtils.anyNull(obj, tClass)) {
            return Optional.empty();
        }
        return SimpleTypeConverter.convertIfNecessary(obj, tClass);
    }

    /**
     * 十六进制字符转字节数组
     *
     * @param hexString
     * @return
     */
    public static Optional<Byte[]> hexToByteArray(final String hexString) {
        if (StringUtils.isBlank(hexString)) {
            return Optional.empty();
        }
        String tempStr;
        if (hexString.startsWith("0x") || hexString.startsWith("0X")) {
            tempStr = hexString.replaceAll("0x", "").replaceAll("0X", "");
        } else {
            tempStr = hexString;
        }
        char[] chars = tempStr.toUpperCase().toCharArray();
        int resultLength = chars.length / 2;
        Byte[] resultBytes = new Byte[resultLength];
        for (int i = 0; i < resultLength; i++) {
            int pos = i * 2;
            resultBytes[i] = (byte) (Character.digit(chars[pos], 16) << 4 | Character.digit(chars[pos + 1], 16));
        }
        return Optional.of(resultBytes);
    }

    /**
     * 十六进制字符转成字节
     *
     * @param hexString
     * @return
     */
    public static Optional<Byte> hexToByte(final String hexString) {
        Optional<Byte[]> bytes = hexToByteArray(hexString);
        return bytes.map(value -> value[0]);
    }


    /**
     * 字节数组转成十六进制字符
     *
     * @param bytes
     * @return
     */
    public static Optional<String> byteArrayToHex(Byte[] bytes) {
        if (ObjectUtils.isEmpty(bytes)) {
            return Optional.empty();
        }
        StringBuilder stringBuffer = new StringBuilder();
        for (byte aByte : bytes) {
            int v = aByte & 0xFF;
            String s = Integer.toHexString(v).toUpperCase();
            if (s.length() < 2) {
                stringBuffer.append(0);
            }
            stringBuffer.append(s);
        }
        return Optional.of(stringBuffer.toString());
    }

    /**
     * 字节数组转成十六进制字符
     *
     * @param bytes
     * @return
     */
    public static Optional<String> byteArrayToHex(byte[] bytes) {
        if (ObjectUtils.isEmpty(bytes)) {
            return Optional.empty();
        }
        // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））
        char[] resultCharArray = new char[bytes.length * 2];

        // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
        int index = 0;
        for (byte b : bytes) {
            resultCharArray[index++] = HEX_DIGITS[b >>> 4 & 0xf];
            resultCharArray[index++] = HEX_DIGITS[b & 0xf];
        }

        // 字符数组组合成字符串返回
        return Optional.of(new String(resultCharArray));
    }

    /**
     * <p>
     * Converts a hexadecimal digit into an int using the default (Lsb0) bit ordering.
     * </p>
     * <p>
     * '1' is converted to 1
     * </p>
     *
     * @param hexDigit the hexadecimal digit to convert
     * @return an int equals to {@code hexDigit}
     */
    public static Optional<Integer> hexDigitToInt(final char hexDigit) {
        final int digit = Character.digit(hexDigit, 16);
        if (digit < 0) {
            System.err.println("Cannot interpret '" + hexDigit + "' as a hexadecimal digit");
            return Optional.empty();
        }
        return Optional.of(digit);
    }
}
