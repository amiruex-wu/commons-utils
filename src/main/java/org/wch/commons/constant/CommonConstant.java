package org.wch.commons.constant;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * @Description: TODO
 * @Author: wuchu
 * @Version: 1.0
 * @CreateTime: 2022/9/4 10:56
 */
public class CommonConstant {

    // BASIC_
    public static final List<Class<?>> GENERAL_CLASS = Arrays.asList(
            byte.class, short.class, int.class, float.class, double.class, long.class, boolean.class, char.class,
            Byte.class, Short.class, Integer.class, Float.class, Double.class, Long.class, Boolean.class, BigDecimal.class, BigInteger.class,
            Character.class, String.class);

    public static final List<Class<?>> DATE_TIME_CLASS = Arrays.asList(LocalDateTime.class, LocalDate.class, LocalTime.class, Date.class);

}
