package org.wch.commons.beans;

import lombok.*;
import lombok.experimental.Accessors;
import org.wch.commons.enums.DatabaseType;
import org.wch.commons.lang.ObjectUtils;
import org.wch.commons.lang.StringUtils;

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
        StringBuffer stringBuffer = new StringBuffer()
                .append(ObjectUtils.isNull(useUnicode) ? "" : ("useUnicode=" + useUnicode + "&"))
                .append(StringUtils.isBlank(characterEncoding) ? "" : ("characterEncoding=" + characterEncoding + "&"))
                .append(ObjectUtils.isNull(allowMultiQueries) ? "" : ("allowMultiQueries=" + allowMultiQueries + "&"))
                .append(ObjectUtils.isNull(useSSL) ? "" : ("useSSL=" + useSSL + "&"))
                .append(ObjectUtils.isNull(autoReconnect) ? "" : ("autoReconnect=" + autoReconnect + "&"))
                .append(ObjectUtils.isNull(autoReconnectForPools) ? "" : ("autoReconnectForPools=" + autoReconnectForPools + "&"))
                .append(ObjectUtils.isNull(failOverReadOnly) ? "" : ("failOverReadOnly=" + failOverReadOnly + "&"))
                .append(ObjectUtils.isNull(maxReconnects) ? "" : ("maxReconnects=" + maxReconnects + "&"))
                .append(ObjectUtils.isNull(initialTimeout) ? "" : ("initialTimeout=" + initialTimeout + "&"))
                .append(ObjectUtils.isNull(connectTimeout) ? "" : ("connectTimeout=" + connectTimeout + "&"))
                .append(ObjectUtils.isNull(socketTimeout) ? "" : ("socketTimeout=" + socketTimeout + "&"))
                .append(ObjectUtils.isNull(serverTimezone) ? "" : ("serverTimezone=" + serverTimezone + "&"));
        String result = stringBuffer.toString();
        return StringUtils.isEndWithThenDelete(result, "&");
    }

}
