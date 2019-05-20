package com.bunjlabs.largo.compiler.lexer;

import com.bunjlabs.largo.compiler.CompilatorException;

public class LexerException extends CompilatorException {
    public LexerException(Lexer lexer, String msg) {
        super("(" + lexer.line + ":" + lexer.column + ") " + msg);
    }
}
