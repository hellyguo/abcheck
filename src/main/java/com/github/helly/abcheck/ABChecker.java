package com.github.helly.abcheck;

/**
 * A/B机检查器，判断是否主机
 *
 * @author HellyGuo
 */
public final class ABChecker {

    private ABStateHolder stateHolder;
    private ABThreadHolder threadHolder;

    /**
     * 创建检查器
     *
     * @param hostPorts 形如："127.0.0.1:22222;127.0.0.1:22223"，西文分号切割
     */
    public ABChecker(String hostPorts) {
        this(hostPorts.split(";"));
    }


    /**
     * 创建检查器
     *
     * @param hostPorts 形如：new String[]{"127.0.0.1:22222", "127.0.0.1:22223"}
     */
    public ABChecker(String[] hostPorts) {
        stateHolder = new ABStateHolder();
        ABDataQueues dataQueues = new ABDataQueues();
        threadHolder = new ABThreadHolder(hostPorts);
        ABCommander commander = new ABCommander(stateHolder, dataQueues, threadHolder);
        threadHolder.setCommander(commander);
    }

    /**
     * 初始化
     */
    public void init() {
        threadHolder.init();
    }

    /**
     * 销毁
     */
    public void destroy() {
        threadHolder.destroy();
    }

    /**
     * 是否主机
     *
     * @return true:主机；false:备份
     */
    public boolean isMain() {
        return stateHolder.isMain();
    }

}
