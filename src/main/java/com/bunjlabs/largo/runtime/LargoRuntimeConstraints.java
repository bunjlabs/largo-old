package com.bunjlabs.largo.runtime;

public interface LargoRuntimeConstraints {

    int getMaxCodeLength();

    long getMaxExecutionTime();

    int getMaxStackSize();

    int getMaxVariables();

    int getMaxConstantPoolSize();

    int getMaxInstructions();
}
