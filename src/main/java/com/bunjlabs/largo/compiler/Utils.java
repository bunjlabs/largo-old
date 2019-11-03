package com.bunjlabs.largo.compiler;

import com.bunjlabs.largo.compiler.lexer.Lexer;
import com.bunjlabs.largo.compiler.lexer.Token;
import com.bunjlabs.largo.compiler.parser.ParserException;
import com.bunjlabs.largo.compiler.parser.nodes.Node;
import com.bunjlabs.largo.compiler.parser.nodes.NodeType;
import com.bunjlabs.largo.compiler.parser.nodes.OperatorType;

import static com.bunjlabs.largo.compiler.parser.nodes.OperatorType.*;

public abstract class Utils {

    public static ParserException expectedTokenException(Lexer l, Token unexpected, Token expected) {
        return new ParserException(l, "Unexpected symbol: " + unexpected + " expected: " + expected);
    }

    public static ParserException unexpectedTokenException(Lexer l, Token unexpected) {
        return new ParserException(l, "Unexpected symbol: " + unexpected);
    }

    public static ParserException unexpectedTokenException(Lexer l, Token unexpected, String explain) {
        return new ParserException(l, "Unexpected symbol: " + unexpected + ". " + explain);
    }

    public static SemanticException unexpectedNodeException(Node node) {
        return new SemanticException(node, "Unexpected node");
    }

    public static SemanticException semanticException(Node node, String msg) {
        return new SemanticException(node, msg);
    }

    public static SemanticException undefinedException(Node node, String var) {
        return new SemanticException(node, "Undefined variable: " + var);
    }

    public static boolean checkNodeChildsForNull(Node node, int childNumber) {
        if (childNumber > Node.MAX_CHILDS) return false;

        for (int i = 0; i < childNumber; i++) {
            if (node.getChild(i) == null) return false;
        }

        return true;
    }


    public static boolean checkNodeType(Node node, NodeType... types) {
        for (NodeType type : types) {
            if (node.getType() == type) return true;
        }
        return false;
    }

    public static boolean checkNodeOperator(Node node, OperatorType... types) {
        for (OperatorType type : types) {
            if (node.getOperator() == type) return true;
        }
        return false;
    }

    public static OperatorType getBOp(Token token) {
        switch (token) {
            case TK_ASSIGN:
                return OP_ASSIGN;
            case TK_PLUSEQ:
                return OP_PLUSEQ;
            case TK_MINUSEQ:
                return OP_MINUSEQ;
            case TK_MULTEQ:
                return OP_MULTEQ;
            case TK_DIVEQ:
                return OP_DIVEQ;
            case TK_MODEQ:
                return OP_MODEQ;
            case TK_PLUS:
                return OP_PLUS;
            case TK_MINUS:
                return OP_MINUS;
            case TK_MULT:
                return OP_MULT;
            case TK_DIV:
                return OP_DIV;
            case TK_MOD:
                return OP_MOD;
            case TK_DAND:
                return OP_LAND;
            case TK_DOR:
                return OP_LOR;
            case TK_NOT:
                return OP_LNOT;
            case TK_EQ:
                return OP_EQ;
            case TK_NOTEQ:
                return OP_NOTEQ;
            case TK_GREAT:
                return OP_GREAT;
            case TK_GREATEQ:
                return OP_GREATEQ;
            case TK_LESS:
                return OP_LESS;
            case TK_LESSEQ:
                return OP_LESSEQ;
            case TK_OPEN_PAR:
                return OP_CALL;
            case TK_OPEN_BR:
                return OP_INDEX_SEL;
            case TK_DOT:
                return OP_FIELD_SEL;
            case TK_ARROW:
                return OP_ARROW_FUNC;
            default:
                return OP_UNKNOWN;
        }
    }

    public static OperatorType getUOp(Token token) {
        switch (token) {
            case TK_PLUS:
                return OP_PLUS;
            case TK_MINUS:
                return OP_MINUS;
            case TK_NOT:
                return OP_LNOT;
            case TK_DPLUS:
                return OP_DPREPLUS;
            case TK_DMINUS:
                return OP_DPREMINUS;
            default:
                return OP_UNKNOWN;
        }
    }

    public static void setNodeLine(Node node, Lexer lexer) {
        setNodeLine(node, lexer.line, lexer.column);
    }

    public static void setNodeLine(Node node, int lineNumber, int columnNumber) {
        node.setLineNumber(lineNumber);
        node.setColumnNumber(columnNumber);
    }
}
