package com.github.helly.abcheck.role;

import com.github.helly.abcheck.ABStateHolder;
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
    public void perform(ABStateHolder holder, EventObject event) {
        ABCheckEvent abCheckEvent = (ABCheckEvent) event;
        switch (abCheckEvent.type()) {
            case TIMEOUT: {
                // 升级为候选人
                holder.changeToCandidate();
                // 请求投票
                holder.sendReqPackage(new ReqVotePackage());
                // 重置超时
                holder.resetTimeout();
                break;
            }
            case RECV_PING: {
                // 重置超时
                holder.resetTimeout();
                break;
            }
            case RECV_REQ_VOTE: {
                // 投票同意
                holder.sendReqPackage(new VotePackage(true));
                // 重置超时
                holder.resetTimeout();
                break;
            }
            default: {
                break;
            }
        }

    }
}
