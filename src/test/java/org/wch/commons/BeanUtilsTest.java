package org.wch.commons;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: TODO
 * @Author: wuchu
 * @CreateTime: 2022-07-13 17:22
 */
@RunWith(JUnit4.class)
public class BeanUtilsTest {

    @Test
    public void test(){
        Person person = new Person();
        Person person1 = new Person();
        person.setUserName("aaaa");
        person.setPassword("aaaaerererere");
        person.setAge(20);
        Object o = BeanUtils.copyProperties(person, Person.class);
        System.out.println("person1 is "+ o);

    }

    @Test
    public void test1(){
        Person person = new Person();
        Person person1 = new Person();
        person.setUserName("aaaa");
        person.setPassword("aaaaerererere");
        person.setAge(20);
        Object o = BeanUtils.copyProperties(person, Person.class);
        System.out.println("person1 is "+ o);

    }

    @Test
    public void test2(){
        Person person = new Person();
        Person1 person1 = new Person1();
        person.setUserName("aaaa");
        person.setPassword("aaaaerererere");
        person.setAge(20);
        person.setNickName("John simith");
        person.setPhone("151255455252");
        person.setEmail("151255455252@qq.com");
        person.setIdCardNo1("5515125545525");
        AddressVO addressVO = new AddressVO();
        addressVO.setProvince("湖南省");
        addressVO.setCity("长沙市");
        addressVO.setDistrict("望城区");
        person.setAddress(addressVO);
        person.setEmergencyLinkMan("赵市明");
        final StopWatch stopWatch = StopWatch.create();
//        try {
//            BeanUtils.getMethod(person);
//        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
//            e.printStackTrace();
//        }
        BeanUtils.copyProperties(person, person1, false);
        System.out.println("person1 read cost time is "+  stopWatch.getTotalTimeMillis()+ "ms");
        System.out.println("person1 is "+ person1);

    }

    @Test
    public void test3(){
        final StopWatch stopWatch = StopWatch.create();

        List<Person1> temp = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            Person person = new Person();

            person.setUserName("aaaa");
            person.setPassword("aaaaerererere");
            person.setAge(20);
            person.setBirthday(new Date());
            person.setNickName("John simith" + i);
            person.setPhone("151255455252"+ (i*2));
            person.setEmail("151255455252@qq.com");
            person.setIdCardNo1("5515125545525");
            AddressVO addressVO = new AddressVO();

            addressVO.setProvince("湖南省");
            addressVO.setCity("长沙市");
            addressVO.setDistrict("望城区xxxx"+i+"号");
            addressVO.setDetail("xxxx团结路幸福街"+i+"号");
            person.setAddress(addressVO);
            person.setEmergencyLinkMan("赵市明");

            Person1 person1 = new Person1();
            BeanUtils.copyProperties(person, person1, true);
            temp.add(person1);
        }

//        try {
//            BeanUtils.getMethod(person);
//        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
//            e.printStackTrace();
//        }

        System.out.println("person1 read cost time is "+  stopWatch.getTotalTimeMillis()+ "ms");
        System.out.println("person1 is "+ temp.size());
        for (int i = 0; i < 50; i++) {
            System.out.println("person1 is "+ temp.get(i));
        }

    }

}
