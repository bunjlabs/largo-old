package com.bunjlabs.largo.runtime;

import com.bunjlabs.largo.types.LargoValue;

public interface LargoEnvironment {

    LargoRuntimeConstraints getConstraints();

    void export(String name, LargoValue value);

    LargoValue getExported(String name);

}
