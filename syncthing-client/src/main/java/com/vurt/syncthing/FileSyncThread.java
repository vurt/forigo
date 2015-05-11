package com.vurt.syncthing;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class FileSyncThread extends Thread {

	private String syncRuntimePath;
	
	private Process process;
	
	private boolean keepRunning=true;
	

	public FileSyncThread(String synchome) {
		syncRuntimePath = synchome+"/runtime";
	}
	
	@Override
	public void run() {
		//同步服务已启动，则无需再次启动
		if(SyncthingClient.getInstance().isValid())return;
		
		Runtime rn = Runtime.getRuntime();
		StringBuilder builder = new StringBuilder();
		String command = builder.append(syncRuntimePath)
				.append("/syncthing.exe").append(" ").append("-home=\"")
				.append(syncRuntimePath).append("\"").toString();
		try {
			process = rn.exec(command);

			BufferedReader br = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			String s = br.readLine();
			while (null != s && keepRunning) {
				if (StringUtils.isNotEmpty(s.trim()))
				System.out.println(s);
				s = br.readLine();
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭进程
	 */
	public void stopProcess(){
		//停止当前线程
		keepRunning=false;
		//关闭syncthing进程
		process.destroy();
	}
	
	
}
