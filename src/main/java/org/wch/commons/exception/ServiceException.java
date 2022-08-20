package org.wch.commons.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 业务异常类
 * @Author: wuchu
 * @CreateTime: 2022-07-13 17:22
 */
public class ServiceException extends RuntimeException {

    @Getter
    @Setter
    private Integer status;

    @Setter
    @Getter
    private String errorMsg;

    public ServiceException(Integer status, String errorMsg) {
        super("exception code is：" + status + "；exception message is：" + errorMsg);
        this.status = status;
        this.errorMsg = errorMsg;
    }

    public static void throwException(Integer status, String errorMsg){
        throw new ServiceException(status, errorMsg);
    }
}
