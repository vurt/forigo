package com.vurt.node.agent.application;


/**
 * 应用初始化异常
 */
public class InitializeFailedException extends RuntimeException {
    public InitializeFailedException(String message) {
        super(message);
    }
}
