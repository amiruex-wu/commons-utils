package top.itoolbox.commons;

import lombok.Data;
import top.itoolbox.commons.model.OtherSampleBean;

/**
 * @Description: TODO
 * @Author: wuchu
 * @Version: 1.0
 * @CreateTime: 2022/8/20 16:37
 */
@Data
public class Address {

    private String province;
    private String city;
    private String district;
    private String moreInfo;

    private OtherSampleBean sampleBean;
}
