package top.itoolbox.commons.beans;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description: 登录用户信息
 * @Author: wuchu
 * @Version: 1.0
 * @CreateTime: 2022/8/21 10:09
 */
@Data
public class LoginUser {

    /**
     * 主键ID
     */
    private String userId;

    /**
     * 登录用户名称
     */
    private String userName;

    /**
     * 登录用户账户名称
     */
    private String accountName;

    /**
     * 登录时间
     */
    private LocalDateTime loginTime;
    /**
     * 公司ID
     */
    private String companyId;
    /**
     * 公司名称
     */
    private String companyName;
    /**
     * 部门ID
     */
    private String departmentId;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 用于记录其他信息
     */
    private JSONObject jsonObject;
}
