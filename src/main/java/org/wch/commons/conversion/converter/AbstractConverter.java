package org.wch.commons.conversion.converter;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.wch.commons.lang.ObjectUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractConverter<T> {

    protected Object source;

    protected Class<T> requiredType;

    abstract public Optional<T> convert();

    protected static <T> Optional<T> castToRequiredType(Class<?> sourceClazz, Class<T> requiredType, String parseFunction, Object value) {
        try {
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
    }
}
