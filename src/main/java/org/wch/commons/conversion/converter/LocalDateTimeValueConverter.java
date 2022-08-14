package org.wch.commons.conversion.converter;

import lombok.NoArgsConstructor;
import org.wch.commons.enums.FormatPattern;
import org.wch.commons.lang.DateUtils;
import org.wch.commons.lang.ObjectUtils;

import java.time.LocalDateTime;
import java.util.Optional;

@NoArgsConstructor
public class LocalDateTimeValueConverter<T> extends AbstractConverter<T> {

    public LocalDateTimeValueConverter(Object source, Class<T> requiredType) {
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
                result = Optional.of(requiredType.cast(DateUtils.format((LocalDateTime) obj, FormatPattern.FULL_DATE_FORMAT_1)));
                break;
            case "LocalDateTime":
                result = Optional.of(requiredType.cast(obj));
                break;
            case "LocalDate":
                result = Optional.of(requiredType.cast(((LocalDateTime) obj).toLocalDate()));
                break;
            case "LocalTime":
                result = Optional.of(requiredType.cast(((LocalDateTime) obj).toLocalTime()));
                break;
            case "Date":
                result = DateUtils.toDate((LocalDateTime) obj).map(requiredType::cast);
                break;
            default:
                result = Optional.empty();
                break;
        }
        return result;
    }
}
