package com.bunjlabs.largo.compiler.semantic;

import com.bunjlabs.largo.compiler.parser.nodes.Node;
import com.bunjlabs.largo.types.LargoValue;

public class SemanticInfo {
    private final Node root;
    private final LargoValue[] constPool;
    private final SemanticInfo[] functions;
    private final int argumentsCount;
    private final int localVariablesCount;

    public SemanticInfo(Node root, LargoValue[] constPool, SemanticInfo[] functions, int argumentsCount, int localVariablesCount) {
        this.root = root;
        this.constPool = constPool;
        this.functions = functions;
        this.argumentsCount = argumentsCount;
        this.localVariablesCount = localVariablesCount;
    }

    public Node getRoot() {
        return root;
    }

    public LargoValue[] getConstPool() {
        return constPool;
    }

    public SemanticInfo[] getFunctions() {
        return functions;
    }

    public int getArgumentsCount() {
        return argumentsCount;
    }

    public int getLocalVariablesCount() {
        return localVariablesCount;
    }
}
