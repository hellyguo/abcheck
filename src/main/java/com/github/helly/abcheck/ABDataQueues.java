package com.github.helly.abcheck;

import com.github.helly.abcheck.event.ABCheckEvent;
import com.github.helly.abcheck.sock.ReqPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import static com.github.helly.abcheck.constant.ABCheckConstants.MILLS_ONE_SEC;
import static com.github.helly.abcheck.constant.ABCheckConstants.QUEUE_CAPACITY;

/**
 * 队列持有
 *
 * @author HellyGuo
 */
final class ABDataQueues {
    private static final Logger LOGGER = LoggerFactory.getLogger(ABDataQueues.class);

    /**
     * 事件队列，见{@link ABCheckEvent}
     */
    private final ArrayBlockingQueue<ABCheckEvent> events = new ArrayBlockingQueue<>(QUEUE_CAPACITY, true);
    /**
     * 发送TCP包队列，见{@link ReqPackage}
     */
    private final ArrayBlockingQueue<ReqPackage> reqQueue = new ArrayBlockingQueue<>(QUEUE_CAPACITY, true);

    /**
     * 队列持有对象，内部使用，不可被直接使用
     */
    ABDataQueues() {
    }

    /**
     * 将事件放入队列
     *
     * @param event 事件
     */
    void pushEvent(ABCheckEvent event) {
        try {
            events.offer(event, MILLS_ONE_SEC, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            LOGGER.warn("send to queue failed, event[{}] dropped", event, e);
        }
    }

    /**
     * 读取队列中第一个事件
     *
     * @return 当前第一个事件，队列为空时返回null
     */
    ABCheckEvent fetchEvent() {
        try {
            return events.poll(MILLS_ONE_SEC, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            LOGGER.warn("fetch from event queue failed", e);
            return null;
        }
    }

    /**
     * 发送TCP包
     *
     * @param reqPackage TCP包
     */
    void sendReqPackage(ReqPackage reqPackage) {
        try {
            reqQueue.offer(reqPackage, MILLS_ONE_SEC, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            LOGGER.warn("send to queue failed, package[{}] dropped", reqPackage, e);
        }
    }

    /**
     * 读取队列中第一个TCP包
     *
     * @return 当前第一个TCP包，队列为空时返回null
     */
    ReqPackage fetchReqPackage() {
        try {
            return reqQueue.poll(MILLS_ONE_SEC, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            LOGGER.warn("fetch from req queue failed", e);
            return null;
        }
    }

}
