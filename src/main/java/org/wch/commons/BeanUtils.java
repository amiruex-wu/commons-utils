package org.wch.commons;

import org.wch.commons.lang.ConvertUtils;
import org.wch.commons.lang.ObjectUtils;
import org.wch.commons.text.GsonUtils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 属性拷贝工具
 * @Author: wuchu
 * @CreateTime: 2022-07-13 16:48
 */
public class BeanUtils {

    private final static Class<?>[] BASIC_CLAZZ = {Byte.class, Short.class, Integer.class, Float.class, Double.class,
            Long.class, BigDecimal.class, BigInteger.class, String.class, Boolean.class, Character.class,
            byte.class, short.class, int.class, float.class, double.class,
            long.class, boolean.class, char.class, Map.class};

    public static <T> T copyProperties(Object source, Class<T> targetClass) {
        if (ObjectUtils.anyNull(source, targetClass)) {
            return null;
        }
        String jsonStr = GsonUtils.toJSONStringWithNull(source);
        T parse = GsonUtils.parse(jsonStr, targetClass);
        return parse;
    }

    public static void copyProperties(Object source, Object target) {
        final List<Class<?>> basicClass = Arrays.stream(BASIC_CLAZZ).collect(Collectors.toList());
        copyProperties(source, target, true, basicClass);
    }

    public static void copyProperties(Object source, Object target, boolean isExactMatchType) {
        final List<Class<?>> basicClass = Arrays.stream(BASIC_CLAZZ).collect(Collectors.toList());
        copyProperties(source, target, isExactMatchType, basicClass);
    }

    /*@Deprecated
    public static void getMethod(Object obj) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        Class clazz = obj.getClass();//获得实体类名
        Field[] fields = obj.getClass().getDeclaredFields();//获得属性
        //获得Object对象中的所有方法
        for (Field field : fields) {
            PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
            Method getMethod = pd.getReadMethod();//获得get方法
            final Object invoke = getMethod.invoke(obj);//此处为执行该Object对象的get方法
            Method setMethod = pd.getWriteMethod();//获得set方法
            //setMethod.invoke(obj,"参数");//此处为执行该Object对象的set方法
            System.out.println(pd.getName() + ",a:" + pd.getDisplayName() + "read result is " + invoke);
        }
    }
*/

    /**
     * 克隆对象
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
    private static void copyProperties(Object source, Object target, boolean isExactMatchType, List<Class<?>> basicClass) {
        if (ObjectUtils.anyNull(source, target)) {
            return;
        }
        // 获得数据实体类名
        final Class<?> sourceClass = source.getClass();
        final Class<?> targetClass = target.getClass();

        // 获得属性
        final Field[] sourceFields = sourceClass.getDeclaredFields();
        final Field[] targetFields = targetClass.getDeclaredFields();
        for (Field sourceField : sourceFields) {
            final Optional<Field> optional = Arrays.stream(targetFields)
                    .filter(targetField -> ObjectUtils.equals(targetField.getName(), sourceField.getName()))
                    .findFirst();
            if (optional.isPresent()) {
                try {
                    copyPropertyValue(source, target, isExactMatchType, sourceClass, targetClass, sourceField, optional.get(), basicClass);
                } catch (IntrospectionException | IllegalAccessException
                        | InvocationTargetException | InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
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
            final Object targetObj = targetField.getType().newInstance();
            copyProperties(invoke, targetObj, isExactMatchType, basicClass);
        } else {
            // 其他类型
            final Optional<?> optional = ConvertUtils.convertIfNecessary(invoke, targetField.getType());
            writeMethod.invoke(target, optional.orElse(null));
        }
    }
}
