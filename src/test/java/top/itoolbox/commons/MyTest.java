package top.itoolbox.commons;


import top.itoolbox.commons.callableInterface.FileReaderCallable;
import top.itoolbox.commons.callback.Mother;
import top.itoolbox.commons.callback.Son;
import top.itoolbox.commons.io.FileUtils;
import top.itoolbox.commons.lang.ConvertUtils2;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Description: TODO
 * @Author: wuchu
 * @CreateTime: 2022-07-06 17:38
 */
public class MyTest {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int aNum = 0;
        int bNum = 0;
        int cNum = 0;
        int dNum = 0;
        int eNum = 0;
        int errIpNum = 0;
        int pNum = 0;
        // 注意 hasNext 和 hasNextLine 的区别
        while (in.hasNextLine()) { // 注意 while 处理多个 case
            String temp = in.nextLine();
            String[] ipAddr = temp.split("~");
            int ipFirst = getIpSeq(ipAddr[0], 0);
            int ipSecond = getIpSeq(ipAddr[0], 1);
            if (ipFirst == 0 || ipFirst == 127) {
                continue;
            }
            if (!isIpValid(ipAddr[0])) {
                errIpNum++;
                continue;
            }
            if (!isMaskValid(ipAddr[1])) {
                errIpNum++;
                continue;
            }
            if (ipFirst >= 1 && ipFirst <= 126) {
                aNum++;
            }
            if (ipFirst >= 128 && ipFirst <= 191) {
                bNum++;
            } else if (ipFirst >= 192 && ipFirst <= 223) {
                cNum++;
            } else if (ipFirst >= 224 && ipFirst <= 239) {
                dNum++;
            } else if (ipFirst >= 240 && ipFirst <= 255) {
                eNum++;
            }
            if (ipFirst == 10 || (ipFirst == 172 && ipSecond >= 16 &&
                    ipSecond <= 31) || (ipFirst == 192 && ipSecond == 168)) {
                pNum++;
            }



        }
        System.out.println(aNum + " " + bNum + " " + cNum + " " + dNum + " " + eNum +
                " " + errIpNum + " " + pNum);

        Map<Integer, Integer> temp = new TreeMap<>();

    }

    public static boolean isMaskValid(String maskIp) {
        String[] maskArr = maskIp.split("\\.");
        if (maskArr.length != 4) {
            return false;
        }
        String maskBinary = toBinary(maskArr[0]) + toBinary(maskArr[1]) + toBinary(
                maskArr[2]) + toBinary(maskArr[3]);
        if (!maskBinary.matches("[1]{1,}[0]{1,}")) {
            return false;
        }
        return true;
    }

    public static String toBinary(String num) {
        String numBinary = Integer.toBinaryString(Integer.valueOf(num));
        while (numBinary.length() < 8) {
            numBinary = "0" + numBinary;
        }
        return numBinary;
    }

    public static boolean isIpValid(String ip) {
        String[] temp = ip.split("\\.");
        if (temp.length != 4) {
            return false;
        }
        if (Integer.valueOf(temp[0]) > 255 || Integer.valueOf(temp[1]) > 255 ||
                Integer.valueOf(temp[2]) > 255 || Integer.valueOf(temp[3]) > 255) {
            return false;
        }
        return true;
    }

    public static int getIpSeq(String ip, int idx) {
        String[] ips = ip.split("\\.");
        final String ip1 = ips[idx];
        if(Objects.isNull(ip1) || "".equalsIgnoreCase(ip1)){
            return -1;
        }
        return Integer.valueOf(ip1);
    }

    public static void test(String[] args) {
        String temp = "232332323";
        if (temp.length() <= 8) {
            System.out.println(temp);
        } else {
            int size = temp.length();
            StringBuffer buf = new StringBuffer();
            for (int i = 0; i < size; i++) {
                int remain = i % 8;
                if (i > 0 && remain == 0) {
                    buf.append("\n");
                }
                buf.append(temp.charAt(i));
                final int i1 = (i + 1) % 8;
                if (i >= (size - 1) && i1 > 0) {
                    for (int j = 0; j < (8-i1); j++) {
                        buf.append("0");
                    }
                }
            }
            System.out.println(buf.toString());

        }

        String a= "0xAA";
        System.out.println(Integer.parseInt(a.substring(2),16));
//        System.out.println("result is " + count);
        /*  Integer one = Integer.valueOf("333222");
        System.out.println("result is " +Byte.valueOf(one.toString()) );*/
//        testReadFile();
//        testWriterFile();
    }


    private static void testWriterFile() {
        String path = "E:/安装sublimeText4之后的操作.txt";
        String path1 = "E:/安装sublimeText4之后的操作1.txt";
        FileUtils.writeByOneLineStep(path, path1, t -> t + " this is add value");
    }

    private static void testReadFile() {
        String path = "E:/安装sublimeText4之后的操作.txt";
        Optional<String> s = FileUtils.readFile(path);
        s.ifPresent(s1 -> {
            System.out.println("result is " + s1);
        });
        FileUtils.readFileByOneLineStep(path, t -> {
            System.out.println("t--->" + t);
        });
    }

    private static void callBackTest() {
        Son jack = new Son();
        Mother mother = new Mother(jack);
        mother.parting();
        testCallBack("执行", t -> {
            System.out.println("t " + t);
        });
    }

    private static <T> void testCallBack(String param, FileReaderCallable fileReaderCallable) {
        System.out.println("执行同步回调开始。。。。。");
        for (int i = 0; i < 3; i++) {
            System.out.println("循环中,第" + i + "次调用");
            fileReaderCallable.call(param + i);
        }
        System.out.println("执行同步回调结束。。。。。");
    }

    private static void testConvert() {
        String intStr = "222";
        Optional<Integer> convert = ConvertUtils2.convertIfNecessary(intStr, Integer.class);
        System.out.println("result is " + convert.orElse(0));
        Optional<Double> doubleValue = new MyTest().testMain(Byte.class, Double.class, "doubleValue", "20");
        System.out.println("final result is :" + doubleValue.orElse(null));
        byte b = 20;
        Optional<BigDecimal> aDouble = ConvertUtils2.convertIfNecessary(b, BigDecimal.class);
        System.out.println("final result1 is :" + aDouble.orElse(null));
        Map<String, Object> temp = new HashMap<>();
        temp.put("userName", "zhangSan");
        temp.put("password", "zhangSan1233");
        temp.put("age", 33);
        Optional<Person> person = ConvertUtils2.convertIfNecessary(temp, Person.class);
        System.out.println("final result2 is :" + person.orElse(null));

        Person person1 = new Person();
        person1.setAge(50);
        person1.setUserName("liWu");
        Optional<Person> person2 = ConvertUtils2.convertIfNecessary(person1, Person.class);
        System.out.println("final result3 is :" + person2.orElse(null));

    }

    public <T> Optional<T> testMain(Class<?> tClass, Class<T> requiredType, String parseFunction, Object value) {
        try {
            Method declaredMethod = tClass.getDeclaredMethod("valueOf", String.class);
            Object[] args = {value.toString()};
            Optional<Object> instance = getInstance(tClass);
            Object invoke = declaredMethod.invoke(instance.orElse(null), args);
            Method parseMethod = tClass.getDeclaredMethod(parseFunction);
            Object invoke1 = parseMethod.invoke(invoke);
            return Optional.of(requiredType.cast(invoke1));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private <T> Optional<Object> getInstance(Class<T> clazz) {
        try {
            Object instance = clazz.newInstance();
            return Optional.of(instance);
        } catch (InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
            return Optional.empty();
        }
    }
}
