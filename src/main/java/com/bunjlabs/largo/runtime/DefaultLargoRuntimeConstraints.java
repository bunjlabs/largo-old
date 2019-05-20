package com.bunjlabs.largo.runtime;

public class DefaultLargoRuntimeConstraints implements LargoRuntimeConstraints {

    private int maxCodeLength;
    private long maxExecutionTime;
    private int maxStackSize;
    private int maxVariables;
    private int maxConstantPoolSize;
    private int maxInstructions;

    public DefaultLargoRuntimeConstraints() {
        this.maxCodeLength = Integer.MAX_VALUE;
        this.maxExecutionTime = Long.MAX_VALUE;
        this.maxStackSize = Integer.MAX_VALUE;
        this.maxVariables = Integer.MAX_VALUE;
        this.maxConstantPoolSize = Integer.MAX_VALUE;
        this.maxInstructions = Integer.MAX_VALUE;
    }

    public DefaultLargoRuntimeConstraints(int maxCodeLength, long maxExecutionTime, int maxStackSize, int maxVariables, int maxConstantPoolSize, int maxInstructions) {
        this.maxCodeLength = maxCodeLength;
        this.maxExecutionTime = maxExecutionTime;
        this.maxStackSize = maxStackSize;
        this.maxVariables = maxVariables;
        this.maxConstantPoolSize = maxConstantPoolSize;
        this.maxInstructions = maxInstructions;
    }

    @Override
    public int getMaxCodeLength() {
        return maxCodeLength;
    }

    public void setMaxCodeLength(int maxCodeLength) {
        this.maxCodeLength = maxCodeLength;
    }

    @Override
    public long getMaxExecutionTime() {
        return maxExecutionTime;
    }

    public void setMaxExecutionTime(long maxExecutionTime) {
        this.maxExecutionTime = maxExecutionTime;
    }

    @Override
    public int getMaxStackSize() {
        return maxStackSize;
    }

    public void setMaxStackSize(int maxStackSize) {
        this.maxStackSize = maxStackSize;
    }

    @Override
    public int getMaxVariables() {
        return maxVariables;
    }

    public void setMaxVariables(int maxVariables) {
        this.maxVariables = maxVariables;
    }

    @Override
    public int getMaxConstantPoolSize() {
        return maxConstantPoolSize;
    }

    public void setMaxConstantPoolSize(int maxConstantPoolSize) {
        this.maxConstantPoolSize = maxConstantPoolSize;
    }

    @Override
    public int getMaxInstructions() {
        return maxInstructions;
    }

    public void setMaxInstructions(int maxInstructions) {
        this.maxInstructions = maxInstructions;
    }
}
