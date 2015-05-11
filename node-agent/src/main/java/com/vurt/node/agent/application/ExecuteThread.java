package com.vurt.node.agent.application;

import org.apache.maven.cli.MavenCli;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.*;

/**
 * 应用程序启动包装类
 *
 * @author Vurt
 */
public class ExecuteThread extends Thread {

    private static volatile int instanceNum = 0;

    private File rootFolder;

    public ExecuteThread(String folderPath) {
        if (instanceNum > 0) {
            throw new RuntimeException("应用守护线程只允许被实例化一次");
        }
        this.rootFolder = new File(folderPath);
        if (!rootFolder.exists()) {
            rootFolder = null;
            throw new IllegalArgumentException("应用的根目录无效");
        }
        instanceNum++;
    }

    /**
     * 获取应用根目录
     *
     * @return
     */
    public File getApplicationFolder() {
        if (rootFolder != null) {
            return rootFolder;
        }
        throw new InitializeFailedException("应用进程还未实例化");
    }


    @Override
    public void run() {
        MavenCli cli = new MavenCli();

        MavenXpp3Reader reader = new MavenXpp3Reader();
        MavenXpp3Writer writer = new MavenXpp3Writer();
        try {
            File pom = new File("/Users/Vurt/Workspace/app-agent/pom.xml");
            Model model = reader.read(new FileInputStream(pom));
            System.out.println(model.toString());

            model.setVersion("1.0.1");

            writer.write(new FileOutputStream(pom), model);

            model = reader.read(new FileInputStream(pom));
            System.out.println(model.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }


        cli.doMain(new String[]{"clean", "install", "-U", "jetty:run", "--settings", "/Users/Vurt/Workspace/maven/apache-maven-3.2.2/conf/settings.xml"}, "/Users/Vurt/Workspace/app-agent", System.out, System.err);
    }


    public void stopApplication() {
        MavenCli cli = new MavenCli();
        cli.doMain(new String[]{"jetty:stop", "--settings", "/Users/Vurt/Workspace/maven/apache-maven-3.2.2/conf/settings.xml"}, "/Users/Vurt/Workspace/app-agent", null, null);
    }
}
