package top.itoolbox.commons.text;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.SneakyThrows;
import top.itoolbox.commons.lang.ObjectUtils;
import top.itoolbox.commons.lang.StringUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @Description: Fastjson数据工具类
 * @Author: wuchu
 * @CreateTime: 2022-07-13 17:22
 */
public class FastJsonUtils {

    private static final SerializeConfig config = new SerializeConfig();

    public static <T> Optional<T> parse(Object object, Class<T> clazz) {
        if (ObjectUtils.anyNull(object, clazz)) {
            return Optional.empty();
        }
        try {
            if (object instanceof String) {
                final T t = JSON.parseObject((String) object, clazz);
                return Optional.ofNullable(t);
            } else if (object instanceof Map) {
                final T t = JSON.parseObject(JSON.toJSONString(object), clazz);
                return Optional.ofNullable(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static Optional<String> toJsonString(Object object) {
        if (ObjectUtils.anyNull(object)) {
            return Optional.empty();
        }
        try {
            final String s = JSON.toJSONString(object, SerializerFeature.WriteMapNullValue);
            return Optional.of(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }


    /**
     * 适用于Java对象中含枚举类型转换
     *
     * @param object
     * @param enums
     * @param <T>
     * @return
     */
    @SafeVarargs
    public static <T> String toJsonString(Object object, Class<? extends Enum>... enums) {
        if (Objects.isNull(object)) {
            return null;
        }
        if (Objects.nonNull(enums)) {
            for (Object anEnum : enums) {
                config.put((Type) anEnum, new EnumSerializerConfig());
            }
        }
        return JSONObject.toJSONString(object, config);
    }

    /**
     * 适用于JSON字符中含枚举类型解析
     *
     * @param sourceStr
     * @param requiredType
     * @param enums
     * @param <T>
     * @return
     */
    public static <T> T parseToObject(String sourceStr, Class<T> requiredType, Object... enums) {
        if (StringUtils.isBlank(sourceStr) || Objects.isNull(requiredType)) {
            return null;
        }

        ParserConfig globalInstance = ParserConfig.getGlobalInstance();
        if (Objects.nonNull(enums) && enums.length > 0) {
            for (Object anEnum : enums) {
                globalInstance.putDeserializer(anEnum.getClass(), new EnumValueDeserializer());
            }
        }

        final T object = JSONObject.parseObject(sourceStr, requiredType);
        return requiredType.cast(object);
    }

    /**
     * 适用于JSON字符中含枚举类型解析
     *
     * @param sourceStr
     * @param requiredType
     * @param enums
     * @param <T>
     * @return
     */
    public static <T> List<T> parseArray(String sourceStr, Class<T> requiredType, Object... enums) {
        if (StringUtils.isBlank(sourceStr) || Objects.isNull(requiredType)) {
            return null;
        }

        ParserConfig globalInstance = ParserConfig.getGlobalInstance();
        if (Objects.nonNull(enums) && enums.length > 0) {
            for (Object anEnum : enums) {
                globalInstance.putDeserializer(anEnum.getClass(), new EnumValueDeserializer());
            }
        }

        return JSONObject.parseArray(sourceStr, requiredType);
    }

    /**
     * 枚举反序列化
     */
    static class EnumValueDeserializer implements ObjectDeserializer {

        @SneakyThrows
        @Override
        public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
            final JSONLexer lexer = parser.getLexer();
            final int token = lexer.intValue();
            Class cls = (Class) type;
            Object[] enumConstants = cls.getEnumConstants();
            if (Enum.class.isAssignableFrom(cls)) {
                final Method[] declaredMethods = cls.getDeclaredMethods();
                for (Method declaredMethod : declaredMethods) {
                    if (declaredMethod.getReturnType().equals(cls)) {
                        final Object newInstance = cls.newInstance();
                        return (T) declaredMethod.invoke(newInstance, lexer.intValue());
                    }
                }
            } else {
                // 没实现EnumValue接口的 默认的按名字或者按ordinal
                if (token == JSONToken.LITERAL_INT) {
                    int intValue = lexer.intValue();
                    lexer.nextToken(JSONToken.COMMA);

                    if (intValue < 0 || intValue > enumConstants.length) {
                        throw new JSONException("parse enum " + cls.getName() + " error, value : " + intValue);
                    }
                    return (T) enumConstants[intValue];
                } else if (token == JSONToken.LITERAL_STRING) {
                    return (T) Enum.valueOf(cls, lexer.stringVal());
                }
            }
            return null;
        }

        @Override
        public int getFastMatchToken() {
            return JSONToken.LITERAL_INT;
        }
    }

    /**
     * 枚举类序列化
     */
    public static class EnumSerializerConfig implements ObjectSerializer {

        @SneakyThrows
        public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
            if (Objects.isNull(object)) {
                return;
            }

            final Class<?> aClass = object.getClass();
            final Method[] declaredMethods = aClass.getDeclaredMethods();
            for (Method declaredMethod : declaredMethods) {
                if (declaredMethod.getReturnType().equals(Integer.class)) {
                    final Object invoke = declaredMethod.invoke(object);
                    serializer.write(invoke);
                    return;
                }
            }
        }
    }

}
