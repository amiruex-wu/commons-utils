package org.wch.commons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: TODO
 * @Author: wuchu
 * @CreateTime: 2022-07-07 11:12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person implements Serializable {

    private String userName;

    private String password;

    private Integer age;
}
