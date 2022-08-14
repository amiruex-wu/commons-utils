package org.wch.commons.io.xml;

import lombok.Data;

import java.util.List;

/**
 * @Description: TODO
 * @Author: wuchu
 * @CreateTime: 2022-07-25 15:25
 */
@Data
public class Student<T> {
    private String id;
    private String name;
    private int age;
    private List<String> interest;
    private TClass studentClass;
    private T data;
}
