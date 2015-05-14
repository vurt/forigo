package com.vurt.node.server.exception;

import com.chinacreator.c2.exception.C2RuntimeException;

public class InvalidNodeException extends C2RuntimeException {

	private static final long serialVersionUID = 8851041227590907596L;

	public InvalidNodeException(String message) {
		super(message);
	}

}
