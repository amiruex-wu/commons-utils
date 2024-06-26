package top.itoolbox.commons.conversion.converter;

import lombok.NoArgsConstructor;
import top.itoolbox.commons.enums.FormatPattern;
import top.itoolbox.commons.lang.DateUtils;
import top.itoolbox.commons.lang.ObjectUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

/**
 * @Description: 时间类型转换器
 * @Author: wuchu
 * @CreateTime: 2022-07-13 17:22
 */
@NoArgsConstructor
public class LocalTimeValueConverter<T> extends AbstractConverter<T> {

    public LocalTimeValueConverter(Object source, Class<T> requiredType) {
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
                result = DateUtils.format((LocalTime) obj, FormatPattern.DATE_TIME_FORMAT_1).map(requiredType::cast);
                break;
            case "LocalDateTime":
                result = Optional.of(requiredType.cast(LocalDateTime.of(LocalDate.now(), (LocalTime) obj)));
                break;
            case "Date":
                result = DateUtils.toDate(LocalDateTime.of(LocalDate.now(), (LocalTime) obj)).map(requiredType::cast);
                break;
            default:
                // 其他数据类型不进行转换
                result = Optional.empty();
                break;
        }
        return result;
    }
}
