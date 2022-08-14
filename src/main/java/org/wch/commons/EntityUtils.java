package org.wch.commons;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.wch.commons.lang.ObjectUtils;
import org.wch.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: TODO 新增的
 * @Author: wuchu
 * @CreateTime: 2022-07-20 16:44
 */
public class EntityUtils {

    public static <T> void setFieldValue(T t, Map<String, Object> fieldValues) {
        setFieldValue(t, fieldValues, false);
    }

    public static <T> void setFieldValue(T t, Map<String, Object> fieldValues, boolean ignoreException) {
        if (ObjectUtils.anyNull(t, fieldValues) || ObjectUtils.isEmpty(fieldValues)) {
            return;
        }
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            Object value = fieldValues.get(field.getName());
            if (ObjectUtils.nonNull(value)) {
                field.setAccessible(true);
                try {
                    field.set(t, value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    if (!ignoreException) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    @Deprecated
    public static <T> Optional<T> resetFieldValue(T t, Map<String, Object> fieldValues, boolean ignoreException) {
        if (ObjectUtils.anyNull(t, fieldValues) || ObjectUtils.isEmpty(fieldValues)) {
            return Optional.empty();
        }
        try {
            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(t));
            fieldValues.forEach(jsonObject::put);
            return Optional.of(jsonObject.toJavaObject((Type) t.getClass()));
        } catch (Exception e) {
            if (!ignoreException) {
                throw new RuntimeException(e);
            }
            return Optional.empty();
        }
    }

    public static <T> void setFieldValue(T t, String fieldName, Object fieldValue) {
        setFieldValue(t, fieldName, fieldValue, false);
    }

    public static <T> void setFieldValue(T t, String fieldName, Object fieldValue, boolean ignoreException) {
        if (ObjectUtils.anyNull(t, fieldValue)) {
            return;
        }
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (Objects.equals(field.getName(), fieldName)) {
                field.setAccessible(true);
                try {
                    field.set(t, fieldValue);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    if (!ignoreException) {
                        throw new RuntimeException(e);
                    }
                }
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

}
