package org.wch.commons;

import org.wch.commons.lang.StringUtils;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @Description: TODO
 * @Author: wuchu
 * @CreateTime: 2022-07-20 16:59
 */
public class HttpUtils {
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
