package com.bunjlabs.largo.compiler.parser.nodes;

import static com.bunjlabs.largo.compiler.parser.nodes.NodeType.ND_ROOT;

public class RootNode extends Node {

    public RootNode(Node child) {
        super(ND_ROOT, child);
    }
}
