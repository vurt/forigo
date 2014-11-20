package com.vurt.syncthing.exception;

/**
 * 文件同步进程更新失败的异常
 * @author Vurt
 *
 */
public class SyncthingUpdateException extends Exception {

	private static final long serialVersionUID = -444849333086541269L;
	
	public SyncthingUpdateException(String message) {
		super(message);
	}

	public SyncthingUpdateException(String message,Throwable throwable) {
		super(message,throwable);
	}
	
	public SyncthingUpdateException(Throwable throwable) {
		super(throwable);
	}
}
