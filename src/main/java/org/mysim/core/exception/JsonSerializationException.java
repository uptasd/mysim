package org.mysim.core.exception;

public class JsonSerializationException extends RuntimeException{
    public JsonSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
