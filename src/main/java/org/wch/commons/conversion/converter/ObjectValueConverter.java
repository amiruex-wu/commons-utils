package org.wch.commons.conversion.converter;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.wch.commons.BeanUtils;
import org.wch.commons.beans.FieldPropertyDescriptor;
import org.wch.commons.conversion.SimpleTypeConverter;
import org.wch.commons.lang.ObjectUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

/**
 * @Description: 普通对象类型转换器
 * @Author: wuchu
 * @CreateTime: 2022-07-13 17:22
 */
@NoArgsConstructor
public class ObjectValueConverter<T> extends AbstractConverter<T> {

    public ObjectValueConverter(Object source, Class<T> requiredType) {
        super(source, requiredType);
    }

    @Override
    public Optional<T> convert() {
        return (Optional<T>) convert(source, requiredType);
    }

    // todo 待重新实现算法
    private Optional<?> convert(Object source, Class<?> requiredType) {
        if (ObjectUtils.anyNull(source, requiredType) || Class.class.equals(requiredType)) {
            return Optional.empty();
        }
//        System.out.println("ObjectConvert执行过程....");

        // 获得属性
        final Class<?> sourceClass = source.getClass();
        final Object target;
        try {
            target = requiredType.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return Optional.empty();
        }
        final FieldPropertyDescriptor[] propertyDescriptor = getPropertyDescriptor(requiredType);
        for (FieldPropertyDescriptor targetFieldPD : propertyDescriptor) {
            final Optional<FieldPropertyDescriptor> optional = getPropertyDescriptor(sourceClass, targetFieldPD.getName());
            if (!optional.isPresent()) {
                continue;
            }
            FieldPropertyDescriptor sourceFieldPD = optional.get();
            try {
                final Method readMethod = sourceFieldPD.getReadMethod();
                final Method writerMethod = targetFieldPD.getWriterMethod();
                final Object invoke = readMethod.invoke(source);
                if (Objects.isNull(invoke)) {
                    continue;
                }
                if (Objects.equals(sourceFieldPD.getType(), targetFieldPD.getType())
                        && (BeanUtils.GENERAL_CLASS.contains(targetFieldPD.getType())
                        || BeanUtils.DATE_TIME_CLASS.contains(targetFieldPD.getType()))) {
                    writerMethod.invoke(target, invoke);
                } else if (Objects.equals(sourceFieldPD.getType(), targetFieldPD.getType())
                        && Map.class.isAssignableFrom(targetFieldPD.getType())) {
                    final HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
                    final Map<Object, Object> temp = (Map<Object, Object>) invoke;
                    for (Map.Entry<Object, Object> entry : temp.entrySet()) {
                        objectObjectHashMap.put(entry.getKey(), entry.getValue());
                    }
                    writerMethod.invoke(target, objectObjectHashMap);
                } else if (Objects.equals(sourceFieldPD.getType(), targetFieldPD.getType())
                        && Collection.class.isAssignableFrom(targetFieldPD.getType())) {
                    // 同为集合对象
                    final Object object = getObjectByRequireType(invoke, sourceFieldPD, targetFieldPD);
                    writerMethod.invoke(target, object);
                } else if ((BeanUtils.GENERAL_CLASS.contains(targetFieldPD.getType()) && BeanUtils.GENERAL_CLASS.contains(sourceFieldPD.getType()))
                        || (BeanUtils.GENERAL_CLASS.contains(targetFieldPD.getType()) && BeanUtils.DATE_TIME_CLASS.contains(sourceFieldPD.getType()))
                        || (BeanUtils.DATE_TIME_CLASS.contains(targetFieldPD.getType()) && BeanUtils.DATE_TIME_CLASS.contains(sourceFieldPD.getType()))
                        || (BeanUtils.DATE_TIME_CLASS.contains(targetFieldPD.getType()) && BeanUtils.GENERAL_CLASS.contains(sourceFieldPD.getType()))) {
                    // all of those are general class
                    final Optional<?> result1 = SimpleTypeConverter.convertIfNecessary(invoke, targetFieldPD.getType());
                    if (result1.isPresent()) {
                        writerMethod.invoke(target, result1.get());
                    }
                } else if (Collection.class.isAssignableFrom(targetFieldPD.getType())
                        && Collection.class.isAssignableFrom(sourceFieldPD.getType())) {
                    // 同为集合对象
                    final Object object = getObjectByRequireType(invoke, sourceFieldPD, targetFieldPD);
                    writerMethod.invoke(target, object);
                } else if (!Map.class.isAssignableFrom(targetFieldPD.getType())
                        && !Map.class.isAssignableFrom(sourceFieldPD.getType())) {
                    // 自定义对象类型
                  /*  Object targetTemp = targetFieldPD.getType().newInstance();
                    BeanUtils.copyProperties(invoke, targetTemp);*/

                    final Optional<?> convert = convert(invoke, targetFieldPD.getType());
                    if (convert.isPresent()) {
                        writerMethod.invoke(target, convert.get());
                    }
                }

            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return Optional.of((T) target);
    }

    /**
     * 获取类中的所有属性
     *
     * @param clazz
     * @return
     */
    @SneakyThrows
    public static FieldPropertyDescriptor[] getPropertyDescriptor(Class<?> clazz) {
        if (classFieldPD.containsKey(clazz)) {
            return classFieldPD.get(clazz);
        }
        final BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
        final PropertyDescriptor[] var1 = beanInfo.getPropertyDescriptors();

        final FieldPropertyDescriptor[] var2 = Arrays.stream(var1)
                .filter(item -> !Class.class.equals(item.getPropertyType()))
                .map(FieldPropertyDescriptor::new)
                .toArray(FieldPropertyDescriptor[]::new);
        classFieldPD.put(clazz, var2);
        return var2;
    }

    /**
     * 获取类中定义的指定属性
     *
     * @param clazz
     * @param propertyName
     * @return
     */
    @SneakyThrows
    public static Optional<FieldPropertyDescriptor> getPropertyDescriptor(Class<?> clazz, String propertyName) {
        if (classFieldPD.containsKey(clazz)) {
            return Stream.of(classFieldPD.get(clazz))
                    .filter(item -> Objects.nonNull(item) && Objects.equals(item.getName(), propertyName))
                    .findFirst();
        }
        AtomicReference<FieldPropertyDescriptor> result = new AtomicReference<>(null);
        final BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
        final PropertyDescriptor[] var1 = beanInfo.getPropertyDescriptors();

        final FieldPropertyDescriptor[] var2 = Arrays.stream(var1)
                .filter(item -> !Class.class.equals(item.getPropertyType()))
                .map(item -> {
                    final FieldPropertyDescriptor temp = new FieldPropertyDescriptor(item);
                    if (Objects.equals(temp.getName(), propertyName)) {
                        result.set(temp);
                    }
                    return temp;
                })
                .toArray(FieldPropertyDescriptor[]::new);
        classFieldPD.put(clazz, var2);
        return Optional.ofNullable(result.get());
    }

    /**
     * TODO 转换集合类型的数据
     *
     * @param source
     * @param sourcePD
     * @param targetPD
     * @return
     */
    private Object getObjectByRequireType(Object source, FieldPropertyDescriptor sourcePD, FieldPropertyDescriptor targetPD) {
        final Collection<Object> source1 = (Collection<Object>) source;
        final Class<?> sourceActualParamType = sourcePD.getListActualTypeArgument();
        final Class<?> targetActualParamType = targetPD.getListActualTypeArgument();
        final boolean isSetCollection = Set.class.isAssignableFrom(targetPD.getType());
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
}
