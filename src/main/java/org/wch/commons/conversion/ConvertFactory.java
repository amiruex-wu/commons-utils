package org.wch.commons.conversion;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.wch.commons.conversion.converter.*;
import org.wch.commons.lang.ObjectUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

public class ConvertFactory {

    public static <T> Optional<AbstractConverter<T>> getConverter(Object source, Class<T> requiredType) {
        if (ObjectUtils.anyNull(source, requiredType)) {
            return Optional.empty();
        }
        try {
            if (source instanceof Character) {
                return Optional.of(new CharacterValueConverter<>(source, requiredType));
            } else if (source instanceof String) {
                return Optional.of(new StringValueConverter<>(source, requiredType));
            } else if (source instanceof Date) {
                return Optional.of(new DateValueConverter<T>(source, requiredType));
            } else if (source instanceof LocalDateTime) {
                return Optional.of(new LocalDateTimeValueConverter<>(source, requiredType));
            } else if (source instanceof LocalDate) {
                return Optional.of(new LocalDateValueConverter<>(source, requiredType));
            } else if (source instanceof LocalTime) {
                return Optional.of(new LocalTimeValueConverter<>(source, requiredType));
            } else if (source instanceof Number) {
                return Optional.of(new NumberValueConverter<>(source, requiredType));
            } else if (source instanceof Collection) {
                return Optional.of(new CollectionValueConverter<>(source, requiredType));
            }  else if (source instanceof Map) {
                return Optional.of(new MapValueConverter<>(source, requiredType));
            } else {
                return Optional.of(new ObjectValueConverter<>(source, requiredType));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
