package com.github.helly.abcheck.constant;

/**
 * 常量类
 *
 * @author Helly Guo
 * Created on 11/29/18 11:05 PM
 */
public final class ABCheckConstants {

    /**
     * 配套线程池大小
     */
    public static final int POOL_SIZE = 4;
    /**
     * 线程池配套任务队列深度，实际无意义，只要非零即可。因为没有用到任务队列。
     */
    public static final int POOL_QUEUE_CAPACITY = 4;
    /**
     * 内部队列深度，固定
     */
    public static final int QUEUE_CAPACITY = 2048;
    /**
     * 一秒的毫秒数
     */
    public static final long MILLS_ONE_SEC = 1000L;

    /**
     * 投票同意:yes
     */
    public static final int YES_VAL = 1;
    /**
     * 投票不同意:no
     */
    public static final int NO_VAL = 0;
    /**
     * 字节缓冲区大小
     */
    public static final int BUF_CAPACITY = 1024;
    /**
     * 连接信息大小
     */
    public static final int HOST_INFO_LEN = 24;
    /**
     * 固定时长
     */
    public static final long FIX_TIME = 120L;
    /**
     * 随机最小时长
     */
    public static final int MIN_MILLIS_RANDOM = 150;

    public ABCheckConstants() {
    }
}
