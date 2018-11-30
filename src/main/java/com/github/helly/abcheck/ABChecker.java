package com.github.helly.abcheck;

/**
 * A/B机检查器，判断是否主机
 *
 * @author HellyGuo
 */
public final class ABChecker {

    private ABStateHolder holder;

    /**
     * 创建检查器
     *
     * @param hostports 形如："127.0.0.1:22222;127.0.0.1:22223"，西文分号切割
     */
    public ABChecker(String hostports) {
        this(hostports.split(";"));
    }


    /**
     * 创建检查器
     *
     * @param hostports 形如：new String[]{"127.0.0.1:22222", "127.0.0.1:22223"}
     */
    public ABChecker(String[] hostports) {
        holder = new ABStateHolder(hostports);
    }

    /**
     * 初始化
     */
    public void init() {
        holder.init();
    }

    /**
     * 销毁
     */
    public void destroy() {
        holder.destroy();
    }

    /**
     * 是否主机
     *
     * @return true:主机；false:备份
     */
    public boolean isMain() {
        return holder.isMain();
    }

}
