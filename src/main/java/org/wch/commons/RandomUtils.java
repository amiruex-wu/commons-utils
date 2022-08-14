package org.wch.commons;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description: TODO
 * @Author: wuchu
 * @CreateTime: 2022-07-19 10:33
 */
public class RandomUtils {
    private static final int[] SPECIAL_CHARS = {33, 64, 35, 36, 37, 94, 38, 42, 95};
    private static final int[] NUMBER_CHAR = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57};
    private static final Integer[] MIX_ALL_CHAR;
    private static final Random RANDOM = new Random();

    static {
        List<Integer> chars = new ArrayList<>();
        for (int i = 65; i < 123; i++) {
            if (i > 90 && i < 97) {
                continue;
            }
            chars.add(i);
        }

        List<Integer> numbers = Arrays.stream(NUMBER_CHAR).boxed().collect(Collectors.toList());
        chars.addAll(numbers);
        List<Integer> collect = Arrays.stream(SPECIAL_CHARS).boxed().collect(Collectors.toList());
        chars.addAll(collect);
        MIX_ALL_CHAR = chars.toArray(new Integer[0]);
    }

    /**
     * 生成UUID
     *
     * @return
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }


    /**
     * <p>
     * Returns a random boolean value
     * </p>
     *
     * @return the random boolean
     */
    public static boolean nextBoolean() {
        return RANDOM.nextBoolean();
    }

    public static int nextInt(int start, int end) {
        if (start > -1 && end > -1 && end > start) {
            return RANDOM.nextInt(end - start + 1) + start;
        } else if (start == end) {
            return start;
        }
        return 0;
    }

    public static long nextLong() {
        return RANDOM.nextLong();
    }

    public static long nextLong(long start, long end) {
        if (start > -1 && end > -1 && end > start) {
            long aLong = RANDOM.nextLong();
            if (aLong > end) {
                return aLong + start - end;
            }
            if (aLong < start) {
                return aLong + end - start;
            }
        } else if (start == end) {
            return start;
        }
        return 0L;
    }

    public static float nextFloat() {
        return RANDOM.nextFloat();
    }

    public static float nextFloat(float start, float end) {
        if (start > -1 && end > -1 && end > start) {
            return RANDOM.nextFloat() * (end - start) + start;
        } else if (start == end) {
            return start;
        }
        return 0.0f;
    }

    public static double nextDouble(double start, double end) {
        if (start > 0 && end > 0 && end > start) {
            return RANDOM.nextDouble() * (end - start) + start;
        } else if (start == end) {
            return start;
        }
        return 0.0;
    }

    public static Optional<String> randomString(int length, MixType mixType) {
        if (length == 0 || Objects.isNull(mixType)) {
            return Optional.empty();
        }
        StringBuilder stringBuffer = new StringBuilder();
        switch (mixType) {
            case NUMBER:
                for (int i = 0; i < length; i++) {
                    stringBuffer.append(nextInt(0, 9));
                }
                break;
            case LETTER:
                // a-z 97->122
                // A-Z 65->90
                for (int i = 0; i < length; i++) {
                    int index = nextInt(65, 122);
                    if (index > 90 && index < 97) {
                        index += 7;
                    }
                    stringBuffer.append((char) index);
                }
                break;
            case SPECIAL_CHAR:
                for (int i = 0; i < length; i++) {
                    stringBuffer.append((char) SPECIAL_CHARS[nextInt(0, SPECIAL_CHARS.length - 1)]);
                }
                break;
            case MIX_NUM_SPECIAL_CHAR:
                Integer[] integers = Stream.concat(Arrays.stream(SPECIAL_CHARS).boxed(), Arrays.stream(NUMBER_CHAR).boxed())
                        .toArray(Integer[]::new);
                for (int i = 0; i < length; i++) {
                    stringBuffer.append((char) integers[nextInt(0, integers.length - 1)].intValue());
                }
                break;
            case MIX_ALL:
                for (int i = 0; i < length; i++) {
                    stringBuffer.append((char) MIX_ALL_CHAR[nextInt(0, MIX_ALL_CHAR.length - 1)].intValue());
                }
                break;
        }
        return Optional.of(stringBuffer.toString());
    }

    public enum MixType {
        NUMBER,
        LETTER,
        SPECIAL_CHAR,
        MIX_NUM_SPECIAL_CHAR,
        MIX_ALL,
        ;
    }
}
