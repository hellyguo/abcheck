package com.github.helly.abcheck;

import com.github.helly.abcheck.event.ABCheckEvent;
import com.github.helly.abcheck.role.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 状态持有
 *
 * @author HellyGuo
 */
final class ABStateHolder {
    private static final Logger LOGGER = LoggerFactory.getLogger(ABStateHolder.class);

    /**
     * 节点角色，默认角色为{@link Role#FOLLOWER}
     */
    private Role role = Role.FOLLOWER;
    /**
     * 是否主机标志，缓存结果用以提速
     */
    private final AtomicBoolean mainFlag = new AtomicBoolean(false);

    /**
     * 构造状态持有对象，内部使用，不可被直接使用
     */
    ABStateHolder() {
    }

    /**
     * 查找是否主机
     *
     * @return true:主机；false:备份
     */
    boolean isMain() {
        return mainFlag.get();
    }

    /**
     * 事件检查
     *
     * @param event 事件
     */
    void check(ABCommander commander, ABCheckEvent event) {
        role.performAction(commander, event);
    }

    /**
     * 本节点角色换为{@link Role#LEADER}
     */
    void changeToLeader() {
        changeRole(Role.LEADER);
    }

    /**
     * 本节点角色换为{@link Role#CANDIDATE}
     */
    void changeToCandidate() {
        changeRole(Role.CANDIDATE);
    }

    /**
     * 本节点角色换为{@link Role#FOLLOWER}
     */
    void changeToFollower() {
        changeRole(Role.FOLLOWER);
    }

    /**
     * 切换角色
     *
     * @param role 目标角色
     */
    private void changeRole(Role role) {
        LOGGER.info("role change from [{}] to [{}]", this.role, role);
        this.role = role;
        if (Role.LEADER.equals(this.role)) {
            mainFlag.set(true);
        }
    }

}
