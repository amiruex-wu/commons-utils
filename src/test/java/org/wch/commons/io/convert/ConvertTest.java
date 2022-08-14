package org.wch.commons.io.convert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.wch.commons.lang.ConvertUtils2;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RunWith(JUnit4.class)
public class ConvertTest {

    @Test
    public void test() {
        Object obj = 15L;
        Optional<?> s = ConvertUtils2.convertIfNecessary(obj, Long.class);
        System.out.println("result is " + s.orElse(null));
    }

    @Test
    public void test1() {
        Object obj = "2022-07-16 23:44:56";
        Optional<?> s = ConvertUtils2.convertIfNecessary(obj, LocalDateTime.class);
        System.out.println("result is " + s.orElse(null));
    }

    @Test
    public void test2() {
        String obj = "2022-07-16 23:44:56";
        LocalDateTime parse = LocalDateTime.parse(obj, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("result is " + parse);
    }

}
