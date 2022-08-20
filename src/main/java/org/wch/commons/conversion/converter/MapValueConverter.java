package org.wch.commons.conversion.converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import lombok.NoArgsConstructor;
import org.wch.commons.lang.ObjectUtils;

import java.util.Optional;

/**
 * @Description: 键值对类型转换器
 * @Author: wuchu
 * @CreateTime: 2022-07-13 17:22
 */
@NoArgsConstructor
public class MapValueConverter<T> extends AbstractConverter<T> {

    public MapValueConverter(Object source, Class<T> requiredType) {
        super(source, requiredType);
    }

    @Override
    public Optional<T> convert() {
        return convert(source, requiredType);
    }

    public <K, V> Optional<T> convert(Object obj, Class<T> requiredType) {
        if (ObjectUtils.anyNull(obj, requiredType)) {
            return Optional.empty();
        }
        Optional<T> result;
        final String jsonString = JSON.toJSONString(obj);
        if ("String".equals(requiredType.getSimpleName())) {
            result = Optional.of(requiredType.cast(jsonString));
        } else if ("Map".equals(requiredType.getSimpleName())) {
            return Optional.of((T) obj);
        } else {
            try {
                final T t = JSONObject.parseObject(jsonString, requiredType);
                result = Optional.ofNullable(t);
            } catch (JSONException e) {
                e.printStackTrace();
                result = Optional.empty();
            }
        }
        return result;
    }
}
