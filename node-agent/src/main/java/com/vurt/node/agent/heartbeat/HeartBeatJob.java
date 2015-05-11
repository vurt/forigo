package com.vurt.node.agent.heartbeat;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.vurt.node.data.Constants;
import com.vurt.node.agent.util.ChannelHolder;
import com.vurt.node.agent.util.ConfigManager;
import com.vurt.node.data.HeartBeat;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * 节点心跳
 *
 * @author Vurt
 */
public class HeartBeatJob implements Job {
    private Channel channel;

    private Scheduler scheduler;

    private String id;

    public void stop() {
        Connection connection = channel.getConnection();
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
            channel.queueDeclare(Constants.MQ_CHANNEL_HEARTBEAT, false, false,
                    false, null);

            id = ConfigManager.getInstance().getConfig(Constants.NODE_ID);

            HeartBeat heartBeat = new HeartBeat(true, id);
            heartBeat.setAddress(ConfigManager.getInstance().getConfig(Constants.NODE_ADDRESS));
            heartBeat.setPosition(ConfigManager.getInstance().getConfig(Constants.NODE_POSITION));
            //启动时先发送一次心跳
            channel.basicPublish("", Constants.MQ_CHANNEL_HEARTBEAT, null, JSON.toJSONBytes(heartBeat));

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
            Channel channel = (Channel) ctx.getJobDetail().getJobDataMap().get("channel");
            channel.basicPublish("", Constants.MQ_CHANNEL_HEARTBEAT, null, JSON.toJSONBytes(new HeartBeat(id)));
        } catch (IOException e) {
        }
    }
}