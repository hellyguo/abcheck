package com.github.helly.abcheck;

import com.github.helly.abcheck.thread.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.github.helly.abcheck.constant.ABCheckConstants.POOL_QUEUE_CAPACITY;
import static com.github.helly.abcheck.constant.ABCheckConstants.POOL_SIZE;

/**
 * 线程持有
 *
 * @author HellyGuo
 */
final class ABThreadHolder {
    private static final Logger LOGGER = LoggerFactory.getLogger(ABThreadHolder.class);

    /**
     * 线程池
     */
    private ThreadPoolExecutor loopThreads;
    /**
     * 事件循环线程主体
     */
    private ABCheckMainLoop mainLoop;
    /**
     * 端口监听服务线程主体
     */
    private ABCheckSrvSockLoop srvSockLoop;
    /**
     * A/B机发送TCP包线程主体
     */
    private ABCheckSockLoop sockLoop;
    /**
     * 超时线程主体
     */
    private ABCheckTimeoutLoop timeoutLoop;

    /**
     * 特殊地，获取超时线程对象
     */
    private Thread timeoutThread;

    /**
     * 线程池前缀，直接用AB机连接信息
     */
    private final String threadPoolPrefix;

    /**
     * 传入的AB机信息
     */
    private final String[] hostports;
    /**
     * 本机地址
     */
    private String selfHost;
    /**
     * 本机端口
     */
    private int selfPort;
    /**
     * 另一台地址
     */
    private String otherHost;
    /**
     * 另一台端口
     */
    private int otherPort;

    /**
     * 命令执行器
     */
    private ABCommander commander;

    /**
     * 构造状态持有对象，内部使用，不可被直接使用
     *
     * @param hostports AB机信息，形如：new String[]{"127.0.0.1:22222", "127.0.0.1:22223"}
     */
    ABThreadHolder(String[] hostports) {
        this.threadPoolPrefix = Arrays.toString(hostports);
        this.hostports = hostports;
    }

    void setCommander(ABCommander commander) {
        this.commander = commander;
    }

    /**
     * 初始化
     */
    void init() {
        findSelfAndOther(hostports);
        loopThreads = new ThreadPoolExecutor(POOL_SIZE, POOL_SIZE,
                0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(POOL_QUEUE_CAPACITY),
                new SimpleThreadFactory(threadPoolPrefix),
                new ThreadPoolExecutor.AbortPolicy());
        mainLoop = new ABCheckMainLoop(commander);
        srvSockLoop = new ABCheckSrvSockLoop(commander, selfHost, selfPort);
        sockLoop = new ABCheckSockLoop(commander, selfHost + ":" + selfPort, otherHost, otherPort);
        timeoutLoop = new ABCheckTimeoutLoop(commander);
        loopThreads.submit(mainLoop);
        loopThreads.submit(srvSockLoop);
        loopThreads.submit(sockLoop);
        loopThreads.submit(timeoutLoop);
    }

    /**
     * 查找辨识本机信息及另一台信息
     *
     * @param hosts AB机信息，形如：new String[]{"127.0.0.1:22222", "127.0.0.1:22223"}
     */
    private void findSelfAndOther(String[] hosts) {
        boolean bindSuccess = false;
        boolean result;
        for (String hostPort : hosts) {
            result = find(hostPort, bindSuccess);
            bindSuccess = bindSuccess || result;
        }
        if (bindSuccess && selfHost != null && otherHost != null) {
            LOGGER.info("success to find self[{}:{}] and other[{}:{}]", selfHost, selfPort, otherHost, otherPort);
        } else {
            LOGGER.warn("try to find self and other failed[{}], stop the startup", Arrays.toString(hosts));
            throw new RuntimeException("find self and other failed");
        }
    }

    /**
     * 确认是否本节点信息，通过端口绑定来检查，第一个能绑定的信息认定为本节点。绑定过后立即关闭，防止影响真正绑定。
     *
     * @param hostPort 某一台机器信息，形如："127.0.0.1:22222"
     * @param found    已找到本节点
     * @return 是否找到本节点
     */
    private boolean find(String hostPort, boolean found) {
        String[] hostPortArray = hostPort.split(":");
        String checkHost = hostPortArray[0];
        int checkPort = Integer.parseInt(hostPortArray[1]);
        ServerSocket srvSock = null;
        if (found) {
            otherHost = checkHost;
            otherPort = checkPort;
            return false;
        } else {
            try {
                srvSock = new ServerSocket();
                srvSock.bind(new InetSocketAddress(checkHost, checkPort));
                // 可监听上，即可表明该节点信息可使用
                selfHost = checkHost;
                selfPort = checkPort;
                return true;
            } catch (IOException e) {
                // 触发异常，表明不可监听上，即可表明该节点信息不可使用
                otherHost = checkHost;
                otherPort = checkPort;
                LOGGER.warn("try to bind [{}]:[{}] failed, continue...", checkHost, checkPort);
                return false;
            } finally {
                if (srvSock != null) {
                    try {
                        //确保关闭，令后面为真正使用监听时不会出问题。
                        srvSock.close();
                    } catch (IOException e) {
                        LOGGER.warn(e.getMessage(), e);
                    }
                }
            }
        }
    }

    /**
     * 销毁
     */
    void destroy() {
        timeoutLoop.stop();
        sockLoop.stop();
        srvSockLoop.stop();
        mainLoop.stop();
        loopThreads.shutdownNow();
    }

    /**
     * 标记超时线程
     *
     * @param thread 超时线程
     */
    void markTimeoutThread(Thread thread) {
        this.timeoutThread = thread;
    }

    /**
     * 通过打断超时来重置超时
     */
    void resetTimeout() {
        if (timeoutThread != null) {
            timeoutThread.interrupt();
        }
    }

}
