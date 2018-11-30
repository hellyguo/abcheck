package com.github.helly.abcheck.role;

import com.github.helly.abcheck.ABStateHolder;
import com.github.helly.abcheck.event.ABCheckEvent;

import java.util.EventObject;

/**
 * 节点角色
 *
 * @author HellyGuo
 */
public enum Role {
    /**
     * 跟随者，备份节点
     */
    FOLLOWER(new FollowerAction()),
    /**
     * 候选人
     */
    CANDIDATE(new CandidateAction()),
    /**
     * 主节点
     */
    LEADER(new LeaderAction());

    /**
     * 节点动作
     */
    private RoleAction action;

    Role(RoleAction action) {
        this.action = action;
    }

    public void performAction(ABStateHolder holder, ABCheckEvent event) {
        action.perform(holder, (EventObject) event);
    }
}
