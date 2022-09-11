package org.wch.commons.conversion;

import com.sun.istack.internal.Nullable;
import lombok.*;
import net.sf.cglib.core.Converter;
import org.wch.commons.BeanUtils;
import org.wch.commons.beans.ConvertorCurrentClass;
import org.wch.commons.beans.FieldPropertyDescriptor;
import org.wch.commons.lang.ObjectUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.stream.Stream;

/**
 * @Description: 用于cglib拷贝属性时的自定义转换工具
 * @Author: wuchu
 * @CreateTime: 2022-09-07 14:09
 */
@NoArgsConstructor
@AllArgsConstructor
public class CustomConverter implements Converter {

    private final ThreadLocal<Deque<ConvertorCurrentClass>> currentClasses = new ThreadLocal<>();

    private Class<?> sourceClass;

    private Class<?> targetClass;


    /**
     * custom data converter
     *
     * @param value       source data
     * @param targetClass target param type
     * @param var3        target param setter
     * @return
     */
    @SneakyThrows
    @Override
    public Object convert(Object value, Class targetClass, Object var3) {
        if (Objects.isNull(value)) {
            return null;
        }
       /* System.out.println("source data is:" + value);
        System.out.println("target param type is:" + targetClass);
        System.out.println("target param setter is:" + var3 + ", type is " + var3.getClass());*/
        if (ObjectUtils.isNull(currentClasses.get())) {
            return null;
        } else {
            final ConvertorCurrentClass peek = currentClasses.get().peek();
            if (ObjectUtils.isNull(peek)) {
                return null;
            }
            this.sourceClass = peek.getTargetClass();
            this.targetClass = peek.getTargetClass();
        }
        return convert0(value, var3.toString(), targetClass);
    }

    public void addCurrentClasses(ConvertorCurrentClass convertorCurrentClass) {
        final Deque<ConvertorCurrentClass> convertorCurrentClasses = currentClasses.get();
        if (ObjectUtils.isNull(convertorCurrentClasses)) {
            currentClasses.set(new ArrayDeque<>(64));
        }
        currentClasses.get().push(convertorCurrentClass);
    }

    public void clear() {
        currentClasses.remove();
    }

    public void pop() {
        if (ObjectUtils.nonNull(currentClasses.get()) && !currentClasses.get().isEmpty()) {
            currentClasses.get().pop();
        }
        if (currentClasses.get().isEmpty()) {
            currentClasses.remove();
        }
    }

