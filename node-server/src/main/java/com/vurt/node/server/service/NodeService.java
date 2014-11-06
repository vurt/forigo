package com.vurt.node.server.service;

import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;

import com.chinacreator.c2.dao.Dao;
import com.chinacreator.c2.dao.DaoFactory;
import com.vurt.node.server.bean.Node;

public class NodeService {
	private Dao<Node> dao; 
	
	public NodeService(){
		dao=DaoFactory.create(Node.class);
	}
	
	public Map<String,String> getGroups(){
		Map<String,String> groups=new LinkedHashMap<String, String>();
		groups.put("分组1", "分组1");
		groups.put("分组2", "分组2");
		groups.put("分组3", "分组3");
		return groups;
	}
	
	public boolean containsNode(String id){
		return false;
	}
	
	/**
	 * 新增一个节点，会在插入前设置一些时间相关的数据
	 * @param node 
	 */
	public void addNode(Node node){
		Timestamp curr=new Timestamp(System.currentTimeMillis());
		node.setLastHeartBeat(curr);
		node.setRegTime(curr);
		dao.insert(node);
	}
	
	public void updateNode(Node node){
		Timestamp curr=new Timestamp(System.currentTimeMillis());
		node.setLastHeartBeat(curr);
		dao.insert(node);
	}
}
