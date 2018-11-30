package com.github.helly.abcheck.thread;

import com.github.helly.abcheck.ABStateHolder;
import com.github.helly.abcheck.event.RecvPingEvent;
import com.github.helly.abcheck.event.RecvReqVoteEvent;
import com.github.helly.abcheck.event.RecvVoteEvent;
import com.github.helly.abcheck.sock.PackageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import static com.github.helly.abcheck.constant.ABCheckConstants.BUF_CAPACITY;
import static com.github.helly.abcheck.constant.ABCheckConstants.HOST_INFO_LEN;
import static com.github.helly.abcheck.constant.ABCheckConstants.MILLS_ONE_SEC;

/**
 * 端口监听线程主体
 */
public class ABCheckSrvSockLoop extends AbstractABCheckLoop implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ABCheckSrvSockLoop.class);

    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private ByteBuffer buffer;

    public ABCheckSrvSockLoop(ABStateHolder holder, String host, int port) {
        super(holder);
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(host, port));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            LOGGER.warn("open selector failed", e);
            throw new RuntimeException(e.getMessage(), e);
        }
        buffer = ByteBuffer.allocateDirect(BUF_CAPACITY);
    }

    @Override
    void loop() {
        try {
            int keys = selector.select(MILLS_ONE_SEC);
            if (keys > 0) {
                Set<SelectionKey> keySet = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keySet.iterator();
                SelectionKey key;
                while (iterator.hasNext()) {
                    key = iterator.next();
                    if (key.isAcceptable()) {
                        handleAccept(key);
                    } else if (key.isReadable()) {
                        handleRead(key);
                    }
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            LOGGER.warn(e.getMessage(), e);
        }
    }

    @Override
    String threadSubName() {
        return "srvSockLoop";
    }

    @Override
    public void stop() {
        close(selector);
        close(serverSocketChannel);
    }

    private void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            LOGGER.warn(e.getMessage(), e);
        }
    }

    /**
     * 接受监听
     *
     * @param key 选中的键
     */
    private void handleAccept(SelectionKey key) {
        ServerSocketChannel srvChannel = (ServerSocketChannel) key.channel();
        try {
            SocketChannel channel = srvChannel.accept();
            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            LOGGER.warn(e.getMessage(), e);
        }
    }

    /**
     * 读取数据
     *
     * @param key 选中的键
     */
    private void handleRead(SelectionKey key) {
        SocketChannel channel = (SocketChannel) key.channel();
        try {
            byte[] hostInfoBytes = new byte[HOST_INFO_LEN];
            buffer.clear();
            channel.read(buffer);
            buffer.flip();
            buffer.get(hostInfoBytes);
            String hostInfo = new String(hostInfoBytes).trim();
            int type = buffer.getInt();
            long version = buffer.getLong();
            handlePackage(hostInfo, type, version);
        } catch (IOException e) {
            LOGGER.warn(e.getMessage(), e);
        } finally {
            try {
                channel.close();
            } catch (IOException e) {
                LOGGER.warn(e.getMessage(), e);
            }
        }
    }

    /**
     * 解析包，转化为事件
     *
     * @param hostInfo 客户端的连接信息
     * @param type     包类型
     * @param version  版本
     */
    private void handlePackage(String hostInfo, int type, long version) {
        LOGGER.debug("[{}][{}][{}]", hostInfo, type, version);
        PackageType packageType = PackageType.values()[type];
        switch (packageType) {
            case PING: {
                // 心跳包
                holder.pushEvent(new RecvPingEvent());
                break;
            }
            case REQ_VOTE: {
                // 请求投票包
                holder.pushEvent(new RecvReqVoteEvent());
                break;
            }
            case VOTE: {
                // 投票结果包
                holder.pushEvent(new RecvVoteEvent(version > 0));
                break;
            }
            default: {
                break;
            }
        }
    }
}
