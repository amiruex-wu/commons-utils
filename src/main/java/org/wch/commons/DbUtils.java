package org.wch.commons;

import lombok.*;
import lombok.experimental.Accessors;
import org.wch.commons.beans.DBParams;
import org.wch.commons.enums.DatabaseType;
import org.wch.commons.lang.ObjectUtils;
import org.wch.commons.lang.StringUtils;

import java.util.Objects;
import java.util.Optional;

/**
 * @Description: TODO
 * @Author: wuchu
 * @CreateTime: 2022-07-21 16:44
 */
public class DbUtils {

    public static Optional<String> buildUrl(DBParams dbParams) {
        if (null == dbParams || dbParams.isNecessaryPropertyNull()) {
            return Optional.empty();
        }

        StringBuffer buffer = new StringBuffer();
        switch (dbParams.getDatabaseType()) {
            case MYSQL_V_5:
            case MYSQL_V_8:
                buffer.append("jdbc:mysql://").append(dbParams.getHost()).append(":")
                        .append(dbParams.getPort())
                        .append("/")
                        .append(dbParams.getDatabaseName());
//                url =  +  +  +  +  + ; + "?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&useSSL=false&serverTimezone=Asia/Shanghai";
                break;
            case ORACLE:
                // 参数详解
                // jdbc:oracle:driver_type:@[ip]:[port]:[sid]
                //  drive_type有thin和oci
                // 示例：jdbc:oracle:thin:@localhost:1521:XE
                String driverType;
                if (StringUtils.isEmpty(dbParams.getOracleDriverType())) {
                    driverType = "thin";
                } else {
                    driverType = dbParams.getOracleDriverType();
                }
                buffer.append("jdbc:oracle:").append(driverType).append(":@").append(dbParams.getHost())
                        .append(":")
                        .append(dbParams.getPort())
                        .append(":")
                        .append(dbParams.getDatabaseName());
                break;
            default:
                break;
        }
        if (buffer.length() > 0 && dbParams.isAnyPropertyNonNull()) {
            buffer.append("?")
                    .append(dbParams.getUrlParams());
        }
        return Optional.empty();
    }

    /**
     * 将数据库对应的数据类型转换成java数据类型
     *
     * @param dataType
     * @return
     */
    public static String convertDbTypeToJavaType(String dataType) {
        if (Objects.isNull(dataType)) {
            return null;
        }
        String result = null;
        switch (dataType) {
            case "int":
            case "integer":
                result = "Integer";
                break;
            case "float":
                result = "Float";
                break;
            case "tinyInt":
            case "tinyint":
                result = "Boolean";
                break;
            case "double":
                result = "Double";
                break;
            case "bigint":
            case "long":
                result = "Long";
                break;
            case "varchar":
            case "char":
            case "text":
            case "longtext":
                result = "String";
                break;
            case "date":
                result = "LocalDate";
                break;
            case "datetime":
            case "timestamp":
                result = "LocalDateTime";
                break;
            case "time":
                result = "LocalTime";
                break;
            default:
                result = StringUtils.capitalize(dataType).orElse(null);
                break;
        }
        return result;
    }

}
