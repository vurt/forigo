package com.vurt.node.agent.exception;


public class ApplicationFileNotExistException extends RuntimeException {

	private static final long serialVersionUID = -3933396589217934710L;

	public ApplicationFileNotExistException(String message){
		super(message);
	}
}
