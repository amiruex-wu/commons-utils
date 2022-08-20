package org.wch.commons;


import org.wch.commons.lang.ObjectUtils;

import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 耗时检测工具里
 * @Author: wuchu
 * @CreateTime: 2022-07-13 17:22
 */
public class StopWatch {

    private final String id;
    private boolean keepTaskList;
    private final List<StopWatch.TaskInfo> taskList;
    private long startTimeNanos;
    private String currentTaskName;
    private StopWatch.TaskInfo lastTaskInfo;
    private int taskCount;
    private long totalTimeNanos;

    public StopWatch() {
        this(RandomUtils.uuid());
    }

    public StopWatch(String id) {
        this.keepTaskList = true;
        this.taskList = new LinkedList<>();
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setKeepTaskList(boolean keepTaskList) {
        this.keepTaskList = keepTaskList;
    }

    /**
     * Creates a stopwatch for convenience.
     *
     * @return StopWatch a stopwatch.
     */
    public static StopWatch create() {
        return new StopWatch();
    }

    /**
     * Creates a stopwatch for convenience.
     *
     * @param id id
     * @return StopWatch a stopwatch.
     */
    public static StopWatch create(String id) {
        return new StopWatch(id);
    }

    /**
     * Creates a started stopwatch for convenience.
     *
     * @return StopWatch a stopwatch that's already been started.
     */
    public static StopWatch createStarted() {
        final StopWatch sw = new StopWatch();
        sw.start();
        return sw;
    }

    /**
     * Creates a started stopwatch for convenience.
     *
     * @param taskName the task name
     * @return StopWatch a stopwatch that's already been started.
     */
    public static StopWatch createStarted(String taskName) {
        final StopWatch sw = new StopWatch();
        sw.start(taskName);
        return sw;
    }

    /**
     * <p>
     * Starts the stopwatch.
     * </p>
     *
     * <p>
     * This method starts a new timing session, clearing any previous values.
     * </p>
     *
     * @throws IllegalStateException if the StopWatch is already running.
     */
    public void start() throws IllegalStateException {
        this.start("");
    }

    /**
     * <p>
     * Starts the stopwatch.
     * </p>
     *
     * <p>
     * This method starts a new timing session, clearing any previous values.
     * </p>
     *
     * @param taskName th task name
     * @throws IllegalStateException if the StopWatch is already running.
     */
    public void start(String taskName) throws IllegalStateException {
        if (ObjectUtils.nonNull(this.currentTaskName)) {
            throw new IllegalStateException("Can't start StopWatchSpring: it's already running");
        } else {
            this.currentTaskName = taskName;
            this.startTimeNanos = System.nanoTime();
        }
    }


    /**
     * <p>
     * Stops the stopwatch.
     * </p>
     *
     * <p>
     * This method ends a new timing session, allowing the time to be retrieved.
     * </p>
     *
     * @throws IllegalStateException if the StopWatch is not running.
     */
    public void stop() throws IllegalStateException {
        if (ObjectUtils.isNull(this.currentTaskName)) {
            throw new IllegalStateException("Can't stop StopWatchSpring: it's not running");
        } else {
            long lastTime = System.nanoTime() - this.startTimeNanos;
            this.totalTimeNanos += lastTime;
            this.lastTaskInfo = new StopWatch.TaskInfo(this.currentTaskName, lastTime);
            if (this.keepTaskList) {
                this.taskList.add(this.lastTaskInfo);
            }

            ++this.taskCount;
            this.currentTaskName = null;
        }
    }

    public boolean isRunning() {
        return this.currentTaskName != null;
    }

    public Optional<String> currentTaskName() {
        return Optional.ofNullable(this.currentTaskName);
    }

    public Optional<Long> getLastTaskTimeNanos() throws IllegalStateException {
        if (ObjectUtils.isNull(this.lastTaskInfo)) {
//            throw new IllegalStateException("No tasks run: can't get last task interval");
            return Optional.empty();
        } else {
            return Optional.of(this.lastTaskInfo.getTimeNanos());
        }
    }

    public Optional<Long> getLastTaskTimeMillis() throws IllegalStateException {
        if (ObjectUtils.isNull(this.lastTaskInfo)) {
//            throw new IllegalStateException("No tasks run: can't get last task interval");
            return Optional.empty();
        } else {
            return Optional.of(this.lastTaskInfo.getTimeMillis());
        }
    }

    public Optional<String> getLastTaskName() throws IllegalStateException {
        if (ObjectUtils.isNull(this.lastTaskInfo)) {
//            throw new IllegalStateException("No tasks run: can't get last task name");
            return Optional.empty();
        } else {
            return Optional.of(this.lastTaskInfo.getTaskName());
        }
    }

    public Optional<StopWatch.TaskInfo> getLastTaskInfo() throws IllegalStateException {
        if (ObjectUtils.isNull(this.lastTaskInfo)) {
//            throw new IllegalStateException("No tasks run: can't get last task info");
            return Optional.empty();
        } else {
            return Optional.of(this.lastTaskInfo);
        }
    }

    public long getTotalTimeNanos() {
        return this.totalTimeNanos;
    }

    public long getTotalTimeMillis() {
        return nanosToMillis(this.totalTimeNanos);
    }

    public double getTotalTimeSeconds() {
        return nanosToSeconds(this.totalTimeNanos);
    }

    public int getTaskCount() {
        return this.taskCount;
    }

    public StopWatch.TaskInfo[] getTaskInfo() {
        if (!this.keepTaskList) {
            throw new UnsupportedOperationException("Task info is not being kept!");
        } else {
            return this.taskList.toArray(new TaskInfo[0]);
        }
    }

    public String shortSummary() {
        return "StopWatchSpring '" + this.getId() + "': running time = " + this.getTotalTimeNanos() + " ns";
    }

    public String prettyPrint() {
        StringBuilder sb = new StringBuilder(this.shortSummary());
        sb.append('\n');
        if (!this.keepTaskList) {
            sb.append("No task info kept");
        } else {
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
                sb.append(nf.format(task.getTimeNanos())).append("  ");
                sb.append(pf.format((double) task.getTimeNanos() / (double) this.getTotalTimeNanos())).append("  ");
                sb.append(task.getTaskName()).append("\n");
            }
        }

        return sb.toString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(this.shortSummary());
        if (this.keepTaskList) {
            StopWatch.TaskInfo[] taskInfos = this.getTaskInfo();

            for (TaskInfo task : taskInfos) {
                sb.append("; [").append(task.getTaskName()).append("] took ").append(task.getTimeNanos()).append(" ns");
                long percent = Math.round(100.0D * (double) task.getTimeNanos() / (double) this.getTotalTimeNanos());
                sb.append(" = ").append(percent).append("%");
            }
        } else {
            sb.append("; no task info kept");
        }

        return sb.toString();
    }

    private static long nanosToMillis(long duration) {
        return TimeUnit.NANOSECONDS.toMillis(duration);
    }

    private static double nanosToSeconds(long duration) {
        return (double) duration / 1.0E9D;
    }

    public static final class TaskInfo {
        private final String taskName;
        private final long timeNanos;

        TaskInfo(String taskName, long timeNanos) {
            this.taskName = taskName;
            this.timeNanos = timeNanos;
        }

        public String getTaskName() {
            return this.taskName;
        }

        public long getTimeNanos() {
            return this.timeNanos;
        }

        public long getTimeMillis() {
            return StopWatch.nanosToMillis(this.timeNanos);
        }

        public double getTimeSeconds() {
            return StopWatch.nanosToSeconds(this.timeNanos);
        }
    }
}
