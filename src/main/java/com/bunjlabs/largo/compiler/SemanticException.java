package com.bunjlabs.largo.compiler;

import com.bunjlabs.largo.compiler.parser.nodes.Node;

public class SemanticException extends CompilationException {
    public SemanticException(Node node, String msg) {
        super("(" + node.getType() + " " + node.getLineNumber() + ":" + node.getColumnNumber() + ") " + msg);
    }
}
