package com.vurt.node.server.exception;

import com.chinacreator.c2.exception.C2RuntimeException;

public class FileRootNotValid extends C2RuntimeException {

	private static final long serialVersionUID = -8085328458001042314L;

	public FileRootNotValid(String message) {
		super(message);
	}

}
