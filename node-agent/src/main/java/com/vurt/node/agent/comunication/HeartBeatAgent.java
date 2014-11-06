package com.vurt.node.agent.comunication;

import java.io.IOException;

import javax.servlet.ServletContext;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinacreator.c2.web.init.ServerStartup;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * Created by Vurt on 14/11/4.
 */
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
	public void startup(ServletContext arg0) {
		try {
            this.channel = ChannelHolder.createChannel();
            channel.queueDeclare(Constants.CHANNEL_HEARTBEAT, false, false, false, null);
			channel.basicPublish("", Constants.CHANNEL_HEARTBEAT, null, "{'firstHearBeat':true,'id':'1'}".getBytes());
			LOGGER.info("发送了心跳信息");
			
//			FileSystemManager fsManager = VFS.getManager();
//			FileObject fileObject=fsManager.resolveFile("http://");
//			fileObject.getContent().getInputStream();
        } catch (IOException e) {
        }		
	}
}

class HearBeat{
	private String id;
	
	private String group;
	
	private String position;
	
	private String address;
	
	public HearBeat(){
	}
	
	public HearBeat(String id){
		this.id=id;
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
}
