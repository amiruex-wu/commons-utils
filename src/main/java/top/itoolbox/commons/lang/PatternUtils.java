package top.itoolbox.commons.lang;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: 正则表达式工具类
 * @Author: wuchu
 * @CreateTime: 2022-07-13 14:37
 */
public class PatternUtils {

    /**
     * <p>重构字符串，替换指定正则表达式（"\\$\\{(.+?)\\}"）匹配的参数</p>
     * The following types are supported:
     * <ul>
     * <li>{@link String}: Considered blank if its null.</li>
     * </ul>
     * <pre>
     * Map<String, Object> params = new HashMap<>();
     * params.put("name","Jack Chen");
     * PatternUtils.refactoring("his name is ${name}", parmas) = "his name is Jack Chen"
     * </pre>
     *
     * @param source 源数据
     * @param params 参数键值对
     * @return sdf
     */
    public static Optional<String> refactoring(String source, Map<String, Object> params) {
        if (ObjectUtils.anyNull(source, params)) {
            return Optional.empty();
        }
        String regex = "\\$\\{(.+?)\\}";
        return getRebuildString(source, params, regex, true);
    }

    /**
     * 重构字符串，替换指定正则表达式（"\\$\\{(.+?)\\}"）匹配的参数，如果是URL则对部分参数做转义
     *
     * @param source 源数据
     * @param isUrl  是否是URL地址
     * @param params 参数键值对
     * @return
     */
    public static Optional<String> refactoring(String source, boolean isUrl, Map<String, Object> params) {
        if (ObjectUtils.anyNull(source, params)) {
            return Optional.empty();
        }
        String regex = "\\$\\{(.+?)\\}";
        return getRebuildString(source, params, regex, isUrl);
    }

    /**
     * <p>重构字符串，替换指定正则表达式匹配的参数，如果是URL则对部分参数做转义</p>
     *
     * @param source  源数据
     * @param pattern 正则表达式匹配规则
     * @param isUrl   是否是URL地址
     * @param params  参数键值对
     * @return
     */
    public static Optional<String> refactoring(String source, String pattern, boolean isUrl, Map<String, Object> params) {
        if (ObjectUtils.anyNull(source, pattern, params)) {
            return Optional.empty();
        }
        return getRebuildString(source, params, pattern, isUrl);
    }

    // region 私有方法区
    private static Optional<String> getRebuildString(String data, Map<String, Object> paramData, String regex, boolean isUrl) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(data);
        StringBuffer msg = new StringBuffer();
        while (matcher.find()) {
            String key = matcher.group(1);// 键名
            Object value = paramData.get(key);// 键值
            // 下一步是替换并且把替换好的值放到sb中
            Optional<String> optional = ConvertUtils.convertIfNecessary(value, String.class);
            optional.ifPresent(s -> matcher.appendReplacement(msg, isUrl ? getParamRefactoring(s) : s));
        }
        // 把符合的数据追加到sb尾
        matcher.appendTail(msg);
        return Optional.of(msg.toString());
    }

    /**
     * 用于URL参数编码重构
     * @param param
     * @return
     */
    private static String getParamRefactoring(String param) {
        if (StringUtils.isBlank(param)) {
            return StringUtils.EMPTY;
        }
        try {
            return URLEncoder.encode(param, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return StringUtils.EMPTY;
    }
}