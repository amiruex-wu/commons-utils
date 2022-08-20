package org.wch.commons.juel;

import lombok.Data;

import java.lang.reflect.Method;

/**
 * @Description: 表达式使用函数
 * @Author: wuchu
 * @CreateTime: 2022-08-12 17:48
 */
@Data
public class Functions {

    private String prefix;

    private String functionName;

    private Method method;
}
