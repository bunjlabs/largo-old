package com.bunjlabs.largo;

public class LargoRuntimeException extends Exception {
    public LargoRuntimeException() {
    }

    public LargoRuntimeException(String message) {
        super(message);
    }

    public LargoRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public LargoRuntimeException(Throwable cause) {
        super(cause);
    }
}
