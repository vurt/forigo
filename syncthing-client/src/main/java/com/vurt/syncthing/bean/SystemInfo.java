package com.vurt.syncthing.bean;

public class SystemInfo {
	private String myID;
	
	private long alloc;
	
	private float cpuPercent;
	
	private boolean extAnnounceOK;
	
	private int goroutines;
	
	private long sys;

	/**
	 * 设备ID
	 */
	public String getMyID() {
		return myID;
	}

	public void setMyID(String myID) {
		this.myID = myID;
	}

	/**
	 * 已用内存
	 * @return
	 */
	public long getAlloc() {
		return alloc;
	}

	public void setAlloc(long alloc) {
		this.alloc = alloc;
	}
	
	/**
	 * 总内存
	 */
	public long getSys() {
		return sys;
	}

	public void setSys(long sys) {
		this.sys = sys;
	}

	/**
	 * CPU占用率
	 */
	public float getCpuPercent() {
		return cpuPercent;
	}

	public void setCpuPercent(float cpuPercent) {
		this.cpuPercent = cpuPercent;
	}

	/**
	 * 远程注册服务器状态(可以无视)
	 */
	public boolean isExtAnnounceOK() {
		return extAnnounceOK;
	}

	public void setExtAnnounceOK(boolean extAnnounceOK) {
		this.extAnnounceOK = extAnnounceOK;
	}

	public int getGoroutines() {
		return goroutines;
	}

	public void setGoroutines(int goroutines) {
		this.goroutines = goroutines;
	}
	
}
