package com.vurt.node.agent.file;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;

public class RemoteFileReader {
	public static void main(String[] args) {
		FileSystemManager fsManager;
		try {
			fsManager = VFS.getManager();
			FileObject fileObject=fsManager.resolveFile("http://");
			fileObject.getContent().getInputStream();
		} catch (FileSystemException e) {
			e.printStackTrace();
		}
	}
}
