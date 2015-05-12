package com.vurt.node.agent.application;

import com.vurt.node.agent.Launcher;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import javax.print.DocFlavor;
import java.io.*;

/**
 * Created by Vurt on 15/5/12.
 */
public class ApplicationOperator {
    private static MavenXpp3Reader reader = new MavenXpp3Reader();
    private static MavenXpp3Writer writer = new MavenXpp3Writer();

    private static File appFolder = new File(Launcher.AGENT_FOLDER_PATH + File.separator + Launcher.APPLICATION_FOLDER_NAME);

    public static Model getApplicationModel() {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File(appFolder, Launcher.POM_FILE_NAME));
            return reader.read(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return null;
    }

    public static void writeApplicationModel(Model model) {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(new File(appFolder, Launcher.POM_FILE_NAME));
            writer.write(outputStream, model);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
    }

    public static boolean equals(Model m1, Model m2) {
        return StringUtils.equals(m1.getVersion(), m2.getVersion())
                && StringUtils.equals(m1.getArtifactId(), m2.getArtifactId())
                && StringUtils.equals(m1.getGroupId(), m2.getGroupId());
    }
}
