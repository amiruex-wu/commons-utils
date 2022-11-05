package org.wch.commons.conversion.converter;

import lombok.NoArgsConstructor;
import org.wch.commons.enums.FormatPattern;
import org.wch.commons.lang.DateUtils;
import org.wch.commons.lang.ObjectUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @Description: 日期类型转换器
 * @Author: wuchu
 * @CreateTime: 2022-07-13 17:22
 */
@NoArgsConstructor
public class LocalDateValueConverter<T> extends AbstractConverter<T> {

    public LocalDateValueConverter(Object source, Class<T> requiredType) {
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
                result = DateUtils.format((LocalDate) obj, FormatPattern.LOCAL_DATE_FORMAT_1).map(requiredType::cast);
                break;
            case "LocalDateTime":
                result = DateUtils.toLocalDateTime((LocalDate) obj).map(requiredType::cast);
                break;
            case "LocalDate":
                result = Optional.of(requiredType.cast(obj));
                break;
            case "Date":
                result = DateUtils.toDate((LocalDate) obj).map(requiredType::cast);
                break;
            default:
                // 其他数据类型不进行转换
                result = Optional.empty();
                break;
        }
        return result;
    }
}
