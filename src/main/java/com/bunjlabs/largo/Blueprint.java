package com.bunjlabs.largo;

import com.bunjlabs.largo.types.LargoValue;

public class Blueprint {
    private final Instruction[] code;
    private final LargoValue[] constantPool;
    private final int argumentsCount;
    private final int registersCount;
    private final int variablesCount;
    private final int variablesIndex;
    private final int callStackSize;
    private final Blueprint[] blueprints;

    public Blueprint(Instruction[] code, LargoValue[] constantPool, int registersCount, int argumentsCount, int variablesCount, int variablesIndex, int callStackSize, Blueprint[] blueprints) {
        this.code = code;
        this.constantPool = constantPool;
        this.registersCount = registersCount;
        this.argumentsCount = argumentsCount;
        this.variablesCount = variablesCount;
        this.variablesIndex = variablesIndex;
        this.callStackSize = callStackSize;
        this.blueprints = blueprints;
    }

    public Blueprint(Instruction[] code, LargoValue[] constantPool, int registersCount, int argumentsCount, int variablesCount, int variablesIndex, int callStackSize) {
        this.code = code;
        this.constantPool = constantPool;
        this.registersCount = registersCount;
        this.argumentsCount = argumentsCount;
        this.variablesCount = variablesCount;
        this.variablesIndex = variablesIndex;
        this.callStackSize = callStackSize;
        this.blueprints = new Blueprint[0];
    }

    public Instruction[] getCode() {
        return code;
    }

    public LargoValue[] getConstantPool() {
        return constantPool;
    }

    public int getRegistersCount() {
        return registersCount;
    }

    public int getArgumentsCount() {
        return argumentsCount;
    }

    public int getVariablesCount() {
        return variablesCount;
    }

    public int getVariablesIndex() {
        return variablesIndex;
    }

    public int getCallStackSize() {
        return callStackSize;
    }

    public Blueprint[] getBlueprints() {
        return blueprints;
    }

    public int getInstructionsCount() {
        int count = code.length;
        for (Blueprint blueprint : blueprints) {
            count += blueprint.getInstructionsCount();
        }
        return count;
    }
}
