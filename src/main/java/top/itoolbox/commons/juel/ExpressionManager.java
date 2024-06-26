package top.itoolbox.commons.juel;

import de.odysseus.el.ExpressionFactoryImpl;
import lombok.AllArgsConstructor;
import lombok.Data;
import top.itoolbox.commons.collections.CollectionUtils;
import top.itoolbox.commons.lang.ObjectUtils;
import top.itoolbox.commons.lang.StringUtils;

import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 表达式管理类
 * @Author: wuchu
 * @CreateTime: 2022-07-13 17:22
 */
@Data
@AllArgsConstructor
public class ExpressionManager {

    /**
     * 表达式工厂
     */
    private ExpressionFactory factory;

    /**
     * 表达式上下文
     */
    private SimpleContext context;

    /**
     * 添加的bean名称及类的映射关系
     */
    private Map<String, Object> beanMappingClass = new ConcurrentHashMap<>();

    /**
     * 指定函数
     */
    private List<Functions> functions = new ArrayList<>();

    public ExpressionManager() {
        this.factory = new ExpressionFactoryImpl();
        this.context = new SimpleContext();
    }

    public ExpressionManager(Map<String, Object> beanMappingClass) {
        this();
        this.beanMappingClass = beanMappingClass;
        initContext();
    }

    public ExpressionManager(List<Functions> functions) {
        this();
        this.functions = functions;
        initContext();
    }

    public ExpressionManager(Map<String, Object> beanMappingClass, List<Functions> functions) {
        this();
        this.beanMappingClass = beanMappingClass;
        this.functions = functions;
        initContext();
    }

    public static ExpressionManager of(String functionPrefix, Object functionInstance) {
        Map<String, Object> beanMappingClass = new HashMap<>();
        beanMappingClass.put(functionPrefix, functionInstance);
        return new ExpressionManager(beanMappingClass);
    }

    public static ExpressionManager of(List<Functions> functions) {
        return new ExpressionManager(functions);
    }

    public static ExpressionManager of(Map<String, Object> beanMappingClass) {
        return new ExpressionManager(beanMappingClass);
    }

    public static ExpressionManager of(Map<String, Object> beanMappingClass, List<Functions> functions) {
        return new ExpressionManager(beanMappingClass, functions);
    }

    /**
     * express example:
     * has prefix --> "${myFunctions.notContaining('name', '2022', 1, params)}"
     * no prefix  --> "${notContaining('name', '2022', 1, params)}"
     *
     * @param expression 表达式
     * @param params     参数值
     * @param clazz      返回值类型
     * @return
     */
    public <T> Optional<T> evaluate(String expression, Map<String, Object> params, Class<T> clazz) {
        if (StringUtils.isBlank(expression)) {
            return Optional.empty();
        }
        // 初始化参数
        if (ObjectUtils.isNotEmpty(params)) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                context.setVariable(entry.getKey(), factory.createValueExpression(entry.getValue(), entry.getValue().getClass()));
            }
        } else {
            params = new HashMap<>();
        }

        // 计算表达式
        String regex = "\\$\\{[0-9a-zA-Z.]+[0-9a-zA-Z]+[(]";
        boolean matches = StringUtils.matches(expression, regex);
        Object invoke;
        if (ObjectUtils.isNotEmpty(beanMappingClass) && matches) {
            MethodExpression methodExpression = factory.createMethodExpression(context, expression, Object.class, new Class[]{});
            invoke = methodExpression.invoke(context, new Object[]{params});
        } else {
            ValueExpression e = factory.createValueExpression(context, expression, clazz);
            invoke = e.getValue(context);
        }

        // 清除变量
        remove();
        return Optional.of(clazz.cast(invoke));
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> evaluate(String expression, Map<String, Object> params) {
        return (Optional<T>) evaluate(expression, params, Object.class);
    }

    /**
     * release data to avoiding memory leaks
     */
    public void remove() {
        context.removeVariables();
    }

    // region 私有方法
    private void initContext() {
        if (ObjectUtils.isEmpty(beanMappingClass) && CollectionUtils.isEmpty(functions)) {
            return;
        }

        for (Map.Entry<String, Object> entry : beanMappingClass.entrySet()) {
            ValueExpression valueExpression = factory.createValueExpression(context, "#{" + entry.getKey() + "}", entry.getValue().getClass());
            valueExpression.setValue(context, entry.getValue());
        }
        for (Functions function : functions) {
            context.setFunction(function.getPrefix(), function.getFunctionName(), function.getMethod());
        }
    }

    public <T> void addFunction(List<Functions> functions) {
        if (CollectionUtils.isEmpty(functions)) {
            return;
        }
        if (ObjectUtils.isNull(factory)) {
            factory = new ExpressionFactoryImpl();
        }
        if (ObjectUtils.isNull(context)) {
            context = new SimpleContext();
        }
        for (Functions function : functions) {
            context.setFunction(function.getPrefix(), function.getFunctionName(), function.getMethod());
        }
    }

}
