package org.wch.commons.enums;

public enum ProtocolType {

    HTTP(1, "http"),
    HTTPS(2, "https"),
    FTP(3, "ftp");


    private Integer code;

    private String name;

    ProtocolType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
