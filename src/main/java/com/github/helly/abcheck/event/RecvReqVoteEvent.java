package com.github.helly.abcheck.event;

/**
 * 接手请求投票事件
 *
 * @author HellyGuo
 */
public class RecvReqVoteEvent extends AbstractABCheckEvent implements ABCheckEvent {

    @Override
    public ABCheckEventType type() {
        return ABCheckEventType.RECV_REQ_VOTE;
    }
}
