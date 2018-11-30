package com.github.helly.abcheck.thread;

import com.github.helly.abcheck.ABStateHolder;
import com.github.helly.abcheck.sock.ReqPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static com.github.helly.abcheck.constant.ABCheckConstants.BUF_CAPACITY;
import static com.github.helly.abcheck.constant.ABCheckConstants.HOST_INFO_LEN;

/**
 * 客户端请求线程主体
 *
 * @author HellyGuo
 */
public class ABCheckSockLoop extends AbstractABCheckLoop implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ABCheckSockLoop.class);

    /**
     * 本机信息
     */
    private byte[] self;
    /**
     * 另一节点信息
     */
    private InetSocketAddress otherHostPort;
    private ByteBuffer buffer;

    public ABCheckSockLoop(ABStateHolder holder, String self, String other, int port) {
        super(holder);
        this.self = new byte[HOST_INFO_LEN];
        byte[] bytes = self.getBytes();
        System.arraycopy(bytes, 0, this.self, 0, bytes.length);
        this.otherHostPort = new InetSocketAddress(other, port);
        this.buffer = ByteBuffer.allocateDirect(BUF_CAPACITY);
    }

    @Override
    void loop() {
        ReqPackage reqPackage = holder.fetchReqPackage();
        if (reqPackage != null) {
            // 读取到TCP包，以短连接模式发送出
            sendToOther(reqPackage);
        }
    }

    /**
     * 以短连接发送TCP包
     *
     * @param reqPackage TCP包
     */
    private void sendToOther(ReqPackage reqPackage) {
        try {
            SocketChannel channel = SocketChannel.open();
            channel.connect(otherHostPort);
            buffer.clear();
            buffer.put(self);
            buffer.putInt(reqPackage.getType());
            buffer.putLong(reqPackage.getVersion());
            buffer.flip();
            channel.write(buffer);
        } catch (ConnectException e) {
            LOGGER.warn("connect to other[{}] failed", otherHostPort);
        } catch (IOException e) {
            LOGGER.warn(e.getMessage(), e);
        }
    }

    @Override
    String threadSubName() {
        return "sockLoop";
    }
}
