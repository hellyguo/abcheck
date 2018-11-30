package com.github.helly.abcheck.event;

/**
 * 超时事件
 *
 * @author HellyGuo
 */
public class TimeoutEvent extends AbstractABCheckEvent implements ABCheckEvent {

    @Override
    public ABCheckEventType type() {
        return ABCheckEventType.TIMEOUT;
    }
}
