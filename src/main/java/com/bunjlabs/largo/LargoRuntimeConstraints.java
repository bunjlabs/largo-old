package com.bunjlabs.largo;

public interface LargoRuntimeConstraints {

    int getMaxCodeLength();

    int getMaxStackSize();

    int getMaxVariables();

    int getMaxConstantPoolSize();
}
