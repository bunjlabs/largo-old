package com.bunjlabs.largo.compiler.parser.nodes;


public class BaseNode {
    private NodeType type;

    BaseNode(NodeType type) {
        this.type = type;
    }

    public NodeType getType() {
        return type;
    }
}
