package com.github.helly.abcheck.thread;

import com.github.helly.abcheck.ABCommander;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 抽象线程主体
 *
 * @author HellyGuo
 */
abstract class AbstractABCheckLoop implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractABCheckLoop.class);

    /**
     * 命令执行器
     */
    ABCommander commander;

    /**
     * 循环控制开关变量
     */
    private final AtomicBoolean active = new AtomicBoolean(true);

    AbstractABCheckLoop(ABCommander commander) {
        this.commander = commander;
    }

    @Override
    public void run() {
        Thread current = Thread.currentThread();
        current.setName(current.getName() + "#" + threadSubName());
        while (active.get()) {
            try {
                loop();
            } catch (Exception e) {
                LOGGER.warn(e.getMessage(), e);
            }
        }
    }

    /**
     * 循环运行主体
     */
    abstract void loop();

    /**
     * 线程子名称，用于拼接到线程名上
     *
     * @return subName
     */
    abstract String threadSubName();

    /**
     * 停止线程，通过将循环控制开关变量关闭完成
     */
    public void stop() {
        active.set(false);
    }
}
