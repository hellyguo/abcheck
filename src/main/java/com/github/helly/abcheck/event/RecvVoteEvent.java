package com.github.helly.abcheck.event;

/**
 * 接收投票结果事件
 *
 * @author HellyGuo
 */
public class RecvVoteEvent extends AbstractABCheckEvent implements ABCheckEvent {

    public RecvVoteEvent(Boolean yes){
        super(yes);
    }

    @Override
    public ABCheckEventType type() {
        return ABCheckEventType.RECV_VOTE;
    }
}
