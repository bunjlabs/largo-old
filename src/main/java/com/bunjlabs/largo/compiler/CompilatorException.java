package com.bunjlabs.largo.compiler;

public class CompilatorException extends Exception {
    public CompilatorException() {
    }

    public CompilatorException(String message) {
        super(message);
    }

    public CompilatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public CompilatorException(Throwable cause) {
        super(cause);
    }
}
