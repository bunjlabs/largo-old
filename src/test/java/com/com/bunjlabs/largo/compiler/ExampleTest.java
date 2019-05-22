package com.com.bunjlabs.largo.compiler;

import com.bunjlabs.largo.compiler.SemanticException;
import com.bunjlabs.largo.compiler.codegen.CodeGenerator;
import com.bunjlabs.largo.compiler.lexer.LexerException;
import com.bunjlabs.largo.compiler.parser.Parser;
import com.bunjlabs.largo.compiler.parser.ParserException;
import com.bunjlabs.largo.compiler.parser.nodes.RootNode;
import com.bunjlabs.largo.compiler.semantic.SemanticAnalyzer;
import com.bunjlabs.largo.compiler.semantic.SemanticInfo;
import com.bunjlabs.largo.lib.MathLib;
import com.bunjlabs.largo.lib.functions.LibFunctions;
import com.bunjlabs.largo.runtime.*;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExampleTest {

    @Test
    void start() throws IOException, LexerException, ParserException, SemanticException, LargoRuntimeException {
        Parser parser = new Parser(new FileReader("test.lgo"));

        RootNode root = parser.parse();
        CodeGenerator codeGenerator = new CodeGenerator();
        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();
        SemanticInfo semanticInfo = semanticAnalyzer.analyze(root);
        Program program = codeGenerator.generate(root, semanticInfo);

        Map<Object, Object> userData = new HashMap<>();

        DefaultLargoRuntimeConstraints constraints = new DefaultLargoRuntimeConstraints();
        constraints.setMaxExecutionTime(1000);
        LargoEnvironment environment = new DefaultLargoEnvironment(constraints);
        environment.export("math", MathLib.MATH);
        environment.export("print", LibFunctions.biConsumer((ctx, value) -> System.out.println(value.asJString())));

        LargoRuntime runtime = new DefaultLargoRuntime();

        runtime.execute(environment, program);
    }
}
