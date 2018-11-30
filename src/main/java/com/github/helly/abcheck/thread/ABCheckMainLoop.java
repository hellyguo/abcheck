package com.github.helly.abcheck.thread;

import com.github.helly.abcheck.ABCommander;
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

    public ABCheckMainLoop(ABCommander commander) {
        super(commander);
    }

    @Override
    void loop() {
        ABCheckEvent event = commander.fetchEvent();
        if (event != null) {
            LOGGER.info("recv event:[{}]", event.type());
            commander.check(event);
        }
    }

    @Override
    String threadSubName() {
        return "mainLoop";
    }

}
