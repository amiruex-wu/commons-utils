package org.wch.commons.conversion.converter;

import lombok.NoArgsConstructor;
import org.wch.commons.beans.FieldPropertyDescriptor;
import org.wch.commons.conversion.SimpleTypeConverter;
import org.wch.commons.lang.ObjectUtils;

import java.lang.reflect.*;
import java.util.*;
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
        return convert(source, requiredType);
    }

    // todo 待重新实现算法
    private Optional<T> convert(Object source, Class<T> requiredType) {
        if (ObjectUtils.anyNull(source, requiredType)) {
            return Optional.empty();
        }
        System.out.println("ObjectConvert执行过程....");

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
                final Field sourceField = sourceFieldPD.getField();
                sourceField.setAccessible(true);
                final Field targetField = targetFieldPD.getField();
                targetField.setAccessible(true);
                final Object invoke = sourceField.get(source);
                if (Objects.isNull(invoke)) {
                    continue;
                }

                // 判断双方是相同类型
                if (ObjectUtils.equals(targetFieldPD.getType(), sourceFieldPD.getType())) {
                    targetField.set(target, invoke);
                } else if (basicClass.contains(sourceField.getType()) && basicClass.contains(targetField.getType())) {
                    // 判断双方都是基础类型
                    final Optional<?> optional1 = SimpleTypeConverter.convertIfNecessary(invoke, targetField.getType());
                    targetField.set(target, optional1.orElse(null));
                } else if (invoke instanceof Collection && Collection.class.isAssignableFrom(targetField.getType())) {
                    // 双方都是集合类型
                    Collection collection = (Collection) invoke;
                    if (Objects.equals(sourceFieldPD.getListActualTypeArgument(), targetFieldPD.getListActualTypeArgument())) {
                        targetField.set(target, collection);
                    } else {
                        List<Object> list = new ArrayList<>();
                        for (Object obj : collection) {
                            final Optional<?> optional1 = convert0(obj, targetFieldPD.getListActualTypeArgument());
                            optional1.ifPresent(list::add);
                        }
                        targetField.set(target, list);
                    }
                } else {
                    // 其他类型
                    final Optional<?> optional1 = SimpleTypeConverter.convertIfNecessary(invoke, targetField.getType());
                    targetField.set(target, optional1.orElse(null));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return Optional.of((T) target);
    }

    private Optional<?> convert0(Object source, Class<?> requiredType) {
        if (ObjectUtils.anyNull(source, requiredType)) {
            return Optional.empty();
        }

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
                final Field sourceField = sourceFieldPD.getField();
                sourceField.setAccessible(true);
                final Field targetField = targetFieldPD.getField();
                targetField.setAccessible(true);
                final Object invoke = sourceField.get(source);
                if (Objects.isNull(invoke)) {
                    continue;
                }

                // 判断双方是相同类型
                if (ObjectUtils.equals(targetFieldPD.getType(), sourceFieldPD.getType())) {
                    targetField.set(target, invoke);
                } else if (basicClass.contains(sourceField.getType()) && basicClass.contains(targetField.getType())) {
                    // 判断双方都是基础类型
                    final Optional<?> optional1 = SimpleTypeConverter.convertIfNecessary(invoke, targetField.getType());
                    targetField.set(target, optional1.orElse(null));
                } else if (invoke instanceof Collection && Collection.class.isAssignableFrom(targetField.getType())) {
                    // 双方都是集合类型
                    Collection collection = (Collection) invoke;
                    if (Objects.equals(sourceFieldPD.getListActualTypeArgument(), targetFieldPD.getListActualTypeArgument())) {
                        targetField.set(target, collection);
                    } else {
                        List<Object> list = new ArrayList<>();
                        for (Object obj : collection) {
                            final Optional<?> optional1 = convert0(obj, targetFieldPD.getListActualTypeArgument());
                            optional1.ifPresent(item -> list.add(item));
                        }
                        targetField.set(target, list);
                    }
                } else {
                    // 其他类型
                    final Optional<?> optional1 = SimpleTypeConverter.convertIfNecessary(invoke, targetField.getType());
                    targetField.set(target, optional1.orElse(null));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
      /*


        final Class<?> sourceClass = source.getClass();
        final Field[] sourceFields = sourceClass.getDeclaredFields();
        final Field[] targetFields = requiredType.getDeclaredFields();
        final List<Field> fields = Arrays.asList(sourceFields);


        for (Field targetField : targetFields) {
            final Optional<Field> optional = fields.stream()
                    .filter(item -> Objects.equals(targetField.getName(), item.getName()))
                    .findAny();
            if (optional.isPresent()) {
                final Field sourceField = optional.get();
                try {
                    PropertyDescriptor sourcePd = new PropertyDescriptor(sourceField.getName(), sourceClass);
                    PropertyDescriptor targetPd = new PropertyDescriptor(targetField.getName(), requiredType);
                    // 获得get方法
                    Method getMethod = sourcePd.getReadMethod();

                    // 此处为执行该Object对象的get方法
                    Object invoke = getMethod.invoke(source);
                    Method writeMethod = targetPd.getWriteMethod();

                    // 判断双方都是基础类型且相同类型
                    if (basicClass.contains(sourceField.getType())
                            && basicClass.contains(targetField.getType())
                            && ObjectUtils.equals(sourceField.getType(), targetField.getType())) {
                        writeMethod.invoke(target, invoke);
                    } else if (basicClass.contains(sourceField.getType()) && basicClass.contains(targetField.getType())) {
                        // 判断双方都是基础类型
                        final Optional<?> optional1 = ConvertFactory.getConverter0(invoke, targetField.getType());
                        writeMethod.invoke(target, optional1.orElse(null));
                    } else if (invoke instanceof Collection && Collection.class.isAssignableFrom(targetField.getType())) {
                        // 双方都是集合类型
                        Collection collection = (Collection) invoke;
                        Type genericType = targetField.getGenericType();
                        if (genericType instanceof ParameterizedType) {
                            ParameterizedType pt = (ParameterizedType) genericType;
                            // 得到泛型里的class类型对象
                            Class<?> actualTypeArgument = (Class<?>) pt.getActualTypeArguments()[0];

                            if (basicClass.contains(actualTypeArgument)) {
                                writeMethod.invoke(target, collection);
                            } else {
                                List<Object> list = new ArrayList<>();
                                for (Object obj1 : collection) {
                                    Optional<AbstractConverter<?>> converter = ConvertFactory.getConverter0(obj1, targetField.getType());
                                    converter.flatMap(AbstractConverter::convert).ifPresent(list::add);
                                }
                                writeMethod.invoke(target, list);
                            }
                        }
                    } else if (!basicClass.contains(sourceField.getType())
                            && !basicClass.contains(targetField.getType())
                            && ObjectUtils.equals(targetField.getType(), sourceField.getType())) {
                        // 普通自定义对象
                        Optional<AbstractConverter<?>> converter = ConvertFactory.getConverter0(invoke, targetField.getType());
                        if (converter.isPresent()) {
                            final Optional<?> convert = converter.get().convert();
                            writeMethod.invoke(target, convert.orElse(null));
                        }
                    } else {
                        // 其他类型
                        final Optional<AbstractConverter<?>> converter0 = ConvertFactory.getConverter0(invoke, targetField.getType());
                        if (converter0.isPresent()) {
                            final Optional<?> convert = converter0.get().convert();
                            writeMethod.invoke(target, convert.orElse(null));
                        }
                    }
                } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }*/
        return Optional.of((T) target);
    }

    public static FieldPropertyDescriptor[] getPropertyDescriptor(Class<?> clazz) {
        if (classFieldPD.containsKey(clazz)) {
            return classFieldPD.get(clazz);
        }
        final Field[] declaredFields = clazz.getDeclaredFields();
        int length = declaredFields.length;
        FieldPropertyDescriptor[] var1 = new FieldPropertyDescriptor[length];
        for (int i = 0; i < length; i++) {
            var1[i] = new FieldPropertyDescriptor(declaredFields[i]);
        }
        System.out.println("jinru。。。。。1-2");
        classFieldPD.put(clazz, var1);
        return var1;
    }

    public static <T> Optional<FieldPropertyDescriptor> getPropertyDescriptor(Class<?> clazz, String propertyName) {
        if (classFieldPD.containsKey(clazz)) {
            return Stream.of(classFieldPD.get(clazz)).filter(item -> Objects.equals(item.getName(), propertyName)).findFirst();
        }
        FieldPropertyDescriptor result = null;
        final Field[] fields = clazz.getDeclaredFields();
        int length = fields.length;
        FieldPropertyDescriptor[] var2 = new FieldPropertyDescriptor[length];
        for (int i = 0; i < length; i++) {
            var2[i] = new FieldPropertyDescriptor(fields[i]);
            if (Objects.equals(fields[i].getName(), propertyName)) {
                result = var2[i];
            }
        }
        System.out.println("jinru。。。。。1-2-1");
        classFieldPD.put(clazz, var2);
        return Optional.ofNullable(result);
    }


}
