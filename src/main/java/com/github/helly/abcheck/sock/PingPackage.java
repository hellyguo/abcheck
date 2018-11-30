package com.github.helly.abcheck.sock;

/**
 * 心跳包结构
 *
 * @author HellyGuo
 */
public class PingPackage extends BaseReqPackage {
    public PingPackage() {
        super(PackageType.PING);
    }

    public PingPackage(long version) {
        super(PackageType.PING, version);
    }
}
