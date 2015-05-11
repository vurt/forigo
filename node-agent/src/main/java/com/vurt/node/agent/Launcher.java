package com.vurt.node.agent;

import com.vurt.node.agent.application.AssembleThread;
import com.vurt.node.agent.heartbeat.HeartBeatJob;
import com.vurt.node.agent.util.ConfigManager;
import com.vurt.node.agent.util.JarUtil;

import java.io.File;

public class Launcher {
    public static void main(String[] args) {
        JarUtil util = new JarUtil(Launcher.class);

        System.out.println(util.getJarPath());

        File propertiesFile = new File(util.getJarPath() + "/config.properties");
        if (!propertiesFile.exists()) {
            System.err.println("未找到全局配置文件config.properties");
            System.exit(0);
        }

        ConfigManager.init(propertiesFile);

        //启动心跳
        HeartBeatJob heartBeatJob = new HeartBeatJob();
        heartBeatJob.startup();

        AssembleThread assembleThread = new AssembleThread();
        assembleThread.start();

        ShutdownHook hook = new ShutdownHook(assembleThread, heartBeatJob);
        Runtime.getRuntime().addShutdownHook(hook);
    }

}