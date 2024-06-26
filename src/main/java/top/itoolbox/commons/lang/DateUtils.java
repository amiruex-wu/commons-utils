package top.itoolbox.commons.lang;

import top.itoolbox.commons.enums.FormatPattern;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Description: 日期转换类
 * @Author: wuchu
 * @CreateTime: 2022-07-04 17:34
 */
public class DateUtils {

    private final static String[][] BEIJING_LOCAL_DATE_PATTERN = {
            {"yyyy-MM-dd", "\\d{4}-\\d{2}-\\d{2}",},
            {"yyyy-MM", "\\d{4}-\\d{2}",},
            {"yy-MM-dd", "\\d{2}-\\d{2}-\\d{2}",},
            {"dd-MM-yyyy", "\\d{2}-\\d{2}-\\d{4}",},
            {"MM-yyyy", "\\d{2}-\\d{4}",},
            {"yyyy/MM/dd", "\\d{4}/\\d{2}/\\d{2}",},
            {"yyyy/MM", "\\d{4}/\\d{2}",},
            {"yy/MM/dd", "\\d{2}/\\d{2}/\\d{2}",},
            {"dd/MM/yyyy", "\\d{2}/\\d{2}/\\d{4}",},
            {"MM/yyyy", "\\d{2}/\\d{4}",},
    };

