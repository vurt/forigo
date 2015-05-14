package com.vurt.node.server.comunication;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.chinacreator.c2.dao.Dao;
import com.chinacreator.c2.dao.DaoFactory;
import com.chinacreator.c2.fs.FileServer;
import com.chinacreator.c2.ioc.ApplicationContextManager;
import com.rabbitmq.client.Channel;
import com.vurt.node.data.Command;
import com.vurt.node.data.Constants;
import com.vurt.node.server.bean.ApplicationVersion;
import com.vurt.node.server.bean.Group;
import com.vurt.node.server.bean.Node;
import com.vurt.node.server.exception.InvalidApplicationVersionException;

public class CommandService {
	private static final Logger LOGGER=LoggerFactory.getLogger(CommandService.class);
	
	private static CommandService instance;
	
	private Channel channel;
	
	private FileServer fileServer;
	
	private boolean valid;
	
	public static CommandService getInstance(){
		if(instance==null || !instance.isValid()){
			//无效的话要最好要有retry
			instance=new CommandService();
		}
		return instance;
	}

	private CommandService(){
		try {
			channel = ChannelHolder.createChannel();
			channel.exchangeDeclare(Constants.MQ_EXCHANGE_APPLICATION, "topic");
			
			fileServer = ApplicationContextManager.getContext().getBean("dirFileServer", FileServer.class);
			valid = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isValid(){
		return valid;
	}
	
	public void destory() throws IOException{
		channel.close();
	}

	/**
	 * 初始化节点的命令队列，包括分配分组，同时发送该分组当前的应用版本信息
	 * 
	 * @param node
	 * @throws Exception
	 *             读取应用pom.xml和发送消息时发生了错误
	 */
	public void initNodeConnection(Node node) throws Exception {
		String routeKey = "group." + Constants.NONE_GROUP_NAME + "." + node.getId();

		Command command = createNodeUpdateCommand(node);

		channel.basicPublish(Constants.MQ_EXCHANGE_APPLICATION, routeKey, null,
				JSON.toJSONBytes(command));
	}
	
	/**
	 * 修改节点的分组
	 * 
	 * @param nodes 要修改的节点
	 * @param group 新分组
	 * @throws Exception 读取应用pom.xml和发送消息时发生了错误
	 */
	public void changeNodeGroup(Node[] nodes,String group) throws Exception{
		if(nodes==null||nodes.length==0||StringUtils.isEmpty(group))return;
		Dao<Node> dao=DaoFactory.create(Node.class);
		Map<String,Command> commands=new HashMap<String, Command>();
		for(Node node:nodes){
			if(StringUtils.isEmpty(node.getId())){
				LOGGER.warn("要修改分组的节点没有id，忽略");
				continue;
			}
			Node curr=dao.selectByID(node.getId());
			String currGroup = curr.getGroup();
			if(StringUtils.equals(currGroup,group)){
				continue;
			}
			curr.setGroup(group);
			
			if(StringUtils.isEmpty(currGroup)){
				currGroup=Constants.NONE_GROUP_NAME;
			}
			String routeKey= "group." + currGroup + "."	+ node.getId();
			
			Command command=createNodeUpdateCommand(curr);
			
			//节点id不同，routeKey必然不同
			commands.put(routeKey, command);
			
			dao.update(curr);
		}
		
		for(Entry<String, Command> entry:commands.entrySet()){
			//应该不至于出现部分发送成功的情况
			channel.basicPublish(Constants.MQ_EXCHANGE_APPLICATION, entry.getKey(), null,JSON.toJSONBytes(entry.getValue()));
		}
	}
	
	/**
	 * 修改分组上部署的应用的版本
	 * @param groups 分组id
	 * @param version 应用版本id，{@link ApplicationVersion#getId()}属性
	 * @throws IOException 
	 */
	public void changeAppVersion(String[] groups,int version) throws IOException{
		if(groups==null||groups.length==0){
			return ;
		}
		
		Map<String,Command> commands=new HashMap<String, Command>();
		for(String group:groups){
			Dao<Group> dao=DaoFactory.create(Group.class);
			Group targetGroup=dao.selectByID(group);
			if(targetGroup==null){
				LOGGER.warn("修改应用版本请求的节点分组："+group+"不存在，忽略请求!");
				continue;
			}
			if(targetGroup.getVersion().getId()==version){
				LOGGER.debug("请求的节点分组："+group+"当前应用版本与请求的版本一致，忽略请求!");
				continue;
			}
			
			Dao<ApplicationVersion> versionDao=DaoFactory.create(ApplicationVersion.class);
			ApplicationVersion targetVersion=versionDao.selectByID(version);
			if(targetVersion==null){
				LOGGER.warn("请求修改的目标版本："+version+"不存在，忽略请求!");
				continue;
			}
			
			targetGroup.setVersion(targetVersion);
			dao.update(targetGroup);
			
			Command command=new Command();
			InputStream inputStream;
			try {
				inputStream = fileServer.get(targetVersion.getPom());
				
				MavenXpp3Reader reader = new MavenXpp3Reader();
				Model model = reader.read(inputStream);
				command.setApplication(model);
			} catch (Exception e) {
				throw new InvalidApplicationVersionException("应用版本["+version+"]对应的pom.xml文件读取失败，原因："+e.getMessage(), e);
			}
			
			String routeKey="group."+group+".*";
			commands.put(routeKey, command);
		}
		
		for(Entry<String, Command> entry:commands.entrySet()){
			//应该不至于出现部分发送成功的情况
			channel.basicPublish(Constants.MQ_EXCHANGE_APPLICATION, entry.getKey(), null,JSON.toJSONBytes(entry.getValue()));
		}
	}

	/**
	 * 创建节点状态更新命令，包括节点分组信息和节点应用信息
	 * @param node 要更新的节点
	 * @throws Exception
	 */
	private Command createNodeUpdateCommand(Node node) throws Exception {
		Command command = new Command();
		// 将节点通讯渠道分配到指定分组
		command.setGroup(node.getGroup());

		// 节点上当前的应用版本
		String currVersion = node.getAppVersion();

		Dao<Group> groupDao = DaoFactory.create(Group.class);
		Group condition = new Group();
		condition.setName(node.getGroup());
		Group realGroup = groupDao.selectOne(condition);
		String realVersion = realGroup.getVersion().getVersion();

		if (!StringUtils.equals(realVersion, currVersion)) {
			InputStream inputStream = fileServer.get(realGroup.getVersion().getPom());

			MavenXpp3Reader reader = new MavenXpp3Reader();
			Model model = reader.read(inputStream);

			command.setApplication(model);
		}
		return command;
	}
}
