package com.github.helly.abcheck.sock;

/**
 * TCP包结构
 *
 * @author HellyGuo
 */
public interface ReqPackage {
    /**
     * 包类型
     *
     * @return {@link PackageType}
     */
    PackageType getPackType();

    /**
     * 数值化的包类型
     *
     * @return {@link PackageType#ordinal()}
     */
    int getType();

    /**
     * 版本
     *
     * @return 版本
     */
    long getVersion();

    /**
     * 设置版本
     *
     * @param version 新版本
     */
    void setVersion(long version);
}
