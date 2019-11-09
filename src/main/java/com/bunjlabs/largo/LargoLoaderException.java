package com.bunjlabs.largo;

public class LargoLoaderException extends Exception {
    public LargoLoaderException() {
    }

    public LargoLoaderException(String message) {
        super(message);
    }

    public LargoLoaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public LargoLoaderException(Throwable cause) {
        super(cause);
    }
}
