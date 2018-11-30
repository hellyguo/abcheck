package com.github.helly.abcheck.role;

import com.github.helly.abcheck.ABStateHolder;
import com.github.helly.abcheck.event.ABCheckEvent;
import com.github.helly.abcheck.event.ABCheckEventType;
import com.github.helly.abcheck.sock.PingPackage;

import java.util.EventObject;

/**
 * 主节点动作
 *
 * @author HellyGuo
 */
public class LeaderAction implements RoleAction {
    @Override
    public void perform(ABStateHolder holder, EventObject event) {
        ABCheckEvent abCheckEvent = (ABCheckEvent) event;
        if (ABCheckEventType.TIMEOUT.equals(abCheckEvent.type())) {
            // 定时发送心跳包
            holder.sendReqPackage(new PingPackage());
        }
    }
}
