package org.wch.commons.conversion.converter;

import lombok.NoArgsConstructor;
import org.wch.commons.lang.BooleanUtils;
import org.wch.commons.lang.ObjectUtils;

import java.math.BigDecimal;
import java.util.Optional;

@NoArgsConstructor
public class NumberValueConverter<T> extends AbstractConverter<T> {

    public NumberValueConverter(Object source, Class<T> requiredType) {
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
        Optional<T> result;
        switch (requiredType.getSimpleName()) {
            case "String":
                result = Optional.of(requiredType.cast(obj.toString()));
                break;
            case "Byte":
                result = castToRequiredType(obj.getClass(), requiredType, "byteValue", obj);
                break;
            case "Short":
                result = castToRequiredType(obj.getClass(), requiredType, "shortValue", obj);
                break;
            case "Integer":
                result = castToRequiredType(obj.getClass(), requiredType, "intValue", obj);
                break;
            case "Long":
                result = castToRequiredType(obj.getClass(), requiredType, "longValue", obj);
                break;
            case "Double":
                result = castToRequiredType(obj.getClass(), requiredType, "doubleValue", obj);
                break;
            case "Float":
                result = castToRequiredType(obj.getClass(), requiredType, "floatValue", obj);
                break;
            case "Boolean":
                result = Optional.of(requiredType.cast(BooleanUtils.toBoolean(obj)));
                break;
            case "BigDecimal":
                result = Optional.of(requiredType.cast(new BigDecimal(obj.toString())));
                break;
            default:
                // 其他数据类型不进行转换
                result = Optional.empty();
                break;
        }
        return result;
    }
}
