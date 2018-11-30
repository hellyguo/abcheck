package com.github.helly.abcheck.role;

import com.github.helly.abcheck.ABStateHolder;
import com.github.helly.abcheck.event.ABCheckEvent;
import com.github.helly.abcheck.sock.PingPackage;
import com.github.helly.abcheck.sock.VotePackage;

import java.util.EventObject;

/**
 * 候选人动作
 *
 * @author HellyGuo
 */
public class CandidateAction implements RoleAction {
    @Override
    public void perform(ABStateHolder holder, EventObject event) {
        ABCheckEvent abCheckEvent = (ABCheckEvent) event;
        switch (abCheckEvent.type()) {
            case TIMEOUT: {
                asLeader(holder);
                break;
            }
            case RECV_PING: {
                asFollower(holder);
                break;
            }
            case RECV_REQ_VOTE: {
                // 降级为备份节点
                holder.changeToFollower();
                // 投票不同意
                holder.sendReqPackage(new VotePackage(false));
                // 重置超时
                holder.resetTimeout();
                break;
            }
            case RECV_VOTE: {
                Boolean yes = (Boolean) event.getSource();
                if (yes) {
                    // 对方投票同意
                    asLeader(holder);
                } else {
                    // 对方投票不同意
                    asFollower(holder);
                }
                break;
            }
            default: {
                break;
            }
        }
    }

    private void asLeader(ABStateHolder holder) {
        // 升级为主节点
        holder.changeToLeader();
        // 发送心跳包
        holder.sendReqPackage(new PingPackage(System.currentTimeMillis()));
        // 重置超时
        holder.resetTimeout();
    }

    private void asFollower(ABStateHolder holder) {
        // 降级为备份节点
        holder.changeToFollower();
        // 重置超时
        holder.resetTimeout();
    }
}
