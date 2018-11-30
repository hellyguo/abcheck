package com.github.helly.abcheck.sock;

/**
 * TCP包类型
 *
 * @author HellyGuo
 */
public enum PackageType {
    /**
     * 心跳包
     */
    PING,
    /**
     * 请求投票包
     */
    REQ_VOTE,
    /**
     * 投票结果包
     */
    VOTE
}
