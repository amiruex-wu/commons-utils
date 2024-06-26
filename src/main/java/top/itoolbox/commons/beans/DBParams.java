package top.itoolbox.commons.beans;

import lombok.*;
import lombok.experimental.Accessors;
import top.itoolbox.commons.enums.DatabaseType;
import top.itoolbox.commons.lang.ObjectUtils;
import top.itoolbox.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Optional;

@Setter
@Getter
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class DBParams implements Serializable {
    private String host;
    private Integer port;
    private String databaseName;
    private DatabaseType databaseType;
    private String oracleDriverType;
    private Boolean useUnicode;
    private String characterEncoding;
    private Boolean allowMultiQueries;
    private Boolean useSSL;
    private Boolean autoReconnect;
    private Boolean autoReconnectForPools;
    private Boolean failOverReadOnly;
    private Integer maxReconnects;
    // 单位：秒
    private Long initialTimeout;
    // 单位：毫秒,0表示永不超时
    private Long connectTimeout;
    // 单位：毫秒,0表示永不超时
    private Long socketTimeout;
    private String serverTimezone = "Asia/Shanghai";

    public boolean isNecessaryPropertyNull() {
        return ObjectUtils.allNull(host, port, databaseType) && StringUtils.isBlank(host) && StringUtils.isBlank(databaseName);
    }

    public boolean isAnyPropertyNonNull() {
        return !ObjectUtils.allNull(useUnicode, characterEncoding, allowMultiQueries, useSSL, autoReconnect, autoReconnectForPools,
                failOverReadOnly, maxReconnects, initialTimeout, connectTimeout, socketTimeout, serverTimezone);
    }

    public Optional<String> getUrlParams() {
        String result = (ObjectUtils.isNull(useUnicode) ? "" : ("useUnicode=" + useUnicode + "&")) +
                (StringUtils.isBlank(characterEncoding) ? "" : ("characterEncoding=" + characterEncoding + "&")) +
                (ObjectUtils.isNull(allowMultiQueries) ? "" : ("allowMultiQueries=" + allowMultiQueries + "&")) +
                (ObjectUtils.isNull(useSSL) ? "" : ("useSSL=" + useSSL + "&")) +
                (ObjectUtils.isNull(autoReconnect) ? "" : ("autoReconnect=" + autoReconnect + "&")) +
                (ObjectUtils.isNull(autoReconnectForPools) ? "" : ("autoReconnectForPools=" + autoReconnectForPools + "&")) +
                (ObjectUtils.isNull(failOverReadOnly) ? "" : ("failOverReadOnly=" + failOverReadOnly + "&")) +
                (ObjectUtils.isNull(maxReconnects) ? "" : ("maxReconnects=" + maxReconnects + "&")) +
                (ObjectUtils.isNull(initialTimeout) ? "" : ("initialTimeout=" + initialTimeout + "&")) +
                (ObjectUtils.isNull(connectTimeout) ? "" : ("connectTimeout=" + connectTimeout + "&")) +
                (ObjectUtils.isNull(socketTimeout) ? "" : ("socketTimeout=" + socketTimeout + "&")) +
                (ObjectUtils.isNull(serverTimezone) ? "" : ("serverTimezone=" + serverTimezone + "&"));
        return StringUtils.isEndWithThenDelete(result, "&");
    }

}
