package org.wch.commons.io;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.wch.commons.BeanUtils;

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

}
