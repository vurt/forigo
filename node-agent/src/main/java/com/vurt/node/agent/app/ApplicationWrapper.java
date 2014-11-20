package com.vurt.node.agent.app;

import java.io.File;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * 应用程序启动包装类
 * 
 * @author Vurt
 * 
 */
public class ApplicationWrapper extends Thread {

	private int port;

	private File appFile;

	private File resources;
	
	private String appContextPath;
	
	private String resourceContextPath;
	
	private Server server;

	/**
	 * 创建程序启动信息
	 * @param port 端口
	 * @param application 应用程序文件(war包或解压后的目录)
	 * @param appContextPath 应用上下文地址
	 * @param resources 资源目录
	 * @param resourceContextPath 资源上下文地址
	 */
	public ApplicationWrapper(int port, File application, String appContextPath, File resources,String resourceContextPath) {
		this.port = port;
		this.appFile = application;
		this.appContextPath = appContextPath;
		this.resources=resources;
		this.resourceContextPath=resourceContextPath;
	}

	@Override
	public void run() {
		this.server = new Server(port);

		WebAppContext webapp = new WebAppContext();
		webapp.setContextPath(appContextPath);
		if(appFile.isFile()){
			webapp.setWar(appFile.getAbsolutePath());
		}else{
			webapp.setResourceBase(appFile.getAbsolutePath());
		}

		ContextHandler resourceContext = new ContextHandler();
		resourceContext.setContextPath(resourceContextPath);
		resourceContext.setResourceBase(resources.getAbsolutePath());
		resourceContext.setHandler(new ResourceHandler());

		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { webapp, resourceContext });

		server.setHandler(handlers);
		try {
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(100);
		}
	}
	
	public void stopServer(){
		server.destroy();
	}
}
