package com.bunjlabs.largo.compiler.lexer;

import com.bunjlabs.largo.compiler.CompilationException;

public class LexerException extends CompilationException {
    public LexerException(Lexer lexer, String msg) {
        super("(" + lexer.line + ":" + lexer.column + ") " + msg);
    }
}
