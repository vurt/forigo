package com.vurt.node.data;

import org.apache.maven.model.Model;

/**
 * 分组和应用分配命令
 */
public class Command {
    private String group;

    private Model application;

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
}
