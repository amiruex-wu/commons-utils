package top.itoolbox.commons;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import top.itoolbox.commons.conversion.SimpleTypeConverter;
import top.itoolbox.commons.lang.ConvertUtils;
import top.itoolbox.commons.model.SampleBean;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

/**
 * @Description: TODO 新增20220805
 * @Author: wuchu
 * @CreateTime: 2022-08-05 15:52
 */
@RunWith(JUnit4.class)
public class ConvertUtilsTest {

    @Test
    public void test() {
//        final String src = "CDF1F0C10F12345678";
        final String src = "10";
        Optional<Byte[]> bytes = ConvertUtils.hexToByteArray(src);
        if(bytes.isPresent()){

            assertEquals((byte) 0x01, (byte)bytes.get()[0]);
        }
//        assertEquals(Optional.of((byte) 0x01), bytes.get()[0]);
//        assertEquals((byte) 0x0C, ConvertUtils11.hexToByte(src, 0, (byte) 0, 0, 1));
//        assertEquals((byte) 0xDC, ConvertUtils11.hexToByte(src, 0, (byte) 0, 0, 2));
//        assertEquals((byte) 0xFD, ConvertUtils11.hexToByte(src, 1, (byte) 0, 0, 2));
//        assertEquals((byte) 0x34, ConvertUtils11.hexToByte(src, 0, (byte) 0x34, 0, 0));
//        assertEquals((byte) 0x84, ConvertUtils11.hexToByte(src, 17, (byte) 0x34, 4, 1));
    }

    @Test
    public void test1() {
        int digit = Character.digit('Y', 16);
        System.out.println("result is " + digit);
     }
   @Test
    public void test3() {

        String str = "1211ADBBBDD";
       Optional<Byte[]> bytes = ConvertUtils.hexToByteArray(str);
       bytes.ifPresent(bytes1 -> {
           for (Byte aByte : bytes1) {
               System.out.println("result is " + aByte);
           }

           Optional<String> s = ConvertUtils.byteArrayToHex(bytes1);
           System.out.println("convert result is " + s.get());
       });
     }

    @Test
    public void test4() {
        int aByte = -83 & 0xfff;

//        final char c1 = Character.forDigit(aByte >> 4, 16);
//        final char c2 = Character.forDigit(aByte << 4, 16);
//        String c = "" +c1 + c2;
        String c = Integer.toHexString(aByte);
        System.out.println("result is " + c);
    }
    @Test
    public void test2() {
        char hexDigit = 'D';
        int digit = Character.digit(hexDigit, 16);
        System.out.println("result is " + digit);
        String hex = "ADDFFB01";
        Optional<Byte[]> result = ConvertUtils.hexToByteArray(hex);
        for (byte b : result.get()) {
            System.out.println("result 1 is:" + b);
        }

        byte[] bytes = hexStringToBytes(hex);
        for (byte aByte : bytes) {
            System.out.println("result 2 is:" + aByte);
        }

    }

    @Test
    public void test5(){
        AddressVO addressVO = new AddressVO();

        addressVO.setProvince("湖南省");
        addressVO.setCity("长沙市");
        addressVO.setDistrict("望城区xxxx" + 0 + "号");
        addressVO.setDetail("xxxx团结路幸福街" + 1 + "号");
        final SampleBean adadfadf = new SampleBean("adadfadf", 20, 25);
        addressVO.setSampleBean(adadfadf);
        final Optional<Address> address = SimpleTypeConverter.convertIfNecessary(addressVO, Address.class);
        address.ifPresent(address1 -> System.out.println("resutli si " + address1));
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || "".equals(hexString)) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
