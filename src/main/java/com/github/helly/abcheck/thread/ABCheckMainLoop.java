package com.github.helly.abcheck.thread;

import com.github.helly.abcheck.ABStateHolder;
import com.github.helly.abcheck.event.ABCheckEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 事件处理线程主体
 *
 * @author HellyGuo
 */
public class ABCheckMainLoop extends AbstractABCheckLoop implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ABCheckMainLoop.class);

    public ABCheckMainLoop(ABStateHolder holder) {
        super(holder);
    }

    @Override
    void loop() {
        ABCheckEvent event = holder.fetchEvent();
        if (event != null) {
            LOGGER.info("recv event:[{}]", event.type());
            holder.check(event);
        }
    }

    @Override
    String threadSubName() {
        return "mainLoop";
    }

}
