package top.itoolbox.commons.conversion;

import top.itoolbox.commons.conversion.converter.*;
import top.itoolbox.commons.lang.ObjectUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * @Description: 类型转换器工厂
 * @Author: wuchu
 * @CreateTime: 2022-07-13 17:22
 */
public class ConvertFactory {

    private static final Map<Class, AbstractConverter> converterMap = new HashMap<>();

    static {
        converterMap.put(Character.class, new CharacterValueConverter<>());
        converterMap.put(String.class, new StringValueConverter<>());
        converterMap.put(Date.class, new DateValueConverter<>());
        converterMap.put(LocalDateTime.class, new LocalDateTimeValueConverter<>());
        converterMap.put(LocalDate.class, new LocalDateValueConverter<>());
        converterMap.put(LocalTime.class, new LocalTimeValueConverter<>());
        converterMap.put(Byte.class, new NumberValueConverter<>());
        converterMap.put(Short.class, new NumberValueConverter<>());
        converterMap.put(Integer.class, new NumberValueConverter<>());
        converterMap.put(Long.class, new NumberValueConverter<>());
        converterMap.put(Float.class, new NumberValueConverter<>());
        converterMap.put(Double.class, new NumberValueConverter<>());
        converterMap.put(BigInteger.class, new NumberValueConverter<>());
        converterMap.put(BigDecimal.class, new NumberValueConverter<>());
        converterMap.put(Number.class, new NumberValueConverter<>());
        converterMap.put(Collection.class, new CollectionValueConverter<>());
        converterMap.put(Map.class, new MapValueConverter<>());
        converterMap.put(Object.class, new ObjectValueConverter<>());
    }

    public static <T> Optional<AbstractConverter<T>> getConverter(Object source, Class<T> requiredType) {
        if (ObjectUtils.anyNull(source, requiredType)) {
            return Optional.empty();
        }
        try {
            AbstractConverter<T> converter = converterMap.get(source.getClass());
            if (Objects.isNull(converter)) {
                if (source instanceof Number) {
                    converter = converterMap.get(Number.class);
                } else if (source instanceof Collection) {
                    converter = converterMap.get(Collection.class);
                } else if (source instanceof Map) {
                    converter = converterMap.get(Map.class);
                } else {
                    converter = converterMap.get(Object.class);
                }
            }
            converter.setSource(source);
            converter.setRequiredType(requiredType);
            return Optional.of(converter);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
