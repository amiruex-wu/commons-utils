package org.wch.commons;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.wch.commons.juel.SimpleContext;
import org.wch.commons.lang.ObjectUtils;
import org.wch.commons.text.GsonUtils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @Description: TODO 考虑使用fastjson, 后续研究
 * @Author: wuchu
 * @CreateTime: 2022-07-13 16:48
 */
@Deprecated
public class BeanUtils {

    public static <T> T copyProperties(Object source, Class<T> targetClass) {
        if (ObjectUtils.anyNull(source, targetClass)) {
            return null;
        }
        String jsonStr = GsonUtils.toJSONStringWithNull(source);
        T parse = GsonUtils.parse(jsonStr, targetClass);
        return parse;
    }

    public static void copyProperties(Object source, Object target, boolean ignoreTypeMatch) {
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
                    copyPropertyValue(source, target, ignoreTypeMatch, sourceClass, targetClass, sourceField, optional.get());
                } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
          /*  optional
                    .ifPresent(targetField -> {
                        copyPropertyValue(source, target, ignoreTypeMatch, sourceClass, targetClass, sourceField, targetField);
                    });*/
        }

        //获得Object对象中的所有方法
        /*for(Field field:fields){
            PropertyDescriptor pd = new PropertyDescriptor(field.getName(), sourceClass);
            Method getMethod = pd.getReadMethod();//获得get方法
            final Object invoke = getMethod.invoke(obj);//此处为执行该Object对象的get方法
            Method setMethod = pd.getWriteMethod();//获得set方法
            //setMethod.invoke(obj,"参数");//此处为执行该Object对象的set方法
            System.out.println("read result is "+ invoke);
        }*/

    }

    private static void copyPropertyValue(Object source,
                                          Object target,
                                          boolean ignoreTypeMatch,
                                          Class<?> sourceClass,
                                          Class<?> targetClass,
                                          Field sourceField,
                                          Field targetField)
            throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        if (!sourceField.getType().equals(targetField.getType()) && !ignoreTypeMatch) {
            return;
        }

        PropertyDescriptor sourcePd = new PropertyDescriptor(sourceField.getName(), sourceClass);
        PropertyDescriptor targetPd = new PropertyDescriptor(targetField.getName(), targetClass);
        Method getMethod = sourcePd.getReadMethod();//获得get方法
        final Object invoke = getMethod.invoke(source);//此处为执行该Object对象的get方法
        final Method writeMethod = targetPd.getWriteMethod();
        writeMethod.invoke(target, invoke);

    }

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



    /*public static <T> T clone(T source) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        Object o = gson.fromJson(gson.toJson(source), source.getClass());
        return (T) o;
    }*/

    public static <T> Optional<T> clone(T source) {
        // 深度克隆
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(512);
             ObjectOutputStream oos = new ObjectOutputStream(bos);
        ) {
            //return super.clone();//默认浅克隆，只克隆八大基本数据类型和String
            //序列化
            oos.writeObject(source);

            //反序列化
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
}
