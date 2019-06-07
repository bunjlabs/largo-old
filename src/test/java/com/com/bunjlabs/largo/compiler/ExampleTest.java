package com.com.bunjlabs.largo.compiler;

import com.bunjlabs.largo.compiler.SemanticException;
import com.bunjlabs.largo.compiler.codegen.CodeGenerator;
import com.bunjlabs.largo.compiler.lexer.LexerException;
import com.bunjlabs.largo.compiler.parser.Parser;
import com.bunjlabs.largo.compiler.parser.ParserException;
import com.bunjlabs.largo.compiler.parser.nodes.Node;
import com.bunjlabs.largo.compiler.semantic.SemanticAnalyzer;
import com.bunjlabs.largo.lib.functions.LibFunctions;
import com.bunjlabs.largo.runtime.LargoRuntimeException;
import com.bunjlabs.largo.types.LargoClosure;
import com.bunjlabs.largo.types.LargoObject;
import com.bunjlabs.largo.types.LargoString;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;

public class ExampleTest {

    @Test
    void start() throws IOException, LexerException, ParserException, SemanticException, LargoRuntimeException {
        Parser parser = new Parser(new FileReader("test.lgo"));

        Node root = parser.parse();
        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(root);
        CodeGenerator codeGenerator = new CodeGenerator(semanticAnalyzer.analyze());

        LargoObject api = new LargoObject();
        api.set(LargoString.from("print"), LibFunctions.biConsumer((ctx, value) -> System.out.println(value.asJString())));

        LargoClosure largoClosure = new LargoClosure(api, codeGenerator.generate());

        largoClosure.call();
    }
}
