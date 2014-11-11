package com.vurt.node.server.service;

import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.chinacreator.c2.dao.Dao;
import com.chinacreator.c2.dao.DaoFactory;
import com.chinacreator.c2.dao.mybatis.enhance.Order;
import com.chinacreator.c2.dao.mybatis.enhance.Sortable;
import com.vurt.node.server.bean.Node;

public class NodeService {
	private Dao<Node> dao; 
	
	public NodeService(){
		dao=DaoFactory.create(Node.class);
	}
	
	public Map<String,String> getGroups(){
		List<Node> nodes = dao.selectAll(new Sortable(new Order("group_", "asc")));
	
		Map<String,String> groups=new LinkedHashMap<String, String>();
		
		for(Node node:nodes){
			String group=node.getGroup();
			groups.put(group, group);
		}
		
		return groups;
	}
	
	public boolean containsNode(String id){
		Node node=new Node();
		node.setId(id);
		return dao.count(node)>0;
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
		dao.update(node);
	}
}