    // region 私有方法
    private Optional<FieldPropertyDescriptor[]> getPropertyDescriptor(Class<?> clazz) {
        if (BeanUtils.classFieldPD.containsKey(clazz)) {
            return Optional.ofNullable(BeanUtils.classFieldPD.get(clazz));
        }
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] var1 = beanInfo.getPropertyDescriptors();
            final int length = var1.length;
            FieldPropertyDescriptor[] var2 = new FieldPropertyDescriptor[length];
            for (int i = 0; i < length; i++) {
                var2[i] = new FieldPropertyDescriptor(var1[i]);
               /* if (!Objects.equals("class", var1[i].getName())) {
                }*/
            }
            BeanUtils.classFieldPD.put(clazz, var2);
            return Optional.of(var2);
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private Optional<FieldPropertyDescriptor> getPropertyDescriptor(Class<?> clazz, String propertyName) {
        /*if (Objects.equals(clazz, Class.class)) {
            return Optional.empty();
        }*/
        if (BeanUtils.classFieldPD.containsKey(clazz)) {
            final Optional<FieldPropertyDescriptor> first = Stream.of(BeanUtils.classFieldPD.get(clazz))
                    .filter(item -> ObjectUtils.nonNull(item) && Objects.equals(item.getName(), propertyName))
                    .findAny();
            return first;
        }
        try {
            FieldPropertyDescriptor result = null;
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] var1 = beanInfo.getPropertyDescriptors();
            final int length = var1.length;
            FieldPropertyDescriptor[] var2 = new FieldPropertyDescriptor[length];
            for (int i = 0; i < length; i++) {
                var2[i] = new FieldPropertyDescriptor(var1[i]);
                if (Objects.equals(var1[i].getName(), propertyName)) {
                    result = var2[i];
                }
            }
            BeanUtils.classFieldPD.put(clazz, var2);
            return Optional.ofNullable(result);
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @SneakyThrows
    private Object convert0(Object source, String fieldName, Class<?> requireType) {

        Class<?> sourceClass = source.getClass();
        if (Objects.equals(sourceClass, requireType)
                && (BeanUtils.GENERAL_CLASS.contains(requireType) || BeanUtils.DATE_TIME_CLASS.contains(requireType))) {
            return source;
        } else if (Objects.equals(sourceClass, requireType) && Map.class.isAssignableFrom(requireType)) {
            final HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
            final Map<Object, Object> temp = (Map<Object, Object>) source;
            for (Map.Entry<Object, Object> entry : temp.entrySet()) {
                objectObjectHashMap.put(entry.getKey(), entry.getValue());
            }
            return objectObjectHashMap;
        } else if (Objects.equals(sourceClass, requireType) && Collection.class.isAssignableFrom(requireType)) {
            // 同为集合对象
            return getObjectByRequireType(source, fieldName, requireType);
        } else if ((BeanUtils.GENERAL_CLASS.contains(requireType) && BeanUtils.GENERAL_CLASS.contains(sourceClass))
                || (BeanUtils.GENERAL_CLASS.contains(requireType) && BeanUtils.DATE_TIME_CLASS.contains(sourceClass))
                || (BeanUtils.DATE_TIME_CLASS.contains(requireType) && BeanUtils.DATE_TIME_CLASS.contains(sourceClass))
                || (BeanUtils.DATE_TIME_CLASS.contains(requireType) && BeanUtils.GENERAL_CLASS.contains(sourceClass))) {
            // all of those are general class
            final Optional<?> result1 = SimpleTypeConverter.convertIfNecessary(source, requireType);
            return result1.map(requireType::cast).orElse(null);
        } else if (Collection.class.isAssignableFrom(requireType) && Collection.class.isAssignableFrom(sourceClass)) {
            // 同为集合对象
            return getObjectByRequireType(source, fieldName, requireType);
        } else if (!Map.class.isAssignableFrom(requireType) && !Map.class.isAssignableFrom(sourceClass)) {
            // 自定义对象类型
            Object target = requireType.newInstance();
            BeanUtils.copyProperties(source, target);
            return target;
        } else {
            return null;
        }
    }

    @Nullable
    private Object getObjectByRequireType(Object source, String fieldName, Class<?> requireType) {
        final Collection<Object> source1 = (Collection<Object>) source;

        final Optional<FieldPropertyDescriptor> sourcePd = getPropertyDescriptor(sourceClass, fieldName);
        final Optional<FieldPropertyDescriptor> targetPd = getPropertyDescriptor(targetClass, fieldName);
        if (!sourcePd.isPresent() || !targetPd.isPresent()) {
            return null;
        }
        final Class<?> sourceActualParamType = sourcePd.get().getListActualTypeArgument();
        final Class<?> targetActualParamType = targetPd.get().getListActualTypeArgument();
        final boolean isSetCollection = Set.class.isAssignableFrom(requireType);
        Collection<Object> result;
        if (isSetCollection) {
            result = new HashSet<>();
        } else {
            result = new ArrayList<>();
        }
        if (Objects.equals(sourceActualParamType, targetActualParamType) && BeanUtils.GENERAL_CLASS.contains(targetActualParamType)) {
            result.addAll(source1);
        } else {
            source1.stream()
                    .filter(Objects::nonNull)
                    .map(o -> {
                        try {
                            Object targetTemp = targetActualParamType.newInstance();
                            BeanUtils.copyProperties(o, targetTemp);
                            return targetTemp;
                        } catch (InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .forEach(result::add);
        }
        return result;
    }

// endregion
}
