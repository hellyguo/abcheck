package com.github.helly.abcheck.role;

import com.github.helly.abcheck.ABCommander;
import com.github.helly.abcheck.event.ABCheckEvent;
import com.github.helly.abcheck.event.ABCheckEventType;
import com.github.helly.abcheck.sock.PingPackage;
import com.github.helly.abcheck.sock.VotePackage;

import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

/**
 * 候选人动作
 *
 * @author HellyGuo
 */
public class CandidateAction implements RoleAction {
    private static final Map<ABCheckEventType, ActionInvoker> INVOKERS = new HashMap<>();

    static {
        INVOKERS.put(ABCheckEventType.TIMEOUT, CandidateAction::asLeader);
        INVOKERS.put(ABCheckEventType.RECV_PING, CandidateAction::asFollower);
        INVOKERS.put(ABCheckEventType.RECV_REQ_VOTE, CandidateAction::voteDisagree);
        INVOKERS.put(ABCheckEventType.RECV_VOTE, CandidateAction::voteByRole);
    }

    @Override
    public void perform(ABCommander commander, EventObject event) {
        ABCheckEvent abCheckEvent = (ABCheckEvent) event;
        ActionInvoker invoker = INVOKERS.get(abCheckEvent.type());
        if (invoker != null) {
            invoker.invoke(commander, event);
        }
    }

    private static void asLeader(ABCommander commander, EventObject event) {
        // 升级为主节点
        commander.changeToLeader();
        // 发送心跳包
        commander.sendReqPackage(new PingPackage(System.currentTimeMillis()));
        // 重置超时
        commander.resetTimeout();
    }

    private static void asFollower(ABCommander commander, EventObject event) {
        // 降级为备份节点
        commander.changeToFollower();
        // 重置超时
        commander.resetTimeout();
    }

    private static void voteDisagree(ABCommander commander, EventObject event) {
        // 降级为备份节点
        commander.changeToFollower();
        // 投票不同意
        commander.sendReqPackage(new VotePackage(false));
        // 重置超时
        commander.resetTimeout();
    }

    private static void voteByRole(ABCommander commander, EventObject event) {
        Boolean yes = (Boolean) event.getSource();
        if (yes) {
            // 对方投票同意
            asLeader(commander, event);
        } else {
            // 对方投票不同意
            asFollower(commander, event);
        }
    }
}