    private final static String[][] BEIJING_LOCAL_DATE_TIME_PATTERN = {
            {"yyyy-MM-dd'T'hh:mm:ss.SSS'Z'", "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}[.]\\d{3}Z",},
            {"yyyy-MM-dd'T'hh:mm:ss.SSS", "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}[.]\\d{3}",},
            {"yyyy-MM-dd'T'hh:mm:ss", "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}",},
            {"yyyy-MM-dd'T'hh:mm", "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}",},
            {"yyyy-MM-dd hh:mm:ss.SSS'Z'", "\\d{4}-\\d{2}-\\d{2}\\s{1}\\d{2}:\\d{2}:\\d{2}[.]\\d{3}Z",},
            {"yyyy-MM-dd hh:mm:ss.SSS", "\\d{4}-\\d{2}-\\d{2}\\s{1}\\d{2}:\\d{2}:\\d{2}[.]\\d{3}",},
            {"yyyy-MM-dd hh:mm:ss", "\\d{4}-\\d{2}-\\d{2}\\s{1}\\d{2}:\\d{2}:\\d{2}",},
            {"yyyy-MM-dd hh:mm", "\\d{4}-\\d{2}-\\d{2}\\s{1}\\d{2}:\\d{2}",},

            {"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}[.]\\d{3}Z",},
            {"yyyy-MM-dd'T'HH:mm:ss.SSS", "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}[.]\\d{3}",},
            {"yyyy-MM-dd'T'HH:mm:ss", "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}",},
            {"yyyy-MM-dd'T'HH:mm", "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}",},
            {"yyyy-MM-dd HH:mm:ss.SSS'Z'", "\\d{4}-\\d{2}-\\d{2}\\s{1}\\d{2}:\\d{2}:\\d{2}[.]\\d{3}Z",},
            {"yyyy-MM-dd HH:mm:ss.SSS", "\\d{4}-\\d{2}-\\d{2}\\s{1}\\d{2}:\\d{2}:\\d{2}[.]\\d{3}",},
            {"yyyy-MM-dd HH:mm:ss", "\\d{4}-\\d{2}-\\d{2}\\s{1}\\d{2}:\\d{2}:\\d{2}",},
            {"yyyy-MM-dd HH:mm", "\\d{4}-\\d{2}-\\d{2}\\s{1}\\d{2}:\\d{2}",},

            {"yyyy/MM/dd'T'hh:mm:ss.SSS'Z'", "\\d{4}/\\d{2}/\\d{2}T\\d{2}:\\d{2}:\\d{2}[.]\\d{3}Z",},
            {"yyyy/MM/dd'T'hh:mm:ss.SSS", "\\d{4}/\\d{2}/\\d{2}T\\d{2}:\\d{2}:\\d{2}[.]\\d{3}",},
            {"yyyy/MM/dd'T'hh:mm:ss", "\\d{4}/\\d{2}/\\d{2}T\\d{2}:\\d{2}:\\d{2}",},
            {"yyyy/MM/dd'T'hh:mm", "\\d{4}/\\d{2}/\\d{2}T\\d{2}:\\d{2}",},
            {"yyyy/MM/dd hh:mm:ss.SSS'Z'", "\\d{4}/\\d{2}/\\d{2}\\s{1}\\d{2}:\\d{2}:\\d{2}[.]\\d{3}Z",},
            {"yyyy/MM/dd hh:mm:ss.SSS", "\\d{4}/\\d{2}/\\d{2}\\s{1}\\d{2}:\\d{2}:\\d{2}[.]\\d{3}",},
            {"yyyy/MM/dd hh:mm:ss", "\\d{4}/\\d{2}/\\d{2}\\s{1}\\d{2}:\\d{2}:\\d{2}",},
            {"yyyy/MM/dd hh:mm", "\\d{4}/\\d{2}/\\d{2}\\s{1}\\d{2}:\\d{2}",},
            {"yyyy/MM/dd'T'HH:mm:ss.SSS'Z'", "\\d{4}/\\d{2}/\\d{2}T\\d{2}:\\d{2}:\\d{2}[.]\\d{3}Z",},
            {"yyyy/MM/dd'T'HH:mm:ss.SSS", "\\d{4}/\\d{2}/\\d{2}T\\d{2}:\\d{2}:\\d{2}[.]\\d{3}",},
            {"yyyy/MM/dd'T'HH:mm:ss", "\\d{4}/\\d{2}/\\d{2}T\\d{2}:\\d{2}:\\d{2}",},
            {"yyyy/MM/dd'T'HH:mm", "\\d{4}/\\d{2}/\\d{2}T\\d{2}:\\d{2}",},
            {"yyyy/MM/dd HH:mm:ss.SSS'Z'", "\\d{4}/\\d{2}/\\d{2}\\s{1}\\d{2}:\\d{2}:\\d{2}[.]\\d{3}Z",},
            {"yyyy/MM/dd HH:mm:ss.SSS", "\\d{4}/\\d{2}/\\d{2}\\s{1}\\d{2}:\\d{2}:\\d{2}[.]\\d{3}",},
            {"yyyy/MM/dd HH:mm:ss", "\\d{4}/\\d{2}/\\d{2}\\s{1}\\d{2}:\\d{2}:\\d{2}",},
            {"yyyy/MM/dd HH:mm", "\\d{4}/\\d{2}/\\d{2}\\s{1}\\d{2}:\\d{2}",},
    };
    // todo
    //1、英文日期分英式和美式，英式日期格式：Date,Month,Year；美式日期格式：“Month,Date,Year”。
    //
    //2、具体举例如下：
    //
    //（1）8th March,2004或8 March,2004（英式）；
    //
    //（2）March 8th,2004或March 8,2004（美式）。
    private final static String[][] US_LOCAL_DATE_PATTERN = {
            {"MM-dd-yyyy", "\\d{2}-\\d{2}-\\d{4}",},
            {"MM-yyyy", "\\d{2}-\\d{4}",},
            {"MM/dd/yyyy", "\\d{2}/\\d{2}/\\d{4}",},
            {"MM/yyyy", "\\d{2}-\\d{4}",},
            {"MMMM dd, yyyy", "\\w{3,}\\s\\d{1,2},\\s\\d{4}",},

            {"MMM dd, yyyy H:mm:ss.SSS", "\\w{3,}\\s\\d{2},\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}[.]\\d{3}",},

            {"MM-dd-yyyy'T'HH:mm:ss.SSS'Z'", "\\d{2}-\\d{2}-\\d{4}T\\d{2}:\\d{2}:\\d{2}[.]\\d{3}Z",},
            {"MM-dd-yyyy'T'HH:mm:ss.SSS", "\\d{2}-\\d{2}-\\d{4}T\\d{2}:\\d{2}:\\d{2}[.]\\d{3}",},
            {"MM-dd-yyyy'T'HH:mm:ss", "\\d{2}-\\d{2}-\\d{4}T\\d{2}:\\d{2}:\\d{2}",},
            {"MM-dd-yyyy'T'HH:mm", "\\d{2}-\\d{2}-\\d{4}T\\d{2}:\\d{2}",},
            {"MM-dd-yyyy HH:mm:ss.SSS'Z'", "\\d{2}-\\d{2}-\\d{4}\\s\\d{2}:\\d{2}:\\d{2}[.]\\d{3}Z",},
            {"MM-dd-yyyy HH:mm:ss.SSS", "\\d{2}-\\d{2}-\\d{4}\\s\\d{2}:\\d{2}:\\d{2}[.]\\d{3}",},
            {"MM-dd-yyyy HH:mm:ss", "\\d{2}-\\d{2}-\\d{4}\\s\\d{2}:\\d{2}:\\d{2}",},
            {"MM-dd-yyyy HH:mm", "\\d{2}-\\d{2}-\\d{4}\\s\\d{2}:\\d{2}",},

            {"MM-dd-yyyy'T'hh:mm:ss.SSS'Z'", "\\d{2}-\\d{2}-\\d{4}T\\d{2}:\\d{2}:\\d{2}[.]\\d{3}Z",},
            {"MM-dd-yyyy'T'hh:mm:ss.SSS", "\\d{2}-\\d{2}-\\d{4}T\\d{2}:\\d{2}:\\d{2}[.]\\d{3}",},
            {"MM-dd-yyyy'T'hh:mm:ss", "\\d{2}-\\d{2}-\\d{4}T\\d{2}:\\d{2}:\\d{2}",},
            {"MM-dd-yyyy'T'hh:mm", "\\d{2}-\\d{2}-\\d{4}T\\d{2}:\\d{2}",},
            {"MM-dd-yyyy hh:mm:ss.SSS'Z'", "\\d{2}-\\d{2}-\\d{4}\\s\\d{2}:\\d{2}:\\d{2}[.]\\d{3}Z",},
            {"MM-dd-yyyy hh:mm:ss.SSS", "\\d{2}-\\d{2}-\\d{4}\\s\\d{2}:\\d{2}:\\d{2}[.]\\d{3}",},
            {"MM-dd-yyyy hh:mm:ss", "\\d{2}-\\d{2}-\\d{4}\\s\\d{2}:\\d{2}:\\d{2}",},
            {"MM-dd-yyyy hh:mm", "\\d{2}-\\d{2}-\\d{4}\\s\\d{2}:\\d{2}",},

            {"MM/dd/yyyy'T'HH:mm:ss.SSS'Z'", "\\d{2}/\\d{2}/\\d{4}T\\d{2}:\\d{2}:\\d{2}[.]\\d{3}Z",},
            {"MM/dd/yyyy'T'HH:mm:ss.SSS", "\\d{2}/\\d{2}/\\d{4}T\\d{2}:\\d{2}:\\d{2}[.]\\d{3}",},
            {"MM/dd/yyyy'T'HH:mm:ss", "\\d{2}/\\d{2}/\\d{4}T\\d{2}:\\d{2}:\\d{2}",},
            {"MM/dd/yyyy'T'HH:mm", "\\d{2}/\\d{2}/\\d{4}T\\d{2}:\\d{2}",},
            {"MM/dd/yyyy HH:mm:ss.SSS'Z'", "\\d{2}/\\d{2}/\\d{4}\\s\\d{2}:\\d{2}:\\d{2}[.]\\d{3}Z",},
            {"MM/dd/yyyy HH:mm:ss.SSS", "\\d{2}/\\d{2}/\\d{4}\\s\\d{2}:\\d{2}:\\d{2}[.]\\d{3}",},
            {"MM/dd/yyyy HH:mm:ss", "\\d{2}/\\d{2}/\\d{4}\\s\\d{2}:\\d{2}:\\d{2}",},
            {"MM/dd/yyyy HH:mm", "\\d{2}/\\d{2}/\\d{4}\\s\\d{2}:\\d{2}",},

            {"MM/dd/yyyy'T'hh:mm:ss.SSS'Z'", "\\d{2}/\\d{2}/\\d{4}T\\d{2}:\\d{2}:\\d{2}[.]\\d{3}Z",},
            {"MM/dd/yyyy'T'hh:mm:ss.SSS", "\\d{2}/\\d{2}/\\d{4}T\\d{2}:\\d{2}:\\d{2}[.]\\d{3}",},
            {"MM/dd/yyyy'T'hh:mm:ss", "\\d{2}/\\d{2}/\\d{4}T\\d{2}:\\d{2}:\\d{2}",},
            {"MM/dd/yyyy'T'hh:mm", "\\d{2}/\\d{2}/\\d{4}T\\d{2}:\\d{2}",},
            {"MM/dd/yyyy hh:mm:ss.SSS'Z'", "\\d{2}/\\d{2}/\\d{4}\\s\\d{2}:\\d{2}:\\d{2}[.]\\d{3}Z",},
            {"MM/dd/yyyy hh:mm:ss.SSS", "\\d{2}/\\d{2}/\\d{4}\\s\\d{2}:\\d{2}:\\d{2}[.]\\d{3}",},
            {"MM/dd/yyyy hh:mm:ss", "\\d{2}/\\d{2}/\\d{4}\\s\\d{2}:\\d{2}:\\d{2}",},
            {"MM/dd/yyyy hh:mm", "\\d{2}/\\d{2}/\\d{4}\\s\\d{2}:\\d{2}",},
    };

