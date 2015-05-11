package com.vurt.node.data;

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
}