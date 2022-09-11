package org.wch.commons;

import lombok.Data;
import org.wch.commons.beans.FieldPropertyDescriptor;
import org.wch.commons.constant.CommonConstant;
import org.wch.commons.conversion.SimpleTypeConverter;
import org.wch.commons.lang.ConvertUtils;
import org.wch.commons.lang.ObjectUtils;
import org.wch.commons.text.GsonUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description: 属性拷贝工具
 * @Author: wuchu
 * @CreateTime: 2022-07-13 16:48
 */
@Data
@Deprecated
public class BeanUtils12 {

    // 用处不大
    private static Map<Class<?>, Map<String, FieldPropertyDescriptor>> classMap = new ConcurrentHashMap<>(64);
    private static Map<Class<?>, FieldPropertyDescriptor[]> classFieldPD = new ConcurrentHashMap<>(64);
    private static Map<Class<?>, PropertyDescriptor[]> strongClassFieldPD = new ConcurrentHashMap<>(64);

//    private Object source;
//
//    private Object target;
//
//    private boolean isExactMatchType = false;

//    private List<FieldPropertyDescriptor> sourceFields;
//
//    private List<FieldPropertyDescriptor> targetFields;

    /*public BeanUtils(Object source, Object target) {
        this.source = source;
        this.target = target;
        if (ObjectUtils.anyNull(source, target)) {

            return;
        }
        if (ObjectUtils.isNull(threadLocal.get())) {
            threadLocal.set(new ArrayList<>());
        }
//        if (ObjectUtils.nonNull(threadLocal.get())) {
        final boolean sourcePresent = threadLocal.get().stream().anyMatch(item -> ObjectUtils.equals(source.getClass(), item.getAClass()));
        if (!sourcePresent) {
            this.sourceFields = getFieldAndPropertyDescriptor(source.getClass());
            threadLocal.get().add(new ClassPropertyDescriptor(source.getClass(), this.sourceFields));
        } else {
            threadLocal.get().stream()
                    .filter(item -> ObjectUtils.equals(item.getAClass(), source.getClass()))
                    .findFirst()
                    .ifPresent(item -> this.sourceFields = item.getFieldPropertyDescriptors());
        }
        final boolean targetPresent = threadLocal.get().stream().anyMatch(item -> ObjectUtils.equals(target.getClass(), item.getAClass()));
        if (!targetPresent) {
            this.targetFields = getFieldAndPropertyDescriptor(target.getClass());
            threadLocal.get().add(new ClassPropertyDescriptor(target.getClass(), this.targetFields));
        } else {
            threadLocal.get().stream()
                    .filter(item -> ObjectUtils.equals(item.getAClass(), target.getClass()))
                    .findFirst()
                    .ifPresent(item -> this.targetFields = item.getFieldPropertyDescriptors());
        }
//        } else {
//
//            this.sourceFields = getFieldAndPropertyDescriptor(source.getClass());
//            this.targetFields = getFieldAndPropertyDescriptor(target.getClass());
//            threadLocal.get().add(new ClassPropertyDescriptor(source.getClass(), this.sourceFields));
//            threadLocal.get().add(new ClassPropertyDescriptor(target.getClass(), this.targetFields));
//        }
    }
*/
    /*public BeanUtils(Object source, Object target, boolean isExactMatchType) {
        this(source, target);
        this.isExactMatchType = isExactMatchType;
    }

    public static BeanUtils of(Object source, Object target) {
        return new BeanUtils(source, target);
    }

    public static BeanUtils of(Object source, Object target, boolean isExactMatchType) {
        return new BeanUtils(source, target, isExactMatchType);
    }

    public void copy() {
        if (ObjectUtils.anyNull(source, target)) {
            return;
        }
        for (FieldPropertyDescriptor targetFieldPD : targetFields) {
            final Optional<FieldPropertyDescriptor> optional = sourceFields.stream()
                    .filter(item1 -> Objects.equals(targetFieldPD.getField().getName(), item1.getField().getName()))
                    .findAny();
            if (optional.isPresent()) {
                FieldPropertyDescriptor sourceFieldPD = optional.get();
                if (!sourceFieldPD.getField().getType().equals(targetFieldPD.getField().getType()) && isExactMatchType) {
                    continue;
                }
                try {
                    Method getMethod = sourceFieldPD.getDescriptor().getReadMethod();//获得get方法
                    final Object invoke = getMethod.invoke(source);//此处为执行该Object对象的get方法
                    final Method writeMethod = targetFieldPD.getDescriptor().getWriteMethod();

                    // 判断双方都是基础类型且相同类型
                    if (basicClass.contains(sourceFieldPD.getField().getType())
                            && basicClass.contains(targetFieldPD.getField().getType())
                            && ObjectUtils.equals(targetFieldPD.getField().getType(), sourceFieldPD.getField().getType())) {
                        writeMethod.invoke(target, invoke);
                    } else if (basicClass.contains(sourceFieldPD.getField().getType()) && basicClass.contains(targetFieldPD.getField().getType())) {
                        // 判断双方都是基础类型
                        final Optional<?> optional1 = ConvertUtils.convertIfNecessary(invoke, targetFieldPD.getField().getType());
                        writeMethod.invoke(target, optional1.orElse(null));
                    } else if (invoke instanceof Collection && Collection.class.isAssignableFrom(targetFieldPD.getField().getType())) {
                        // 双方都是集合类型
                        Collection collection = (Collection) invoke;
                        Type genericType = targetFieldPD.getField().getGenericType();
                        if (genericType instanceof ParameterizedType) {
                            ParameterizedType pt = (ParameterizedType) genericType;
                            // 得到泛型里的class类型对象
                            Class<?> actualTypeArgument = (Class<?>) pt.getActualTypeArguments()[0];

                            if (basicClass.contains(actualTypeArgument)) {
                                writeMethod.invoke(target, collection);
                            } else {
                                List<Object> list = new ArrayList<>();
                                for (Object obj : collection) {
                                    Object obj1 = actualTypeArgument.newInstance();
                                    copyProperties(obj, obj1, isExactMatchType);
                                    list.add(obj1);
                                }
                                writeMethod.invoke(target, list);
                            }
                        }
                    } else if (ObjectUtils.equals(targetFieldPD.getField().getType(), sourceFieldPD.getField().getType())) {
                        // 普通自定义对象
                        if (Objects.equals(targetFieldPD.getField().getType(), List.class)) {
                            if (Objects.nonNull(invoke) && invoke instanceof Collection) {
                                List<Object> list = new ArrayList<>();
                                Collection collection = (Collection) invoke;
                                ParameterizedType pt = (ParameterizedType) targetFieldPD.getField().getGenericType();
                                // 得到泛型里的class类型对象
                                Class<?> actualTypeArgument = (Class<?>) pt.getActualTypeArguments()[0];
                                for (Object obj : collection) {
                                    Object obj1 = actualTypeArgument.newInstance();
                                    copyProperties(obj, obj1, isExactMatchType);
                                    list.add(obj1);
                                }
                                writeMethod.invoke(target, list);
                            }
                        } else {
                            final Object targetObj = targetFieldPD.getField().getType().newInstance();
                            copyProperties(invoke, targetObj, isExactMatchType);
                        }
                    } else {
                        // 其他类型
                        final Optional<?> optional1 = ConvertUtils.convertIfNecessary(invoke, targetFieldPD.getField().getType());
                        writeMethod.invoke(target, optional1.orElse(null));
                    }
                } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
//                    e.printStackTrace();
                }
            }
        }
    }*/

