package top.itoolbox.commons.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.itoolbox.commons.enums.XMLOperateType;

import java.util.Map;
import java.util.Objects;

/**
 * @Description: xml文件节点属性
 * @Author: wuchu
 * @CreateTime: 2022-07-29 10:32
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class XMLAttribute {

    private String identifierKey;

    private String identifierValue;

    private String parentRoute;

    private Boolean updateOnExists;

    private XMLOperateType xmlOperateType;

    private Map<String, Object> properties;

    public boolean isAnyNull() {
        return Objects.isNull(identifierKey)
                || Objects.isNull(identifierValue)
                || Objects.isNull(parentRoute)
                || Objects.isNull(updateOnExists)
                || Objects.isNull(properties);
    }
}
