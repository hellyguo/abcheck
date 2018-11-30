package com.github.helly.abcheck;

import com.github.helly.abcheck.event.ABCheckEvent;
import com.github.helly.abcheck.role.Role;
import com.github.helly.abcheck.sock.ReqPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 命令执行器
 *
 * @author HellyGuo
 */
public final class ABCommander {


    private ABStateHolder stateHolder;
    private ABDataQueues dataQueues;
    private ABThreadHolder threadHolder;

    /**
     * 命令执行对象，内部使用，不可被直接使用
     *
     * @param stateHolder  {@link ABStateHolder} 状态持有对象
     * @param dataQueues   {@link ABDataQueues} 队列持有对象
     * @param threadHolder {@link ABThreadHolder} 线程持有对象
     */
    ABCommander(ABStateHolder stateHolder, ABDataQueues dataQueues, ABThreadHolder threadHolder) {
        this.stateHolder = stateHolder;
        this.dataQueues = dataQueues;
        this.threadHolder = threadHolder;
    }

    /**
     * 查找是否主机
     *
     * @return true:主机；false:备份
     */
    public boolean isMain() {
        return stateHolder.isMain();
    }

    /**
     * 事件检查
     *
     * @param event 事件
     */
    public void check(ABCheckEvent event) {
        stateHolder.check(this, event);
    }

    /**
     * 将事件放入队列
     *
     * @param event 事件
     */
    public void pushEvent(ABCheckEvent event) {
        dataQueues.pushEvent(event);
    }

    /**
     * 读取队列中第一个事件
     *
     * @return 当前第一个事件，队列为空时返回null
     */
    public ABCheckEvent fetchEvent() {
        return dataQueues.fetchEvent();
    }

    /**
     * 发送TCP包
     *
     * @param reqPackage TCP包
     */
    public void sendReqPackage(ReqPackage reqPackage) {
        dataQueues.sendReqPackage(reqPackage);
    }

    /**
     * 读取队列中第一个TCP包
     *
     * @return 当前第一个TCP包，队列为空时返回null
     */
    public ReqPackage fetchReqPackage() {
        return dataQueues.fetchReqPackage();
    }

    /**
     * 本节点角色换为{@link Role#LEADER}
     */
    public void changeToLeader() {
        stateHolder.changeToLeader();
    }

    /**
     * 本节点角色换为{@link Role#CANDIDATE}
     */
    public void changeToCandidate() {
        stateHolder.changeToCandidate();
    }

    /**
     * 本节点角色换为{@link Role#FOLLOWER}
     */
    public void changeToFollower() {
        stateHolder.changeToFollower();
    }

    /**
     * 标记超时线程
     *
     * @param thread 超时线程
     */
    public void markTimeoutThread(Thread thread) {
        threadHolder.markTimeoutThread(thread);
    }

    /**
     * 通过打断超时来重置超时
     */
    public void resetTimeout() {
        threadHolder.resetTimeout();
    }

}
