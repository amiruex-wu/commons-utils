package org.wch.commons.io.juel;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Objects;

public class MyFunctions {

    @Getter
    @Setter
    private int minValue;

    public static boolean max(int a, int b){
//        return Math.max(a, b);
        return a> b;
    }

    /**
     * 使用静态方法
     * @param field
     * @param fieldValue
     * @param fieldType
     * @param formValues
     * @return
     */
    public boolean notContaining(String field, String fieldValue, Integer fieldType, Map<String, Object> formValues) {
        Object obj = formValues.get(field);

        System.out.println("执行了该方法:"+ field + "-->" + obj);
        return Objects.isNull(obj) || !obj.toString().contains(fieldValue);
    }

    private void test(){
        System.out.println("this is test function");
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
