package org.wch.commons.beans;

import java.util.Optional;

/**
 * @Description: 主要用于记录当前登录用户
 * @Author: wuchu
 * @CreateTime: 2022/8/21 10:08
 */
public class ThreadLocalHolder {

    private static final ThreadLocal<LoginUser> userThreadLocal = new InheritableThreadLocal<>();

    public static Optional<LoginUser> getLoginUser(){
        return Optional.ofNullable(userThreadLocal.get());
    }

    public static void setLoginUser(LoginUser loginUser){
        userThreadLocal.set(loginUser);
    }

    public static void remove(){
        userThreadLocal.remove();
    }
}
