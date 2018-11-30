package com.github.helly.abcheck.event;

/**
 * 事件
 *
 * @author HellyGuo
 */
public interface ABCheckEvent {
    /**
     * 事件类型
     * @return {@link ABCheckEventType}
     */
    ABCheckEventType type();
}
