package org.wch.commons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Description: TODO
 * @Author: wuchu
 * @CreateTime: 2022-07-07 11:12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person1 implements Serializable {

    private String userName;

    private String password;

    private Integer age;

    private LocalDateTime birthday;
    private String nickName;
    private String phone;
    private String email;
    private String idCardNo;
    private Address address;
    private String emergencyLinkMan1;
}
