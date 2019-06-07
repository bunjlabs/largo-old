package com.bunjlabs.largo.runtime;

import com.bunjlabs.largo.Blueprint;

public interface LargoRuntime {

    void execute(LargoEnvironment environment, Blueprint blueprint) throws LargoRuntimeException;
}
