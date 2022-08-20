package org.wch.commons.conversion.converter;

import lombok.NoArgsConstructor;
import org.wch.commons.lang.ObjectUtils;

import java.util.Optional;

/**
 * @Description: 普通对象类型转换器
 * @Author: wuchu
 * @CreateTime: 2022-07-13 17:22
 */
@NoArgsConstructor
public class ObjectValueConverter<T> extends AbstractConverter<T> {

    public ObjectValueConverter(Object source, Class<T> requiredType) {
        super(source, requiredType);
    }

    @Override
    public Optional<T> convert() {
        return convert(source, requiredType);
    }

    public Optional<T> convert(Object obj, Class<T> requiredType) {
        if (ObjectUtils.anyNull(obj, requiredType)) {
            return Optional.empty();
        }

        return caseJsonStrConvert();
    }
}
