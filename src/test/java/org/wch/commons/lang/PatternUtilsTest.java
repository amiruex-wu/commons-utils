package org.wch.commons.lang;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.wch.commons.enums.FormatPattern;
import org.wch.commons.lang.DateUtils;
import org.wch.commons.lang.PatternUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(JUnit4.class)
public class PatternUtilsTest {

    @Test
    public void test() {
//        String start = "this is first test ${myName}, the grade will generate at ${date}";
        String start = "this is first test myName, the grade will generate at ${date}";
//        String start = "";
        Map<String, Object> map = new HashMap<>();
        map.put("myName", "English");
        map.put("date", DateUtils.format(LocalDateTime.now(), FormatPattern.FULL_DATE_FORMAT_2).orElse(""));

        Optional<String> result = PatternUtils.refactoring(start, map);
        System.out.println("result is:" + result.orElse(""));
    }

    @Test
    public void test1() {
        String start = "this is first test $myName$, the grade will generate at $date$";
//        String start = "this is first test myName, the grade will generate at ${date";
//        String start = "";
        Map<String, Object> map = new HashMap<>();
        map.put("myName", "English");
//        map.put("date", DateUtils.format(LocalDateTime.now(), FormatPattern.FULL_DATE_FORMAT_2).orElse(""));
        map.put("date", 222.222f);

        Optional<String> result = PatternUtils.refactoring(start, "\\$(.+?)\\$", true, map);
        System.out.println("result is:" + result.orElse(""));
    }

    @Test
    public void test2() {
//        String regexTemp1 = "http://[\\d]{1,3}(\\.)[\\d]{1,3}(\\.)[\\d]{1,3}(\\.)[\\d]{1,3}(:)[.]+[&]";
        String start = "http://localhost:8080/test/getUrl?id=123&code=TESS&url=http://172.56.21.55:8088/cest?type=2&code=WEBAPP";
        String regexTemp1 = "http://[\\d]{1,3}(\\.)[\\d]{1,3}(\\.)[\\d]{1,3}(\\.)[\\d]{1,3}";
        String regexTemp2 = "https://[\\d]{1,3}(\\.)[\\d]{1,3}(\\.)[\\d]{1,3}(\\.)[\\d]{1,3}";
        String substring = start.substring(start.indexOf("?") + 1);
        Matcher matcher = Pattern.compile(regexTemp1).matcher(substring);
        boolean findResult = false;
        while (matcher.find()) {
            findResult = true;
            String key = matcher.group(1);// 键名
            System.out.println("find key is " + key);
            System.out.println("find is " + matcher.group());
            System.out.println("find is " + matcher.group(0));
            System.out.println("find is " + matcher.group(1));
        }
        Matcher matcher1 = Pattern.compile(regexTemp2).matcher(substring);
        while (matcher1.find()) {
            findResult = true;
            String key = matcher1.group(1);// 键名
            System.out.println("find key is " + key);
            System.out.println("find is " + matcher1.group());
        }

        /*if(findResult){
            String paramRefactoring = PatternUtils.getParamRefactoring(substring);
            System.out.println("replace result is "+ paramRefactoring);
        }*/

//        String start = "this is first test myName, the grade will generate at ${date";
//        String start = "";
       /* Map<String, Object> map = new HashMap<>();
        map.put("myName","English");
//        map.put("date", DateUtils.format(LocalDateTime.now(), FormatPattern.FULL_DATE_FORMAT_2).orElse(""));
        map.put("date", 222.222f);

        Optional<String> result = PatternUtils.refactoring(start,"\\$(.+?)\\$",true, map);*/
        System.out.println("result is:" + findResult);
        System.out.println("result is:" + substring.matches(regexTemp1));
        System.out.println("result1 is:" + substring.matches(regexTemp2));
    }

    @Test
    public void test3() {
//        String start = "http://localhost:8080/test/getUrl?id=123&code=TESS&url=${url}";
        String start = "http://baidu.com?redirectUrl=${url}";
        Map<String, Object> params = new HashMap<>();
//        params.put("url","http://172.56.21.55:8088/cest?type=2&code=WEBAPP");
        params.put("url","https://blog.csdn.net/alan_liuyue/article/details/78982905?test=werere&tes=bbbb");

        Optional<String> refactoring = PatternUtils.refactoring(start, params);
        System.out.println("result is "+ refactoring.orElse(null));
    }
}
