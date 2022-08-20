package org.wch.commons.text;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.wch.commons.lang.ObjectUtils;

import java.util.Map;
import java.util.Optional;

/**
 * @Description: Fastjson数据工具类
 * @Author: wuchu
 * @CreateTime: 2022-07-13 17:22
 */
public class FastJsonUtils {

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

}
