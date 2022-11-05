package org.wch.commons.lang;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description: 数组工具类
 * @Author: wuchu
 * @CreateTime: 2022-07-06 14:07
 */
public class ArrayUtils {

    public static boolean isEmpty(final Object[] array) {
        return ObjectUtils.isNull(array) || Array.getLength(array) == 0;
    }

    public static boolean isEmpty(final Object[][] array) {
        if (ObjectUtils.isNull(array) || Array.getLength(array) == 0) {
            return true;
        }
        return Arrays.stream(array).allMatch(item -> Array.getLength(item) == 0);
    }

    public static boolean isNotEmpty(final Object[] array) {
        return !isEmpty(array);
    }

    public static boolean isNotEmpty(final Object[][] array) {
        return !isEmpty(array);
    }

    /**
     * 并集
     *
     * @param list1
     * @param list2
     * @return
     */
    public static Optional<Collection<?>> unionSet(final Collection<?> list1, final Collection<?> list2) {
        if (ObjectUtils.allNull(list1, list2)) {
            return Optional.empty();
        }
        if (ObjectUtils.isNull(list1)) {
            return Optional.of(list2);
        }
        if (ObjectUtils.isNull(list2)) {
            return Optional.of(list1);
        }
        try {
            Set<?> collect = Stream.concat(list1.stream(), list2.stream()).collect(Collectors.toSet());
            return Optional.of(new ArrayList<>(collect));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * 并集
     *
     * @param list1
     * @param list2
     * @param <T>
     * @return
     */
    public static <T> Optional<List<T>> unionSet(final List<T> list1, final List<T> list2) {
        if (ObjectUtils.allNull(list1, list2)) {
            return Optional.empty();
        }
        if (ObjectUtils.isNull(list1)) {
            return Optional.of(list2);
        }
        if (ObjectUtils.isNull(list2)) {
            return Optional.of(list1);
        }
        Set<T> collect = Stream.concat(list1.stream(), list2.stream()).collect(Collectors.toSet());
        return Optional.of(new ArrayList<>(collect));
    }

    /**
     * 数组并集
     *
     * @param array1
     * @param array2
     * @return
     */

    public static Optional<Object[]> unionSet(final Object[] array1, final Object[] array2) {
        if (ObjectUtils.anyNull(array1, array2)) {
            return Optional.empty();
        }
        if (ObjectUtils.isNull(array1)) {
            return Optional.of(array2);
        }
        if (ObjectUtils.isNull(array2)) {
            return Optional.of(array1);
        }
        // 将数组转换为set集合
        Set<Object> set1 = new HashSet<>(Arrays.asList(array1));
        Set<Object> set2 = new HashSet<>(Arrays.asList(array2));

        // 合并两个集合
        set1.addAll(set2);
        return Optional.of(set1.toArray());
    }

    public static <T> Optional<Collection<T>> intersectionSet(final Collection<T> list1, final Collection<T> list2) {
        if (ObjectUtils.anyNull(list1, list2)) {
            return Optional.empty();
        }
        Collection<T> collect;
        if (list1.size() >= list2.size()) {
            collect = list1.stream()
                    .filter(t -> list2.stream().anyMatch(t1 -> Objects.equals(t, t1)))
                    .collect(Collectors.toList());
        } else {
            collect = list2.stream()
                    .filter(t -> list1.stream().anyMatch(t1 -> Objects.equals(t, t1)))
                    .collect(Collectors.toList());
        }
        return Optional.of(collect);
    }


    /**
     * 交集
     *
     * @param list1
     * @param list2
     * @param <T>
     * @return
     */
    public static <T> Optional<List<T>> intersectionSet(final List<T> list1, final List<T> list2) {
        if (ObjectUtils.anyNull(list1, list2)) {
            return Optional.empty();
        }
        return intersectionSet(list1, list2);
    }

    /**
     * 数组交集
     *
     * @param array1
     * @param array2
     * @return
     */
    public static Optional<Object[]> intersectionSet(final Object[] array1, final Object[] array2) {
        if (ObjectUtils.anyNull(array1, array2)) {
            return Optional.empty();
        }
        List<Object> rs = new ArrayList<>();

        // 将较长的数组转换为set
        Set<Object> set = new HashSet<>(Arrays.asList(array1.length > array2.length ? array1 : array2));

        // 遍历较短的数组，实现最少循环
        for (Object obj : array1.length > array2.length ? array2 : array1) {
            if (set.contains(obj)) {
                rs.add(obj);
            }
        }
        return Optional.of(rs.toArray());

    }

    /**
     * 差集
     *
     * @param list1 集合1
     * @param list2 集合2
     * @return
     */
    public static <T> Optional<Collection<T>> differenceSet(final Collection<T> list1, final Collection<T> list2) {
        if (ObjectUtils.anyNull(list1, list2)) {
            return Optional.empty();
        }
        try {
            if (list1.size() >= list2.size()) {
                boolean b = list1.removeAll(list2);
                return Optional.of(list1);
            } else {
                boolean b = list2.removeAll(list1);
                return Optional.of(list2);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * 差集
     *
     * @param list1
     * @param list2
     * @param <T>
     * @return
     */
    public static <T> Optional<List<T>> differenceSet(final List<T> list1, final List<T> list2) {
        if (ObjectUtils.anyNull(list1, list2)) {
            return Optional.empty();
        }
        return differenceSet(list1, list2);
    }


    /**
     * 数组差集
     *
     * @param array1
     * @param array2
     * @return
     */
    public static Optional<Object[]> differenceSet(final Object[] array1, final Object[] array2) {
        if (ObjectUtils.anyNull(array1, array2)) {
            return Optional.empty();
        }
        // 将较长的数组转换为set
        Set<Object> set = new HashSet<>(Arrays.asList(array1.length > array2.length ? array1 : array2));

        // 遍历较短的数组，实现最少循环
        for (Object obj : array1.length > array2.length ? array2 : array1) {
            // 若是集合里有相同的就删掉，若是没有就将值添加到集合
            if (set.contains(obj)) {
                set.remove(obj);
            } else {
                set.add(obj);
            }
        }

        return Optional.of(set.toArray());
    }

    public static <T> Optional<T[]> add(T[] array, T element) {
        T[] newArray;
        if (ObjectUtils.nonNull(array)) {
            if (ObjectUtils.isNull(element)) {
                return Optional.of(array);
            }
            newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), array.length + 1);
            System.arraycopy(array, 0, newArray, 0, array.length);
        } else {
            if (ObjectUtils.isNull(element)) {
                System.out.println("Arguments cannot both be null");
                // "Arguments cannot both be null"
//                throw new IllegalArgumentException();
                return Optional.empty();
            }
            newArray = (T[]) Array.newInstance(element.getClass().getComponentType(), 1);
        }
        try {
            newArray[newArray.length - 1] = element;
        } catch (ArrayStoreException var6) {
            var6.printStackTrace();
            return Optional.empty();
        }
        return Optional.of(newArray);
    }

    @SafeVarargs
    public static <T> Optional<T[]> addAll(T[] array1, T... array2) {
        if (ObjectUtils.isNull(array1)) {
            return Optional.ofNullable(clone(array2));
        } else if (ObjectUtils.isNull(array2)) {
            return Optional.ofNullable(clone(array1));
        } else {
            Class<?> type1 = array1.getClass().getComponentType();
            T[] joinedArray = (T[]) Array.newInstance(type1, array1.length + array2.length);
            System.arraycopy(array1, 0, joinedArray, 0, array1.length);

            try {
                System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
                return Optional.of(joinedArray);
            } catch (ArrayStoreException var6) {
                var6.printStackTrace();
                /*Class<?> type2 = array2.getClass().getComponentType();
                if (!type1.isAssignableFrom(type2)) {
                    throw new IllegalArgumentException("Cannot store " + type2.getName() + " in an array of " + type1.getName(), var6);
                } else {
                    throw var6;
                }*/
                return Optional.empty();
            }
        }
    }

    public static double[] addAll(double[] array1, double... array2) {
        if (ObjectUtils.isNull(array1)) {
            return clone(array2);
        } else if (ObjectUtils.isNull(array2)) {
            return clone(array1);
        } else {
            double[] joinedArray = new double[array1.length + array2.length];
            System.arraycopy(array1, 0, joinedArray, 0, array1.length);
            System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
            return joinedArray;
        }
    }

    public static Optional<Object[]> toArray(Object object) {
        if (ObjectUtils.isNull(object)) {
            return Optional.empty();
        }
        if (object.getClass().isArray()) {
            int length = Array.getLength(object);
            List<Object> temp = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                temp.add(Array.get(object, i));
            }
            if (CollectionUtils.isEmpty(temp)) {
                return Optional.empty();
            }
            return Optional.of(temp.toArray());
        } else if (object instanceof Collection) {
            Collection<?> collection = (Collection<?>) object;
            List<Object> temp = new ArrayList<>(collection);
            if (CollectionUtils.isEmpty(temp)) {
                return Optional.empty();
            }
            return Optional.of(temp.toArray());
        } else if (object instanceof String) {
            String temp = (String) object;
            try {
                JSONArray objects = JSON.parseArray(temp);
                return Optional.of(objects.toArray());
            } catch (Exception e) {
                // e.printStackTrace();
                String regex = "\\w+[,]\\w+[,]\\w+.*";
                if (temp.matches(regex)) {
                    String[] split = temp.split(",");
                    return Optional.of(split);
                }
            }
        } else {

        }
        return Optional.empty();
    }

    public static Optional<Byte[]> toWrap(byte[] bytes) {
        if (ObjectUtils.isEmpty(bytes)) {
            return Optional.empty();
        }
        Byte[] result = new Byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            result[i] = bytes[i];
        }
        return Optional.of(result);
    }

    public static byte[] toWrap(Byte[] bytes) {
        if (ObjectUtils.isEmpty(bytes)) {
            return new byte[0];
        }
        byte[] result = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            result[i] = bytes[i];
        }
        return result;
    }

    public static double[] clone(double[] array) {
        return ObjectUtils.isNull(array) ? null : array.clone();
    }

    public static <T> T[] clone(T[] array) {
        return ObjectUtils.isNull(array) ? null : array.clone();
    }

    // region 私有方法区
    private static <T> T[] copyArrayAndGrow1(T[] array, Class<?> newArrayComponentType) {
        if (array != null) {
            int arrayLength = Array.getLength(array);
            T[] newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), arrayLength + 1);
            System.arraycopy(array, 0, newArray, 0, arrayLength);
            return newArray;
        } else {
            return (T[]) Array.newInstance(newArrayComponentType, 1);
        }
    }
}
