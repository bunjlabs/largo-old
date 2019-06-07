package com.bunjlabs.largo;

import com.bunjlabs.largo.runtime.OpCode;
import com.bunjlabs.largo.types.LargoValue;

public class Blueprint {
    private final OpCode[] code;
    private final LargoValue[] constantPool;
    private final int argumentsCount;
    private final int localVariablesCount;
    private final int maxStackSize;
    private final Blueprint[] blueprints;

    public Blueprint(OpCode[] code, LargoValue[] constantPool, int argumentsCount, int localVariablesCount, int maxStackSize, Blueprint[] blueprints) {
        this.code = code;
        this.constantPool = constantPool;
        this.argumentsCount = argumentsCount;
        this.localVariablesCount = localVariablesCount;
        this.maxStackSize = maxStackSize;
        this.blueprints = blueprints;
    }

    public Blueprint(OpCode[] code, LargoValue[] constantPool, int argumentsCount, int localVariablesCount, int maxStackSize) {
        this.code = code;
        this.constantPool = constantPool;
        this.argumentsCount = argumentsCount;
        this.localVariablesCount = localVariablesCount;
        this.maxStackSize = maxStackSize;
        this.blueprints = new Blueprint[0];
    }

    public OpCode[] getCode() {
        return code;
    }

    public LargoValue[] getConstantPool() {
        return constantPool;
    }

    public int getLocalVariablesCount() {
        return localVariablesCount;
    }

    public int getArgumentsCount() {
        return argumentsCount;
    }

    public int getMaxStackSize() {
        return maxStackSize;
    }

    public Blueprint[] getBlueprints() {
        return blueprints;
    }

}
