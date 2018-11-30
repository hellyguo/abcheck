package com.github.helly.abcheck.event;

/**
 * 心跳事件
 *
 * @author HellyGuo
 */
public class RecvPingEvent extends AbstractABCheckEvent implements ABCheckEvent {

    @Override
    public ABCheckEventType type() {
        return ABCheckEventType.RECV_PING;
    }
}
