package com.com.bunjlabs.largo.compiler;

import com.bunjlabs.largo.compiler.codegen.CodeGenerator;
import com.bunjlabs.largo.compiler.parser.Parser;
import com.bunjlabs.largo.compiler.parser.nodes.RootNode;
import com.bunjlabs.largo.compiler.semantic.SemanticAnalyzer;
import com.bunjlabs.largo.compiler.semantic.SemanticInfo;
import com.bunjlabs.largo.lib.MathLib;
import com.bunjlabs.largo.lib.functions.LibFunctions;
import com.bunjlabs.largo.runtime.*;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RuntimeTest {
    private static SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();
    private static CodeGenerator codeGenerator = new CodeGenerator();
    private static DefaultLargoRuntimeConstraints constraints = new DefaultLargoRuntimeConstraints();
    private static LargoRuntime runtime = new DefaultLargoRuntime();

    String run(String source) throws Exception {
        Parser parser = new Parser(new StringReader("import r;import m;" + source));
        RootNode root = parser.parse();
        SemanticInfo semanticInfo = semanticAnalyzer.analyze(root);
        Program program = codeGenerator.generate(root, semanticInfo);

        LargoEnvironment environment = new DefaultLargoEnvironment(constraints);

        final String[] result = new String[1];
        environment.export("r", LibFunctions.biConsumer((ctx, value) -> result[0] = value.asJString()));
        environment.export("m", MathLib.MATH);

        runtime.execute(environment, program);

        return result[0];
    }

    @Test
    void basicCases() throws Exception {
        assertEquals("null", run("r(null);"));
        assertEquals("undefined", run("r(undefined);"));
        assertEquals("true", run("r(true);"));
        assertEquals("false", run("r(false);"));
        assertEquals("3", run("r(3);"));
        assertEquals("3.5", run("r(3.5);"));
        assertEquals("asd", run("r('asd');"));
        assertEquals("false", run("r(true == false);"));
        assertEquals("true", run("r(true == true);"));
        assertEquals("true", run("r(false == false);"));
    }

    @Test
    void compareCases() throws Exception {
        assertEquals("true", run("r('asd' == 'asd');"));
        assertEquals("false", run("r('asd' == 3);"));
        assertEquals("false", run("r('asd' == 3.3);"));
        assertEquals("false", run("r('asd' == false);"));
        assertEquals("false", run("r('asd' == true);"));
        assertEquals("false", run("r('asd' == null);"));
        assertEquals("false", run("r('asd' == undefined);"));
        assertEquals("false", run("r('asd' == r);"));

        assertEquals("false", run("r(3 == 'asd');"));
        assertEquals("true", run("r(3 == 3);"));
        assertEquals("false", run("r(3 == 3.3);"));
        assertEquals("false", run("r(3 == false);"));
        assertEquals("false", run("r(3 == true);"));
        assertEquals("false", run("r(0 == false);"));
        assertEquals("false", run("r(1 == true);"));
        assertEquals("false", run("r(3 == null);"));
        assertEquals("false", run("r(3 == undefined);"));
        assertEquals("false", run("r(3 == r);"));

        assertEquals("false", run("r(3.3 == 'asd');"));
        assertEquals("false", run("r(3.3 == 3);"));
        assertEquals("true", run("r(3.3 == 3.3);"));
        assertEquals("false", run("r(3.3 == false);"));
        assertEquals("false", run("r(3.3 == true);"));
        assertEquals("false", run("r(0.3 == false);"));
        assertEquals("false", run("r(1.3 == true);"));
        assertEquals("false", run("r(3.3 == null);"));
        assertEquals("false", run("r(3.3 == undefined);"));
        assertEquals("false", run("r(3.3 == r);"));

        assertEquals("false", run("r(false == 'asd');"));
        assertEquals("false", run("r(false == 3);"));
        assertEquals("false", run("r(false == 3.3);"));
        assertEquals("false", run("r(false == true);"));
        assertEquals("true", run("r(false == false);"));
        assertEquals("false", run("r(false == true);"));
        assertEquals("false", run("r(false == null);"));
        assertEquals("false", run("r(false == undefined);"));
        assertEquals("false", run("r(false == r);"));

        assertEquals("false", run("r(true == 'asd');"));
        assertEquals("false", run("r(true == 3);"));
        assertEquals("false", run("r(true == 3.3);"));
        assertEquals("false", run("r(true == false);"));
        assertEquals("true", run("r(true == true);"));
        assertEquals("false", run("r(true == null);"));
        assertEquals("false", run("r(true == undefined);"));
        assertEquals("false", run("r(true == r);"));


        assertEquals("false", run("r(null == 'asd');"));
        assertEquals("false", run("r(null == 3);"));
        assertEquals("false", run("r(null == 3.3);"));
        assertEquals("false", run("r(null == false);"));
        assertEquals("false", run("r(null == true);"));
        assertEquals("true", run("r(null == null);"));
        assertEquals("false", run("r(null == undefined);"));
        assertEquals("false", run("r(null == r);"));


        assertEquals("false", run("r(undefined == 'asd');"));
        assertEquals("false", run("r(undefined == 3);"));
        assertEquals("false", run("r(undefined == 3.3);"));
        assertEquals("false", run("r(undefined == false);"));
        assertEquals("false", run("r(undefined == true);"));
        assertEquals("false", run("r(undefined == null);"));
        assertEquals("true", run("r(undefined == undefined);"));
        assertEquals("false", run("r(undefined == r);"));

        assertEquals("true", run("r(10 == 10);"));
        assertEquals("false", run("r(10 < 10);"));
        assertEquals("false", run("r(10 > 10);"));
        assertEquals("false", run("r(11 <= 10);"));
        assertEquals("false", run("r(10 >= 11);"));
        assertEquals("true", run("r(10 < 11);"));
        assertEquals("true", run("r(11 > 10);"));
        assertEquals("true", run("r(10 <= 10);"));
        assertEquals("true", run("r(10 >= 10);"));
    }

    @Test
    void objectCases() throws Exception {
        assertEquals("[function]", run("r(m.sqrt);"));
        assertEquals("undefined", run("r(m.asd);"));
        assertEquals("undefined", run("r(m.asd.sqrt);"));
        assertEquals("undefined", run("r(m.asd.asd);"));

        assertEquals("undefined", run("r('asd'.asd);"));
        assertEquals("[function]", run("r('asd'.trim);"));


        assertEquals("true", run("r('asd'.asd == undefined);"));
    }

    void functionCases() throws Exception {
        assertEquals("asd", run("r('asd'.trim());"));
        assertEquals("undefined", run("r('asd'.trim()());"));
    }
}
