package top.itoolbox.commons;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Optional;

/**
 * @Description: TODO
 * @Author: wuchu
 * @Version: 1.0
 * @CreateTime: 2022/8/21 16:02
 */
@RunWith(JUnit4.class)
public class StopWatch1Test {

    @Test
    public void test() throws InterruptedException {
        StopWatch stopWatch = StopWatch.create();
        System.out.println("this is result temp ");
        Thread.sleep(2000);
        final Optional<Long> lastTaskTimeMillis = stopWatch.getLastTaskTimeMillis();
        final Optional<Long> lastTaskTimeNanos = stopWatch.getLastTaskTimeNanos();
        System.out.println("cost time is " + lastTaskTimeMillis.orElse(null) + "ms,"+ lastTaskTimeNanos.orElse(null)+ "ns");
    }

    @Test
    public void test1() throws InterruptedException {
        StopWatch stopWatch = StopWatch.create();
        System.out.println("this is result temp ");
        Thread.sleep(2000);
        stopWatch.start("ids2");
        Thread.sleep(1000);
        stopWatch.stop();
        final String s = stopWatch.prettyPrint();
        System.out.println("result:\n" + s);
        final Optional<Long> lastTaskTimeMillis = stopWatch.getLastTaskTimeMillis();
        final Optional<Long> lastTaskTimeNanos = stopWatch.getLastTaskTimeNanos();
        System.out.println("cost time is " + lastTaskTimeMillis.orElse(null) + "ms,"+ lastTaskTimeNanos.orElse(null)+ "ns");
        System.out.println("total cost time is " + stopWatch.getTaskCount() + ","+ stopWatch.getTotalTimeMillis());

    }

}
