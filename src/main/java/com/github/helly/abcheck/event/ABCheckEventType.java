package com.github.helly.abcheck.event;

/**
 * 事件类型
 *
 * @author HellyGuo
 */
public enum ABCheckEventType {
    /**
     * 超时事件
     */
    TIMEOUT,
    /**
     * 收到心跳包事件
     */
    RECV_PING,
    /**
     * 收到请求投票包事件
     */
    RECV_REQ_VOTE,
    /**
     * 收到投票结果包事件
     */
    RECV_VOTE
}
