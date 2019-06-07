package com.bunjlabs.largo.compiler.lexer;

import java.io.IOException;
import java.io.Reader;

import static com.bunjlabs.largo.compiler.lexer.Token.*;

public class Lexer {
    private final Reader r;
    public String sval = null;
    public int line = 1;
    public int column = -1;
    private int curr = -1;

    public Lexer(Reader reader) throws IOException {
        this.r = reader;
        skip();
    }

    public Token next() throws LexerException {
        sval = null;

        try {
            for (; ; ) {
                if (currIsEos()) return TK_EOS;

                switch (curr) {
                    case '\r':
                    case '\n':
                        incLine();
                        break;
                    case ' ':
                    case '\f':
                    case '\t':
                        skip();
                        break;
                    case '/':
                        skip();
                        if (curr == '/' || curr == '*') {
                            comment();
                            break;
                        } else if (curr == '=') {
                            skip();
                            return TK_DIVEQ;
                        } else {
                            return TK_DIV;
                        }
                    case '=':
                        skip();
                        if (curr == '=') {
                            skip();
                            return TK_EQ;
                        } else {
                            return TK_ASSIGN;
                        }
                    case ';':
                        skip();
                        return TK_END_STMT;
                    case '+': {
                        skip();
                        if (curr == '+') {
                            skip();
                            return TK_DPLUS;
                        } else if (curr == '=') {
                            skip();
                            return TK_PLUSEQ;
                        }
                        return TK_PLUS;
                    }
                    case '-': {
                        skip();
                        if (curr == '-') {
                            skip();
                            return TK_DMINUS;
                        } else if (curr == '=') {
                            skip();
                            return TK_MINUSEQ;
                        } else if (curr == '>') {
                            skip();
                            return TK_ARROW;
                        }
                        return TK_MINUS;
                    }
                    case '*': {
                        skip();
                        if (curr == '=') {
                            skip();
                            return TK_MULTEQ;
                        }
                        return TK_MULT;
                    }
                    case '%': {
                        skip();
                        if (curr == '=') {
                            skip();
                            return TK_MODEQ;
                        }
                        return TK_MOD;
                    }
                    case '&': {
                        skip();
                        if (curr == '&') {
                            skip();
                            return TK_DAND;
                        }
                        return TK_AND;
                    }
                    case '|': {
                        skip();
                        if (curr == '|') {
                            skip();
                            return TK_DOR;
                        }
                        return TK_OR;
                    }
                    case '!': {
                        skip();
                        if (curr == '=') {
                            skip();
                            return TK_NOTEQ;
                        }
                        return TK_NOT;
                    }
                    case '>': {
                        skip();
                        if (curr == '=') {
                            skip();
                            return TK_GREATEQ;
                        }
                        return TK_GREAT;
                    }
                    case '<': {
                        skip();
                        if (curr == '=') {
                            skip();
                            return TK_LESSEQ;
                        }
                        return TK_LESS;
                    }
                    case '(': {
                        skip();
                        return TK_OPEN_PAR;
                    }
                    case ')': {
                        skip();
                        return TK_CLOSE_PAR;
                    }
                    case '{': {
                        skip();
                        return TK_OPEN_CS;
                    }
                    case '}': {
                        skip();
                        return TK_CLOSE_CS;
                    }
                    case '[': {
                        skip();
                        return TK_OPEN_BR;
                    }
                    case ']': {
                        skip();
                        return TK_CLOSE_BR;
                    }
                    case ',': {
                        skip();
                        return TK_COMMA;
                    }
                    case '.': {
                        skip();
                        return TK_DOT;
                    }
                    case '"':
                    case '\'':
                        nextString(curr);
                        return TK_STRING;
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                        nextNumber();
                        return TK_NUMBER;
                    default: {
                        if (currIsAlpha()) {
                            nextWord();

                            switch (sval) {
                                case "import":
                                    return TK_IMPORT;
                                case "as":
                                    return TK_AS;
                                case "if":
                                    return TK_IF;
                                case "else":
                                    return TK_ELSE;
                                case "while":
                                    return TK_WHILE;
                                case "for":
                                    return TK_FOR;
                                case "let":
                                    return TK_LET;
                                case "do":
                                    return TK_DO;
                                case "break":
                                    return TK_BREAK;
                                case "continue":
                                    return TK_CONTINUE;
                                case "return":
                                    return TK_RETURN;
                                case "true":
                                    return TK_TRUE;
                                case "false":
                                    return TK_FALSE;
                                case "undefined":
                                    return TK_UNDEFINED;
                                case "null":
                                    return TK_NULL;
                                default:
                                    return TK_ID;
                            }
                        } else {
                            char ct = (char) curr;
                            skip();
                            throw new LexerException(this, "Unexpected symbol: " + ct);
                        }
                    }
                }
            }
        } catch (IOException ex) {
            sval = ex.getLocalizedMessage();
            return TK_ERROR;
        }
    }

    private void nextNumber() throws IOException {
        StringBuilder sb = new StringBuilder();

        boolean isDouble = false;
        while (currIsDigit() || (!isDouble && curr == '.')) {
            if (curr == '.') isDouble = true;
            sb.append((char) curr);
            skip();
        }

        sval = sb.toString();
    }

    private void nextString(int delimiter) throws IOException, LexerException {
        StringBuilder sb = new StringBuilder();
        skip();
        while (curr != delimiter) {
            switch (curr) {
                case -1:
                case '\n':
                case '\r':
                    throw new LexerException(this, "Unfinished string");
                case '\\':
                    skip();
                    if (curr == 'n') {
                        sb.append('\n');
                    } else if (curr == 'r') {
                        sb.append('\r');
                    } else if (curr == 't') {
                        sb.append('\t');
                    } else if (curr == 'f') {
                        sb.append('\f');
                    } else if (curr == '\\') {
                        sb.append('\\');
                    } else {
                        throw new LexerException(this, "Unsupported char escape sequence: \\" + ((char) curr));
                    }
                    break;
                default:
                    sb.append((char) curr);
                    break;
            }
            skip();
        }
        skip();
        sval = sb.toString();
    }

    private void nextWord() throws IOException {
        StringBuilder sb = new StringBuilder();

        while (currIsAlpha() || currIsDigit()) {
            sb.append((char) curr);
            skip();
        }
        sval = sb.toString();
    }


    private void comment() throws IOException, LexerException {
        if (curr == '*') {
            skip();
            for (; ; ) {
                if (curr == '*') {
                    skip();
                    if (curr == '/') {
                        skip();
                        break;
                    }
                } else if (currIsNewline()) {
                    incLine();
                } else if (currIsEos()) {
                    break;
                } else {
                    skip();
                }
            }
        } else {
            while (!currIsNewline() && !currIsEos()) {
                skip();
            }
        }
    }

    private boolean currIsDigit() {
        return (curr >= '0' && curr <= '9');
    }

    private boolean currIsAlpha() {
        return (curr >= 'A' && curr <= 'Z')
                || (curr >= 'a' && curr <= 'z')
                || curr == '_';
    }

    private boolean currIsNewline() {
        return (curr == '\n' || curr == '\r');
    }

    private boolean currIsEos() {
        return curr < 0;
    }

    private void skip() throws IOException {
        curr = r.read();
        column++;
    }

    private void incLine() throws IOException, LexerException {
        int old = curr;
        skip();
        if (currIsNewline() && curr != old) {
            skip();
        }
        if (++line >= Integer.MAX_VALUE) {
            throw new LexerException(this, "Input size reached limit");
        }
        column = 0;
    }

}
