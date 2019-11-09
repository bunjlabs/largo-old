package com.bunjlabs.largo;

import com.bunjlabs.largo.compiler.SemanticException;
import com.bunjlabs.largo.compiler.codegen.CodeGenerator;
import com.bunjlabs.largo.compiler.lexer.LexerException;
import com.bunjlabs.largo.compiler.parser.Parser;
import com.bunjlabs.largo.compiler.parser.ParserException;
import com.bunjlabs.largo.compiler.parser.nodes.Node;
import com.bunjlabs.largo.compiler.semantic.SemanticAnalyzer;
import com.bunjlabs.largo.compiler.semantic.SemanticInfo;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public abstract class AbstractLargoLoader implements LargoLoader {

    abstract Reader getSourceReader(String id) throws LargoLoaderException;

    @Override
    public Blueprint load(String id) throws LargoLoaderException {
        Reader source = getSourceReader(id);
        return loadSource(source);
    }

    @Override
    public Blueprint loadSource(String source) throws LargoLoaderException {
        return loadSource(new StringReader(source));
    }

    @Override
    public Blueprint loadSource(Reader source) throws LargoLoaderException {
        try {
            Parser parser = new Parser(source);
            Node root = parser.parse();

            SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(root);
            SemanticInfo semanticInfo = semanticAnalyzer.analyze();

            CodeGenerator codeGenerator = new CodeGenerator(semanticInfo);
            return codeGenerator.generate();
        } catch (LexerException | ParserException | SemanticException e) {
            throw new LargoLoaderException("Compilation error", e);
        } catch (IOException e) {
            throw new LargoLoaderException("Unable load source", e);
        }
    }
}
