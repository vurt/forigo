package com.vurt.node.server.file;

import com.chinacreator.c2.web.ds.impl.DefaultTreeNode;

public class FileTreeNode extends DefaultTreeNode {
	private String fileUrl;
	
	public FileTreeNode(String pid, String id, String name,String url, boolean isParent) {
		super(pid, id, name, isParent);
		this.fileUrl=url;
	}
	
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	
	public String getFileUrl() {
		return fileUrl;
	}

}
