package com.vurt.node.data;

import org.apache.maven.model.Model;

public class HeartBeat {
    private boolean firstHeartBeat;

    /**
     * 节点id
     */
    private String id;

    /**
     * 经纬度
     */
    private String position;

    /**
     * 节点地址
     */
    private String address;

    private String application;

    public HeartBeat() {

    }

    public HeartBeat(String id) {
        this.id = id;
    }

    public HeartBeat(boolean firstHeartBeat, String id) {
        this.id = id;
        this.firstHeartBeat = firstHeartBeat;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public boolean isFirstHeartBeat() {
        return firstHeartBeat;
    }

    public void setFirstHeartBeat(boolean firstHeartBeat) {
        this.firstHeartBeat = firstHeartBeat;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }
}