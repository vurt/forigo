package com.vurt.node.agent.file;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;

public class FileSyncThread extends Thread {

	private String syncHomeFolder;
	private Process process;
	

	public FileSyncThread(String appRoot) {
		syncHomeFolder = appRoot + "/sync";
	}
	
	
	@Override
	public void run() {
		Runtime rn = Runtime.getRuntime();
		StringBuilder builder = new StringBuilder();
		String command = builder.append(syncHomeFolder)
				.append("/syncthing.exe").append(" ").append("-home=\"")
				.append(syncHomeFolder).append("\"").toString();
		try {
			process = rn.exec(command);

			BufferedReader br = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			String s = br.readLine();
			while (null != s) {
				if (StringUtils.isNotEmpty(s.trim()))
				System.out.println(s);
				s = br.readLine();
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
