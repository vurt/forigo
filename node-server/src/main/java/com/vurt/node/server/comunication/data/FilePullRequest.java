package com.vurt.node.server.comunication.data;

public class FilePullRequest {
	/**
	 * 文件下载路径，暂只支持http协议，支持目录
	 */
	private String file;

	/**
	 * 文件下载后放到什么路径(相对于节点代理中配置的文件存储根目录)
	 */
	private String target;

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}
	
	
}
