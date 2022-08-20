package org.wch.commons;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.wch.commons.RandomUtils;

import java.util.Optional;
import java.util.Random;

/**
 * @Description:
 * @Author: wuchu
 * @CreateTime: 2022-07-19 10:52
 */
@RunWith(JUnit4.class)
public class RandomTest {

    @Test
    public void test(){
        for (int i = 0; i < 10; i++) {
            float v = RandomUtils.nextFloat();
            System.out.println("result is " + v);

        }
    }

    @Test
    public void test1(){
        for (int i = 0; i < 10; i++) {
            float v = RandomUtils.nextFloat(2.2f,5.2f);
            System.out.println("result is " + v);

        }
    }

    @Test
    public void test2(){
        for (int i = 0; i < 56; i++) {
            int i1 = new Random().nextInt(4);
            int v = RandomUtils.nextInt(0,9);
            System.out.println("result is " + v);
            // a-z 97->122
            // A-Z 65->90

        }
    }

    @Test
    public void test3(){
        for (int i = 0; i < 26; i++) {
//            int v = 'a'+ i;
            int v = 'A'+ i;
//            int v = RandomUtils.nextInt(1,10);
            System.out.println("result is " + v);
            // a-z 97->122
            // A-Z 65->90

        }
    }

    @Test
    public void test4(){
        for (int i = 0; i < 60; i++) {
            Optional<String> s = RandomUtils.randomString(10, RandomUtils.MixType.MIX_ALL);
            System.out.println("result is " + s.orElse(null));
        }
    }

}
