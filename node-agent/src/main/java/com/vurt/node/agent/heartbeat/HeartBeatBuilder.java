package com.vurt.node.agent.heartbeat;

import com.vurt.node.agent.Launcher;
import com.vurt.node.agent.util.ConfigManager;
import com.vurt.node.agent.util.JarUtil;
import com.vurt.node.data.Constants;
import com.vurt.node.data.HeartBeat;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class HeartBeatBuilder {
    private HeartBeat heartBeat;

    public HeartBeatBuilder() {
        this.heartBeat = new HeartBeat();
        this.heartBeat.setId(ConfigManager.getInstance().getConfig(Constants.NODE_ID));
    }

    public HeartBeatBuilder firstHeartBeat() {
        this.heartBeat.setFirstHeartBeat(true);
        this.heartBeat.setPosition(ConfigManager.getInstance().getConfig(Constants.NODE_POSITION));
        this.heartBeat.setAddress(ConfigManager.getInstance().getConfig(Constants.NODE_ADDRESS));
        return this;
    }

    public HeartBeatBuilder appendApplicationInfo() {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        //classpath根目录，运行时应该对应到/lib目录
        File classpathFolder = new File(new JarUtil(Launcher.class).getJarPath());

        File appPom = new File(classpathFolder.getParentFile(), "app/pom.xml");

        if (appPom.exists()) {
            try {
                FileInputStream inputStream = new FileInputStream(appPom);
                Model model = reader.read(inputStream);
                this.heartBeat.setApplication(model.getVersion());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return this;
    }

    public HeartBeat build() {
        return this.heartBeat;
    }
}
