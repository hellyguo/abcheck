package com.github.helly.abcheck.thread;

import com.github.helly.abcheck.ABCommander;
import com.github.helly.abcheck.event.TimeoutEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

import static com.github.helly.abcheck.constant.ABCheckConstants.FIX_TIME;
import static com.github.helly.abcheck.constant.ABCheckConstants.MIN_MILLIS_RANDOM;

/**
 * 超时控制线程主体
 *
 * @author HellyGuo
 */
public class ABCheckTimeoutLoop extends AbstractABCheckLoop implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ABCheckTimeoutLoop.class);
    private static final Random RANDOM = new Random(System.currentTimeMillis());

    public ABCheckTimeoutLoop(ABCommander commander) {
        super(commander);
    }

    @Override
    public void run() {
        commander.markTimeoutThread(Thread.currentThread());
        super.run();
    }

    @Override
    void loop() {
        try {
            long sleepTime;
            if (commander.isMain()) {
                // 当前为主节点情况下，固定频率发送PING
                // 此频率低于随机数，防止备份节点触发超时进行状态无谓切换
                sleepTime = FIX_TIME;
            } else {
                // 非主节点，随机超时时间。最大程度降低主节点和备份节点撞一起发起的概率。
                sleepTime = getRandomTimeout();
            }
            Thread.sleep(sleepTime);
            commander.pushEvent(new TimeoutEvent());
        } catch (InterruptedException e) {
            LOGGER.info("interrupted, cancel timeout");
        }
    }

    @Override
    String threadSubName() {
        return "timeoutLoop";
    }

    /**
     * 随机数，返回在150ms-299ms间随机数
     *
     * @return 随机数
     */
    private static long getRandomTimeout() {
        return RANDOM.nextInt(MIN_MILLIS_RANDOM) + MIN_MILLIS_RANDOM;
    }
}
