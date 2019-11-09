package com.bunjlabs.largo;

import com.bunjlabs.largo.compiler.CompilationException;
import com.bunjlabs.largo.types.LargoFunction;

import java.io.IOException;
import java.io.Reader;

public interface LargoRuntime {

    static LargoRuntime createDefaultRuntime() {
        return new LargoRuntimeBuilder().build();
    }

    LargoEnvironment getEnvironment();

    LargoLoader getLoader();

    LargoModule load(String id) throws IOException, CompilationException, LargoRuntimeException, LargoLoaderException;

    LargoModule load(String id, String source) throws IOException, CompilationException, LargoRuntimeException, LargoLoaderException;

    LargoModule load(String id, Reader source) throws IOException, CompilationException, LargoRuntimeException, LargoLoaderException;

    LargoModule load(String id, Blueprint blueprint) throws LargoRuntimeException;
}
