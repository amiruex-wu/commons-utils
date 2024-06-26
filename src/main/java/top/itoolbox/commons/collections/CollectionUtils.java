package top.itoolbox.commons.collections;

import top.itoolbox.commons.lang.NumberUtils;
import top.itoolbox.commons.lang.NumberUtils1;
import top.itoolbox.commons.lang.ObjectUtils;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Description: 集合类型工具类
 * @Author: wuchu
 * @CreateTime: 2022-07-22 15:24
 */
public class CollectionUtils {

    public static <T> List<T> emptyList() {
        return new ArrayList<>();
    }

    public static <T> Set<T> emptySet() {
        return new HashSet<>();
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] emptyArray(Class<T> clazz) {
        return (T[]) Array.newInstance(clazz, 0);
    }

    public static <K, V> Map<K, V> emptyMap() {
        return new HashMap<>();
    }

    public static <K, V> Map<K, V> emptySafeMap() {
        return new ConcurrentHashMap<>();
    }

    public static <T> Collection<T> defaultIfNull(Collection<T> collection) {
        if (isEmpty(collection)) {
            return Collections.emptyList();
        }
        return collection;
    }

    public static <T> Collection<T> defaultIfNull(Collection<T> collection, T defaultContainValue) {
        if (isEmpty(collection)) {
            return ObjectUtils.isNull(defaultContainValue) ? Collections.emptyList() : Collections.singleton(defaultContainValue);
        }
        return collection;
    }

    public static <T> boolean isEmpty(Collection<T> collection) {
        return ObjectUtils.isNull(collection) || collection.isEmpty();
    }

    public static <T> boolean isEmpty(T[] array) {
        return ObjectUtils.isNull(array) || array.length == 0;
    }

    public static <T> boolean isNotEmpty(Collection<T> collection) {
        return ObjectUtils.nonNull(collection) && !collection.isEmpty();
    }

    public static <T> boolean isNotEmpty(T[] array) {
        return ObjectUtils.nonNull(array) && array.length != 0;
    }

    public static boolean isAnyEmpty(Collection... collections) {
        if (null == collections) {
            return true;
        }
        for (Collection collection : collections) {
            if (null == collection || collection.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAllNotEmpty(Collection... collections) {
        if (null == collections) {
            return false;
        }
        for (Collection collection : collections) {
            if (null == collection || collection.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public static <T, U extends Comparable<? super U>> List<T> distinct(List<T> list, Function<? super T, ? extends U> keyExtractor) {
        if (ObjectUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        if (ObjectUtils.isNull(keyExtractor)) {
            return list;
        }
        List<T> collect = list.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                new TreeSet<>(Comparator.comparing(keyExtractor))), ArrayList::new));
        return collect;
    }

    public static <T> List<T> subList(List<T> parentNode, Integer startIndex) {
        return subList(parentNode, startIndex, null);
    }

    public static <T> List<T> subList(List<T> parentNode, Integer startIndex, Integer endIndex) {
        if (ObjectUtils.anyNull(parentNode, startIndex)
                || parentNode.isEmpty()
                || parentNode.size() <= startIndex) {
            return emptyList();
        }

        int toIndex = NumberUtils.defaultIfNull(endIndex, parentNode.size());
        return parentNode.subList(startIndex, NumberUtils1.min(toIndex, parentNode.size()));
    }

}
