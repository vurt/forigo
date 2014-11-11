package com.vurt.node.server.file;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.vurt.node.server.comunication.ChannelHolder;
import com.vurt.node.server.comunication.Constants;
import com.vurt.node.server.comunication.data.FilePullRequest;
import com.vurt.node.server.exception.GroupNotValidException;
import com.vurt.node.server.service.NodeService;

/**
 * 文件分发工具
 * 
 * @author Vurt
 */
public class FileDistributor {
	private static FileDistributor instance;

	private static Object lock = new Object();

	public static final FileDistributor getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new FileDistributor();
				}
			}
		}
		return instance;
	}
	
	private Channel channel;
	
	private FileDistributor(){
		try {
			channel=ChannelHolder.createChannel();
			channel.exchangeDeclare(Constants.EXCHANGE_FILE, "direct",true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 向指定分组的节点推送文件获取请求，如果推送的是目录，那么节点端会遍历目录下的所有文件，在节点上不存或者有修改的文件全部都会被同步过去
	 * 
	 * @param group 节点id，还支持{@link Constants#EMPTY_GROUP_SEVERITY}和{@link Constants#ALL_GROUP_SEVERITY}
	 * @param request 请求详情
	 * @throws IOException
	 */
	public void broadcastFilePullRequest(String group, FilePullRequest request) throws IOException{
		if(StringUtils.isEmpty(group)){
			//未指定分组即为发送到所有分组
			group=Constants.ALL_GROUP_SEVERITY;
		}
		byte[] requestContent=JSON.toJSONBytes(request);
		if(StringUtils.equals(Constants.ALL_GROUP_SEVERITY, group)){
			channel.basicPublish(Constants.EXCHANGE_FILE, Constants.EMPTY_GROUP_SEVERITY, null,requestContent);
			for(String target:new NodeService().getGroups().keySet()){
				channel.basicPublish(Constants.EXCHANGE_FILE, target, null,requestContent);
			}
		}else{
			if(!new NodeService().getGroups().containsKey(group)){
				throw new GroupNotValidException("分组："+group+"不存在");
			}
			channel.basicPublish(Constants.EXCHANGE_FILE, group, null, requestContent);
		}
	}
}
