package com.vurt.node.server.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.chinacreator.c2.dao.Dao;
import com.chinacreator.c2.dao.DaoFactory;
import com.vurt.node.data.Constants;
import com.vurt.node.server.bean.Group;
import com.vurt.node.server.bean.Node;
import com.vurt.node.server.comunication.CommandService;
import com.vurt.node.server.exception.CommandSentFailed;
import com.vurt.node.server.exception.InvalidNodeException;

public class NodeService {
	private Dao<Node> dao;

	public NodeService() {
		dao = DaoFactory.create(Node.class);
	}

	public Map<String, String> getGroupVersions() {
		Dao<Group> groupDao=DaoFactory.create(Group.class);
		List<Group> groups=groupDao.selectAll();
		
		Map<String, String> groupsVersions=new HashMap<String, String>();
		for(Group group:groups){
			groupsVersions.put(group.getName(), group.getVersion().getVersion());
		}
		
		return groupsVersions;
	}
	
	public void updateNodeLocation(int nodeid,String position,String address){
		if(StringUtils.isEmpty(position)&&StringUtils.isEmpty(address)){
			return;
		}
		Dao<Node> dao=DaoFactory.create(Node.class);
		Node node=dao.selectByID(nodeid);
		if(node==null){
			throw new InvalidNodeException("节点["+nodeid+"]不存在!");
		}
		
		Map<String, String> properties=new HashMap<String, String>();
		if(StringUtils.isNotEmpty(position)){
			node.setPosition(position);
			properties.put(Constants.NODE_POSITION, position);
		}
		if(StringUtils.isNotEmpty(address)){
			node.setAddress(address);
			properties.put(Constants.NODE_ADDRESS, address);
		}
		
		try {
			dao.update(node);
			CommandService.getInstance().changeNodeProperties(nodeid,properties);
		} catch (IOException e) {
			throw new CommandSentFailed("属性更新请求发送失败，服务器的终端数据已更新，终端上的数据将在终端下次连接时同步；您也可以现在多重试几次");
		}
	}

	public Node getNode(String id) {
		Node node = new Node();
		node.setId(id);
		return dao.selectOne(node);
	}

	/**
	 * 新增一个节点，会在插入前设置一些时间相关的数据
	 * 
	 * @param node
	 */
	public void addNode(Node node) {
		Timestamp curr = new Timestamp(System.currentTimeMillis());
		node.setLastHeartBeat(curr);
		node.setRegTime(curr);
		dao.insert(node);
	}

	public void updateNode(Node node) {
		Timestamp curr = new Timestamp(System.currentTimeMillis());
		node.setLastHeartBeat(curr);
		dao.update(node);
	}

	/**
	 * 计算分组下终端个数
	 * 
	 * @param groups
	 *            分组名称，多个以逗号隔开，传空表示计算所有分组
	 * @return
	 */
	public int count(String[] groups) {
		Dao<Node> dao = DaoFactory.create(Node.class);

		int total = 0;
		Node node = new Node();
		for (String group : groups) {
			node.setGroup(group);
			total += dao.count(node);
		}
		return total;
	}
}
