package com.vurt.node.server.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.provider.ftp.FtpFileSystemConfigBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinacreator.c2.config.ConfigManager;
import com.chinacreator.c2.web.ds.TreeContentProvider;
import com.chinacreator.c2.web.ds.TreeContext;
import com.chinacreator.c2.web.ds.TreeNode;
import com.vurt.node.server.exception.FileRootNotValid;

public class FileTreeContentProvider implements TreeContentProvider{
	private static final Logger LOGGER = LoggerFactory
			.getLogger(FileTreeContentProvider.class);
	
	private FileObject root;
	
	private FileSystemOptions opts;
	
	public FileTreeContentProvider(){
		try {
			opts = new FileSystemOptions();
			FtpFileSystemConfigBuilder.getInstance().setPassiveMode(opts, true);
			FileSystemManager fsManager = VFS.getManager();
			String url=ConfigManager.getInstance().getConfig("node.server.fileServerURL");
			if(StringUtils.isEmpty(url)){
				LOGGER.error("没有配置node.server.fileServerURL，无法找到任何文件");
			}
			root=fsManager.resolveFile(url,opts);
		} catch (FileSystemException e) {
			e.printStackTrace();
		}
	}

	@Override
	public TreeNode[] getElements(TreeContext ctx) {
		List<TreeNode> nodes=new ArrayList<TreeNode>();
		try {
			if(!root.exists()){
				throw new FileRootNotValid("文件服务器不存在!请检查node.server.fileServerURL属性");
			}
			FileObject parent=null;
			String pid=(String)ctx.getConditions().get("id");
			if(StringUtils.isEmpty(pid)){
				parent=root;
			}else{
				parent=root.resolveFile(pid);
			}
			if(!parent.exists()){
				return new TreeNode[0];
			}
			for(FileObject file:parent.getChildren()){
				nodes.add(new FileTreeNode(pid, file.getName().getPath(), file.getName().getBaseName(),file.getURL().toString(), file.getType()==FileType.FOLDER));
			}
		} catch (FileSystemException e) {
			throw new FileRootNotValid("读取文件时发生错误");
		}
		return nodes.toArray(new TreeNode[nodes.size()]);
	}

}
