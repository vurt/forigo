package com.vurt.node.data;

import org.apache.maven.model.Model;

import java.util.Date;

public class ApplicationUpdateCommand {
    /**
     * 命令创建时间
     */
    private Date timestamp;

    /**
     * 应用更新生效时间
     */
    private Date updateTime;

    /**
     * 新应用的信息
     */
    private Model application;

    public ApplicationUpdateCommand() {

    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Model getApplication() {
        return application;
    }

    public void setApplication(Model application) {
        this.application = application;
    }
}
