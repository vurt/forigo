package com.vurt.node.server.exception;

import com.chinacreator.c2.exception.C2RuntimeException;

public class CommandSentFailed extends C2RuntimeException {

	private static final long serialVersionUID = -2425349495142430754L;

	public CommandSentFailed(String message) {
		super(message);
	}

}
