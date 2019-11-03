package com.bunjlabs.largo;

import com.bunjlabs.largo.compiler.CompilationException;
import com.bunjlabs.largo.compiler.codegen.CodeGenerator;
import com.bunjlabs.largo.compiler.parser.Parser;
import com.bunjlabs.largo.compiler.parser.nodes.Node;
import com.bunjlabs.largo.compiler.semantic.SemanticAnalyzer;
import com.bunjlabs.largo.compiler.semantic.SemanticInfo;
import com.bunjlabs.largo.types.LargoClosure;
import com.bunjlabs.largo.types.LargoFunction;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class DefaultLargoRuntime implements LargoRuntime {

    private final LargoEnvironment environment;

    public DefaultLargoRuntime() {
        this.environment = new DefaultLargoEnvironment();
    }

    public DefaultLargoRuntime(LargoEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public LargoFunction load(String script) throws IOException, CompilationException, LargoRuntimeException {
        return load(new StringReader(script));
    }

    @Override
    public LargoFunction load(Reader reader) throws IOException, CompilationException, LargoRuntimeException {
        Parser parser = new Parser(reader);
        Node root = parser.parse();

        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(root);
        SemanticInfo semanticInfo = semanticAnalyzer.analyze();

        CodeGenerator codeGenerator = new CodeGenerator(semanticInfo);
        Blueprint blueprint = codeGenerator.generate();

        return load(blueprint);
    }

    @Override
    public LargoFunction load(Blueprint blueprint) throws LargoRuntimeException {
        LargoRuntimeConstraints constraints = environment.getConstraints();

        if (blueprint.getInstructionsCount() > constraints.getMaxCodeLength()) {
            throw new LargoRuntimeException("Maximum code length exceeded " +
                    "(limit: " + constraints.getMaxCodeLength() +
                    " actual: " + blueprint.getInstructionsCount());
        }

        if (blueprint.getConstantPool().length > constraints.getMaxConstantPoolSize()) {
            throw new LargoRuntimeException("Maximum constant pool size exceeded " +
                    "(limit: " + constraints.getMaxConstantPoolSize() +
                    " actual: " + blueprint.getConstantPool().length);
        }

        if (blueprint.getVariablesCount() > constraints.getMaxVariables()) {
            throw new LargoRuntimeException("Maximum variables count exceeded " +
                    "(limit: " + constraints.getMaxVariables() +
                    " actual: " + blueprint.getVariablesCount());
        }

        if (blueprint.getCallStackSize() > constraints.getMaxStackSize()) {
            throw new LargoRuntimeException("Maximum stack size exceeded " +
                    "(limit: " + constraints.getMaxStackSize() +
                    " actual: " + blueprint.getCallStackSize());
        }

        return LargoClosure.from(blueprint);
    }
}
