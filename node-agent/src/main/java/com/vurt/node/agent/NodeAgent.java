package com.vurt.node.agent;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinacreator.c2.config.ConfigManager;
import com.vurt.node.agent.comunication.Constants;

public class NodeAgent {
	private static final Logger LOGGER=LoggerFactory.getLogger(NodeAgent.class);
	
	private static String nodeId;
	
	private static String group;
	
	private static String position;
	
	private static String address;
	
	private static boolean valid=false;
	
	public static final void init(){
		   nodeId = ConfigManager.getInstance().getConfig("node.id");
           if(StringUtils.isEmpty(nodeId)){
        	   LOGGER.error("节点id为空，节点代理无法正常工作");
        	   return;
           }
          
           group=ConfigManager.getInstance().getConfig("node.group");
           if(StringUtils.isEmpty(group)){
        	   group=Constants.EMPTY_GROUP_SEVERITY;
           }
           position=ConfigManager.getInstance().getConfig("node.position");
           address=ConfigManager.getInstance().getConfig("node.address");
           valid=true;
	}
	
	public static String getNodeId(){
		if(!valid) throw new RuntimeException("节点代理无效"); 
		return nodeId;
	}
	
	public static String getGroup(){
		if(!valid) throw new RuntimeException("节点代理无效"); 
		return group;
	}
	
	public static String getPosition(){
		if(!valid) throw new RuntimeException("节点代理无效"); 
		return position;
	}
	
	public static String getAddress(){
		if(!valid) throw new RuntimeException("节点代理无效"); 
		return address;
	}
}
