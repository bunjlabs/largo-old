package com.com.bunjlabs.largo.compiler;

import com.bunjlabs.largo.*;
import com.bunjlabs.largo.compiler.CompilationException;
import com.bunjlabs.largo.LargoRuntimeBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ExampleTest {

    @Test
    void start() throws LargoRuntimeException, LargoLoaderException, CompilationException, IOException {
        LargoRuntime runtime = LargoRuntime.createDefaultRuntime();
        runtime.getEnvironment().addModule("System", new LargoSystemModule());

        LargoModule module = runtime.load("test.lgo");
    }
}
