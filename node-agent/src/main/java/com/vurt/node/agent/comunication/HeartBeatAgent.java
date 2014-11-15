package com.vurt.node.agent.comunication;

import java.io.IOException;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import com.alibaba.fastjson.JSON;
import com.chinacreator.c2.web.init.ServerStartup;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.vurt.node.agent.NodeAgent;
import com.vurt.node.agent.file.FileSyncThread;
import com.vurt.syncthing.SyncthingClient;
import com.vurt.syncthing.bean.SyncthingConfig;

public class HeartBeatAgent implements ServerStartup{
	private static final Logger LOGGER=LoggerFactory.getLogger(HeartBeatAgent.class);
    private Channel channel;

	@Override
	public void close() {
		Connection connection=channel.getConnection();
		try {
			channel.close();
			connection.close();
		} catch (IOException e) {
			LOGGER.warn("关闭服务器清理Channel时发生错误",e);
		}
	}

	@Override
	public void startup(ServletContext ctx) {
		try {
		    Thread fileSyncThread=new FileSyncThread(ctx.getRealPath("/"));
		    fileSyncThread.start();
			NodeAgent.init();
			
			SyncthingClient  client=new SyncthingClient("http://127.0.0.1:8765","vph619gjv08qng1qmh44omsc6j2g1g");
			client.waitForValid();
			SyncthingConfig config=client.getConfig();
//			Folder folder=new Folder();
//			folder.setPath("D:/test");
//			folder.setId("myTest");
//			config.getFolders().add(folder);
//			client.setConfig(config);
			
            this.channel = ChannelHolder.createChannel();
            channel.queueDeclare(Constants.CHANNEL_HEARTBEAT, false, false,
					false, null);
            
            HeartBeat heartBeat=new HeartBeat(true,NodeAgent.getNodeId());
            heartBeat.setAddress(NodeAgent.getAddress());
            heartBeat.setGroup(NodeAgent.getGroup());
            heartBeat.setPosition(NodeAgent.getPosition());
            //启动时先发送一次心跳
			channel.basicPublish("", Constants.CHANNEL_HEARTBEAT, null, JSON.toJSONBytes(heartBeat));
			
			new FilePullRequestListener().start();
        } catch (IOException e) {
        }		
	}
	
	/**
	 * 每分钟一次心跳
	 */
	@Scheduled(cron="1 * *  * * * ")
	public void SendHeartBeat(){
		LOGGER.debug("Heart Beat!");
		try {
			channel.basicPublish("", Constants.CHANNEL_HEARTBEAT, null, JSON.toJSONBytes(new HeartBeat(NodeAgent.getNodeId())));
		} catch (IOException e) {
		}
	}
}

class HeartBeat{
	private boolean firstHearBeat;
	
	private String id;
	
	private String group;
	
	private String position;
	
	private String address;
	
	public HeartBeat(){
	}
	
	public HeartBeat(String id){
		this.id=id;
	}
	
	public HeartBeat(boolean firstHearBeat,String id){
		this.id=id;
		this.firstHearBeat=firstHearBeat;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public boolean isFirstHearBeat() {
		return firstHearBeat;
	}
	
	public void setFirstHearBeat(boolean firstHearBeat) {
		this.firstHearBeat = firstHearBeat;
	}
}
