package com.bunjlabs.largo.runtime;

public interface LargoRuntime {

    void execute(LargoEnvironment environment, Program program) throws LargoRuntimeException;
}