    private final static List<Class<?>> basicClass = Arrays.stream(CommonConstant.BASIC_NUM_STR_CLAZZ).collect(Collectors.toList());
    private final static List<Class<?>> basicDateClass = Arrays.stream(CommonConstant.BASIC_DATE_TIME_CLAZZ).collect(Collectors.toList());

    public static <T> T copyProperties(Object source, Class<T> targetClass) {
        if (ObjectUtils.anyNull(source, targetClass)) {
            return null;
        }
        String jsonStr = GsonUtils.toJSONStringWithNull(source);
        T parse = GsonUtils.parse(jsonStr, targetClass);
        return parse;
    }

    public static void copyProperties(Object source, Object target) {
        copyProperties(source, target, false);
    }

    public static void copyProperties(Object source, Object target, boolean isExactMatchType) {
        if (ObjectUtils.anyNull(source, target)) {
            return;
        }
        // 获得数据实体类名
        final Class<?> sourceClass = source.getClass();
        final Class<?> targetClass = target.getClass();

        // 获得属性
        final Field[] sourceFields = sourceClass.getDeclaredFields();
        final Field[] targetFields = targetClass.getDeclaredFields();
        final List<Field> collect1 = Stream.of(targetFields).collect(Collectors.toList());

        for (Field sourceField : sourceFields) {
            final Optional<Field> optional = collect1.stream()
                    .filter(targetField -> ObjectUtils.equals(targetField.getName(), sourceField.getName()))
                    .findFirst();
            if (optional.isPresent()) {
                try {
                    final Field targetField = optional.get();
                    if (!sourceField.getType().equals(targetField.getType()) && isExactMatchType) {
                        continue;
                    }

                    PropertyDescriptor sourcePd = new PropertyDescriptor(sourceField.getName(), sourceClass);
                    PropertyDescriptor targetPd = new PropertyDescriptor(targetField.getName(), targetClass);

                    Method getMethod = sourcePd.getReadMethod();//获得get方法
                    if (!Modifier.isPublic(getMethod.getDeclaringClass().getModifiers())) {
                        getMethod.setAccessible(true);
                    }
                    final Object invoke = getMethod.invoke(source);//此处为执行该Object对象的get方法
                    final Method writeMethod = targetPd.getWriteMethod();
                    if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                        writeMethod.setAccessible(true);
                    }

                    // 判断双方都是基础类型且相同类型
                    if (basicClass.contains(sourceField.getType())
                            && basicClass.contains(targetField.getType())
                            && ObjectUtils.equals(sourceField.getType(), targetField.getType())) {
                        writeMethod.invoke(target, invoke);
                    } else if (basicClass.contains(sourceField.getType()) && basicClass.contains(targetField.getType())) {
                        // 判断双方都是基础类型
                        final Optional<?> optional1 = ConvertUtils.convertIfNecessary(invoke, targetField.getType());
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
                                for (Object obj : collection) {
                                    Object obj1 = actualTypeArgument.newInstance();
                                    copyProperties(obj, obj1, isExactMatchType);
                                    list.add(obj1);
                                }
                                writeMethod.invoke(target, list);
                            }
                        }
                    } else if (ObjectUtils.equals(targetField.getType(), sourceField.getType())) {
                        // 普通自定义对象
                        if (Objects.equals(targetField.getType(), List.class)) {
                            if (Objects.nonNull(invoke) && invoke instanceof Collection) {
                                List<Object> list = new ArrayList<>();
                                Collection collection = (Collection) invoke;
                                ParameterizedType pt = (ParameterizedType) targetField.getGenericType();
                                // 得到泛型里的class类型对象
                                Class<?> actualTypeArgument = (Class<?>) pt.getActualTypeArguments()[0];
                                for (Object obj : collection) {
                                    Object obj1 = actualTypeArgument.newInstance();
                                    copyProperties(obj, obj1, isExactMatchType);
                                    list.add(obj1);
                                }
                                writeMethod.invoke(target, list);
                            }
                        } else {
                            final Object targetObj = targetField.getType().newInstance();
                            copyProperties(invoke, targetObj, isExactMatchType);
                        }
                    } else {
                        // 其他类型
                        final Optional<?> optional1 = ConvertUtils.convertIfNecessary(invoke, targetField.getType());
                        writeMethod.invoke(target, optional1.orElse(null));
                    }
                } catch (IntrospectionException | IllegalAccessException
                        | InvocationTargetException | InstantiationException e) {
//                    e.printStackTrace();
                }
            }
        }
    }

    private static List<FieldPropertyDescriptor> getFieldAndPropertyDescriptor(Class<?> clazz) {
        return Stream.of(clazz.getDeclaredFields())
                .map(field -> {
//                    PropertyDescriptor propertyDescriptor;
//                    try {
//                        propertyDescriptor = new PropertyDescriptor(field.getName(), clazz);
//                    } catch (IntrospectionException e) {
//                        propertyDescriptor = null;
//                    }
                    return new FieldPropertyDescriptor(field);
                })
                .collect(Collectors.toList());
    }

    private static Map<String, FieldPropertyDescriptor> getFieldAndPropertyDescriptor1(Class<?> clazz) {
        if (classMap.containsKey(clazz)) {
            return classMap.get(clazz);
        }
        final Map<String, FieldPropertyDescriptor> collect = Stream.of(clazz.getDeclaredFields())
                .map(field -> {
                    PropertyDescriptor propertyDescriptor;
                    try {
                        propertyDescriptor = new PropertyDescriptor(field.getName(), clazz);
                    } catch (IntrospectionException e) {
                        propertyDescriptor = null;
                    }
                    return new FieldPropertyDescriptor(field, propertyDescriptor);
                })
                .collect(Collectors.toMap(field -> field.getField().getName(), field -> field, (oldData, newData) -> newData));
        System.out.println("jinru。。。。。");
        classMap.put(clazz, collect);
        return collect;
    }

    private static FieldPropertyDescriptor[] getFieldAndPropertyDescriptor2(Class<?> clazz) {
        if (classFieldPD.containsKey(clazz)) {
            return classFieldPD.get(clazz);
        }
        final Field[] declaredFields = clazz.getDeclaredFields();
        int length = declaredFields.length;
        FieldPropertyDescriptor[] var1 = new FieldPropertyDescriptor[length];
        for (int i = 0; i < length; i++) {
            var1[i] = new FieldPropertyDescriptor(declaredFields[i]);
        }
        System.out.println("jinru。。。。。");
        classFieldPD.put(clazz, var1);
        return var1;
    }

    private static Optional<FieldPropertyDescriptor> getFieldAndPropertyDescriptor2(Class<?> clazz, String propertyName) {
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
        System.out.println("jinru。。。。。1");
        classFieldPD.put(clazz, var2);
        return Optional.ofNullable(result);
    }

    private static PropertyDescriptor[] getFieldAndPropertyDescriptor3(Class<?> clazz) {
        if (strongClassFieldPD.containsKey(clazz)) {
            return strongClassFieldPD.get(clazz);
        }

        try {
            // Introspector(内省) 是 jdk 提供的用于描述 Java bean 支持的属性、方法以及事件的工具；利用此类可得到 BeanInfo 接口的实现对象
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
            System.out.println("strongClassFieldPD jinru。。。。。");
            strongClassFieldPD.put(clazz, descriptors);
            return descriptors;
        } catch (IntrospectionException e) {
            e.printStackTrace();
            return new PropertyDescriptor[0];
        }
    }

    private static Optional<PropertyDescriptor> getFieldAndPropertyDescriptor3(Class<?> clazz, String propertyName) {
        if (strongClassFieldPD.containsKey(clazz)) {
            return Stream.of(strongClassFieldPD.get(clazz)).filter(item -> Objects.equals(item.getName(), propertyName)).findFirst();
        }
        try {
            // Introspector(内省) 是 jdk 提供的用于描述 Java bean 支持的属性、方法以及事件的工具；利用此类可得到 BeanInfo 接口的实现对象
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
            System.out.println("strongClassFieldPD jinru 1。。。。。");
            strongClassFieldPD.put(clazz, descriptors);
            return Stream.of(strongClassFieldPD.get(clazz))
                    .filter(item -> Objects.equals(item.getName(), propertyName)).findFirst();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return Optional.empty();
        /*
        final Field[] fields = clazz.getDeclaredFields();
        int length = fields.length;
        FieldPropertyDescriptor[] var2 = new FieldPropertyDescriptor[length];
        for (int i = 0; i < length; i++) {
            var2[i] = new FieldPropertyDescriptor(fields[i]);
            if (Objects.equals(fields[i].getName(), propertyName)) {
                result = var2[i];
            }
        }
        System.out.println("jinru。。。。。1");
        classFieldPD.put(clazz, var2);
        return Optional.ofNullable(result);*/
    }


/*
    public static void copyProperties2(Object source, Object target, boolean isExactMatchType) {
        if (ObjectUtils.anyNull(source, target)) {
            return;
        }
        // 获得数据实体类名
        final Class<?> sourceClass = source.getClass();
        final Class<?> targetClass = target.getClass();

        // 获得属性
        List<FieldPropertyDescriptor> sourceFields = getFieldAndPropertyDescriptor(sourceClass);
        List<FieldPropertyDescriptor> targetFields = getFieldAndPropertyDescriptor(targetClass);

//        final Field[] sourceFields = sourceClass.getDeclaredFields();
//        final Field[] targetFields = targetClass.getDeclaredFields();
        for (FieldPropertyDescriptor targetFieldPD : targetFields) {
            final Optional<FieldPropertyDescriptor> optional = sourceFields.stream()
                    .filter(item1 -> Objects.equals(targetFieldPD.getField().getName(), item1.getField().getName()))
                    .findAny();
            if (optional.isPresent()) {
                FieldPropertyDescriptor sourceFieldPD = optional.get();
                if (!sourceFieldPD.getField().getType().equals(targetFieldPD.getField().getType()) && isExactMatchType) {
                    continue;
                }
                try {
                    Method getMethod = sourceFieldPD.getDescriptor().getReadMethod();//获得get方法
                    final Object invoke = getMethod.invoke(source);//此处为执行该Object对象的get方法
                    final Method writeMethod = targetFieldPD.getDescriptor().getWriteMethod();

                    // 判断双方都是基础类型且相同类型
                    if (basicClass.contains(sourceFieldPD.getField().getType())
                            && basicClass.contains(targetFieldPD.getField().getType())
                            && ObjectUtils.equals(targetFieldPD.getField().getType(), sourceFieldPD.getField().getType())) {
                        writeMethod.invoke(target, invoke);
                    } else if (basicClass.contains(sourceFieldPD.getField().getType()) && basicClass.contains(targetFieldPD.getField().getType())) {
                        // 判断双方都是基础类型
                        final Optional<?> optional1 = ConvertUtils.convertIfNecessary(invoke, targetFieldPD.getField().getType());
                        writeMethod.invoke(target, optional1.orElse(null));
                    } else if (invoke instanceof Collection && Collection.class.isAssignableFrom(targetFieldPD.getField().getType())) {
                        // 双方都是集合类型
                        Collection collection = (Collection) invoke;
                        Type genericType = targetFieldPD.getField().getGenericType();
                        if (genericType instanceof ParameterizedType) {
                            ParameterizedType pt = (ParameterizedType) genericType;
                            // 得到泛型里的class类型对象
                            Class<?> actualTypeArgument = (Class<?>) pt.getActualTypeArguments()[0];

                            if (basicClass.contains(actualTypeArgument)) {
                                writeMethod.invoke(target, collection);
                            } else {
                                List<Object> list = new ArrayList<>();
                                for (Object obj : collection) {
                                    Object obj1 = actualTypeArgument.newInstance();
                                    copyProperties2(obj, obj1, isExactMatchType);
                                    list.add(obj1);
                                }
                                writeMethod.invoke(target, list);
                            }
                        }
                    } else if (ObjectUtils.equals(targetFieldPD.getField().getType(), sourceFieldPD.getField().getType())) {
                        // 普通自定义对象
                        if (Objects.equals(targetFieldPD.getField().getType(), List.class)) {
                            if (Objects.nonNull(invoke) && invoke instanceof Collection) {
                                List<Object> list = new ArrayList<>();
                                Collection collection = (Collection) invoke;
                                ParameterizedType pt = (ParameterizedType) targetFieldPD.getField().getGenericType();
                                // 得到泛型里的class类型对象
                                Class<?> actualTypeArgument = (Class<?>) pt.getActualTypeArguments()[0];
                                for (Object obj : collection) {
                                    Object obj1 = actualTypeArgument.newInstance();
                                    copyProperties(obj, obj1, isExactMatchType);
                                    list.add(obj1);
                                }
                                writeMethod.invoke(target, list);
                            }
                        } else {
                            final Object targetObj = targetFieldPD.getField().getType().newInstance();
                            copyProperties2(invoke, targetObj, isExactMatchType);
                        }
                    } else {
                        // 其他类型
                        final Optional<?> optional1 = ConvertUtils.convertIfNecessary(invoke, targetFieldPD.getField().getType());
                        writeMethod.invoke(target, optional1.orElse(null));
                    }
                } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
//                    e.printStackTrace();
                }
            }
        }
*//*
        for (Field sourceField : sourceFields) {
            final Optional<Field> optional = Arrays.stream(targetFields)
                    .filter(targetField -> ObjectUtils.equals(targetField.getName(), sourceField.getName()))
                    .findFirst();
            if (optional.isPresent()) {
                try {
                    final Field targetField = optional.get();
                    if (!sourceField.getType().equals(targetField.getType()) && isExactMatchType) {
                        continue;
                    }

                    PropertyDescriptor sourcePd = new PropertyDescriptor(sourceField.getName(), sourceClass);
                    PropertyDescriptor targetPd = new PropertyDescriptor(targetField.getName(), targetClass);
                    Method getMethod = sourcePd.getReadMethod();//获得get方法
                    final Object invoke = getMethod.invoke(source);//此处为执行该Object对象的get方法
                    final Method writeMethod = targetPd.getWriteMethod();

                    // 判断双方都是基础类型且相同类型
                    if (basicClass.contains(sourceField.getType())
                            && basicClass.contains(targetField.getType())
                            && ObjectUtils.equals(sourceField.getType(), targetField.getType())) {
                        writeMethod.invoke(target, invoke);
                    } else if (basicClass.contains(sourceField.getType()) && basicClass.contains(targetField.getType())) {
                        // 判断双方都是基础类型
                        final Optional<?> optional1 = ConvertUtils.convertIfNecessary(invoke, targetField.getType());
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
                                for (Object obj : collection) {
                                    Object obj1 = actualTypeArgument.newInstance();
                                    copyProperties(obj, obj1, isExactMatchType);
                                    list.add(obj1);
                                }
                                writeMethod.invoke(target, list);
                            }
                        }
                    } else if (ObjectUtils.equals(targetField.getType(), sourceField.getType())) {
                        // 普通自定义对象
                        if (Objects.equals(targetField.getType(), List.class)) {
                            if (Objects.nonNull(invoke) && invoke instanceof Collection) {
                                List<Object> list = new ArrayList<>();
                                Collection collection = (Collection) invoke;
                                ParameterizedType pt = (ParameterizedType) targetField.getGenericType();
                                // 得到泛型里的class类型对象
                                Class<?> actualTypeArgument = (Class<?>) pt.getActualTypeArguments()[0];
                                for (Object obj : collection) {
                                    Object obj1 = actualTypeArgument.newInstance();
                                    copyProperties(obj, obj1, isExactMatchType);
                                    list.add(obj1);
                                }
                                writeMethod.invoke(target, list);
                            }
                        } else {
                            final Object targetObj = targetField.getType().newInstance();
                            copyProperties(invoke, targetObj, isExactMatchType);
                        }
                    } else {
                        // 其他类型
                        final Optional<?> optional1 = ConvertUtils.convertIfNecessary(invoke, targetField.getType());
                        writeMethod.invoke(target, optional1.orElse(null));
                    }
                } catch (IntrospectionException | IllegalAccessException
                        | InvocationTargetException | InstantiationException e) {
//                    e.printStackTrace();
                }
            }
        }*//*
    }*/

    public static void copyProperties3(Object source, Object target, boolean isExactMatchType) {
        if (ObjectUtils.anyNull(source, target)) {
            return;
        }
        // 获得数据实体类名
        final Class<?> sourceClass = source.getClass();
        final Class<?> targetClass = target.getClass();
        final Map<String, FieldPropertyDescriptor> sourceFields = getFieldAndPropertyDescriptor1(sourceClass);

        final Map<String, FieldPropertyDescriptor> targetFields = getFieldAndPropertyDescriptor1(targetClass);

        for (Map.Entry<String, FieldPropertyDescriptor> targetField : targetFields.entrySet()) {
            final FieldPropertyDescriptor sourceField = sourceFields.get(targetField.getKey());
            if (sourceFields.containsKey(targetField.getKey()) && ObjectUtils.nonNull(sourceField)) {
                // 精确匹配
                if (isExactMatchType && !ObjectUtils.equals(targetField.getValue().getField().getType(), sourceField.getField().getType())) {
                    continue;
                }
                try {
                    final Field field = sourceField.getField();
                    field.setAccessible(true);
                    final Object invoke = field.get(source);

//                    Method getMethod = sourceField.getReadMethod();//获得get方法
//                    final Object invoke = getMethod.invoke(source);//此处为执行该Object对象的get方法
//                    final Method writeMethod = targetField.getValue().getWriteMethod();

                    // 判断双方都是基础类型且相同类型
                    final Field targetFieldTemp = targetField.getValue().getField();
                    targetFieldTemp.setAccessible(true);

                    if (ObjectUtils.equals(sourceField.getField().getType(), targetFieldTemp.getType())) {
                        targetFieldTemp.set(target, invoke);
//                        writeMethod.invoke(target, invoke);
                    } else if (basicClass.contains(targetFieldTemp.getType()) || basicDateClass.contains(targetFieldTemp.getType())) {
                        final Optional<?> optional1 = SimpleTypeConverter.convertIfNecessary(invoke, targetFieldTemp.getType());
                        targetFieldTemp.set(target, optional1.orElse(null));
//                        writeMethod.invoke(target, optional1.orElse(null));

                    } else if (invoke instanceof Collection && Collection.class.isAssignableFrom(targetFieldTemp.getType())) {
                        // 双方都是集合类型
                        Collection collection = (Collection) invoke;
                        if (ObjectUtils.equals(sourceField.getListActualTypeArgument(), targetField.getValue().getListActualTypeArgument())) {
                            targetFieldTemp.set(target, collection);
//                            writeMethod.invoke(target, collection);
                        } else {
                            List<Object> list = new ArrayList<>();
                            for (Object obj : collection) {
                                Object obj1 = sourceField.getListActualTypeArgument().newInstance();
                                copyProperties3(obj, obj1, isExactMatchType);
                                list.add(obj1);
                            }
                            targetFieldTemp.set(target, list);
//                            writeMethod.invoke(target, list);
                        }
                    } else {
                        System.out.println("source Type is " + sourceField.getField().getType() + ",targetType is " + targetFieldTemp.getType());
                        final Object targetObj = targetFieldTemp.getType().newInstance();
                        copyProperties3(invoke, targetObj, isExactMatchType);
                        targetFieldTemp.set(target, targetObj);
//                        writeMethod.invoke(target, targetObj);
                    }
                } catch (IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                }

            }
        }

//        copyProperties4(source, target, isExactMatchType, sourceFields, targetFields);
    }

    public static void copyProperties31(Object source, Object target, boolean isExactMatchType) {
        if (ObjectUtils.anyNull(source, target)) {
            return;
        }
        // 获得数据实体类名
        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();
        FieldPropertyDescriptor[] var1 = getFieldAndPropertyDescriptor2(targetClass);
        for (FieldPropertyDescriptor targetFieldPD : var1) {
            Optional<FieldPropertyDescriptor> optional = getFieldAndPropertyDescriptor2(sourceClass, targetFieldPD.getName());
            if (!optional.isPresent()) {
                continue;
            }
            FieldPropertyDescriptor sourceFieldPD = optional.get();
            try {
                if (!sourceFieldPD.getType().equals(targetFieldPD.getType()) && isExactMatchType) {
                    continue;
                }
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
                    final Optional<?> optional1 = ConvertUtils.convertIfNecessary(invoke, targetField.getType());
                    targetField.set(target, optional1.orElse(null));
                } else if (invoke instanceof Collection && Collection.class.isAssignableFrom(targetField.getType())) {
                    // 双方都是集合类型
                    Collection collection = (Collection) invoke;
                    if (Objects.equals(sourceFieldPD.getListActualTypeArgument(), targetFieldPD.getListActualTypeArgument())) {
                        targetField.set(target, collection);
                    } else {
                        List<Object> list = new ArrayList<>();
                        // TODO 瓶颈
                        for (Object obj : collection) {
                            Object obj1 = targetFieldPD.getListActualTypeArgument().newInstance();
                            copyProperties31(obj, obj1, isExactMatchType);
                            list.add(obj1);
                        }
                        targetField.set(target, list);
                    }
                } else {
                    // 其他类型
                    final Optional<?> optional1 = SimpleTypeConverter.convertIfNecessary(invoke, targetField.getType());
                    targetField.set(target, optional1.orElse(null));
                }
            } catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
    }
    public static void copyProperties32(Object source, Object target, boolean isExactMatchType) {
        if (ObjectUtils.anyNull(source, target)) {
            return;
        }
        // 获得数据实体类名
        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();
        PropertyDescriptor[] var1 = getFieldAndPropertyDescriptor3(targetClass);
        for (PropertyDescriptor targetFieldPD : var1) {
            Optional<PropertyDescriptor> optional = getFieldAndPropertyDescriptor3(sourceClass, targetFieldPD.getName());
            if (!optional.isPresent()) {
                continue;
            }
            PropertyDescriptor sourceFieldPD = optional.get();
            try {
                if (!sourceFieldPD.getPropertyType().equals(targetFieldPD.getPropertyType()) && isExactMatchType) {
                    continue;
                }
                final Method readMethod = sourceFieldPD.getReadMethod();
                final Method writeMethod = targetFieldPD.getWriteMethod();
                final Object invoke = readMethod.invoke(source);


                /*final Field sourceField = sourceFieldPD.getField();
                sourceField.setAccessible(true);
                final Field targetField = targetFieldPD.getField();
                targetField.setAccessible(true);
                final Object invoke = sourceField.get(source);*/
                if (Objects.isNull(invoke) || Objects.isNull(writeMethod)) {
                    continue;
                }

                // 判断双方是相同类型
                if (ObjectUtils.equals(targetFieldPD.getPropertyType(), sourceFieldPD.getPropertyType())) {
//                    targetField.set(target, invoke);
                    writeMethod.invoke(target, invoke);
                } else if (basicClass.contains(sourceFieldPD.getPropertyType()) && basicClass.contains(targetFieldPD.getPropertyType())) {
                    // 判断双方都是基础类型
                    final Optional<?> optional1 = ConvertUtils.convertIfNecessary(invoke, targetFieldPD.getPropertyType());
//                    targetField.set(target, optional1.orElse(null));
                    writeMethod.invoke(target, optional1.orElse(null));

                } else if (invoke instanceof Collection && Collection.class.isAssignableFrom(targetFieldPD.getPropertyType())) {
                    // 双方都是集合类型
                    Collection collection = (Collection) invoke;

                    if (Objects.equals(sourceFieldPD.getPropertyEditorClass(), targetFieldPD.getPropertyEditorClass())) {
//                        targetField.set(target, collection);
                        writeMethod.invoke(target, collection);
                    } else {
                        List<Object> list = new ArrayList<>();
                        // TODO 瓶颈
                        for (Object obj : collection) {
                            Object obj1 = targetFieldPD.getPropertyEditorClass().newInstance();
                            copyProperties32(obj, obj1, isExactMatchType);
                            list.add(obj1);
                        }
//                        targetField.set(target, list);
                        writeMethod.invoke(target, list);
                    }
                } else {
                    // 其他类型
                    final Optional<?> optional1 = SimpleTypeConverter.convertIfNecessary(invoke, targetFieldPD.getPropertyType());
//                    targetField.set(target, optional1.orElse(null));
                    writeMethod.invoke(target, optional1.orElse(null));
                }
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
/*

    private static void copyProperties4(Object source, Object target, boolean isExactMatchType, Map<String, FieldPropertyDescriptor> sourceFields, Map<String, FieldPropertyDescriptor> targetFields) {
        for (Map.Entry<String, FieldPropertyDescriptor> targetField : targetFields.entrySet()) {
            final FieldPropertyDescriptor sourceField = sourceFields.get(targetField.getKey());
            if (sourceFields.containsKey(targetField.getKey()) && ObjectUtils.nonNull(sourceField)) {
                // 精确匹配
                if (isExactMatchType && !ObjectUtils.equals(targetField.getValue().getField().getType(), sourceField.getField().getType())) {
                    continue;
                }
                try {
//                    final long l = System.currentTimeMillis();
                    Method getMethod = sourceField.getReadMethod();//获得get方法
                    final Object invoke = getMethod.invoke(source);//此处为执行该Object对象的get方法
                    final Method writeMethod = targetField.getValue().getWriteMethod();
//                    System.out.println("haosi:"+(System.currentTimeMillis() - l)+"ms");
                    // 判断双方都是基础类型且相同类型
                    final Field targetFieldTemp = targetField.getValue().getField();

//                    sourceField.getField().setAccessible(true);
//                    targetFieldTemp.setAccessible(true);
//                    final Object invoke = sourceField.getField().get(source);
                    if (basicClass.contains(sourceField.getField().getType())
                            && basicClass.contains(targetFieldTemp.getType())
                            && ObjectUtils.equals(sourceField.getField().getType(), targetFieldTemp.getType())) {
                        writeMethod.invoke(target, invoke);


//                        targetFieldTemp.set(target, invoke);

                    } else if (basicClass.contains(sourceField.getField().getType()) && basicClass.contains(targetFieldTemp.getType())) {
                        // 判断双方都是基础类型
//                        targetFieldTemp.set(target, optional1.orElse(null));
                        final Optional<?> optional1 = ConvertUtils.convertIfNecessary(invoke, targetFieldTemp.getType());
                        writeMethod.invoke(target, optional1.orElse(null));
                    } else if (invoke instanceof Collection && Collection.class.isAssignableFrom(targetFieldTemp.getType())) {
                        // 双方都是集合类型
                        Collection collection = (Collection) invoke;
                        Type sourceGenericType = sourceField.getField().getGenericType();
                        Type targetGenericType = targetFieldTemp.getGenericType();
                        Class<?> sourceGTClass = (Class<?>) ((ParameterizedType) sourceGenericType).getActualTypeArguments()[0];
                        Class<?> targetGTClass = (Class<?>) ((ParameterizedType) targetGenericType).getActualTypeArguments()[0];
                        if (ObjectUtils.equals(sourceGTClass, targetGTClass)) {

                            writeMethod.invoke(target, collection);

                        } else {
                            final Map<String, FieldPropertyDescriptor> sourceFields1 = getFieldAndPropertyDescriptor1(sourceGTClass);
                            final Map<String, FieldPropertyDescriptor> targetFields1 = getFieldAndPropertyDescriptor1(targetGTClass);
                            List<Object> list = new ArrayList<>();
                            for (Object obj : collection) {
                                Object obj1 = targetGTClass.newInstance();
                                copyProperties4(obj, obj1, isExactMatchType, sourceFields1, targetFields1);
                                list.add(obj1);
                            }
                            writeMethod.invoke(target, list);
                        }
                    } else if (basicDateClass.contains(targetFieldTemp.getType())) {
                        final Optional<?> optional1 = SimpleTypeConverter.convertIfNecessary(invoke, targetFieldTemp.getType());
                        writeMethod.invoke(target, optional1.orElse(null));
                    } else {
                        final Object targetObj = targetFieldTemp.getType().newInstance();
                        copyProperties3(invoke, targetObj, isExactMatchType);
                        writeMethod.invoke(target, targetObj);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }

            }
        }
    }
*/

    /**
     * 深度克隆对象
     *
     * @param source 源数据（需要实现序列化接口）
     * @param <T>
     * @return
     */
    public static <T> Optional<T> clone(T source) {
        // 深度克隆
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(512);
             ObjectOutputStream oos = new ObjectOutputStream(bos);) {
            // 序列化
            oos.writeObject(source);
            // 反序列化
            bis = new ByteArrayInputStream(bos.toByteArray());
            ois = new ObjectInputStream(bis);
            final Object object = ois.readObject();
            return Optional.ofNullable((T) object);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        } finally {
            try {
                if (ObjectUtils.nonNull(bis)) {
                    bis.close();
                }
                if (ObjectUtils.nonNull(ois)) {
                    ois.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //region 私有方法

    private static void copyProperties1(Object source, Object target, boolean isExactMatchType) {

    }

    private static void copyPropertyValue(Object source,
                                          Object target,
                                          boolean isExactMatchType,
                                          Class<?> sourceClass,
                                          Class<?> targetClass,
                                          Field sourceField,
                                          Field targetField,
                                          List<Class<?>> basicClass)
            throws IntrospectionException, InvocationTargetException, IllegalAccessException, InstantiationException {
        if (!sourceField.getType().equals(targetField.getType()) && !isExactMatchType) {
            return;
        }

        PropertyDescriptor sourcePd = new PropertyDescriptor(sourceField.getName(), sourceClass);
        PropertyDescriptor targetPd = new PropertyDescriptor(targetField.getName(), targetClass);
        Method getMethod = sourcePd.getReadMethod();//获得get方法
        final Object invoke = getMethod.invoke(source);//此处为执行该Object对象的get方法
        final Method writeMethod = targetPd.getWriteMethod();

        // 判断双方都是基础类型且相同类型
        if (basicClass.contains(sourceField.getType())
                && basicClass.contains(targetField.getType())
                && ObjectUtils.equals(sourceField.getType(), targetField.getType())) {
            writeMethod.invoke(target, invoke);
        } else if (basicClass.contains(sourceField.getType()) && basicClass.contains(targetField.getType())) {
            // 判断双方都是基础类型
            final Optional<?> optional = ConvertUtils.convertIfNecessary(invoke, targetField.getType());
            writeMethod.invoke(target, optional.orElse(null));
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
                    for (Object obj : collection) {
                        Object obj1 = actualTypeArgument.newInstance();
                        copyProperties(obj, obj1, isExactMatchType);
                        list.add(obj1);
                    }
                    writeMethod.invoke(target, list);
                }
            }
        } else if (ObjectUtils.equals(targetField.getType(), sourceField.getType())) {
            // 普通自定义对象
            if (Objects.equals(targetField.getType(), List.class)) {
                if (Objects.nonNull(invoke) && invoke instanceof Collection) {
                    List<Object> list = new ArrayList<>();
                    Collection collection = (Collection) invoke;
                    ParameterizedType pt = (ParameterizedType) targetField.getGenericType();
                    // 得到泛型里的class类型对象
                    Class<?> actualTypeArgument = (Class<?>) pt.getActualTypeArguments()[0];
                    for (Object obj : collection) {
                        Object obj1 = actualTypeArgument.newInstance();
                        copyProperties(obj, obj1, isExactMatchType);
                        list.add(obj1);
                    }
                    writeMethod.invoke(target, list);
                }
            } else {
                final Object targetObj = targetField.getType().newInstance();
                copyProperties1(invoke, targetObj, isExactMatchType);
            }
        } else {
            // 其他类型
            final Optional<?> optional = ConvertUtils.convertIfNecessary(invoke, targetField.getType());
            writeMethod.invoke(target, optional.orElse(null));
        }
    }

    //endregion
}
