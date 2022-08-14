package org.wch.commons.enums;

import org.wch.commons.lang.StringUtils;

import java.util.Optional;

public enum OperateSystemType {

    WINDOWS(1, "windows"),
    UNIX(2, "unix"),
    LINUX(3, "linux"),
    OTHER(0, "other");

    OperateSystemType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    private int code;

    private String name;

    public static Optional<OperateSystemType> getInstance(String name) {
        if (StringUtils.isBlank(name)) {
            return Optional.empty();
        }
        for (OperateSystemType value : values()) {
            if (StringUtils.equals(value.getName(), name)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }

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
