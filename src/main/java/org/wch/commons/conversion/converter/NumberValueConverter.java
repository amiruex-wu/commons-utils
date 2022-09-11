package org.wch.commons.conversion.converter;

import lombok.NoArgsConstructor;
import org.wch.commons.lang.BooleanUtils;
import org.wch.commons.lang.ObjectUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
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
                result = Optional.of(requiredType.cast(Byte.valueOf(obj.toString())));
                break;
            case "Short":
                result = Optional.of(requiredType.cast(Short.valueOf(obj.toString())));
                break;
            case "Integer":
                result = Optional.of(requiredType.cast(Integer.valueOf(obj.toString())));
                break;
            case "Long":
                result = Optional.of(requiredType.cast(Long.valueOf(obj.toString())));
                break;
            case "Double":
                result = Optional.of(requiredType.cast(Double.valueOf(obj.toString())));
                break;
            case "Float":
                result = Optional.of(requiredType.cast(Float.valueOf(obj.toString())));
                break;
            case "Boolean":
                result = Optional.of(requiredType.cast(BooleanUtils.toBoolean(obj)));
                break;
            case "BigDecimal":
                result = Optional.of(requiredType.cast(new BigDecimal(obj.toString())));
                break;
            case "BigInteger":
                result = Optional.of(requiredType.cast(new BigInteger(obj.toString())));
                break;
            default:
                // 其他数据类型不进行转换
                result = Optional.empty();
                break;
        }
        return result;
    }
}
