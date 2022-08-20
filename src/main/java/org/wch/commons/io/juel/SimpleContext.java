package org.wch.commons.io.juel;

import de.odysseus.el.util.SimpleResolver;

import javax.el.*;
import java.io.*;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Description: 表达式上下文
 * @Author: wuchu
 * @CreateTime: 2022-08-17 11:28
 */
public class SimpleContext extends ELContext implements Serializable {

    private Functions functions;
    private final ThreadLocal<Variables> variables = new ThreadLocal<>();
    private ELResolver resolver;

    public SimpleContext() {
        this((ELResolver) null);
    }

    public SimpleContext(ELResolver resolver) {
        this.resolver = resolver;
    }

    public void setFunction(String prefix, String localName, Method method) {
        if (this.functions == null) {
            this.functions = new Functions();
        }

        this.functions.setFunction(prefix, localName, method);
    }

    public ValueExpression setVariable(String name, ValueExpression expression) {
        if (Objects.isNull(this.variables.get())) {
            this.variables.set(new Variables());
        }

        return this.variables.get().setVariable(name, expression);
    }

    public FunctionMapper getFunctionMapper() {
        if (this.functions == null) {
            this.functions = new Functions();
        }

        return this.functions;
    }

    public VariableMapper getVariableMapper() {
        if (Objects.isNull(this.variables.get())) {
            return new Variables();
        }
        return this.variables.get();
    }

    public ELResolver getELResolver() {
        if (this.resolver == null) {
            this.resolver = new SimpleResolver();
        }

        return this.resolver;
    }

    /**
     * 清除对象
     */
    public void removeVariables(){
        this.variables.remove();
    }

    // todo 待继续优化改进
    public Object deepClone() {
        //深度克隆
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(512);
             ObjectOutputStream oos = new ObjectOutputStream(bos);
        ) {
            //return super.clone();//默认浅克隆，只克隆八大基本数据类型和String
            //序列化
            oos.writeObject(this);

            //反序列化
            bis = new ByteArrayInputStream(bos.toByteArray());
            ois = new ObjectInputStream(bis);
            SimpleContext copy = (SimpleContext) ois.readObject();
            return copy;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                bis.close();
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setELResolver(ELResolver resolver) {
        this.resolver = resolver;
    }

    static class Variables extends VariableMapper {
        Map<String, ValueExpression> map = Collections.emptyMap();

        Variables() {
        }

        public ValueExpression resolveVariable(String variable) {
            return (ValueExpression) this.map.get(variable);
        }

        public ValueExpression setVariable(String variable, ValueExpression expression) {
            if (this.map.isEmpty()) {
                this.map = new HashMap();
            }

            return (ValueExpression) this.map.put(variable, expression);
        }
    }

    static class Functions extends FunctionMapper {
        Map<String, Method> map = Collections.emptyMap();

        Functions() {
        }

        public Method resolveFunction(String prefix, String localName) {
            return (Method) this.map.get(prefix + ":" + localName);
        }

        public void setFunction(String prefix, String localName, Method method) {
            if (this.map.isEmpty()) {
                this.map = new HashMap();
            }

            this.map.put(prefix + ":" + localName, method);
        }
    }
}
