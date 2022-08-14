package org.wch.commons;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.wch.commons.lang.ObjectUtils;
import org.wch.commons.text.GsonUtils;

/**
 * @Description: TODO 考虑使用fastjson
 * @Author: wuchu
 * @CreateTime: 2022-07-13 16:48
 */
public class BeanUtils {

    public static <T> T copyProperties(Object source, Class<T> targetClass) {
        if (ObjectUtils.anyNull(source, targetClass)) {
            return null;
        }
        String jsonStr = GsonUtils.toJSONStringWithNull(source);
        T parse = GsonUtils.parse(jsonStr, targetClass);
        return parse;
    }

   /* public static void copyProperties(Object source, Object target) {
        if (ObjectUtils.anyNull(source, target)) {
            return;
        }
//        String jsonStr = GsonUtils.toJSONStringWithNull(source);
//        T parse = GsonUtils.parse(jsonStr, targetClass);
        return;
    }*/

    public static <T> T clone(T source) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        Object o = gson.fromJson(gson.toJson(source), source.getClass());
        return (T) o;
    }
}
