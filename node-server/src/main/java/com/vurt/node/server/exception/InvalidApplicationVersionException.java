package com.vurt.node.server.exception;

import com.chinacreator.c2.exception.C2BusinessException;
import com.chinacreator.c2.exception.C2RuntimeException;

public class InvalidApplicationVersionException extends C2RuntimeException
		implements C2BusinessException {

	private static final long serialVersionUID = -1321622341392490203L;

	public InvalidApplicationVersionException(String message, Throwable cause) {
		super(message, cause);
	}


}
