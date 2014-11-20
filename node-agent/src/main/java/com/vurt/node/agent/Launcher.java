package com.vurt.node.agent;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;

import com.vurt.node.agent.app.ApplicationWrapper;
import com.vurt.node.agent.exception.ApplicationFileNotExistException;
import com.vurt.node.agent.file.FileSyncThread;
import com.vurt.node.agent.heartbeat.HeartBeatJob;
import com.vurt.node.agent.util.ConfigManager;
import com.vurt.node.agent.util.JarUtil;

public class Launcher {
	public static void main(String[] args) {
		JarUtil util=new JarUtil(NodeAgent.class);
		File propertiesFile=new File(util.getJarPath()+"/config.properties");
		if(!propertiesFile.exists()){
			System.err.println("未找到全局配置文件config.properties");
			System.exit(0);
		}
		
		ConfigManager.init(propertiesFile);
		NodeAgent.init();
		
		
		//启动文件同步进程
		FileSyncThread fileSyncThread=new FileSyncThread(ConfigManager.getInstance().getConfig("sync.home"));
		fileSyncThread.start();
		
		//启动心跳
		HeartBeatJob heartBeatJob=new HeartBeatJob();
		heartBeatJob.startup();
		
		//启动应用
		//启动的应用应该还是同步前的
		ApplicationWrapper wrapper=wrapApplication();
		wrapper.start();
		
		ShutdownHook hook=new ShutdownHook(fileSyncThread,wrapper,heartBeatJob);
		Runtime.getRuntime().addShutdownHook(hook);
	}
	
	
	private static ApplicationWrapper wrapApplication(){
		String syncFolder=ConfigManager.getInstance().getConfig("sync.home");

		File appFolder=new File(syncFolder+"/app");
		
		File resFolder=new File(syncFolder+"/res");
		
		if(!appFolder.exists()||!resFolder.exists()||appFolder.list().length==0){
			System.err.println("应用目录或资源目录不存在");
			throw new ApplicationFileNotExistException("应用目录或资源目录不存在");
		}
		
		List<File> files=Arrays.asList(appFolder.listFiles());
		Collections.sort(files, new VersionComparator());
		
		File appFile=files.get(0);
		
		return new ApplicationWrapper(80, appFile,"/node", resFolder, "/img");
	}
}

class VersionComparator implements Comparator<File>{
	@Override
	public int compare(File o1, File o2) {
		return o2.getName().compareTo(o1.getName());
	}
}
