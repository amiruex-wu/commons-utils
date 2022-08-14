package org.wch.commons.io;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.wch.commons.net.HttpUtils;
import org.wch.commons.StopWatch;

import java.util.Arrays;
import java.util.Optional;

@RunWith(JUnit4.class)
public class ZipUtilsTest {
//    private Logger logger = LoggerFactory.getLogger(ZipUtilsTest.class);

    @Test
    public void test() throws InterruptedException {

        StopWatch stopWatch = new StopWatch();

        // 任务一模拟休眠3秒钟
        stopWatch.start("TaskOneName");
        Thread.sleep(1000 * 3);
        System.out.println("当前任务名称：" + stopWatch.currentTaskName());
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
        System.out.println("耗时：" + stopWatch.getTotalTimeMillis() + "ms");
//        logger.info("耗时：{}ms", stopWatch.getTotalTimeMillis());
    }

    @Test
    public void test1() throws InterruptedException {

        StopWatch stopWatch = StopWatch.createStarted("TaskOneName");

        // 任务一模拟休眠3秒钟
//        stopWatch.start("TaskOneName");
        Thread.sleep(1000 * 3);
        System.out.println("当前任务名称：" + stopWatch.currentTaskName().orElse(null));
        stopWatch.stop();

        System.out.println("耗时：" + stopWatch.getTotalTimeMillis() + "ms");
        stopWatch.start("TaskTwoName");
        Thread.sleep(1000 * 5);
        System.out.println("当前任务名称：" + stopWatch.currentTaskName().orElse(null));
        stopWatch.stop();
//        logger.info("耗时：{}ms", stopWatch.getTotalTimeMillis());
        // 打印出耗时
        System.out.println(stopWatch.prettyPrint());
        System.out.println(stopWatch.shortSummary());
        // stop后它的值为null
        System.out.println(stopWatch.currentTaskName().orElse(null));

        // 最后一个任务的相关信息
        System.out.println(stopWatch.getLastTaskName().orElse(null));
        System.out.println(stopWatch.getLastTaskInfo().orElse(null));

        // 任务总的耗时  如果你想获取到每个任务详情（包括它的任务名、耗时等等）可使用
        System.out.println("所有任务总耗时：" + stopWatch.getTotalTimeMillis());
        System.out.println("任务总数：" + stopWatch.getTaskCount());
        System.out.println("所有任务详情：" + Arrays.toString(stopWatch.getTaskInfo()));
    }

    @Test
    public void test2(){
//        10.0.0.0 - 10.255.255.255
//        172.16.0.0 - 172.31.255.255
//        192.168.0.0 - 192.168.255.255
        String ipUrl ="172.31.0.0";
        boolean b = HttpUtils.isInternalIPAddress(ipUrl);
        System.out.println("result is "+ b);
    }

    public static void main(String[] args) {
//        String sourcePath = "C:\\Windows_Own_Workspace_Folder\\outPutFile\\generate\\27df8b812afb452ba0d55c4b5f238844-all";
//        String targetPath = "C:\\Windows_Own_Workspace_Folder\\outPutFile/zipfiles/";
//        ZipUtils.toZip(sourcePath, targetPath, "myZipFile", true);

        String src = "C:\\Windows_Own_Workspace_Folder\\outPutFile/zipfiles/test.txt";
        Optional<String> dirsIfNotExists = FileUtils.createDirsIfNotExists(src);
        System.out.println("result is " + dirsIfNotExists.orElse(null));
    }
}
