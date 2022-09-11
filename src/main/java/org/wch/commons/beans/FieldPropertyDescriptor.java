package org.wch.commons.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.wch.commons.lang.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Objects;

/**
 * @Description: TODO
 * @Author: wuchu
 * @Version: 1.0
 * @CreateTime: 2022/9/4 18:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldPropertyDescriptor {

    private String name;

    private Field field;

    private Class<?> type;

    private Method readMethod;

    private Method writerMethod;

    private Class<?> listActualTypeArgument;

    @Deprecated
    public FieldPropertyDescriptor(Field field) {
        this.field = field;
        if (Objects.nonNull(field)) {
            this.name = field.getName();
            this.type = field.getType();
        }
        if (Objects.nonNull(field) && Collection.class.isAssignableFrom(field.getType())) {
            Type genericType = field.getGenericType();
            if (genericType instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) genericType;
                // 得到泛型里的class类型对象
                this.listActualTypeArgument = (Class<?>) pt.getActualTypeArguments()[0];
            }
        }
    }

    @Deprecated
    public FieldPropertyDescriptor(Field field, PropertyDescriptor propertyDescriptor) {
        this.field = field;
//        this.descriptor = propertyDescriptor;
        if (Objects.nonNull(field) && Collection.class.isAssignableFrom(field.getType())) {
            Type genericType = field.getGenericType();
            if (genericType instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) genericType;
                // 得到泛型里的class类型对象
                this.listActualTypeArgument = (Class<?>) pt.getActualTypeArguments()[0];
            }
        }
//        if (Objects.nonNull(propertyDescriptor)) {
//            this.readMethod = propertyDescriptor.getReadMethod();
//            this.writeMethod = propertyDescriptor.getWriteMethod();
//        }
    }


    public FieldPropertyDescriptor(PropertyDescriptor propertyDescriptor) {
        if (Objects.nonNull(propertyDescriptor)) {
            StringUtils.capitalize(propertyDescriptor.getName())
                    .ifPresent(str -> this.name = "set" + str);
            this.type = propertyDescriptor.getPropertyType();
            this.readMethod = propertyDescriptor.getReadMethod();
            this.writerMethod = propertyDescriptor.getWriteMethod();
            if (Collection.class.isAssignableFrom(propertyDescriptor.getPropertyType())) {
                Type clazz = propertyDescriptor.getWriteMethod().getGenericParameterTypes()[0];
                ParameterizedType pt = (ParameterizedType) clazz;
                String s1 = pt.getActualTypeArguments()[0].toString();
                try {
                    // 得到泛型里的class类型对象
                    this.listActualTypeArgument = Class.forName(s1.substring(5).replaceAll(" ", ""));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
