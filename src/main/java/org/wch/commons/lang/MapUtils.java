package org.wch.commons.lang;


import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description: Map工具类
 * @Author: wuchu
 * @Version: 1.0
 * @CreateTime: 2022/11/19 22:37
 */
public class MapUtils {

    public static <T, R> boolean isEmpty(Map<T, R> map) {
        return Objects.nonNull(map) && !map.isEmpty();
    }

    public static <K, V> boolean isNotEmpty(Map<K, V> source) {
        return null != source && !source.isEmpty();
    }

    public static boolean isAnyEmpty(Map... maps) {
        if (null == maps) {
            return true;
        }
        for (Map map : maps) {
            if (null == map || map.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAllEmpty(Map... maps) {
        if (null == maps) {
            return true;
        }
        for (Map map : maps) {
            if (null != map && !map.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAllNotEmpty(Map... maps) {
        if (null == maps) {
            return false;
        }
        for (Map map : maps) {
            if (null == map || map.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public static <T, R> Map<T, R> unionMap(Map<T, R> source, Map<T, R> target, Operation operation) {
        if (isAllEmpty(source, target)) {
            return Collections.emptyMap();
        } else if (isEmpty(source) && isNotEmpty(target)) {
            return target;
        } else if (isNotEmpty(source) && isEmpty(target)) {
            return source;
        } else {
            Map<T, R> result;
            switch (operation) {
                case BASE_SOURCE_STRICT:
                    result = unionStrict(source, target);
                    break;
                case BASE_SOURCE_ONLY_ADD:
                    result = unionMapOnlyAdd(source, target);
                    break;
                case BASE_TARGET_NO_STRICT:
                    result = unionMapNoStrict(source, target);
                    break;
                default:
                    result = new HashMap<>();
                    break;
            }
            return result;
        }
    }

    public static <K, V> Optional<Map<K, V>> putAll(Map<K, V> source, Map<K, V> target) {
        Map<K, V> result = new HashMap<>();
        if (null != source && !source.isEmpty()) {
            result.putAll(source);
        }
        if (null != target && !target.isEmpty()) {
            result.putAll(target);
        }
        return Optional.of(result);
    }

    // region 私有方法
    private static <T, R> Map<T, R> unionStrict(Map<T, R> sourceMap, Map<T, R> targetMap) {
        if (MapUtils.isEmpty(sourceMap)) {
            return Collections.emptyMap();
        }
        if (MapUtils.isEmpty(targetMap)) {
            return new HashMap<>(sourceMap);
        }
        Set<T> union = sourceMap.keySet();
        Map<T, R> result = new HashMap<>();
        for (T key : union) {
            Object source = sourceMap.get(key);
            Object target = targetMap.get(key);
            if (Objects.isNull(source)) {
                result.put(key, null == target ? null : (R) target);
            } else {
                if (source instanceof Map && target instanceof Map) {
                    Map<T, R> sourceMapTemp = (Map<T, R>) source;
                    Map<T, R> targetMapTemp = (Map<T, R>) target;
                    Map<T, R> trMap = unionStrict(sourceMapTemp, targetMapTemp);
                    result.put(key, (R) trMap);
                } else {
                    result.put(key, null == target ? null : (R) target);
                }
            }
        }
        return result;
    }

    private static <T, R> Map<T, R> unionMapOnlyAdd(Map<T, R> sourceMap, Map<T, R> targetMap) {
        if (MapUtils.isEmpty(sourceMap)) {
            return Collections.emptyMap();
        }
        if (MapUtils.isEmpty(targetMap)) {
            return new HashMap<>(sourceMap);
        }
        final Set<T> sourceKeys = sourceMap.keySet();
        final Set<T> targetKeys = targetMap.keySet();
        Map<T, R> result = new HashMap<>();

        final Set<T> union = Stream.concat(sourceKeys.stream(), targetKeys.stream()).collect(Collectors.toSet());
        for (T key : union) {
            final boolean sourceContainKey = sourceMap.containsKey(key);
            final boolean targetContainKey = targetMap.containsKey(key);
            // 双方都包含key
            final Object source = sourceMap.get(key);
            final Object target = targetMap.get(key);
            if (sourceContainKey && targetContainKey) {
                if (Objects.isNull(source)) {
                    result.put(key, null == target ? null : (R) target);
                } else {
                    if (source instanceof Map && target instanceof Map) {
                        Map<T, R> sourceMapTemp = (Map<T, R>) source;
                        Map<T, R> targetMapTemp = (Map<T, R>) target;
                        final Map<T, R> trMap = unionMapOnlyAdd(sourceMapTemp, targetMapTemp);
                        result.put(key, (R) trMap);
                    } else {
                        result.put(key, null == target ? null : (R) target);
                    }
                }
            } else if (!sourceContainKey && targetContainKey) {
                // 原数据不存在，目标数据存在
                result.put(key, null == target ? null : (R) target);
            } else {
                // 原数据存在，目标数据不存在
                result.put(key, null == source ? null : (R) source);
            }
        }
        return result;
    }

    private static <T, R> Map<T, R> unionMapNoStrict(Map<T, R> sourceMap, Map<T, R> targetMap) {
        if (MapUtils.isEmpty(sourceMap)) {
            return Collections.emptyMap();
        }
        if (MapUtils.isEmpty(targetMap)) {
            return new HashMap<>(sourceMap);
        }
        return new HashMap<>(targetMap);
    }

    // endregion

    public enum Operation {
        // 两大场景：
        // 1：原数据比目标数据字段多
        // 2：原数据比目标数据字段少
        // 3：原数据和目标数据字段同等
        //
        // 原数据字段存在，目标数据字段不存在
        // 原数据字段不存在，目标数据字段存在
        // 原数据字段存在，目标数据字段存在

        // 以原始数据字段为基准，严格匹配模式，不允许删除和新增字段
        BASE_SOURCE_STRICT,
        // 以原始数据字段为基准，宽松匹配模式,只允许新增字段，
        BASE_SOURCE_ONLY_ADD,
        // 以目标数据字段为基准，宽松匹配模式,允许新增、删除字段，
        BASE_TARGET_NO_STRICT,
    }
}
