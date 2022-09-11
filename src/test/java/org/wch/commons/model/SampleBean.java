package org.wch.commons.model;

import lombok.Data;

import java.util.List;

/**
 * @Description: TODO
 * @Author: wuchu
 * @CreateTime: 2022-09-07 13:56
 */
@Data
public class SampleBean {

    private String value;

    private Integer count;

    private int age;

    private List<String> roles;

    public SampleBean() {
    }

    public SampleBean(String value) {
        this.value = value;
    }

    public SampleBean(String value, Integer count, int age) {
        this.value = value;
        this.count = count;
        this.age = age;
    }

}
