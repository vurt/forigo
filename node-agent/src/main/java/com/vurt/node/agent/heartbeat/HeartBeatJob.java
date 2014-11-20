package com.vurt.node.agent.heartbeat;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.IOException;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.vurt.node.agent.NodeAgent;
import com.vurt.node.agent.comunication.ChannelHolder;
import com.vurt.node.agent.comunication.Constants;
import com.vurt.syncthing.SyncthingClient;
import com.vurt.syncthing.bean.SystemInfo;

/**
 * 节点心跳
 * @author Vurt
 *
 */
public class HeartBeatJob implements Job{
    private Channel channel;

    private Scheduler scheduler ;

	public void stop() {
		Connection connection=channel.getConnection();
		try {
			channel.close();
			connection.close();
			scheduler.shutdown();
		} catch (IOException e) {
			System.err.println("关闭服务器清理Channel时发生错误");
			e.fillInStackTrace();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	public void startup() {
		try {
			this.channel = ChannelHolder.createChannel();
	        channel.queueDeclare(Constants.CHANNEL_HEARTBEAT, false, false,
						false, null);
	            
			SyncthingClient.getInstance().waitForValid();
			
			SystemInfo info=SyncthingClient.getInstance().getSystemInfo();
            
            HeartBeat heartBeat=new HeartBeat(true,NodeAgent.getNodeId());
            heartBeat.setDeviceId(info.getMyID());
            heartBeat.setAddress(NodeAgent.getAddress());
            heartBeat.setGroup(NodeAgent.getGroup());
            heartBeat.setPosition(NodeAgent.getPosition());
            //启动时先发送一次心跳
			channel.basicPublish("", Constants.CHANNEL_HEARTBEAT, null, JSON.toJSONBytes(heartBeat));
			
			scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.start();
			
		    JobDetail job = newJob(HeartBeatJob.class)
		        .withIdentity("heartBeatJob", "defaultGroup")
		        .build();
		    job.getJobDataMap().put("channel", channel);

		    Trigger trigger = newTrigger()
		        .withIdentity("heartBeatTrigger", "defaultGroup")
		        .startNow()
		        .withSchedule(simpleSchedule().withIntervalInMinutes(1)
		                .repeatForever())            
		        .build();

		    scheduler.scheduleJob(job, trigger);
        } catch (IOException e) {
        } catch (SchedulerException e) {
			e.printStackTrace();
		}		
	}
	
	@Override
	public void execute(JobExecutionContext ctx) throws JobExecutionException {
		try {
			Channel channel=(Channel)ctx.getJobDetail().getJobDataMap().get("channel");
			channel.basicPublish("", Constants.CHANNEL_HEARTBEAT, null, JSON.toJSONBytes(new HeartBeat(NodeAgent.getNodeId())));
		} catch (IOException e) {
		}
	}
}

class HeartBeat{
	private boolean firstHearBeat;
	
	private String id;
	
	private String deviceId;
	
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
	
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
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
