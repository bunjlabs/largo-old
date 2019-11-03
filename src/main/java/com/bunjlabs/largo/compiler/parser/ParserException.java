package com.bunjlabs.largo.compiler.parser;

import com.bunjlabs.largo.compiler.CompilationException;
import com.bunjlabs.largo.compiler.lexer.Lexer;

public class ParserException extends CompilationException {
    public ParserException() {
    }

    public ParserException(String message) {
        super(message);
    }

    public ParserException(Lexer lexer, String msg) {
        super("(" + lexer.line + ":" + lexer.column + ") " + msg);
    }
}
