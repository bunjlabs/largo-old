package com.bunjlabs.largo.runtime;

import com.bunjlabs.largo.types.LargoValue;

public class Program {

    private final OpCode[] opCodes;
    private final LargoValue[] constPool;
    private final int localVariablesCount;

    public Program(OpCode[] opCodes, LargoValue[] constPool, int localVariablesCount) {
        this.opCodes = opCodes;
        this.constPool = constPool;
        this.localVariablesCount = localVariablesCount;
    }

    public OpCode[] getOpCodes() {
        return opCodes;
    }

    public LargoValue[] getConstPool() {
        return constPool;
    }

    public int getLocalVariablesCount() {
        return localVariablesCount;
    }
}
