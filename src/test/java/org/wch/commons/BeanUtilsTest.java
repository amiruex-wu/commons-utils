package org.wch.commons;

import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.beans.BeanGenerator;
import net.sf.cglib.beans.BeanMap;
import net.sf.cglib.core.Converter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.wch.commons.model.*;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: TODO
 * @Author: wuchu
 * @CreateTime: 2022-09-07 13:57
 */
@RunWith(JUnit4.class)
public class BeanUtilsTest {

    @Test
    public void test() {
        SampleBean sampleBean = new SampleBean("Hello world", 100, 61);
        OtherSampleBean otherSampleBean = new OtherSampleBean();
        BeanUtils.copyProperties(sampleBean, otherSampleBean);
        System.out.println("otherSampleBean value is " + otherSampleBean.toString());
    }

    @Test
    public void test0() {
        Float fload = Float.valueOf("22.22");
        BeanCopier copier = BeanCopier.create(Float.class, Integer.class, false);
       Integer target = 0;
        copier.copy(fload, target, null);
        System.out.println("otherSampleBean value is " + target);
    }

    @Test
    public void test1() {
        List<PersonB> list = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            list.add(new PersonB("username" + i, "adfadfwef" + i, new PersonC("personC" + i, "pawword" + i)));
        }
        long l = System.currentTimeMillis();
        List<PersonB> collect = list.stream().map(personB -> {
            PersonB temp = new PersonB();
//            org.springframework.beans.BeanUtils.copyProperties(personB, temp);
//            BeanUtils1.copy(personB, temp);
            BeanUtils.copyValue(personB, temp);
            return temp;
        }).collect(Collectors.toList());
        System.out.println("执行用例-->" + collect.size() + "，耗时:" + (System.currentTimeMillis() - l) + "ms");
        for (int i = 0; i < 10; i++) {
            System.out.println("result is " + collect.get(i).toString());
        }
    }

    @Test
    public void test2() {
        List<PersonB> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new PersonB("username" + i, "adfadfwef" + i, new PersonC("personC" + i, "pawword" + i)));
        }
        long l = System.currentTimeMillis();
        List<PersonD> collect = list.stream().map(personB -> {
            PersonD temp = new PersonD();
//            org.springframework.beans.BeanUtils.copyProperties(personB, temp);
//            BeanUtils1.copy(personB, temp);
//            Copy.copyValue(personB, temp);
            BeanUtils.copyProperties(personB, temp);
            return temp;
        }).collect(Collectors.toList());
        System.out.println("执行用例-->" + collect.size() + "，耗时:" + (System.currentTimeMillis() - l) + "ms");
        for (int i = 0; i < 10; i++) {
            System.out.println("result is " + collect.get(i).toString());
        }
    }

    @Test
    public void test3() {
        List<PersonB> list = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            List<String> temp = new ArrayList<>();
            temp.add("item->" + i);
            temp.add("item->" + (i * 2));
            temp.add("item->" + (i * 4));
            list.add(new PersonB("username" + i, "adfadfwef" + i, new PersonC("personC" + i, "pawword" + i), new PersonC(), temp));
        }
        long l = System.currentTimeMillis();
        List<PersonD> collect = list.stream().map(personB -> {
            PersonD temp = new PersonD();
//            org.springframework.beans.BeanUtils.copyProperties(personB, temp);
//            BeanUtils1.copy(personB, temp);
//            Copy.copyValue(personB, temp);
            BeanUtils.copyProperties(personB, temp);
            return temp;
        }).collect(Collectors.toList());
        System.out.println("执行用例-->" + collect.size() + "，耗时:" + (System.currentTimeMillis() - l) + "ms");
        for (int i = 0; i < 10; i++) {
            System.out.println("result is " + collect.get(i).toString());
        }
    }
    @Test
    public void test4() {
        List<PersonB> list = new ArrayList<>();
//        for (int i = 0; i < 1000000; i++) {
//            List<String> temp = new ArrayList<>();
//            temp.add("item->" + i);
//            temp.add("item->" + (i * 2));
//            temp.add("item->" + (i * 4));
//            list.add(new PersonB("username" + i, "adfadfwef" + i, new PersonC("personC" + i, "pawword" + i), new PersonC(), temp));
//        }
//        long l = System.currentTimeMillis();
//        List<PersonD> collect = list.stream().map(personB -> {
//            PersonD temp = new PersonD();
////            org.springframework.beans.BeanUtils.copyProperties(personB, temp);
////            BeanUtils1.copy(personB, temp);
////            Copy.copyValue(personB, temp);
//            Copy.copyProperties1(personB, temp);
//            return temp;
//        }).collect(Collectors.toList());

//        System.out.println("执行用例-->" + collect.size() + "，耗时:" + (System.currentTimeMillis() - l) + "ms");
        List<String> temp = new ArrayList<>();
        temp.add("item->" + 0);
        temp.add("item->" + 222);
        temp.add("item->" + 5555);
        Set<Object> result = new HashSet<>();

        final BeanCopier beanCopier = BeanCopier.create(temp.getClass(), result.getClass(), true);
        beanCopier.copy(temp, result, new Converter() {
            @Override
            public Object convert(Object o, Class aClass, Object o1) {
                System.out.println("o:" + o + ",class:" + aClass+", o1:"+o1);
                return null;
            }
        });
        System.out.println("执行结果；"+ result.size());
        for (Object o : result) {
            System.out.println("result is " + o.toString());
        }

    }

    @Test
    public void testBeanMap() throws Exception {
        BeanGenerator generator = new BeanGenerator();
        generator.addProperty("username", String.class);
        generator.addProperty("password", String.class);
        Object bean = generator.create();
        Method setUserName = bean.getClass().getMethod("setUsername", String.class);
        Method setPassword = bean.getClass().getMethod("setPassword", String.class);
        setUserName.invoke(bean, "admin");
        setPassword.invoke(bean, "password");
        BeanMap map = BeanMap.create(bean);

        System.out.println("admin equal:" + map.get("username"));
        System.out.println("password equal:" + map.get("password"));
    }

    @Test
    public void testG() throws IntrospectionException {
        BeanInfo beanInfo = Introspector.getBeanInfo(PersonD.class);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            if (Objects.equals(propertyDescriptor.getName(), "roles")) {
                System.out.println("class-->" + propertyDescriptor.getPropertyType());
                Type clazz = propertyDescriptor.getWriteMethod().getGenericParameterTypes()[0];
//                Class<?> parameterType = propertyDescriptor.getWriteMethod().getParameterTypes()[0];
//
//                Type clazz = parameterType.getGenericSuperclass();
                ParameterizedType pt = (ParameterizedType) clazz;
                String s = pt.getActualTypeArguments()[0].toString();
                System.out.println("actualParamType is " + s);

            }
        }
        String str = "setUserName";
        System.out.println("str substring is " + str.substring(2));
        System.out.println("str substring is " + str.substring(3));
//        Field[] declaredFields = PersonD.class.getDeclaredFields();
//        for (Field declaredField : declaredFields) {
//            if(Objects.equals(declaredField.getName(), "roles")) {
//                declaredField.get
//                Type clazz = declaredField.getType().getGenericSuperclass();
//                ParameterizedType pt = (ParameterizedType)clazz;
//                String s = pt.getActualTypeArguments()[0].toString();
//                System.out.println("actualParamType is " + s);
//
//            }
//        }
    }
}
