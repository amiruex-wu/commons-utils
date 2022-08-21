package org.wch.commons;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.wch.commons.lang.ConvertUtils;
import org.wch.commons.lang.ObjectUtils;
import org.wch.commons.lang.StringUtils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: TODO 这里应该是重复功能没有多大的用处
 * @Author: wuchu
 * @CreateTime: 2022-07-20 16:44
 */
@Deprecated
public class EntityUtils {

    private final static Class<?>[] BASIC_CLAZZ = {Byte.class, Short.class, Integer.class, Float.class, Double.class,
            Long.class, BigDecimal.class, BigInteger.class, String.class, Boolean.class, Character.class,
            byte.class, short.class, int.class, float.class, double.class,
            long.class, boolean.class, char.class, Map.class};

    public static <T> void setFieldValue(T t, Map<String, Object> fieldValues) {
        setFieldValue(t, fieldValues, true);
    }

    public static <T> void setFieldValue(T t, Map<String, Object> fieldValues, boolean ignoreException) {
        if (ObjectUtils.anyNull(t, fieldValues) || ObjectUtils.isEmpty(fieldValues)) {
            return;
        }
        final Class<?> clazz = t.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (fieldValues.containsKey(field.getName())) {
                Object fieldValue = fieldValues.get(field.getName());
                resetPropertyValue(t, ignoreException, clazz, field, fieldValue);
            }
        }
    }

    public static <T> void setFieldValue(T t, String fieldName, Object fieldValue) {
        setFieldValue(t, fieldName, fieldValue, false);
    }

    public static <T> void setFieldValue(T t, String fieldName, Object fieldValue, boolean ignoreException) {
        if (ObjectUtils.anyNull(t, fieldValue)) {
            return;
        }
        final Class<?> clazz = t.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (Objects.equals(field.getName(), fieldName)) {
                resetPropertyValue(t, ignoreException, clazz, field, fieldValue);
                break;
            }
        }
    }

    public static <T> Optional<Object> getFieldValue(Object t, String fieldName) {
        if (ObjectUtils.anyNull(t, fieldName) || StringUtils.isBlank(fieldName)) {
            return Optional.empty();
        }
        Optional<Field> optional = Arrays.stream(t.getClass().getDeclaredFields())
                .filter(field -> Objects.equals(field.getName(), fieldName))
                .findFirst();
        if (optional.isPresent()) {
            Field field = optional.get();
            field.setAccessible(true);
            try {
                return Optional.of(field.get(t));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

    public static <T> Optional<Map<String, Object>> getFieldValue(Object t, String... fieldNames) {
        if (ObjectUtils.anyNull(t, fieldNames)) {
            return Optional.empty();
        }
        List<Field> fields = Arrays.stream(t.getClass().getDeclaredFields()).collect(Collectors.toList());
        Map<String, Object> result = new HashMap<>();
        for (String fieldName : fieldNames) {
            fields.stream()
                    .filter(field -> Objects.equals(field.getName(), fieldName))
                    .findFirst()
                    .ifPresent(field -> {
                        field.setAccessible(true);
                        try {
                            result.put(fieldName, field.get(t));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                            result.put(fieldName, null);
                        }
                    });
        }
        return Optional.of(result);
    }

    public static <T> Optional<Map<String, Object>> getFieldValue0(Object t, String... fieldNames) {
        if (ObjectUtils.anyNull(t, fieldNames)) {
            return Optional.empty();
        }
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(t));
        Map<String, Object> result = new HashMap<>();
        for (String fieldName : fieldNames) {
            result.put(fieldName, jsonObject.get(fieldName));
        }

        return Optional.of(result);
    }

    //region 私有方法

    private static <T> void resetPropertyValue(T t, boolean ignoreException, Class<?> clazz, Field field, Object fieldValue) {
        try {
            PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
            // 获得set方法
            final Method writeMethod = pd.getWriteMethod();
            final Optional<?> optional = ConvertUtils.convertIfNecessary(fieldValue, field.getType());
            if (optional.isPresent()) {
                writeMethod.invoke(t, optional.get());
            }
        } catch (IllegalAccessException | InvocationTargetException | IntrospectionException e) {
            e.printStackTrace();
            if (!ignoreException) {
                throw new RuntimeException(e);
            }
        }
    }

}
