package com.bunjlabs.largo.compiler.semantic;

import com.bunjlabs.largo.types.LargoValue;

public class SemanticInfo {
    private final LargoValue[] constPool;
    private final int localVariablesCount;

    public SemanticInfo(LargoValue[] constPool, int localVariablesCount) {
        this.constPool = constPool;
        this.localVariablesCount = localVariablesCount;
    }

    public LargoValue[] getConstPool() {
        return constPool;
    }

    public int getLocalVariablesCount() {
        return localVariablesCount;
    }
}
