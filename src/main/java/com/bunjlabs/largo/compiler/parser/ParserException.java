package com.bunjlabs.largo.compiler.parser;

import com.bunjlabs.largo.compiler.CompilatorException;
import com.bunjlabs.largo.compiler.lexer.Lexer;

public class ParserException extends CompilatorException {
    public ParserException() {
    }

    public ParserException(String message) {
        super(message);
    }

    public ParserException(Lexer lexer, String msg) {
        super("(" + lexer.line + ":" + lexer.column + ") " + msg);
    }
}
