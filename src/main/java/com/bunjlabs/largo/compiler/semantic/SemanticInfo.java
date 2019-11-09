package com.bunjlabs.largo.compiler.semantic;

import com.bunjlabs.largo.compiler.parser.nodes.Node;
import com.bunjlabs.largo.types.LargoValue;

public class SemanticInfo {
    private final Node root;
    private final LargoValue[] constPool;
    private final SemanticInfo[] closures;
    private final int argumentsCount;
    private final int variablesCount;
    private final int localVariablesIndex;

    public SemanticInfo(Node root, LargoValue[] constPool, SemanticInfo[] closures, int argumentsCount, int variablesCount, int localVariablesIndex) {
        this.root = root;
        this.constPool = constPool;
        this.closures = closures;
        this.argumentsCount = argumentsCount;
        this.variablesCount = variablesCount;
        this.localVariablesIndex = localVariablesIndex;
    }

    public Node getRoot() {
        return root;
    }

    public LargoValue[] getConstPool() {
        return constPool;
    }

    public SemanticInfo[] getClosures() {
        return closures;
    }

    public int getArgumentsCount() {
        return argumentsCount;
    }

    public int getVariablesCount() {
        return variablesCount;
    }

    public int getLocalVariablesIndex() {
        return localVariablesIndex;
    }
}
