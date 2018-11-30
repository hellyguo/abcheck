package com.github.helly.abcheck.sock;

/**
 * 抽象TCP包结构
 */
abstract class BaseReqPackage implements ReqPackage {
    protected PackageType packType;
    protected int type;
    protected long version;

    public BaseReqPackage(PackageType type) {
        this.packType = type;
        this.type = type.ordinal();
    }

    public BaseReqPackage(PackageType type, long version) {
        this.packType = type;
        this.type = type.ordinal();
        this.version = version;
    }

    @Override
    public PackageType getPackType() {
        return packType;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public long getVersion() {
        return version;
    }

    @Override
    public void setVersion(long version) {
        this.version = version;
    }
}
