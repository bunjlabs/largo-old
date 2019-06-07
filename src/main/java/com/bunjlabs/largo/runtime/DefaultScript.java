package com.bunjlabs.largo.runtime;

import com.bunjlabs.largo.types.LargoValue;

public class DefaultScript implements Script {

    private final OpCode[] opCodes;
    private final LargoValue[] constPool;
    private final int localVariablesCount;

    public DefaultScript(OpCode[] opCodes, LargoValue[] constPool, int localVariablesCount) {
        this.opCodes = opCodes;
        this.constPool = constPool;
        this.localVariablesCount = localVariablesCount;
    }

    @Override
    public OpCode[] getOpCodes() {
        return opCodes;
    }

    @Override
    public LargoValue[] getConstPool() {
        return constPool;
    }

    @Override
    public int getLocalVariablesCount() {
        return localVariablesCount;
    }
}
