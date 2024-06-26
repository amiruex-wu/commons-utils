package top.itoolbox.commons.conversion.converter;

import lombok.NoArgsConstructor;
import top.itoolbox.commons.enums.FormatPattern;
import top.itoolbox.commons.lang.DateUtils;
import top.itoolbox.commons.lang.ObjectUtils;

import java.util.Date;
import java.util.Optional;

/**
 * @Description: 日期类型转换器
 * @Author: wuchu
 * @CreateTime: 2022-07-13 17:22
 */
@NoArgsConstructor
public class DateValueConverter<T> extends AbstractConverter<T> {

    public DateValueConverter(Object source, Class<T> requiredType) {
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
                result = DateUtils.format((Date) obj, FormatPattern.FULL_DATE_FORMAT_1).map(requiredType::cast);
                break;
            case "LocalDateTime":
                result = DateUtils.toLocalDateTime((Date) obj).map(requiredType::cast);
                break;
            case "LocalDate":
                result = DateUtils.toLocalDate((Date) obj).map(requiredType::cast);
                break;
            case "LocalTime":
                result = DateUtils.toLocalDateTime((Date) obj).map(dateTime -> requiredType.cast(dateTime.toLocalTime()));
                break;
            case "Date":
                result = Optional.of(requiredType.cast(obj));
                break;
            default:
                result = Optional.empty();
                break;
        }
        return result;
    }
}
