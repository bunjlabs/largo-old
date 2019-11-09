package com.bunjlabs.largo;

import com.bunjlabs.largo.types.LargoValue;

public interface LargoEnvironment {

    LargoConstraints getConstraints();

    void addModule(String id, LargoModule module);

    LargoModule importModule(String id);
}
