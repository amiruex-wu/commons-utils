package org.wch.commons;


//import org.springframework.cglib.beans.BeanCopier;

import net.sf.cglib.beans.BeanCopier;
import org.wch.commons.beans.ConvertorCurrentClass;
import org.wch.commons.beans.FieldPropertyDescriptor;
import org.wch.commons.conversion.CustomConverter;
import org.wch.commons.lang.ObjectUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: TODO
 * @Author: wuchu
 * @CreateTime: 2022-09-07 11:13
 */
public class BeanUtils {

    private final static Map<String, BeanCopier> copierMap = new ConcurrentHashMap<>();

    public static final List<Class<?>> GENERAL_CLASS = Arrays.asList(
            byte.class, short.class, int.class, float.class, double.class, long.class, boolean.class, char.class,
            Byte.class, Short.class, Integer.class, Float.class, Double.class, Long.class, Boolean.class, BigDecimal.class, BigInteger.class, Character.class, String.class);

    public static final List<Class<?>> DATE_TIME_CLASS = Arrays.asList(LocalDateTime.class, LocalDate.class, LocalTime.class, Date.class);

    public static final Map<Class<?>, FieldPropertyDescriptor[]> classFieldPD = new ConcurrentHashMap<>(64);

    private final static CustomConverter customConverter = new CustomConverter();

  /*  @Deprecated
    public static void copyProperties22(Object source, Object target) {
        final Class<?> sourceClass = source.getClass();
        final Class<?> targetClass = target.getClass();
        BeanCopier copier = BeanCopier.create(sourceClass, targetClass, true);
        addClassFieldPd(sourceClass);
        addClassFieldPd(targetClass);
//        customConverter.setSourceClass(sourceClass);
//        customConverter.setTargetClass(targetClass);
        copier.copy(source, target, customConverter);

    }*/

    public static void copyProperties(Object source, Object target) {
        final Class<?> sourceClass = source.getClass();
        final Class<?> targetClass = target.getClass();
        final String key = sourceClass.getName() + "_" + targetClass.getName();
        BeanCopier copier;
        if (copierMap.containsKey(key)) {
            copier = copierMap.get(key);
        } else {
            copier = BeanCopier.create(sourceClass, targetClass, true);
            copierMap.put(key, copier);
        }
        addClassFieldPd(sourceClass);
        addClassFieldPd(targetClass);
        customConverter.addCurrentClasses(new ConvertorCurrentClass(sourceClass, targetClass));
        copier.copy(source, target, customConverter);
        customConverter.pop();
    }

    // reference address: https://www.runoob.com/w3cnote/cglibcode-generation-library-intro.html
    // reference address1: https://blog.csdn.net/danchu/article/details/70238002


    @Deprecated
    public static void copyValue(Object source, Object target) {
        // 设置为true，则使用converter
        BeanCopier copier = BeanCopier.create(source.getClass(), target.getClass(), false);
        // 设置为true，则传入converter指明怎么进行转换
        copier.copy(source, target, null);
    }

    // region 私有方法
    private static void addClassFieldPd(Class<?> clazz) {
        if (classFieldPD.containsKey(clazz)) {
            return;
        }
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] var1 = beanInfo.getPropertyDescriptors();
            final int length = var1.length;
            FieldPropertyDescriptor[] var2 = new FieldPropertyDescriptor[length];
            for (int i = 0; i < length; i++) {
                var2[i] = new FieldPropertyDescriptor(var1[i]);
            }
            classFieldPD.put(clazz, var2);
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
    }

}
