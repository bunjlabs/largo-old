package com.bunjlabs.largo;

import com.bunjlabs.largo.compiler.CompilationException;
import com.bunjlabs.largo.types.LargoFunction;

import java.io.IOException;
import java.io.Reader;

public interface LargoRuntime {

    LargoFunction load(String script) throws IOException, CompilationException, LargoRuntimeException;

    LargoFunction load(Reader reader) throws IOException, CompilationException, LargoRuntimeException;

    LargoFunction load(Blueprint blueprint) throws LargoRuntimeException;
}
