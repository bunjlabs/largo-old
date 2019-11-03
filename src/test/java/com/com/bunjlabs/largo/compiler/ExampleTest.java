package com.com.bunjlabs.largo.compiler;

import com.bunjlabs.largo.DefaultLargoEnvironment;
import com.bunjlabs.largo.DefaultLargoRuntime;
import com.bunjlabs.largo.LargoRuntime;
import com.bunjlabs.largo.compiler.CompilationException;
import com.bunjlabs.largo.lib.MathLib;
import com.bunjlabs.largo.LargoRuntimeException;
import com.bunjlabs.largo.lib.StdIoLib;
import com.bunjlabs.largo.types.LargoFunction;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;

public class ExampleTest {

    @Test
    void start() throws IOException, CompilationException, LargoRuntimeException {
        DefaultLargoEnvironment environment = new DefaultLargoEnvironment();
        environment.export("println", LargoFunction.fromBiConsumer((ctx, str) -> System.out.println(str.asJString())));
        environment.export("math", MathLib.LIB);
        environment.export("stdio", StdIoLib.LIB);

        LargoRuntime runtime = new DefaultLargoRuntime(environment);

        LargoFunction function = runtime.load(new FileReader("test.lgo"));

        function.call(environment.getContext());
    }
}
