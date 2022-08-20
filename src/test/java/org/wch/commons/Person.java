package org.wch.commons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

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
    private Date birthday;
    private String nickName;
    private String phone;
    private String email;
    private String idCardNo1;
    private AddressVO address;
    private String emergencyLinkMan;

    public Person(String userName, String password, int age) {
        this.userName = userName;
        this.password = password;
        this.age = age;
    }
}