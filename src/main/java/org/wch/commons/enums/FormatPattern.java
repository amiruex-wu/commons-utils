package org.wch.commons.enums;

public enum FormatPattern {

    FULL_DATE_FORMAT_1(1, "yyyy-MM-dd HH:mm:ss.SSS"),
    FULL_DATE_FORMAT_2(2, "dd-MM-yyyy HH:mm:ss.SSS"),
    FULL_DATE_FORMAT_3(3, "MM-dd-yyyy HH:mm:ss.SSS"),
    FULL_DATE_FORMAT_4(4, "yyyy/MM/dd HH:mm:ss.SSS"),
    FULL_DATE_FORMAT_5(5, "dd/MM/yyyy HH:mm:ss.SSS"),
    FULL_DATE_FORMAT_6(6, "MM/dd/yyyy HH:mm:ss.SSS"),


    LOCAL_DATE_TIME_1(7, "yyyy-MM-dd HH:mm:ss"),
    LOCAL_DATE_TIME_2(8, "dd-MM-yyyy HH:mm:ss"),
    LOCAL_DATE_TIME_3(9, "MM-dd-yyyy HH:mm:ss"),
    LOCAL_DATE_TIME_4(10, "yyyy/MM/dd HH:mm:ss"),
    LOCAL_DATE_TIME_5(11, "dd/MM/yyyy HH:mm:ss"),
    LOCAL_DATE_TIME_6(12, "MM/dd/yyyy HH:mm:ss"),
    LOCAL_DATE_TIME_7(13, "yyyy-MM-dd HH:mm"),
    LOCAL_DATE_TIME_8(14, "dd-MM-yyyy HH:mm"),
    LOCAL_DATE_TIME_9(15, "MM-dd-yyyy HH:mm"),
    LOCAL_DATE_TIME_10(16, "yyyy/MM/dd HH:mm"),
    LOCAL_DATE_TIME_11(17, "dd/MM/yyyy HH:mm"),
    LOCAL_DATE_TIME_12(18, "MM/dd/yyyy HH:mm"),


    LOCAL_DATE_FORMAT_1(19, "yyyy-MM-dd"),
    LOCAL_DATE_FORMAT_2(20, "dd-MM-yyyy"),
    LOCAL_DATE_FORMAT_3(21, "MM-dd-yyyy"),
    LOCAL_DATE_FORMAT_4(22, "yyyy-MM"),
    LOCAL_DATE_FORMAT_5(23, "yyyy/MM/dd"),
    LOCAL_DATE_FORMAT_6(24, "dd/MM/yyyy"),
    LOCAL_DATE_FORMAT_7(25, "MM/dd/yyyy"),
    LOCAL_DATE_FORMAT_8(26, "yyyy/MM"),

    DATE_TIME_FORMAT_1(27, "HH:mm:ss.SSS"),
    DATE_TIME_FORMAT_2(28, "HH:mm:ss"),
    DATE_TIME_FORMAT_3(29, "HH:mm");

    FormatPattern(int code, String name) {
        this.code = code;
        this.name = name;
    }

    private int code;

    private String name;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
