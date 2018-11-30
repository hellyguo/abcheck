package com.github.helly.abcheck.role;

import com.github.helly.abcheck.ABCommander;

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
     * @param commander 命令执行器
     * @param event     事件
     */
    void perform(ABCommander commander, EventObject event);
}
