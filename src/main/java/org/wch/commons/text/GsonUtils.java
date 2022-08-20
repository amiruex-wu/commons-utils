package org.wch.commons.text;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * @Description: GSON工具类
 * @Author: wuchu
 * @CreateTime: 2022-07-12 17:45
 */
public class GsonUtils {
    private static final Gson filterNullGson;
    private static final Gson nullableGson;
    private static final Gson nullableAndPrettyPrintGson;
    static {
        nullableGson = new GsonBuilder()
                .enableComplexMapKeySerialization()
                .serializeNulls()
                .setDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
                .create();
        filterNullGson = new GsonBuilder()
                .enableComplexMapKeySerialization()
                .setDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
                .create();
        nullableAndPrettyPrintGson = new GsonBuilder()
                .enableComplexMapKeySerialization()
                .setPrettyPrinting()
                .setDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
                .create();
    }

    protected GsonUtils() {
    }

    /**
     * 根据对象返回json   不过滤空值字段
     */
    public static String toJsonWithNullField(Object obj){
        return nullableGson.toJson(obj);
    }

    /**
     * 根据对象返回json  过滤空值字段
     */
    public static String toJsonFilterNullField(Object obj){
        return filterNullGson.toJson(obj);
    }

    /**
     * 将json转化为对应的实体对象
     * new TypeToken<HashMap<String, Object>>(){}.getType()
     */
    public static <T> T parse(String json, Type type){
        return nullableGson.fromJson(json, type);
    }

    public static <T> T parse(String json, Class<T> clazz){
        return nullableGson.fromJson(json, clazz);
    }

    public static String toJSONString(Object obj) {
        return filterNullGson.toJson(obj);
    }

    public static String toJSONStringWithNull(Object obj) {
        return nullableAndPrettyPrintGson.toJson(obj);
    }

    public static String toJSONWithNull(Object obj, boolean prettyPrint) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        if (prettyPrint) {
            gsonBuilder.setPrettyPrinting();
        }
        Gson gson = gsonBuilder
                .serializeNulls()
                .create();
        return gson.toJson(obj);
    }

}
