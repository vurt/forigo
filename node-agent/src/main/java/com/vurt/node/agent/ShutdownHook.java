package com.vurt.node.agent;

import com.vurt.node.agent.application.AssembleThread;
import com.vurt.node.agent.heartbeat.HeartBeatJob;

public class ShutdownHook extends Thread {

    private AssembleThread appAssembleThread;

    private HeartBeatJob heartBeatJob;


    public ShutdownHook(AssembleThread assembleThread, HeartBeatJob heartBeatJob) {
        this.appAssembleThread = assembleThread;
        this.heartBeatJob = heartBeatJob;
    }


    @Override
    public void run() {
        if (appAssembleThread != null)
            appAssembleThread.stopAssembleThread();
        if (heartBeatJob != null)
            heartBeatJob.stop();
    }
}
