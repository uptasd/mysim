package org.mysim.core.exception;

public class MessageTimeoutException extends RuntimeException{
    public MessageTimeoutException(String message){
        super(message);
    }
}