    private final static String[][] EN_LOCAL_DATE_PATTERN = {
            {"dd-MM-yyyy", "\\d{2}-\\d{2}-\\d{4}",},
            {"MM-yyyy", "\\d{2}-\\d{4}",},
            {"dd/MM/yyyy", "\\d{2}/\\d{2}/\\d{4}",},
            {"MM/yyyy", "\\d{2}/\\d{4}",},
            {"MMMM dd, yyyy", "\\w{3,}\\s\\d{1,2},\\s\\d{4}",},

            {"MMM dd, yyyy H:mm:ss.SSS", "\\w{3,}\\s\\d{2},\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}[.]\\d{3}",},

            {"dd-MM-yyyy'T'HH:mm:ss.SSS'Z'", "\\d{2}-\\d{2}-\\d{4}T\\d{2}:\\d{2}:\\d{2}[.]\\d{3}Z",},
            {"dd-MM-yyyy'T'HH:mm:ss.SSS", "\\d{2}-\\d{2}-\\d{4}T\\d{2}:\\d{2}:\\d{2}[.]\\d{3}",},
            {"dd-MM-yyyy'T'HH:mm:ss", "\\d{2}-\\d{2}-\\d{4}T\\d{2}:\\d{2}:\\d{2}",},
            {"dd-MM-yyyy'T'HH:mm", "\\d{2}-\\d{2}-\\d{4}T\\d{2}:\\d{2}",},
            {"dd-MM-yyyy HH:mm:ss.SSS'Z'", "\\d{2}-\\d{2}-\\d{4}\\s\\d{2}:\\d{2}:\\d{2}[.]\\d{3}Z",},
            {"dd-MM-yyyy HH:mm:ss.SSS", "\\d{2}-\\d{2}-\\d{4}\\s\\d{2}:\\d{2}:\\d{2}[.]\\d{3}",},
            {"dd-MM-yyyy HH:mm:ss", "\\d{2}-\\d{2}-\\d{4}\\s\\d{2}:\\d{2}:\\d{2}",},
            {"dd-MM-yyyy HH:mm", "\\d{2}-\\d{2}-\\d{4}\\s\\d{2}:\\d{2}",},

            {"dd-MM-yyyy'T'hh:mm:ss.SSS'Z'", "\\d{2}-\\d{2}-\\d{4}T\\d{2}:\\d{2}:\\d{2}[.]\\d{3}Z",},
            {"dd-MM-yyyy'T'hh:mm:ss.SSS", "\\d{2}-\\d{2}-\\d{4}T\\d{2}:\\d{2}:\\d{2}[.]\\d{3}",},
            {"dd-MM-yyyy'T'hh:mm:ss", "\\d{2}-\\d{2}-\\d{4}T\\d{2}:\\d{2}:\\d{2}",},
            {"dd-MM-yyyy'T'hh:mm", "\\d{2}-\\d{2}-\\d{4}T\\d{2}:\\d{2}",},
            {"dd-MM-yyyy hh:mm:ss.SSS'Z'", "\\d{2}-\\d{2}-\\d{4}\\s\\d{2}:\\d{2}:\\d{2}[.]\\d{3}Z",},
            {"dd-MM-yyyy hh:mm:ss.SSS", "\\d{2}-\\d{2}-\\d{4}\\s\\d{2}:\\d{2}:\\d{2}[.]\\d{3}",},
            {"dd-MM-yyyy hh:mm:ss", "\\d{2}-\\d{2}-\\d{4}\\s\\d{2}:\\d{2}:\\d{2}",},
            {"dd-MM-yyyy hh:mm", "\\d{2}-\\d{2}-\\d{4}\\s\\d{2}:\\d{2}",},

            {"dd/MM/yyyy'T'HH:mm:ss.SSS'Z'", "\\d{2}/\\d{2}/\\d{4}T\\d{2}:\\d{2}:\\d{2}[.]\\d{3}Z",},
            {"dd/MM/yyyy'T'HH:mm:ss.SSS", "\\d{2}/\\d{2}/\\d{4}T\\d{2}:\\d{2}:\\d{2}[.]\\d{3}",},
            {"dd/MM/yyyy'T'HH:mm:ss", "\\d{2}/\\d{2}/\\d{4}T\\d{2}:\\d{2}:\\d{2}",},
            {"dd/MM/yyyy'T'HH:mm", "\\d{2}/\\d{2}/\\d{4}T\\d{2}:\\d{2}",},
            {"dd/MM/yyyy HH:mm:ss.SSS'Z'", "\\d{2}/\\d{2}/\\d{4}\\s\\d{2}:\\d{2}:\\d{2}[.]\\d{3}Z",},
            {"dd/MM/yyyy HH:mm:ss.SSS", "\\d{2}/\\d{2}/\\d{4}\\s\\d{2}:\\d{2}:\\d{2}[.]\\d{3}",},
            {"dd/MM/yyyy HH:mm:ss", "\\d{2}/\\d{2}/\\d{4}\\s\\d{2}:\\d{2}:\\d{2}",},
            {"dd/MM/yyyy HH:mm", "\\d{2}/\\d{2}/\\d{4}\\s\\d{2}:\\d{2}",},

            {"dd/MM/yyyy'T'hh:mm:ss.SSS'Z'", "\\d{2}/\\d{2}/\\d{4}T\\d{2}:\\d{2}:\\d{2}[.]\\d{3}Z",},
            {"dd/MM/yyyy'T'hh:mm:ss.SSS", "\\d{2}/\\d{2}/\\d{4}T\\d{2}:\\d{2}:\\d{2}[.]\\d{3}",},
            {"dd/MM/yyyy'T'hh:mm:ss", "\\d{2}/\\d{2}/\\d{4}T\\d{2}:\\d{2}:\\d{2}",},
            {"dd/MM/yyyy'T'hh:mm", "\\d{2}/\\d{2}/\\d{4}T\\d{2}:\\d{2}",},
            {"dd/MM/yyyy hh:mm:ss.SSS'Z'", "\\d{2}/\\d{2}/\\d{4}\\s\\d{2}:\\d{2}:\\d{2}[.]\\d{3}Z",},
            {"dd/MM/yyyy hh:mm:ss.SSS", "\\d{2}/\\d{2}/\\d{4}\\s\\d{2}:\\d{2}:\\d{2}[.]\\d{3}",},
            {"dd/MM/yyyy hh:mm:ss", "\\d{2}/\\d{2}/\\d{4}\\s\\d{2}:\\d{2}:\\d{2}",},
            {"dd/MM/yyyy hh:mm", "\\d{2}/\\d{2}/\\d{4}\\s\\d{2}:\\d{2}",},
    };

