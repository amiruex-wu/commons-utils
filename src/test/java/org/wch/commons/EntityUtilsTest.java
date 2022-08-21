package org.wch.commons;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import sun.security.krb5.internal.PAData;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: TODO
 * @Author: wuchu
 * @Version: 1.0
 * @CreateTime: 2022/8/21 9:44
 */
@RunWith(JUnit4.class)
public class EntityUtilsTest {


    @Test
    public void test(){
        Person person = new Person();
        person.setUserName("赵四");
        Map<String, Object> params = new HashMap<>();
        params.put("age","10");
        params.put("password", "1255222");
        EntityUtils.setFieldValue(person,params);
        System.out.println("result is " + person);
    }
}
