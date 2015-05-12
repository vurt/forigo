package com.vurt.node.agent;

import com.vurt.node.agent.command.CommandThread;
import com.vurt.node.agent.heartbeat.HeartBeatJob;
import com.vurt.node.agent.util.ConfigManager;
import com.vurt.node.agent.util.JarUtil;

import java.io.File;

public class Launcher {
    /**
     * 节点代理运行目录
     */
    public static String AGENT_FOLDER_PATH = "";

    public static final String CONFIG_FOLDER_NAME = "conf";

    public static final String APPLICATION_FOLDER_NAME = "work";

    public static final String POM_FILE_NAME = "pom.xml";

    public static final String MAVEN_SETTINGS_FILE_NAME = "settings.xml";

    public static final String NODE_SETTINGS_FILE_NAME = "node.properties";


    public static void main(String[] args) {
        if (args == null && args.length > 0) {
            AGENT_FOLDER_PATH = args[0];
        } else {
            JarUtil util = new JarUtil(Launcher.class);
            System.out.println(util.getJarPath());
            AGENT_FOLDER_PATH = new File(util.getJarPath()).getParentFile().getAbsolutePath();
        }

        File propertiesFile = new File(AGENT_FOLDER_PATH + File.separator + CONFIG_FOLDER_NAME + File.separator + NODE_SETTINGS_FILE_NAME);

        if (!propertiesFile.exists()) {
            System.err.println("未找到节点配置文件" + NODE_SETTINGS_FILE_NAME);
            System.exit(0);
        }

        ConfigManager.init(propertiesFile);

        //启动心跳
        HeartBeatJob heartBeatJob = new HeartBeatJob();
        heartBeatJob.startup();

        CommandThread assembleThread = new CommandThread();
        assembleThread.start();

        ShutdownHook hook = new ShutdownHook(assembleThread, heartBeatJob);
        Runtime.getRuntime().addShutdownHook(hook);
    }

}