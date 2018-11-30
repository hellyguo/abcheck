package com.github.helly.abcheck.role;

import com.github.helly.abcheck.ABCommander;
import com.github.helly.abcheck.event.ABCheckEvent;
import com.github.helly.abcheck.sock.ReqVotePackage;
import com.github.helly.abcheck.sock.VotePackage;

import java.util.EventObject;

/**
 * 备份节点动作
 *
 * @author HellyGuo
 */
public class FollowerAction implements RoleAction {
    @Override
    public void perform(ABCommander commander, EventObject event) {
        ABCheckEvent abCheckEvent = (ABCheckEvent) event;
        switch (abCheckEvent.type()) {
            case TIMEOUT: {
                // 升级为候选人
                commander.changeToCandidate();
                // 请求投票
                commander.sendReqPackage(new ReqVotePackage());
                // 重置超时
                commander.resetTimeout();
                break;
            }
            case RECV_PING: {
                // 重置超时
                commander.resetTimeout();
                break;
            }
            case RECV_REQ_VOTE: {
                // 投票同意
                commander.sendReqPackage(new VotePackage(true));
                // 重置超时
                commander.resetTimeout();
                break;
            }
            default: {
                break;
            }
        }

    }
}
