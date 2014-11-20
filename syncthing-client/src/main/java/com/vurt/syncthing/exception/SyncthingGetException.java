package com.vurt.syncthing.exception;

/**
 * 读取同步进程信息失败
 * 
 * @author Vurt
 */
public class SyncthingGetException extends RuntimeException {

	private static final long serialVersionUID = 1653123551689788123L;
	
	public SyncthingGetException(Throwable throwable){
		super(throwable);
	}
}
