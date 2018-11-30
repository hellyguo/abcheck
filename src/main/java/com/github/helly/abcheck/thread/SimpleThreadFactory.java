package com.github.helly.abcheck.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 简化的线程创建工厂类
 *
 * @author HellyGuo
 */
public class SimpleThreadFactory implements ThreadFactory {
    private String threadName;
    private AtomicInteger count = new AtomicInteger(0);

    public SimpleThreadFactory(String threadName) {
        this.threadName = threadName;
    }

    /**
     * @param r 线程主题
     * @return 新线程
     * @see ThreadFactory#newThread
     */
    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r, threadName + "_" + count.incrementAndGet());
        thread.setDaemon(true);
        return thread;
    }
}
