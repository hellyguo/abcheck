package com.github.helly.abcheck.role;

import com.github.helly.abcheck.ABCommander;
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
    public void perform(ABCommander commander, EventObject event) {
        ABCheckEvent abCheckEvent = (ABCheckEvent) event;
        switch (abCheckEvent.type()) {
            case TIMEOUT: {
                asLeader(commander);
                break;
            }
            case RECV_PING: {
                asFollower(commander);
                break;
            }
            case RECV_REQ_VOTE: {
                // 降级为备份节点
                commander.changeToFollower();
                // 投票不同意
                commander.sendReqPackage(new VotePackage(false));
                // 重置超时
                commander.resetTimeout();
                break;
            }
            case RECV_VOTE: {
                Boolean yes = (Boolean) event.getSource();
                if (yes) {
                    // 对方投票同意
                    asLeader(commander);
                } else {
                    // 对方投票不同意
                    asFollower(commander);
                }
                break;
            }
            default: {
                break;
            }
        }
    }

    private void asLeader(ABCommander commander) {
        // 升级为主节点
        commander.changeToLeader();
        // 发送心跳包
        commander.sendReqPackage(new PingPackage(System.currentTimeMillis()));
        // 重置超时
        commander.resetTimeout();
    }

    private void asFollower(ABCommander commander) {
        // 降级为备份节点
        commander.changeToFollower();
        // 重置超时
        commander.resetTimeout();
    }
}
