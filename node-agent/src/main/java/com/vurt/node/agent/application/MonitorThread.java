package com.vurt.node.agent.application;

/**
 * 监控线程
 */
public class MonitorThread extends Thread {

    public MonitorThread() {
        this.setDaemon(true);
    }
}
