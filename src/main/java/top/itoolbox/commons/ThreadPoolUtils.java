package top.itoolbox.commons;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 线程池工具类
 * @Author: wuchu
 * @CreateTime: 2022-07-20 18:13
 */
public class ThreadPoolUtils {

    public ThreadPoolUtils() {
    }

    public static ExecutorService createService() {
        ExecutorService executor = new ThreadPoolExecutor(10, 10, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));
        return executor;
    }

    public static ExecutorService createService(int corePoolSize, int maxMumPoolSize) {
        ExecutorService executor = new ThreadPoolExecutor(corePoolSize,
                maxMumPoolSize,
                60L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10));
        return executor;
    }

    private static ExecutorService createCachePool() {
        ExecutorService executor = new ThreadPoolExecutor(10, 10,
                60L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10));
        return executor;
    }
}
