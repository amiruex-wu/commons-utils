package top.itoolbox.commons.juel;


import top.itoolbox.commons.lang.ObjectUtils;
import top.itoolbox.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Description: 表达式工具类
 * @Author: wuchu
 * @CreateTime: 2022-08-15 10:55
 */
public class ExpressionUtils {

    private static ExpressionManager expressionManager = new ExpressionManager();

    public ExpressionUtils() {
    }

    public static <T> Optional<T> evaluate(String expression, Map<String, Object> params) {
        if (ObjectUtils.isEmpty(params) || StringUtils.isBlank(expression)) {
            return Optional.empty();
        }
        return expressionManager.evaluate(expression, params);
    }

    public static <T> Optional<T> evaluate(List<Functions> functions, String expression, Map<String, Object> params) {
        if (ObjectUtils.anyNull(functions, params, expression) || StringUtils.isBlank(expression)) {
            return Optional.empty();
        }
        return ExpressionManager.of(functions).evaluate(expression, params);
    }

    public static <T> Optional<T> evaluate(Map<String, Object> beanMappingClass, String expression, Map<String, Object> params) {
        if (ObjectUtils.anyNull(beanMappingClass, params, expression) || StringUtils.isBlank(expression)) {
            return Optional.empty();
        }
        return ExpressionManager.of(beanMappingClass).evaluate(expression, params);
    }

    public static <T> Optional<T> evaluate(Map<String, Object> beanMappingClass, List<Functions> functions, String expression, Map<String, Object> params) {
        if (ObjectUtils.anyNull(beanMappingClass, functions, params, expression) || StringUtils.isBlank(expression)) {
            return Optional.empty();
        }
        return ExpressionManager.of(beanMappingClass, functions).evaluate(expression, params);
    }

    public static void addFunction(List<Functions> functions) {
        if (ObjectUtils.isNull(expressionManager) || ObjectUtils.isEmpty(functions)) {
            return;
        }
        expressionManager.addFunction(functions);
    }

}
