package org.wch.commons;



import org.wch.commons.callableInterface.FileReaderCallable;
import org.wch.commons.callback.Mother;
import org.wch.commons.callback.Son;
import org.wch.commons.io.FileUtils;
import org.wch.commons.lang.ConvertUtils2;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @Description: TODO
 * @Author: wuchu
 * @CreateTime: 2022-07-06 17:38
 */
public class MyTest {


    public static void main(String[] args) {
        /*  Integer one = Integer.valueOf("333222");
        System.out.println("result is " +Byte.valueOf(one.toString()) );*/
        testReadFile();
        testWriterFile();
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
