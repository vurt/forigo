package com.vurt.node.agent;

import com.vurt.node.agent.app.ApplicationWrapper;
import com.vurt.node.agent.file.FileSyncThread;
import com.vurt.node.agent.heartbeat.HeartBeatJob;

public class ShutdownHook extends Thread {

	private FileSyncThread fileSyncThread;
	
	private ApplicationWrapper wrapper;
	
	private HeartBeatJob heartBeatJob;
	
	
	public ShutdownHook(FileSyncThread syncThread,ApplicationWrapper applicationWrapper,HeartBeatJob heartBeatJob){
		this.fileSyncThread=syncThread;
		this.wrapper=applicationWrapper;
		this.heartBeatJob=heartBeatJob;
	}
	
	
	@Override
	public void run() {
		fileSyncThread.stopProcess();
		wrapper.stopServer();
		heartBeatJob.stop();
	}
}
