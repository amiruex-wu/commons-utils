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

   /* public final static Class<?>[] BASIC_NUM_STR_CLAZZ = {Byte.class, Short.class, Integer.class, Float.class, Double.class,
            Long.class, BigDecimal.class, BigInteger.class, String.class, Boolean.class, Character.class, Map.class,
            byte.class, short.class, int.class, float.class, double.class,
            long.class, boolean.class, char.class};*/


    public static final List<Class<?>> GENERAL_CLASS = Arrays.asList(
            byte.class, short.class, int.class, float.class, double.class, long.class, boolean.class, char.class,
            Byte.class, Short.class, Integer.class, Float.class, Double.class, Long.class, Boolean.class, BigDecimal.class, BigInteger.class,
            Character.class, String.class);


    public final static Class<?>[] BASIC_DATE_TIME_CLAZZ = {LocalDateTime.class, LocalDate.class, Date.class, Calendar.class, LocalTime.class};


}
