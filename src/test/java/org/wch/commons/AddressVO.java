package org.wch.commons;

import lombok.Data;
import org.wch.commons.model.SampleBean;

import java.io.Serializable;

/**
 * @Description: TODO
 * @Author: wuchu
 * @Version: 1.0
 * @CreateTime: 2022/8/20 16:37
 */
@Data
public class AddressVO implements Serializable {
    private String province;
    private String city;
    private String district;
    private String detail;

    private SampleBean sampleBean;
}
