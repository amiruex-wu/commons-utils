package org.wch.commons.net;

import org.wch.commons.enums.ProtocolType;
import org.wch.commons.lang.ConvertUtils2;
import org.wch.commons.lang.ObjectUtils;
import org.wch.commons.lang.PatternUtils;
import org.wch.commons.lang.StringUtils;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpUtils {

    //匹配10.0.0.0 - 10.255.255.255的网段
    String pattern_10 = "^(\\D)*10(\\.([2][0-4]\\d|[2][5][0-5]|[01]?\\d?\\d)){3}";

    //匹配172.16.0.0 - 172.31.255.255的网段
    String pattern_172 = "172\\.([1][6-9]|[2]\\d|3[01])(\\.([2][0-4]\\d|[2][5][0-5]|[01]?\\d?\\d)){2}";

    //匹配192.168.0.0 - 192.168.255.255的网段
    String pattern_192 = "192\\.168(\\.([2][0-4]\\d|[2][5][0-5]|[01]?\\d?\\d)){2}";

    // 合起来写
    private static final String pattern = "((192\\.168|172\\.([1][6-9]|[2]\\d|3[01]))"
            + "(\\.([2][0-4]\\d|[2][5][0-5]|[01]?\\d?\\d)){2}|"
            + "^(\\D)*10(\\.([2][0-4]\\d|[2][5][0-5]|[01]?\\d?\\d)){3})";

    /**
     * 局域网ip一般为：
     * <p>
     * 10.0.0.0 - 10.255.255.255
     * 172.16.0.0 - 172.31.255.255
     * 192.168.0.0 - 192.168.255.255
     * </p>
     *
     * @param ipv4URL 4 version of ip url
     * @return
     */
    public static boolean isInternalIPAddress(String ipv4URL) {
        if (StringUtils.isBlank(ipv4URL)) {
            return false;
        }
        Pattern reg = Pattern.compile(pattern);
        Matcher match = reg.matcher(ipv4URL);
        return match.find();
    }

    public static Optional<URL> buildURL(String source, String pattern, Map<String, Object> params) {
        if (ObjectUtils.anyNull(source, pattern, params)) {
            return Optional.empty();
        }
        try {
            Optional<String> refactoring = PatternUtils.refactoring(source, pattern, true, params);
            if (refactoring.isPresent()) {
                URL url = new URL(refactoring.get());
                return Optional.of(url);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static Optional<URL> buildURL(ProtocolType protocolType, String host, Integer port, String pageUrl, Map<String, Object> params) {
        if (ObjectUtils.anyNull(protocolType, host)) {
            return Optional.empty();
        }
        try {
            String[] urlStr = new String[]{
                    protocolType.getName() + "://" + host + (ObjectUtils.isNull(port) ? "" : port) + "/" + (ObjectUtils.nonNull(pageUrl) ? pageUrl : StringUtils.EMPTY)
            };
            if (ObjectUtils.isNotEmpty(params)) {
                urlStr[0] += "?";
                params.forEach((k, v) -> {
                    Optional<String> s = ConvertUtils2.convertIfNecessary(v, String.class);
                    if (s.isPresent()) {
                        try {
                            urlStr[0] += k + "=" + URLEncoder.encode(s.get(), StandardCharsets.UTF_8.name()) + "&";
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            URL url;
            if (urlStr[0].charAt(urlStr[0].length() - 1) == '&') {
                url = new URL(urlStr[0].substring(urlStr[0].length() - 1));
            } else {
                url = new URL(urlStr[0]);
            }
//            new URL(ProtocolVersion.valueOf("TLSv1"), , )
           /* Optional<String> refactoring = PatternUtils.refactoring(host, port, true, params);
            if (refactoring.isPresent()) {
                URL url = new URL(refactoring.get());
            }*/
            return Optional.of(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * 获取导出文件名称,使用当前系统时间作为前缀
     *
     * @param request  request请求
     * @param fileName 指定文件名称
     * @return
     *//*
    public static String getExportFileName(HttpServletRequest request, String fileName) {
        String agent = request.getHeader("User-Agent");
        return getExportFileName(agent, String.valueOf(System.currentTimeMillis()), fileName);
    }

    *//**
     * 获取导出文件名称
     *
     * @param request  request请求
     * @param prefix   名称前缀
     * @param fileName 指定文件名称
     * @return
     *//*
    public static String getExportFileName(HttpServletRequest request, String prefix, String fileName) {
        String agent = request.getHeader("User-Agent");
        return getExportFileName(agent, prefix, fileName);
    }*/

    /** TODO 新增的
     * 获取导出文件名称
     *
     * @param agent    浏览器类型（User-Agent字段）
     * @param prefix   名称前缀
     * @param fileName 指定文件名称
     * @return
     */
    public static String getExportFileName(String agent, String prefix, String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return fileName;
        }
        // 根据不同浏览器进行不同的编码
        try {
            String filenameEncoder = "";
            // 解决获得中文参数的乱码
            if (agent.contains("MSIE")) {
                // IE浏览器IE 11 以下版本
                filenameEncoder = URLEncoder.encode(fileName, "utf-8");
                filenameEncoder = filenameEncoder.replace("+", " ");
            } else if (agent.contains("Firefox")) {
                // 火狐浏览器
                BASE64Encoder base64Encoder = new BASE64Encoder();
                filenameEncoder = "=?utf-8?B?" + base64Encoder.encode(fileName.getBytes(StandardCharsets.UTF_8)) + "?=";
            } else {
                // 其它浏览器
                filenameEncoder = URLEncoder.encode(fileName, "utf-8");
            }
            return prefix + filenameEncoder;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return fileName;
        }
    }
}
