package org.wch.commons.conversion.converter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.wch.commons.beans.FieldPropertyDescriptor;
import org.wch.commons.constant.CommonConstant;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractConverter<T> {

    protected final List<Class<?>> basicClass = Arrays.stream(CommonConstant.BASIC_NUM_STR_CLAZZ).collect(Collectors.toList());

    protected static Map<Class<?>, FieldPropertyDescriptor[]> classFieldPD = new ConcurrentHashMap<>(64);

    protected Object source;

    protected Class<T> requiredType;

    abstract public Optional<T> convert();
/*
    // todo 待优化，使用cglib技术
    protected static <T> Optional<T> castToRequiredType(Class<?> sourceClazz, Class<T> requiredType, String parseFunction, Object value) {
        try {
            System.out.println("转换函数：。。。。");
            if (value instanceof String || value instanceof Character) {
                return getValueOfMethod(requiredType, requiredType, parseFunction, value);
            } else {
                return getValueOfMethod(sourceClazz, requiredType, parseFunction, value);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    protected Optional<T> caseJsonStrConvert() {
        if (ObjectUtils.isNull(source)) {
            return Optional.empty();
        }
        Gson gson = new Gson();
        String json = gson.toJson(source);
        T t = gson.fromJson(json, requiredType);
        return Optional.ofNullable(t);
    }

    private static <T> Optional<T> getValueOfMethod(Class<?> tClass,
                                                    Class<T> requiredType,
                                                    String parseFunction,
                                                    Object value)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method2 = tClass.getDeclaredMethod("valueOf", String.class);
        Optional<Object> instance = getInstance(tClass);
        Object invoke = method2.invoke(instance.orElse(null), value.toString());
        Method parseMethod = tClass.getDeclaredMethod(parseFunction);
        Object invoke1 = parseMethod.invoke(invoke);
        return Optional.of(requiredType.cast(invoke1));
    }

    private static <T> Optional<Object> getInstance(Class<T> clazz) {
        try {
            Object instance = clazz.newInstance();
            return Optional.of(instance);
        } catch (InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
            return Optional.empty();
        }
    }*/
}
