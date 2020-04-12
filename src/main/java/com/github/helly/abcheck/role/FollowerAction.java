package com.github.helly.abcheck.role;

import com.github.helly.abcheck.ABCommander;
import com.github.helly.abcheck.event.ABCheckEvent;
import com.github.helly.abcheck.event.ABCheckEventType;
import com.github.helly.abcheck.sock.ReqVotePackage;
import com.github.helly.abcheck.sock.VotePackage;

import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

/**
 * 备份节点动作
 *
 * @author HellyGuo
 */
public class FollowerAction implements RoleAction {
    private static final Map<ABCheckEventType, ActionInvoker> INVOKERS = new HashMap<>();

    static {
        INVOKERS.put(ABCheckEventType.TIMEOUT, FollowerAction::asCandidate);
        INVOKERS.put(ABCheckEventType.RECV_PING, FollowerAction::resetTimeout);
        INVOKERS.put(ABCheckEventType.RECV_REQ_VOTE, FollowerAction::voteAgree);
    }

    @Override
    public void perform(ABCommander commander, EventObject event) {
        ABCheckEvent abCheckEvent = (ABCheckEvent) event;
        ActionInvoker invoker = INVOKERS.get(abCheckEvent.type());
        if (invoker != null) {
            invoker.invoke(commander, event);
        }
    }

    private static void asCandidate(ABCommander commander, EventObject event) {
        // 升级为候选人
        commander.changeToCandidate();
        // 请求投票
        commander.sendReqPackage(new ReqVotePackage());
        // 重置超时
        commander.resetTimeout();
    }

    private static void resetTimeout(ABCommander commander, EventObject event) {
        // 重置超时
        commander.resetTimeout();
    }

    private static void voteAgree(ABCommander commander, EventObject event) {
        // 投票同意
        commander.sendReqPackage(new VotePackage(true));
        // 重置超时
        commander.resetTimeout();
    }
}
