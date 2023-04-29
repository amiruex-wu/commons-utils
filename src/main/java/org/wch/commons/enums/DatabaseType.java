package org.wch.commons.enums;

import java.util.Arrays;
import java.util.Objects;

public enum DatabaseType {
    /**
     * MySQL数据库5.0+
     */
    MYSQL_V_5(1, "com.mysql.jdbc.Driver"),

    /**
     * MySQL数据库8.0+
     */
    MYSQL_V_8(2, "com.mysql.cj.jdbc.Driver"),

    /**
     * Oracle数据库
     */
    ORACLE(3, "oracle.jdbc.driver.OracleDriver");

    private final Integer id;
    private final String driverClassName;

    DatabaseType(Integer id, String driverClassName) {
        this.id = id;
        this.driverClassName = driverClassName;
    }

    public static DatabaseType getEnum(Integer id) {
        return Arrays.stream(values())
                .filter(item -> Objects.equals(item.getId(), id))
                .findFirst()
                .orElse(null);
    }

    public String getDriverClassName() {
        return this.driverClassName;
    }

    public Integer getId() {
        return id;
    }
}
