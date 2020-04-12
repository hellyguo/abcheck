package com.github.helly.abcheck.sock;

import com.github.helly.abcheck.event.ABCheckEvent;
import com.github.helly.abcheck.event.EventCreator;
import com.github.helly.abcheck.event.RecvPingEvent;
import com.github.helly.abcheck.event.RecvReqVoteEvent;
import com.github.helly.abcheck.event.RecvVoteEvent;

/**
 * TCP包类型
 *
 * @author HellyGuo
 */
public enum PackageType {
    /**
     * 心跳包
     */
    PING((type, version) -> new RecvPingEvent()),
    /**
     * 请求投票包
     */
    REQ_VOTE((type, version) -> new RecvReqVoteEvent()),
    /**
     * 投票结果包
     */
    VOTE((type, version) -> new RecvVoteEvent(version > 0));

    private final EventCreator creator;

    PackageType(EventCreator creator) {
        this.creator = creator;
    }

    public ABCheckEvent createEvent(int type, long version) {
        return creator.create(type, version);
    }
}
