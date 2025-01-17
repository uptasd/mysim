package org.mysim.core.exception;

public class JsonDeserializationException extends RuntimeException{
    public JsonDeserializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