    /**
     * <p>
     * Converts a time string to a date and specifies the conversion format to an English environment
     * </p>
     *
     * @param source  time string
     * @param pattern format pattern
     * @return
     */
    public static Optional<LocalDateTime> parseWithEnLocale(String source, String pattern) {
        if (StringUtils.anyBlank(source, pattern)) {
            return Optional.empty();
        }
        DateFormat dateTimeParser = new SimpleDateFormat(pattern, Locale.ENGLISH);
        try {
            Date date = dateTimeParser.parse(source);
            return toLocalDateTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * <p>
     * Converts a time string to a date and specifies the conversion format
     * </p>
     *
     * @param source  time string
     * @param pattern format pattern
     * @return
     */
    public static Optional<LocalDateTime> parse(String source, String pattern) {
        if (StringUtils.anyBlank(source, pattern)) {
            return Optional.empty();
        }
        try {
            LocalDateTime parse = LocalDateTime.parse(source, DateTimeFormatter.ofPattern(pattern));
            return Optional.of(parse);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * <p>
     * Converts a time string to a date
     * </p>
     *
     * @param source time string
     * @return
     */
    public static Optional<LocalDateTime> parse(String source) {
        if (StringUtils.isBlank(source)) {
            return Optional.empty();
        }
        boolean zh = ZoneId.systemDefault().getId().contains("Asia/Shanghai");
        boolean us = ZoneId.systemDefault().getId().contains("America/");
        if (zh) {
            return getLocalDateTimeWithZh(source);
        }
        if (us) {
            return getLocalDateTimeWithUS(source);
        } else {
            return getLocalDateTimeWithEN(source);
        }
    }

    /**
     * <p>
     * Convert Date to LocalDateTime
     * </p>
     *
     * @param date the date
     * @return
     */
    public static Optional<LocalDateTime> toLocalDateTime(LocalDate date) {
        if (Objects.isNull(date)) {
            return Optional.empty();
        }
        LocalDateTime localDateTime = LocalDateTime.of(date, LocalTime.of(0, 0, 0, 0));
        return Optional.of(localDateTime);
    }

    /**
     * <p>
     * Convert LocalDateTime to Date
     * </p>
     *
     * @param date the date
     * @return
     */
    public static Optional<LocalDateTime> toLocalDateTime(Date date) {
        if (Objects.isNull(date)) {
            return Optional.empty();
        }
        LocalDateTime localDateTime = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        return Optional.of(localDateTime);
    }

    /**
     * <p>
     * Convert timeStamp to LocalDateTime
     * </p>
     *
     * @param timeStamp
     * @return
     */
    public static Optional<LocalDateTime> toLocalDateTime(Long timeStamp) {
        if (Objects.isNull(timeStamp)) {
            return Optional.empty();
        }
        LocalDateTime localDateTime = Instant.ofEpochMilli(timeStamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        return Optional.of(localDateTime);
    }

    /**
     * <p>
     * Convert date to LocalDate
     * </p>
     *
     * @param date the date
     * @return
     */
    public static Optional<LocalDate> toLocalDate(Date date) {
        if (Objects.isNull(date)) {
            return Optional.empty();
        }
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return Optional.of(localDate);
    }

    /**
     * <p>
     * Convert timeStamp to LocalDate
     * </p>
     *
     * @param timeStamp the timestamp with Long type
     * @return
     */
    public static Optional<LocalDate> toLocalDate(Long timeStamp) {
        if (Objects.isNull(timeStamp)) {
            return Optional.empty();
        }
        LocalDate localDate = Instant.ofEpochMilli(timeStamp).atZone(ZoneId.systemDefault()).toLocalDate();
        return Optional.of(localDate);
    }

    /**
     * <p>
     * Convert LocalDate to date
     * </p>
     *
     * @param localDate the localDate
     * @return Optional<Date>
     */
    public static Optional<Date> toDate(LocalDate localDate) {
        if (Objects.isNull(localDate)) {
            return Optional.empty();
        }
        Date date = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        return Optional.of(date);
    }

    /**
     * <p>
     * Calculates the differ days between two dates
     * </p>
     *
     * @param startTime the start time
     * @param endTime   the end time
     * @return Optional<Long>
     */
    public static Optional<Long> differDays(LocalDateTime startTime, LocalDateTime endTime) {
        if (Objects.isNull(startTime) || Objects.isNull(endTime)) {
            return Optional.empty();
        }
        long days;
        if (startTime.isAfter(endTime)) {
            days = -Duration.between(endTime, startTime).toDays();
        } else {
            days = Duration.between(startTime, endTime).toDays();
        }
        return Optional.of(days);
    }

    /**
     * <p>
     * Calculates the differ days between two dates
     * </p>
     *
     * @param startDate the start date
     * @param endDate   the end date
     * @return Optional<Long>
     */
    public static Optional<Long> differDays(LocalDate startDate, LocalDate endDate) {
        if (ObjectUtils.anyNull(startDate, endDate)) {
            return Optional.empty();
        }
        long days;
        if (startDate.isAfter(endDate)) {
            days = -Duration.between(endDate, startDate).toDays();
        } else {
            days = Duration.between(startDate, endDate).toDays();
        }
        return Optional.of(days);
    }

    /**
     * <p>
     * Calculates the differ days between two dates
     * </p>
     *
     * @param startDate the start date
     * @param endDate   the end time
     * @return Optional<Long>
     */
    public static Optional<Long> differDays(LocalDate startDate, LocalDateTime endDate) {
        if (ObjectUtils.anyNull(startDate, endDate)) {
            return Optional.empty();
        }
        long days;
        if (startDate.isAfter(endDate.toLocalDate())) {
            days = -Duration.between(endDate.toLocalDate(), startDate).toDays();
        } else {
            days = Duration.between(startDate, endDate.toLocalDate()).toDays();
        }
        return Optional.of(days);
    }

    /**
     * <p>
     * Calculates the differ days between two dates
     * </p>
     *
     * @param startDate the start date with time
     * @param endDate   the end date
     * @return
     */
    public static Optional<Long> differDays(LocalDateTime startDate, LocalDate endDate) {
        if (ObjectUtils.anyNull(startDate, endDate)) {
            return Optional.empty();
        }
        long days;
        if (startDate.toLocalDate().isAfter(endDate)) {
            days = -Duration.between(endDate, startDate.toLocalDate()).toDays();
        } else {
            days = Duration.between(startDate.toLocalDate(), endDate).toDays();
        }
        return Optional.of(days);
    }

    /**
     * <p>
     * Calculates the differ hours between two dates
     * </p>
     *
     * @param startTime the start time
     * @param endTime   the end time
     * @return
     */
    public static Optional<Long> differHours(LocalDateTime startTime, LocalDateTime endTime) {
        if (ObjectUtils.anyNull(startTime, endTime)) {
            return Optional.empty();
        }
        long days;
        if (startTime.isAfter(endTime)) {
            days = -Duration.between(endTime, startTime).toHours();
        } else {
            days = Duration.between(startTime, endTime).toHours();
        }
        return Optional.of(days);
    }

    /**
     * <p>
     * Calculates the differ hours between two dates
     * </p>
     *
     * @param startDate the start date
     * @param endDate   the end date
     * @return
     */
    public static Optional<Long> differHours(LocalDate startDate, LocalDate endDate) {
        if (ObjectUtils.anyNull(startDate, endDate)) {
            return Optional.empty();
        }
        long days;
        if (startDate.isAfter(endDate)) {
            days = -Duration.between(endDate, startDate).toHours();
        } else {
            days = Duration.between(startDate, endDate).toHours();
        }
        return Optional.of(days);
    }

    /**
     * <p>
     * Calculates the differ hours between two dates
     * </p>
     *
     * @param startDate the start date
     * @param endDate   the end date with time
     * @return
     */
    public static Optional<Long> differHours(LocalDate startDate, LocalDateTime endDate) {
        if (ObjectUtils.anyNull(startDate, endDate)) {
            return Optional.empty();
        }
        LocalDateTime startTime = LocalDateTime.of(startDate, LocalTime.of(0, 0, 0, 0));
        long days;
        if (startTime.isAfter(endDate)) {
            days = -Duration.between(endDate, startTime).toHours();
        } else {
            days = Duration.between(startTime, endDate).toHours();
        }
        return Optional.of(days);
    }

    /**
     * <p>
     * Calculates the differ hours between two dates
     * </p>
     *
     * @param startDate the start date with time
     * @param endDate   the end date
     * @return
     */
    public static Optional<Long> differHours(LocalDateTime startDate, LocalDate endDate) {
        if (ObjectUtils.anyNull(startDate, endDate)) {
            return Optional.empty();
        }
        LocalDateTime endTime = LocalDateTime.of(endDate, LocalTime.of(0, 0, 0, 0));
        long days;
        if (startDate.isAfter(endTime)) {
            days = -Duration.between(endTime, startDate).toHours();
        } else {
            days = Duration.between(startDate, endTime).toHours();
        }
        return Optional.of(days);
    }

    /**
     * convert java.time.LocalDateTime to Date
     *
     * @param localDateTime
     * @return
     */
    public static Optional<Date> toDate(LocalDateTime localDateTime) {
        if (Objects.isNull(localDateTime)) {
            return Optional.empty();
        }
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        return Optional.of(date);
    }

    /**
     * <p>
     * Convert the timestamp to a date
     * </p>
     *
     * @param timeStamp the timeStamp
     * @return
     */
    public static Optional<Date> toDate(Long timeStamp) {
        if (Objects.isNull(timeStamp)) {
            return Optional.empty();
        }
        return Optional.of(new Date(timeStamp));
    }

    /**
     * <p>
     * LocalDate转时间戳
     * </p>
     *
     * @param localDate
     * @return
     */
    public static Optional<Long> toTimestamp(LocalDate localDate) {
        if (Objects.isNull(localDate)) {
            return Optional.empty();
        }
        long milli = localDate.atStartOfDay(ZoneOffset.systemDefault()).toInstant().toEpochMilli();
        return Optional.of(milli);
    }

    /**
     * <p>
     * LocalDateTime转时间戳
     * </p>
     *
     * @param localDateTime
     * @return
     */
    public static Optional<Long> toTimestamp(LocalDateTime localDateTime) {
        if (Objects.isNull(localDateTime)) {
            return Optional.empty();
        }
        long milli = localDateTime.atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli();
        return Optional.of(milli);
    }


    /**
     * <p>Checks if two date objects represent the same instant in time.</p>
     *
     * <p>This method compares the long millisecond time of the two objects.</p>
     *
     * @param date1 the first date, not altered, not null
     * @param date2 the second date, not altered, not null
     * @return true if they represent the same millisecond instant
     * @throws IllegalArgumentException if either date is {@code null}
     */
    public static boolean isSameInstant(final Date date1, final Date date2) {
        if (ObjectUtils.anyNull(date1, date2)) {
            throw nullDateIllegalArgumentException();
        }
        return date1.getTime() == date2.getTime();
    }

    /**
     * <p>Checks if two date objects represent the same instant in time.</p>
     *
     * <p>This method compares the long millisecond time of the two objects.</p>
     *
     * @param localDateTime the localDateTime for format
     * @param formatPattern dateTime format pattern
     * @return String if they represent the non-null
     */
    public static Optional<String> format(LocalDateTime localDateTime, FormatPattern formatPattern) {
        if (ObjectUtils.anyNull(localDateTime, formatPattern)) {
            return Optional.empty();
        }
        String format = localDateTime.format(DateTimeFormatter.ofPattern(formatPattern.getName()));
        return Optional.of(format);
    }

    public static Optional<String> format(Date date, FormatPattern formatPattern) {
        if (ObjectUtils.anyNull(date, formatPattern)) {
            return Optional.empty();
        }
        return toLocalDateTime(date)
                .map(item -> format(item, formatPattern))
                .orElse(null);
    }

    public static Optional<String> format(LocalDate localDate, FormatPattern formatPattern) {
        if (ObjectUtils.anyNull(localDate, formatPattern)) {
            return Optional.empty();
        }
        String format = LocalDateTime.of(localDate, LocalTime.of(0, 0, 0, 0))
                .format(DateTimeFormatter.ofPattern(formatPattern.getName()));
        return Optional.of(format);
    }

    public static Optional<String> format(LocalTime localTime, FormatPattern formatPattern) {
        if (ObjectUtils.anyNull(localTime, formatPattern)) {
            return Optional.empty();
        }
        String format = LocalDateTime.of(LocalDate.now(), localTime)
                .format(DateTimeFormatter.ofPattern(formatPattern.getName()));
        return Optional.of(format);
    }

    // region 私有方法区
    private static Optional<LocalDateTime> getLocalDateTimeWithEN(String dateTimeStr) {
        String trim = dateTimeStr.trim();
        Optional<LocalDateTime> result = Optional.empty();
        int startIndex = trim.length() <= 10 ? 0 : trim.contains("/") ? 22 : 4;
        if (startIndex <= 10) {
            for (int i = startIndex; i < EN_LOCAL_DATE_PATTERN.length; i++) {
                if (trim.matches(EN_LOCAL_DATE_PATTERN[i][1])) {
                    result = parseLocalDateToLocalDateTime(trim, EN_LOCAL_DATE_PATTERN[i][0]);
                    if (result.isPresent()) {
                        break;
                    }
                }
            }
        } else {
            boolean isAM = trim.contains("a") || trim.contains("A");
            boolean isPM = trim.contains("p") || trim.contains("P");
            String replaceAll = trim.replaceAll("a", "")
                    .replaceAll("A", "")
                    .replaceAll("p", "")
                    .replaceAll("P", "");
            for (int i = startIndex; i < EN_LOCAL_DATE_PATTERN.length; i++) {
                if (replaceAll.matches(EN_LOCAL_DATE_PATTERN[i][1])) {
                    Optional<LocalDateTime> parse = parse(replaceAll, EN_LOCAL_DATE_PATTERN[i][0]);
                    if (parse.isPresent()) {
                        if (isPM && parse.get().getHour() < 12) {
                            result = Optional.of(parse.get().plusHours(12));
                        } else if (isAM && parse.get().getHour() > 12) {
                            result = Optional.of(parse.get().minusHours(12));
                        } else {
                            result = parse;
                        }
                    }
                    if (result.isPresent()) {
                        break;
                    }
                }
            }
        }
        return result;
    }

    private static Optional<LocalDateTime> getLocalDateTimeWithUS(String dateTimeStr) {
        String trim = dateTimeStr.trim();
        Optional<LocalDateTime> result = Optional.empty();
        int startIndex = trim.length() <= 10 ? 0 : trim.contains("/") ? 22 : 4;
        if (startIndex <= 10) {
            for (int i = startIndex; i < US_LOCAL_DATE_PATTERN.length; i++) {
                if (trim.matches(US_LOCAL_DATE_PATTERN[i][1])) {
                    result = parseLocalDateToLocalDateTime(trim, US_LOCAL_DATE_PATTERN[i][0]);
                    if (result.isPresent()) {
                        break;
                    }
                }
            }
        } else {
            boolean isAM = trim.contains("a") || trim.contains("A");
            boolean isPM = trim.contains("p") || trim.contains("P");
            String replaceAll = trim.replaceAll("a", "")
                    .replaceAll("A", "")
                    .replaceAll("p", "")
                    .replaceAll("P", "");
            for (int i = startIndex; i < US_LOCAL_DATE_PATTERN.length; i++) {
                if (replaceAll.matches(US_LOCAL_DATE_PATTERN[i][1])) {
                    Optional<LocalDateTime> parse = parse(replaceAll, US_LOCAL_DATE_PATTERN[i][0]);
                    if (parse.isPresent()) {
                        if (isPM && parse.get().getHour() < 12) {
                            result = Optional.of(parse.get().plusHours(12));
                        } else if (isAM && parse.get().getHour() > 12) {
                            result = Optional.of(parse.get().minusHours(12));
                        } else {
                            result = parse;
                        }
                    }
                    if (result.isPresent()) {
                        break;
                    }
                }
            }
        }
        return result;
    }

    private static Optional<LocalDateTime> getLocalDateTimeWithZh(String dateTimeStr) {
        String trim = dateTimeStr.trim();
        AtomicReference<Optional<LocalDateTime>> result = new AtomicReference<>(Optional.empty());
        if (trim.length() <= 10) {
            int startIndex = trim.contains("/") ? 5 : 0;
            for (int i = startIndex; i < BEIJING_LOCAL_DATE_PATTERN.length; i++) {
                if (trim.matches(BEIJING_LOCAL_DATE_PATTERN[i][1])) {
                    result.set(parseLocalDateToLocalDateTime(trim, BEIJING_LOCAL_DATE_PATTERN[i][0]));
                    if (result.get().isPresent()) {
                        break;
                    }
                }
            }
        } else {
            boolean isAM = trim.contains("a") || trim.contains("A");
            boolean isPM = trim.contains("p") || trim.contains("P");
            String replaceAll = trim.replaceAll("a", "")
                    .replaceAll("A", "")
                    .replaceAll("p", "")
                    .replaceAll("P", "");
            int startIndex = replaceAll.contains("/") ? 8 : 0;
            for (int i = startIndex; i < BEIJING_LOCAL_DATE_TIME_PATTERN.length; i++) {
                if (replaceAll.matches(BEIJING_LOCAL_DATE_TIME_PATTERN[i][1])) {
                    Optional<LocalDateTime> parse = parse(replaceAll, BEIJING_LOCAL_DATE_TIME_PATTERN[i][0]);
                    parse.ifPresent(localDateTime -> {
                        if (isPM && localDateTime.getHour() < 12) {
                            result.set(Optional.of(localDateTime.plusHours(12)));
                        } else if (isAM && localDateTime.getHour() > 12) {
                            result.set(Optional.of(localDateTime.minusHours(12)));
                        } else {
                            result.set(Optional.of(localDateTime));
                        }
                    });
                    if (result.get().isPresent()) {
                        break;
                    }
                }
            }
        }
        return result.get();
    }

    private static IllegalArgumentException nullDateIllegalArgumentException() {
        return new IllegalArgumentException("The date must not be null");
    }

    private static Optional<LocalDateTime> parseLocalDateToLocalDateTime(String source, String pattern) {
        try {
            LocalDate parse = LocalDate.parse(source, DateTimeFormatter.ofPattern(pattern));
            LocalDateTime localDateTime = LocalDateTime.of(parse, LocalTime.of(0, 0, 0, 0));
            return Optional.of(localDateTime);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
