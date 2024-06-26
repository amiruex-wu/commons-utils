package top.itoolbox.commons;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import top.itoolbox.commons.enums.StatusType;
import top.itoolbox.commons.lang.ObjectUtils;
import top.itoolbox.commons.lang.StringUtils;

import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 耗时检测工具里
 * @Author: wuchu
 * @CreateTime: 2022-07-13 17:22
 */
@Data
public class StopWatch {

    /**
     * 当前最后一个任务的主键ID
     */
    private final String id;

    /**
     * 任务集合
     */
    private final List<StopWatch.TaskInfo> taskList;

    /**
     * 最后一个任务
     */
    private StopWatch.TaskInfo lastTaskInfo;

    /**
     * 任务总数
     */
    private int taskCount;

    /**
     * 任务总耗时
     */
    private long totalTimeNanos;

    public StopWatch() {
        this(RandomUtils.uuid());
    }

    public StopWatch(String id) {
        this.id = id;
        this.taskList = new LinkedList<>();
        TaskInfo taskInfo = new TaskInfo(id, 0L, StatusType.NOT_STARTED);
        this.taskList.add(taskInfo);
        this.lastTaskInfo = taskInfo;
    }

    /**
     * 创建并启动一个stopwatch.
     *
     * @return 返回一个已经启动的stopwatch.
     */
    public static StopWatch create() {
        final StopWatch sw = new StopWatch();
        sw.start(sw.getId());
        return sw;
    }

    /**
     * 创建一个stopwatch并且启动
     *
     * @param taskName the task name
     * @return StopWatch a stopwatch that's already been started.
     */
    public static StopWatch create(String taskName) {
        final StopWatch sw = new StopWatch();
        sw.start(taskName);
        return sw;
    }

    /**
     * <p>
     * 开启stopwatch.
     * </p>
     *
     * <p>
     * 如果方法当前任务列表中包含相同名称的任务且未开始则启动该stopwatch,否则创建一个新的任务并加入到列表中.
     * </p>
     *
     * @param taskName 任务名称
     */
    public void start(String taskName) {
        if (ObjectUtils.isNull(this.taskList)) {
            throw new IllegalStateException("Can't start StopWatchSpring: task list is empty");
        }
        if (StringUtils.isBlank(taskName)) {
            throw new IllegalStateException("Can't start StopWatchSpring: task name is empty");
        }
        final Optional<TaskInfo> optional = taskList.stream()
                .filter(taskInfo -> ObjectUtils.equals(taskInfo.getName(), taskName)
                        && !StatusType.UNDER_WAY.equals(taskInfo.getStatus()))
                .findFirst();
        if (optional.isPresent()) {
            optional.get().setStatus(StatusType.UNDER_WAY);
            optional.get().setNanos(System.nanoTime());
        } else {
            TaskInfo taskInfo = new TaskInfo(id, System.nanoTime(), StatusType.UNDER_WAY);
            this.taskList.add(taskInfo);
            this.lastTaskInfo = taskInfo;
        }
    }

    /**
     * 停止所有任务
     */
    public void stop() {
        if (ObjectUtils.isEmpty(taskList)) {
            throw new IllegalStateException("Can't stop StopWatchSpring: task list is empty");
        }
        final long count = taskList.stream().filter(taskInfo -> !StatusType.FINISHED.equals(taskInfo.getStatus())).count();
        if (count <= 0) {
            throw new IllegalStateException("Can't stop StopWatchSpring: it's not running");
        }

        for (TaskInfo taskInfo : taskList) {
            if (!StatusType.FINISHED.equals(taskInfo.getStatus())) {
                taskInfo.setStatus(StatusType.FINISHED);
                final long nanos = System.nanoTime() - taskInfo.getNanos();
                taskInfo.setNanos(nanos);
                this.totalTimeNanos += nanos;
            } else {
                this.totalTimeNanos += taskInfo.getNanos();
            }
        }
        this.taskCount = taskList.size();
    }

    /**
     * 获取最后一个任务耗时
     *
     * @return 返回耗时纳秒数
     * @throws IllegalStateException
     */
    public Optional<Long> getLastTaskTimeNanos() throws IllegalStateException {
        if (ObjectUtils.isNull(this.lastTaskInfo)) {
            return Optional.empty();
        } else {
            return Optional.of(this.lastTaskInfo.getNanos());
        }
    }

    /**
     * 获取最后一个任务耗时
     *
     * @param stopWatch 是否停止所有计时
     * @return 返回耗时纳秒数
     * @throws IllegalStateException
     */
    public Optional<Long> getLastTaskTimeNanos(boolean stopWatch) throws IllegalStateException {
        if (stopWatch) {
            stop();
        }
        return getLastTaskTimeNanos();
    }

    /**
     * 获取最后一个任务耗时
     *
     * @return 返回耗时毫秒数
     * @throws IllegalStateException
     */
    public Optional<Long> getLastTaskTimeMillis() throws IllegalStateException {
        if (ObjectUtils.isNull(this.lastTaskInfo)) {
            return Optional.empty();
        } else {
            return Optional.of(this.lastTaskInfo.getTimeMillis());
        }
    }

