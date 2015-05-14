package com.vurt.node.agent;

import com.vurt.node.agent.command.CommandThread;
import com.vurt.node.agent.heartbeat.HeartBeatJob;

public class ShutdownHook extends Thread {

    private CommandThread appAssembleThread;

    private HeartBeatJob heartBeatJob;


    public ShutdownHook(CommandThread assembleThread, HeartBeatJob heartBeatJob) {
        this.appAssembleThread = assembleThread;
        this.heartBeatJob = heartBeatJob;
    }


    @Override
    public void run() {
        System.out.println("开始关闭节点代理");
        if (appAssembleThread != null)
            appAssembleThread.stopThread();
        if (heartBeatJob != null)
            heartBeatJob.stop();
    }
}
