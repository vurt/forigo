package com.vurt.node.server.ds;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.chinacreator.c2.dao.Dao;
import com.chinacreator.c2.dao.DaoFactory;
import com.chinacreator.c2.web.ds.TreeContentProvider;
import com.chinacreator.c2.web.ds.TreeContext;
import com.chinacreator.c2.web.ds.TreeNode;
import com.chinacreator.c2.web.ds.impl.DynamicTreeNode;
import com.vurt.node.server.bean.Group;

public class GroupTreeContentProvider implements TreeContentProvider{

	@Override
	public TreeNode[] getElements(TreeContext ctx) {
		if(StringUtils.isEmpty(ctx.getPid())){
			Dao<Group> dao=DaoFactory.create(Group.class);
			List<Group> groups = dao.selectAll();
			
			List<TreeNode> nodes=new ArrayList<TreeNode>();
			for(Group group:groups){
				DynamicTreeNode treeNode=new DynamicTreeNode("", group.getName(), group.getName(),false);
				treeNode.put("display", group.getName()+" <span class='green pull-right'>"+group.getVersion().getVersion()+"</span>");
				treeNode.put("version", group.getVersion().getVersion());
				nodes.add(treeNode);
			}
			return nodes.toArray(new TreeNode[nodes.size()]);
		}
		return new TreeNode[0];
	}

}
