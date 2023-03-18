package org.wch.commons.conversion;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import net.sf.cglib.core.Converter;
import org.wch.commons.conversion.SimpleTypeConverter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * @Description: 用于cglib拷贝属性时的自定义转换工具
 * @Author: wuchu
 * @CreateTime: 2022-09-07 14:09
 */
public class CustomBeanConverter implements Converter {

    public static final List<Class<?>> GENERAL_CLASS = Arrays.asList(
            byte.class, short.class, int.class, float.class, double.class, long.class, boolean.class, char.class,
            Byte.class, Short.class, Integer.class, Float.class, Double.class, Long.class, Boolean.class, BigDecimal.class, BigInteger.class, Character.class, String.class);

    public static final List<Class<?>> DATE_TIME_CLASS = Arrays.asList(LocalDateTime.class, LocalDate.class, LocalTime.class, Date.class);

    /**
     * custom data converter
     *
     * @param source      source data
     * @param targetClass target param type
     * @param var3        target param setter
     * @return
     */
    @SneakyThrows
    @Override
    public Object convert(Object source, Class targetClass, Object var3) {
        if (Objects.isNull(source)) {
            return null;
        }

        final Class<?> sourceClass = source.getClass();
        if (Objects.equals(sourceClass, targetClass)
                && (GENERAL_CLASS.contains(targetClass) || DATE_TIME_CLASS.contains(targetClass))) {
            return source;
        } else if (Map.class.isAssignableFrom(sourceClass) && Map.class.isAssignableFrom(targetClass)) {
            final HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
            final Map<Object, Object> temp = (Map<Object, Object>) source;
            for (Map.Entry<Object, Object> entry : temp.entrySet()) {
                objectObjectHashMap.put(entry.getKey(), entry.getValue());
            }
            return objectObjectHashMap;
        } else if (Collection.class.isAssignableFrom(sourceClass) && Collection.class.isAssignableFrom(targetClass)) {
            // 同为集合对象
            return source;
        } else if ((GENERAL_CLASS.contains(targetClass) && GENERAL_CLASS.contains(sourceClass))
                || (GENERAL_CLASS.contains(targetClass) && DATE_TIME_CLASS.contains(sourceClass))
                || (DATE_TIME_CLASS.contains(targetClass) && DATE_TIME_CLASS.contains(sourceClass))
                || (DATE_TIME_CLASS.contains(targetClass) && GENERAL_CLASS.contains(sourceClass))) {
            // all of those are general class
            final Optional<?> result1 = SimpleTypeConverter.convertIfNecessary(source, targetClass);
            return result1.map(targetClass::cast).orElse(null);
        } else {
            return null;
        }
    }

// endregion
}
