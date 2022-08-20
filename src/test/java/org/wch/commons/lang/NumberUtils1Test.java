package org.wch.commons.lang;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.wch.commons.lang.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: TODO
 * @Author: wuchu
 * @CreateTime: 2022-07-29 09:50
 */
@RunWith(JUnit4.class)
public class NumberUtils1Test {

    @Test
    public void test(){
        List<String> temp = new ArrayList<>();
        temp.add("1");
        temp.add("2");
        temp.add("3");
        temp.add("4");
        temp.add("5");
        List<String> strings = CollectionUtils.subList(temp, 1);
        System.out.println("result is " + strings);
        List<String> strings1 = CollectionUtils.subList(temp, 5);
        System.out.println("result is " + strings1);
        List<String> strings2 = CollectionUtils.subList(temp, 6);
        System.out.println("result is " + strings2);
    }

    @Test
    public void test1(){
        List<String> temp = new ArrayList<>();
        temp.add("1");
        temp.add("2");
        temp.add("3");
        temp.add("4");
        temp.add("5");
        List<String> strings = CollectionUtils.subList(temp, 1, 3);
        System.out.println("result is " + strings);
        List<String> strings1 = CollectionUtils.subList(temp, 4, 5);
        System.out.println("result1 is " + strings1);
        List<String> strings2 = CollectionUtils.subList(temp, 4, 6);
        System.out.println("result2 is " + strings2);
        List<String> strings3 = CollectionUtils.subList(temp, 5, 6);
        System.out.println("result3 is " + strings3);
        List<String> strings4 = CollectionUtils.subList(temp, 6, 1);
        System.out.println("result4 is " + strings4);
    }

}
