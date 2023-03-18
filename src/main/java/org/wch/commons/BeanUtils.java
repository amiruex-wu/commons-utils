package org.wch.commons;

import net.sf.cglib.beans.BeanCopier;
import org.wch.commons.conversion.CustomBeanConverter;
import org.wch.commons.lang.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * @Description: TODO
 * @Author: wuchu
 * @Version: 1.0
 * @CreateTime: 2023/3/18 12:56
 */

public class BeanUtils {

    private final static Map<String, BeanCopier> copierMap = new ConcurrentHashMap<>();
    public final static CustomBeanConverter CUSTOM_BEAN_CONVERTER = new CustomBeanConverter();

    /**
     * @param source
     * @param supplier
     * @param <T>
     * @param <S>
     * @return
     */
    public static <T, S> T copyBean(S source, Supplier<T> supplier) {
        if (ObjectUtils.anyNull(source, supplier)) {
            return null;
        }
        T target = supplier.get();
        final Class<?> sourceClass = source.getClass();
        final Class<?> targetClass = target.getClass();
        BeanCopier copier = getBeanCopier(sourceClass, targetClass);
        copier.copy(source, target, CUSTOM_BEAN_CONVERTER);
        return target;
    }

    public static <T, S> T copyBean(S source, T target) {
        if (ObjectUtils.anyNull(source, target)) {
            return null;
        }
        final Class<?> sourceClass = source.getClass();
        final Class<?> targetClass = target.getClass();
        BeanCopier copier = getBeanCopier(sourceClass, targetClass);
        copier.copy(source, target, CUSTOM_BEAN_CONVERTER);
        return target;
    }

    /**
     * Bean属性复制工具方法。
     *
     * @param sources   原始集合
     * @param supplier: 目标类::new(eg: UserVO::new)
     */
    public static <S, T> List<T> copyList(List<S> sources, Supplier<T> supplier) {
        List<T> list = new ArrayList<>(sources.size());
        BeanCopier beanCopier = null;
        for (S source : sources) {
            T t = supplier.get();
            if (beanCopier == null) {
                beanCopier = getBeanCopier(source.getClass(), t.getClass());
            }
            beanCopier.copy(source, t, CUSTOM_BEAN_CONVERTER);
            list.add(t);
        }
        return list;
    }

    private static BeanCopier getBeanCopier(Class<?> sourceClass, Class<?> targetClass) {
        final String key = sourceClass.getName() + "_" + targetClass.getName();
        BeanCopier copier;
        if (copierMap.containsKey(key)) {
            copier = copierMap.get(key);
        } else {
            copier = BeanCopier.create(sourceClass, targetClass, true);
            copierMap.put(key, copier);
        }
        return copier;
    }


}
