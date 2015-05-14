package com.vurt.node.data;

import java.util.Map;

import org.apache.maven.model.Model;
import org.apache.maven.settings.Settings;

/**
 * 节点命令，支持远程修改包括分组、应用版本、Maven Settings和node.properties
 */
public class Command {
    private String group;

    private Model application;
    
    private Settings settings;
    
    private Map<String, String> properties;
    
    public Command() {

    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Model getApplication() {
        return application;
    }

    public void setApplication(Model application) {
        this.application = application;
    }

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
}
