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

            HeartBeat heartBeat = new HeartBeatBuilder().firstHeartBeat().appendApplicationInfo().build();
            //启动时先发送一次心跳
            channel.basicPublish("", Constants.MQ_CHANNEL_HEARTBEAT, null, JSON.toJSONBytes(heartBeat));
            System.out.println("发送心跳信息：" + JSON.toJSON(heartBeat));


            scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();

            JobDetail job = newJob(HeartBeatJob.class)
                    .withIdentity("heartBeatJob", "defaultGroup")
                    .build();
            job.getJobDataMap().put("channel", channel);

            Trigger trigger = newTrigger()
                    .withIdentity("heartBeatTrigger", "defaultGroup")
                    .withSchedule(simpleSchedule().withIntervalInMinutes(1).repeatForever())
                    .startNow().build();

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
            HeartBeat heartBeat = new HeartBeatBuilder().appendApplicationInfo().build();
            channel.basicPublish("", Constants.MQ_CHANNEL_HEARTBEAT, null, JSON.toJSONBytes(heartBeat));
        } catch (IOException e) {
        }
    }
}