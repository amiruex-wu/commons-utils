package org.wch.commons.net;

import org.apache.hc.core5.net.URIBuilder;
import org.wch.commons.enums.ProtocolType;
import org.wch.commons.lang.MapUtils;
import org.wch.commons.lang.ObjectUtils;
import org.wch.commons.lang.StringUtils;


import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @Description: 链接构建工具
 * @Author: wuchu
 * @CreateTime: 2023-04-27 11:29
 */
public class URLUtils {

    public static Optional<String> buildURL(String url, Map<String, Object> queryParams) {
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            if (MapUtils.isNotEmpty(queryParams)) {
                for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
                    uriBuilder.addParameter(entry.getKey(), String.valueOf(entry.getValue()));
                }
            }
            return Optional.ofNullable(uriBuilder.build().toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static Optional<String> buildURL(String url, Map<String, Object> pathParams, Map<String, Object> queryParams) {
        return buildURL(url, pathParams, queryParams, StringUtils.ParamPlaceholderRegex.GENERAL.getRegex());
    }

    public static Optional<String> buildURL(String url, Map<String, Object> pathParams, Map<String, Object> queryParams, String regex) {
        try {
            Optional<Map<String, Object>> map = MapUtils.putAll(pathParams, queryParams);
            Optional<String> replace = StringUtils.replace(url, map.orElse(new HashMap<>()), false, regex);
            if (!replace.isPresent()) {
                return Optional.empty();
            }
            URIBuilder uriBuilder = new URIBuilder(replace.get());
            if (MapUtils.isNotEmpty(queryParams)) {
                for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
                    uriBuilder.addParameter(entry.getKey(), String.valueOf(entry.getValue()));
                }
            }
            return Optional.ofNullable(uriBuilder.build().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static Optional<String> buildURL(ProtocolType protocolType, String domainNameOrIp, Integer port, String pathUrl, Map<String, Object> queryParams) {
        if (ObjectUtils.anyNull(protocolType, domainNameOrIp)) {
            return Optional.empty();
        }
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append(protocolType.getName()).append("://")
                .append(domainNameOrIp).append(null == port ? "" : port)
                .append(StringUtils.isBlank(pathUrl) ? "" : pathUrl);
        try {
            URIBuilder uriBuilder = new URIBuilder(stringBuffer.toString());
            if (MapUtils.isNotEmpty(queryParams)) {
                for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
                    uriBuilder.addParameter(entry.getKey(), String.valueOf(entry.getValue()));
                }
            }
            return Optional.ofNullable(uriBuilder.build().toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
