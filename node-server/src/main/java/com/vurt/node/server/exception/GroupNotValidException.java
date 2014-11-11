package com.vurt.node.server.exception;

import com.chinacreator.c2.exception.C2RuntimeException;

public class GroupNotValidException extends C2RuntimeException {

	private static final long serialVersionUID = 2588182721424834695L;

	public GroupNotValidException(String message) {
		super(message);
	}

}
