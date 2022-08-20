//package org.wch.commons.io.juel;
//
//import de.odysseus.el.ExpressionFactoryImpl;
//import de.odysseus.el.TreeMethodExpression;
//import de.odysseus.el.util.SimpleContext;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.JUnit4;
//import org.wch.juel.ExpressionManager2;
//import org.wch.juel.ExpressionManager;
//
//import javax.el.ExpressionFactory;
//import javax.el.MethodExpression;
//import javax.el.ValueExpression;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//
///**
// * @Description: TODO
// * @Author: wuchu
// * @CreateTime: 2022-08-12 14:12
// */
//@RunWith(JUnit4.class)
//public class ExpressTest {
//
//    @Test
//    public void extracted3() {
//        Map<String, Object> map = new HashMap<>();
//        map.put("name", "Tim Smiss");
//        Map<String, Object> map1 = new HashMap<>();
//        map1.put("execution", map);
//
//        Map<String, Object> mapBeans = new HashMap<>();
//        mapBeans.put("myFunctions", new MyFunctions());
//        ExpressionManager2 myFunctions = new ExpressionManager2(mapBeans, map1);
//        String expression = "${myFunctions.notContaining('name', '2022', 1, execution)}";
//        /*boolean evaluate = myFunctions.evaluate(expression);
//        System.out.println("结果：" + evaluate);
//        // finally release data to avoiding memory leaks
//        myFunctions.remove();*/
//    }
//
//    @Test
//    public void extracted4() {
//        Map<String, Object> map = new HashMap<>();
//        map.put("name", "Tim Smiss");
//        Map<String, Object> params = new HashMap<>();
//        params.put("execution", map);
//
//        Map<String, Object> mapBeans = new HashMap<>();
//        mapBeans.put("myFunctions", new CustomFunctions());
//
//        ExpressionManager2 myFunctions = new ExpressionManager2(mapBeans);
//        String expression = "${myFunctions.notContaining('name', '2022', 1, execution)}";
//
//        Optional<Object> evaluate = myFunctions.evaluate(expression, params);
//        System.out.println("结果：" + evaluate.orElse(null));
//        // finally release data to avoiding memory leaks
//        myFunctions.remove();
//    }
//
//    @Test
//    public void extracted5() throws InterruptedException {
//        System.out.println("执行开始....");
//        Map<String, Object> mapBeans = new HashMap<>();
//        mapBeans.put("myFunctions", new CustomFunctions());
//        ExpressionManager2 expressionManager2 = new ExpressionManager2(mapBeans);
//
//        String expression = "${myFunctions.max(a, b)}";
//
//        Map<String, Object> map1 = new HashMap<>();
//        map1.put("a", 50);
//        map1.put("b", 20);
//
//        CustomThread myThread = new CustomThread(expressionManager2, expression, map1);
//
//        Map<String, Object> map2 = new HashMap<>();
//        map2.put("a", 50);
//        map2.put("b", 200);
//        CustomThread myThread2 = new CustomThread(expressionManager2, expression, map2);
//
//        Map<String, Object> map3 = new HashMap<>();
//        map3.put("a", 50);
//        map3.put("b", 40);
//
//        CustomThread myThread3 = new CustomThread(expressionManager2, expression, map3);
//
//        new Thread(myThread).start();
//        new Thread(myThread2).start();
//        new Thread(myThread3).start();
//        Thread.sleep(2000);
//    }
//
//    /**
//     * 特定函数并发测试
//     * @throws InterruptedException
//     */
//    @Test
//    public void extracted6() throws InterruptedException {
//        System.out.println("执行开始....");
//        Map<String, Object> mapBeans = new HashMap<>();
//        mapBeans.put("myFunctions", new CustomFunctions());
//        ExpressionManager expressionManager = new ExpressionManager(mapBeans);
//
//        String expression = "${myFunctions.max(a, b)}";
//
//        Map<String, Object> map1 = new HashMap<>();
//        map1.put("a", 50);
//        map1.put("b", 20);
//
//        CustomThread myThread = new CustomThread(expressionManager, expression, map1);
//
//        Map<String, Object> map2 = new HashMap<>();
//        map2.put("a", 50);
//        map2.put("b", 200);
//        CustomThread myThread2 = new CustomThread(expressionManager, expression, map2);
//
//        Map<String, Object> map3 = new HashMap<>();
//        map3.put("a", 80);
//        map3.put("b", 40);
//        map3.put("c", 100);
//        map3.put("d", 30);
//
//        String expression1 = "${(a+b + c / d)}";
//        CustomThread myThread3 = new CustomThread(expressionManager, expression1, map3);
//
//        new Thread(myThread).start();
//        new Thread(myThread2).start();
//        new Thread(myThread3).start();
//        Thread.sleep(2000);
//    }
//
//    /**
//     * 特定的函数表达式
//     */
//    @Test
//    public void extracted7() {
//        System.out.println("执行开始....");
//        Map<String, Object> mapBeans = new HashMap<>();
//        mapBeans.put("myFunctions", new CustomFunctions());
//        long current = System.currentTimeMillis();
//        ExpressionManager expressionManager = new ExpressionManager(mapBeans);
//
//        String expression = "${myFunctions.max(a, b)}";
//
//        Map<String, Object> map1 = new HashMap<>();
//        map1.put("a", 50);
//        map1.put("b", 20);
//
//        Optional<Object> evaluate = expressionManager.evaluate(expression, map1);
//        System.out.println("result is " + evaluate.orElse(null) + ",耗时：" + (System.currentTimeMillis() - current) + "ms");
//    }
//
//    /**
//     * 普通表达式
//     */
//    @Test
//    public void extracted8() {
//        System.out.println("执行开始....");
//
//        ExpressionManager expressionManager = new ExpressionManager();
//
//        String expression = "${a + b + (c / d)}";
//
//        Map<String, Object> map1 = new HashMap<>();
//        map1.put("a", 5.5);
//        map1.put("b", 4.51);
//        map1.put("c", 100);
//        map1.put("d", 3);
//        long current = System.currentTimeMillis();
//
//        Optional<Object> evaluate = expressionManager.evaluate(expression, map1);
//        System.out.println("result is " + evaluate.orElse(null) + ",耗时：" + (System.currentTimeMillis() - current) + "ms");
//    }
//
//
//    @Test
//    public void extracted2() {
//        // 1. 创建初始化对象，需要考虑多线程
//        ExpressionFactoryImpl factory = new ExpressionFactoryImpl();
//        SimpleContext context = new SimpleContext(); // more on this here...
//        // 设置函数表达式前缀
//        factory.createValueExpression(context, "#{customFunctions}", CustomFunctions.class)
//                .setValue(context, new CustomFunctions());
//
//        // 2. 设置变量参数
//        Map<String, Object> map = new HashMap<>();
//        map.put("name", "Tim Smiss");
//        Map<String, Object> params = new HashMap<>();
//        params.put("execution", map);
//        params.forEach((k, v) -> {
//            context.setVariable(k, factory.createValueExpression(v, v.getClass()));
//        });
//
//        // 3. 初始化表达式并执行输出结果
//        TreeMethodExpression e = factory.createMethodExpression(context, "${customFunctions.notContaining('name', '2022', 1, execution)}", Object.class, new Class[]{});
//        Object invoke = e.invoke(context, new Object[]{map});
//
//        System.out.println("invoke result is " + invoke);
//    }
//
//    @Test
//    public void test6() {
//        long current = System.currentTimeMillis();
//        Map<String, Object> mapBeans = new HashMap<>();
//        mapBeans.put("customFunctions", new CustomFunctions());
//        Map<String, Object> map = new HashMap<>();
//        map.put("name", "Tim Smiss");
//        Map<String, Object> params = new HashMap<>();
//        params.put("execution", map);
//        params.put("a", 12);
//        params.put("b", 35);
//
//        Optional<Object> evaluate = ExpressionManager2.of(mapBeans).evaluate("${customFunctions.notContaining('name', '2022', 1, execution)}", params);
//        Optional<Object> evaluate1 = ExpressionManager2.of(mapBeans).evaluate("${customFunctions.max(a, b)}", params);
//        System.out.println("result is " + evaluate.orElse(null));
//        System.out.println("result1 is " + evaluate1.orElse(null));
//        System.out.println("总耗时：" + (System.currentTimeMillis() - current) + "ms");
//    }
//
//    @Test
//    public void test7() {
//        long current = System.currentTimeMillis();
//        Map<String, Object> mapBeans = new HashMap<>();
//        mapBeans.put("customFunctions", new CustomFunctions());
//        Map<String, Object> map = new HashMap<>();
//        map.put("name", "Tim Smiss");
//        Map<String, Object> params = new HashMap<>();
//        params.put("execution", map);
//        params.put("a", 12);
//        params.put("b", 35);
//        Map<String, Object> params1 = new HashMap<>();
//        params1.put("a", 65);
//        params1.put("b", 35);
//
//        ExpressionManager2 expressionManager2 = ExpressionManager2.of(mapBeans);
//
//        Optional<Object> evaluate = expressionManager2.evaluate("${customFunctions.notContaining('name', '2022', 1, execution)}", params);
//        Optional<Object> evaluate1 = expressionManager2.evaluate("${customFunctions.max(a, b)}", params);
//        Optional<Object> evaluate2 = expressionManager2.evaluate("${customFunctions.max(a, b)}", params1);
//        Optional<Object> evaluate3 = expressionManager2.evaluate("${customFunctions.max(a, b)}", params);
//
//        System.out.println("result is " + evaluate.orElse(null));
//        System.out.println("result1 is " + evaluate1.orElse(null));
//        System.out.println("result2 is " + evaluate2.orElse(null));
//        System.out.println("result3 is " + evaluate3.orElse(null));
//        System.out.println("总耗时：" + (System.currentTimeMillis() - current) + "ms");
//    }
//
//    @Test
//    public void teste() throws NoSuchMethodException {
//        long current = System.currentTimeMillis();
//        // the ExpressionFactory implementation is de.odysseus.el.ExpressionFactoryImpl
//        ExpressionFactory factory = new ExpressionFactoryImpl();
//
//        // package de.odysseus.el.util provides a ready-to-use subclass of ELContext
//        SimpleContext context = new SimpleContext();
//
//        // map variable foo to 0
//        context.setVariable("a", factory.createValueExpression(15, int.class));
//        context.setVariable("b", factory.createValueExpression(10.29, double.class));
//
//        // parse our expression
//        String expressStr = "${a + b}";
//        ValueExpression e = factory.createValueExpression(context, expressStr, double.class);
//        ValueExpression e1 = factory.createValueExpression(context, expressStr, Object.class);
//
//      /*  MethodExpression methodExpression = factory.createMethodExpression(context, expressStr, Object.class, new Class[]{});
//        Object invoke = methodExpression.invoke(context, new Object[]{new HashMap<>()});
//*/
//        // set value for top-level property "bar" to 1
////        factory.createValueExpression(context, "${bar}", int.class).setValue(context, 1);
//
//        // get value for our expression
//        System.out.println(e.getValue(context)); // --> 1
//        System.out.println(e1.getValue(context)); // --> 1
////        System.out.println(invoke); // --> 1
//        System.out.println(", 耗时：" + (System.currentTimeMillis() - current) + "ms");
//    }
//
//}
