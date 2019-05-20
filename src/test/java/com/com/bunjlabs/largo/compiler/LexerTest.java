package com.com.bunjlabs.largo.compiler;

import com.bunjlabs.largo.compiler.lexer.Lexer;
import com.bunjlabs.largo.compiler.lexer.LexerException;
import com.bunjlabs.largo.compiler.lexer.Token;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static com.bunjlabs.largo.compiler.lexer.Token.*;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

class LexerTest {

    Lexer lex(String input) throws IOException {
        return new Lexer(new StringReader(input));
    }

    Lexer lexNext(String input) throws IOException, LexerException {
        Lexer l = new Lexer(new StringReader(input));
        l.next();
        return l;
    }

    List<Token> lexList(String input) throws IOException, LexerException {
        List<Token> list = new ArrayList<>();
        Lexer l = new Lexer(new StringReader(input));

        Token t;
        for (int i = 0; i < 100 && (t = l.next()) != TK_EOS; i++)
            list.add(t);

        return list;
    }

    @Test
    void basicCases() throws IOException, LexerException {
        assertEquals(TK_EOS, lex("").next());
        assertEquals(TK_EOS, lex("\n\r\f\t ").next());
        assertEquals(TK_EOS, lex("//").next());
        assertEquals(TK_EOS, lex("\n//\n").next());
        assertEquals(TK_EOS, lex("//abc").next());
        assertEquals(TK_EOS, lex("/*").next());
        assertEquals(TK_EOS, lex("/**/").next());
        assertEquals(TK_EOS, lex("\n/**/\n").next());
        assertEquals(TK_EOS, lex("/*abc*/").next());

        assertEquals(8, lexNext("\n\n\n\n\n\n\na").line);
        assertEquals(8, lexNext("       a").column);
        assertEquals(8, lexNext("/*   */a").column);
    }

    @Test
    void operators() throws IOException, LexerException {
        assertIterableEquals(asList(
                TK_END_STMT,
                TK_ASSIGN, TK_EQ, TK_PLUS, TK_DPLUS,
                TK_PLUSEQ, TK_MINUS, TK_DMINUS, TK_MINUSEQ,
                TK_MULT, TK_MULTEQ, TK_DIV, TK_DIVEQ,
                TK_MOD, TK_MODEQ, TK_AND, TK_DAND,
                TK_OR, TK_DOR, TK_NOT, TK_NOTEQ,
                TK_GREAT, TK_GREATEQ, TK_LESS, TK_LESSEQ,
                TK_ARROW
        ), lexList(
                "; = == + ++ += - -- -= * " +
                        "*= / /= % %= & && | || " +
                        "! != > >= < <= ->"
        ));
    }

    @Test
    void controls() throws IOException, LexerException {
        assertIterableEquals(asList(
                TK_OPEN_PAR, TK_CLOSE_PAR, TK_OPEN_CS, TK_CLOSE_CS,
                TK_OPEN_BR, TK_CLOSE_BR, TK_COMMA, TK_DOT
        ), lexList(
                "(){}[],."
        ));
    }

    @Test
    void keywords() throws IOException, LexerException {
        assertIterableEquals(asList(
                TK_IF, TK_ELSE, TK_WHILE, TK_FOR,
                TK_LET, TK_DO, TK_BREAK, TK_CONTINUE,
                TK_TRUE, TK_FALSE, TK_IMPORT, TK_AS
        ), lexList(
                "if else while for let do break continue true false import as"
        ));
    }

    @Test
    void sequences() throws IOException, LexerException {
        assertEquals(TK_ID, lex("_keyword").next());
        assertEquals("keyword", lexNext("keyword").sval);

        assertEquals(TK_STRING, lex("\"string\"").next());
        assertEquals("string", lexNext("\"string\"").sval);

        assertEquals(TK_STRING, lex("\"123\\n\\r\\t\\f\\\\\"").next());
        assertEquals("123\n\r\t\f\\", lexNext("\"123\\n\\r\\t\\f\\\\\"").sval);

        assertEquals(TK_NUMBER, lex("123.123").next());
        assertEquals("123.123", lexNext("123.123").sval);

        assertThrows(LexerException.class, () -> lexNext("\"\n\""));
        assertThrows(LexerException.class, () -> lexNext("\"\r\""));
        assertThrows(LexerException.class, () -> lexNext("\"\\w\""));
    }

}
