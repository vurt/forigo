package com.vurt.node.agent.application;

import com.vurt.node.agent.Launcher;
import org.apache.maven.cli.MavenCli;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.*;

/**
 * 应用执行线程
 *
 * @author Vurt
 */
public class ExecuteThread extends Thread {
    private File appFolder;

    private String applicationPath;
    private String settingsFilePath;


    private static ExecuteThread instance;

    /**
     * 获取当前正在运行的应用线程，没有正在运行的应用则返回null
     */
    public static ExecuteThread getCurrThread() {
        return instance;
    }

    public ExecuteThread() {
        if (instance != null) {
            throw new RuntimeException("应用守护线程只允许被实例化一次");
        }
        this.appFolder = new File(Launcher.AGENT_FOLDER_PATH + File.separator + Launcher.APPLICATION_FOLDER_NAME);

        File settingFile = new File(Launcher.AGENT_FOLDER_PATH + File.separator + Launcher.CONFIG_FOLDER_NAME + File.separator + Launcher.MAVEN_SETTINGS_FILE_NAME);

        if (!appFolder.exists() || !settingFile.exists()) {
            appFolder = null;
            throw new IllegalArgumentException("settings.xml不存在或者应用根目录不存在");
        }

        this.applicationPath = appFolder.getAbsolutePath();
        this.settingsFilePath = settingFile.getAbsolutePath();

        instance = this;
    }

    /**
     * 获取应用根目录
     *
     * @return
     */
    public File getApplicationFolder() {
        if (appFolder != null) {
            return appFolder;
        }
        throw new InitializeFailedException("应用进程还未实例化");
    }


    @Override
    public void run() {
//        MavenXpp3Reader reader = new MavenXpp3Reader();
//        MavenXpp3Writer writer = new MavenXpp3Writer();

        File pom = new File(appFolder, Launcher.POM_FILE_NAME);
        if (!pom.exists()) {
            System.err.println("应用运行目录下没有读取到pom.xml");
            return;
        }


        MavenCli cli = new MavenCli();
        cli.doMain(new String[]{"clean", "install", "-U", "jetty:run", "--settings", settingsFilePath}, applicationPath, System.out, System.err);
    }


    public void stopApplication() {
        MavenCli cli = new MavenCli();
        cli.doMain(new String[]{"jetty:stop", "--settings", "/Users/Vurt/Workspace/maven/apache-maven-3.2.2/conf/settings.xml"}, "/Users/Vurt/Workspace/app-agent", null, null);
        instance = null;
    }
}
