package org.wch.commons.io.juel;



import java.util.Map;
import java.util.Optional;

/**
 * @Description: TODO
 * @Author: wuchu
 * @CreateTime: 2022-08-12 15:23
 */
public class CustomThread implements Runnable {
    private ExpressionManager expressionManager;

    private Map<String, Object> mapBeans;

    private String expressStr;

    private Map<String, Object> params;

    public CustomThread(ExpressionManager expressionManager, String expressStr, Map<String, Object> params) {
        this.expressionManager = expressionManager;
        this.expressStr = expressStr;
        this.params = params;
    }


//    public CustomThread(Map<String, Object> mapBeans, String expressStr, Map<String, Object> params) {
//        this.mapBeans = mapBeans;
//        this.expressStr = expressStr;
//        this.params = params;
//    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getId() + "开始进行比较.....");

        long current = System.currentTimeMillis();
        Optional<Object> evaluate;

            evaluate = expressionManager.evaluate(expressStr, params);

        System.out.println(Thread.currentThread().getId() + "-result is " + evaluate.orElse(null) + ", 线程耗时：" + (System.currentTimeMillis() - current) + "ms");
    }
}
