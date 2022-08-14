package org.wch.commons.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.wch.commons.enums.XMLOperateType;
import org.wch.commons.text.XMLUtils;

import java.util.Map;
import java.util.Objects;

/**
 * @Description: todo 新增
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