    /**
     * 获取最后一个任务耗时
     *
     * @param stopWatch 是否停止所有计时
     * @return 返回耗时毫秒数
     * @throws IllegalStateException
     */
    public Optional<Long> getLastTaskTimeMillis(boolean stopWatch) throws IllegalStateException {
        if (stopWatch) {
            stop();
        }
        return getLastTaskTimeMillis();
    }

    /**
     * 获取最后一个任务名称
     *
     * @return 返回字符串或空值
     */
    public Optional<String> getLastTaskName() {
        if (ObjectUtils.isNull(this.lastTaskInfo)) {
            return Optional.empty();
        } else {
            return Optional.of(this.lastTaskInfo.getName());
        }
    }

    /**
     * 获取最后一个任务信息
     *
     * @return 任务对象
     */
    public Optional<StopWatch.TaskInfo> getLastTaskInfo() {
        if (ObjectUtils.isNull(this.lastTaskInfo)) {
            return Optional.empty();
        } else {
            return Optional.of(this.lastTaskInfo);
        }
    }

    /**
     * 获取任务总耗时
     *
     * @return 毫秒数
     */
    public long getTotalTimeMillis() {
        if (totalTimeNanos <= 0 && ObjectUtils.isNotEmpty(taskList)) {
            final long currentNanos = System.nanoTime();
            final long total = taskList.stream()
                    .mapToLong(taskInfo -> {
                        if (StatusType.FINISHED.equals(taskInfo.getStatus())) {
                            return taskInfo.getNanos();
                        }
                        return currentNanos - taskInfo.getNanos();
                    })
                    .sum();
            return nanosToMillis(total);
        }
        return nanosToMillis(this.totalTimeNanos);
    }

    /**
     * 获取任务总耗时
     *
     * @param stopWatch 是否停止所有计时
     * @return 毫秒数
     */
    public long getTotalTimeMillis(boolean stopWatch) {
        if (stopWatch) {
            stop();
        }
        return getTotalTimeMillis();
    }

    /**
     * 获取所有任务总耗时
     *
     * @return 总耗时秒数
     */
    public double getTotalTimeSeconds() {
        if (totalTimeNanos <= 0 && ObjectUtils.isNotEmpty(taskList)) {
            final long currentNanos = System.nanoTime();
            final long total = taskList.stream()
                    .mapToLong(taskInfo -> {
                        if (StatusType.FINISHED.equals(taskInfo.getStatus())) {
                            return taskInfo.getNanos();
                        }
                        return currentNanos - taskInfo.getNanos();
                    })
                    .sum();
            return nanosToSeconds(total);
        }
        return nanosToSeconds(this.totalTimeNanos);
    }

    /**
     * 简短信息
     *
     * @return
     */
    public String shortSummary() {
        return "StopWatchSpring '" + this.getId() + "': running time = " + this.getTotalTimeNanos() + " ns";
    }

    /**
     * 信息打印字符
     *
     * @return
     */
    public String prettyPrint() {
        StringBuilder sb = new StringBuilder(this.shortSummary());
        sb.append('\n');
        sb.append("---------------------------------------------\n");
        sb.append("ns         %     Task name\n");
        sb.append("---------------------------------------------\n");
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMinimumIntegerDigits(9);
        nf.setGroupingUsed(false);
        NumberFormat pf = NumberFormat.getPercentInstance();
        pf.setMinimumIntegerDigits(3);
        pf.setGroupingUsed(false);
        StopWatch.TaskInfo[] taskInfos = this.getTaskInfo();

        for (TaskInfo task : taskInfos) {
            sb.append(nf.format(task.getNanos())).append("  ");
            sb.append(pf.format((double) task.getNanos() / (double) this.getTotalTimeNanos())).append("  ");
            sb.append(task.getName()).append("\n");
        }

        return sb.toString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(this.shortSummary());

        StopWatch.TaskInfo[] taskInfos = this.getTaskInfo();

        for (TaskInfo task : taskInfos) {
            sb.append("; [").append(task.getName()).append("] took ").append(task.getNanos()).append(" ns");
            long percent = Math.round(100.0D * (double) task.getNanos() / (double) this.getTotalTimeNanos());
            sb.append(" = ").append(percent).append("%");
        }

        return sb.toString();
    }

    private static long nanosToMillis(long duration) {
        return TimeUnit.NANOSECONDS.toMillis(duration);
    }

    private static double nanosToSeconds(long duration) {
        return (double) duration / 1.0E9D;
    }

    private StopWatch.TaskInfo[] getTaskInfo() {
        return this.taskList.toArray(new TaskInfo[0]);
    }

    public static final class TaskInfo {

        @Getter
        @Setter
        private String name;

        @Getter
        @Setter
        private long nanos;

        @Getter
        @Setter
        private StatusType status;


        TaskInfo(String name, long timeNanos, StatusType status) {
            this.name = name;
            this.nanos = timeNanos;
            this.status = status;
        }

        public long getTimeMillis() {
            Long temp = getNanoLong();
            return StopWatch.nanosToMillis(temp);
        }

        public double getTimeSeconds() {
            Long temp = getNanoLong();
            return StopWatch.nanosToSeconds(temp);
        }

        private Long getNanoLong() {
            Long temp = null;
            switch (status) {
                case NOT_STARTED:
                    temp = 0L;
                    break;
                case UNDER_WAY:
                    temp = System.nanoTime() - nanos;
                    break;
                case FINISHED:
                    temp = nanos;
                    break;
            }
            return temp;
        }
    }

}
