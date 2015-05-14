package com.vurt.node.server.exception;

import com.chinacreator.c2.exception.C2BusinessException;
import com.chinacreator.c2.exception.C2RuntimeException;

public class InvalidMetadataException extends C2RuntimeException implements C2BusinessException{

	private static final long serialVersionUID = 9181885127497658015L;
	
	public InvalidMetadataException(String message) {
		super(message);
	}

	public InvalidMetadataException(String message, Throwable cause) {
		super(message, cause);
	}

}
