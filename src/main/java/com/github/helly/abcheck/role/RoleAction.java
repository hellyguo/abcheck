package com.github.helly.abcheck.role;

import com.github.helly.abcheck.ABStateHolder;

import java.util.EventObject;

/**
 * 节点动作
 *
 * @author HellyGuo
 */
public interface RoleAction {
    /**
     * 执行动作
     *
     * @param holder 状态持有对象
     * @param event  事件
     */
    void perform(ABStateHolder holder, EventObject event);
}
