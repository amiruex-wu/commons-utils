package top.itoolbox.commons.juel;

import java.util.Map;
import java.util.Objects;

/**
 * @Description: TODO
 * @Author: wuchu
 * @CreateTime: 2022-08-12 14:16
 */
public class CustomFunctions {

    public boolean notContaining(String field, String fieldValue, Integer fieldType, Map<String, Object> formValues) {
        Object obj = formValues.get(field);

//        System.out.println("执行了该方法:" + field + "-->" + obj);
        return Objects.isNull(obj) || !obj.toString().contains(fieldValue);
    }


    public Integer max(Integer a, Integer b) {
        System.out.println(Thread.currentThread().getId()+"执行了该方法：a-->"+ a+ ",b-->" + b);
        if (Objects.isNull(a) && Objects.isNull(b)) {
            return null;
        } else if (Objects.isNull(a)) {
            return b;
        } else if (Objects.isNull(b)) {
            return a;
        }
        return Math.max(a, b);
    }

    public Integer max1(Integer a, Integer b) {
//        System.out.println("执行了该方法：a-->"+ a+ ",b-->" + b);
        if (Objects.isNull(a) && Objects.isNull(b)) {
            return null;
        } else if (Objects.isNull(a)) {
            return b;
        } else if (Objects.isNull(b)) {
            return a;
        }
        return Math.max(a, b);
    }

    public Integer max2(Integer a, Integer b) {
//        System.out.println("执行了该方法：a-->"+ a+ ",b-->" + b);
        if (Objects.isNull(a) && Objects.isNull(b)) {
            return null;
        } else if (Objects.isNull(a)) {
            return b;
        } else if (Objects.isNull(b)) {
            return a;
        }
        return Math.max(a, b);
    }

    public Integer max3(Integer a, Integer b) {
//        System.out.println("执行了该方法：a-->"+ a+ ",b-->" + b);
        if (Objects.isNull(a) && Objects.isNull(b)) {
            return null;
        } else if (Objects.isNull(a)) {
            return b;
        } else if (Objects.isNull(b)) {
            return a;
        }
        return Math.max(a, b);
    }

    public Integer max4(Integer a, Integer b) {
//        System.out.println("执行了该方法：a-->"+ a+ ",b-->" + b);
        if (Objects.isNull(a) && Objects.isNull(b)) {
            return null;
        } else if (Objects.isNull(a)) {
            return b;
        } else if (Objects.isNull(b)) {
            return a;
        }
        return Math.max(a, b);
    }

    public Integer max5(Integer a, Integer b) {
//        System.out.println("执行了该方法：a-->"+ a+ ",b-->" + b);
        if (Objects.isNull(a) && Objects.isNull(b)) {
            return null;
        } else if (Objects.isNull(a)) {
            return b;
        } else if (Objects.isNull(b)) {
            return a;
        }
        return Math.max(a, b);
    }

}
