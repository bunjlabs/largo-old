package com.bunjlabs.largo;

public class DefaultLargoConstraints implements LargoConstraints {

    private int maxCodeLength;
    private int maxStackSize;
    private int maxVariables;
    private int maxConstantPoolSize;

    public DefaultLargoConstraints() {
        this.maxCodeLength = Integer.MAX_VALUE;
        this.maxStackSize = Integer.MAX_VALUE;
        this.maxVariables = Integer.MAX_VALUE;
        this.maxConstantPoolSize = Integer.MAX_VALUE;
    }

    public DefaultLargoConstraints(int maxCodeLength, int maxStackSize, int maxVariables, int maxConstantPoolSize) {
        this.maxCodeLength = maxCodeLength;
        this.maxStackSize = maxStackSize;
        this.maxVariables = maxVariables;
        this.maxConstantPoolSize = maxConstantPoolSize;
    }

    @Override
    public int getMaxCodeLength() {
        return maxCodeLength;
    }

    public void setMaxCodeLength(int maxCodeLength) {
        this.maxCodeLength = maxCodeLength;
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
}
