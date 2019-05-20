package com.bunjlabs.largo.compiler.parser;

import com.bunjlabs.largo.compiler.lexer.Lexer;
import com.bunjlabs.largo.compiler.lexer.LexerException;
import com.bunjlabs.largo.compiler.lexer.Token;
import com.bunjlabs.largo.compiler.parser.nodes.Node;
import com.bunjlabs.largo.compiler.parser.nodes.OperatorType;
import com.bunjlabs.largo.compiler.parser.nodes.RootNode;

import java.io.IOException;
import java.io.Reader;

import static com.bunjlabs.largo.compiler.Utils.*;
import static com.bunjlabs.largo.compiler.lexer.Token.*;
import static com.bunjlabs.largo.compiler.parser.nodes.NodeType.*;
import static com.bunjlabs.largo.compiler.parser.nodes.OperatorType.*;

public class Parser {

    private static final int UNARY_PRIORITY = 12;

    private final Lexer l;
    private Token token;

    public Parser(Reader r) throws IOException {
        this.l = new Lexer(r);
    }

    public RootNode parse() throws LexerException, ParserException {
        lexerNext();

        return new RootNode(statementList());
    }

    private Node expressionList() throws LexerException, ParserException {
        if (token == TK_CLOSE_PAR) {
            return new Node(ND_EMPTY);
        }

        Node list = new Node(ND_EXPR_LIST);
        Node curr = list;

        boolean single = true;

        for (; ; ) {
            Node expr = expression();
            curr.setChild(0, expr);
            if (token == TK_CLOSE_PAR) {
                if (single) {
                    return expr;
                } else {
                    curr.setChild(1, new Node(ND_EMPTY));
                    return list;
                }
            }
            if (token != TK_COMMA) {
                throw expectedTokenException(l, token, TK_COMMA);
            }
            lexerNext();

            single = false;

            Node nextList = new Node(ND_EXPR_LIST);
            setNodeLine(curr, l);
            curr.setChild(1, nextList);
            curr = nextList;
        }
    }

    private Node simpleExpression() throws LexerException, ParserException {
        int ln = l.line, cn = l.column;

        Node node;
        switch (token) {
            case TK_OPEN_PAR:
                lexerNext();
                node = expressionList();
                break;
            case TK_NUMBER:
                node = new Node(ND_NUMBER);
                node.setString(l.sval);
                setNodeLine(node, ln, cn);
                break;
            case TK_TRUE:
            case TK_FALSE:
                node = new Node(ND_BOOL);
                node.setString(token == TK_TRUE ? "true" : "false");
                setNodeLine(node, ln, cn);
                break;
            case TK_NULL:
                node = new Node(ND_NULL);
                setNodeLine(node, ln, cn);
                break;
            case TK_UNDEFINED:
                node = new Node(ND_UNDEFINED);
                setNodeLine(node, ln, cn);
                break;
            case TK_STRING:
                node = new Node(ND_STRING);
                node.setString(l.sval);
                setNodeLine(node, ln, cn);
                break;
            case TK_ID:
                node = new Node(ND_ID);
                node.setString(l.sval);


                setNodeLine(node, ln, cn);

                lexerNext();
                if (token == TK_DPLUS) {
                    lexerNext();
                    node = new Node(ND_UNOP_EXPR, OP_DPOSTPLUS, node);
                } else if (token == TK_DMINUS) {
                    lexerNext();
                    node = new Node(ND_UNOP_EXPR, OP_DPOSTMINUS, node);
                }
                setNodeLine(node, ln, cn);
                return node;
            default: {
                throw unexpectedTokenException(l, token);
            }
        }

        lexerNext();

        return node;
    }

    private Node subExpression(int priority) throws LexerException, ParserException {
        Node leftExpr;

        OperatorType uop = getUOp(token);

        if (uop == OP_DPREPLUS || uop == OP_DPREMINUS) {
            lexerNext();
            leftExpr = new Node(ND_UNOP_EXPR, uop, simpleExpression());
        } else if (uop != OP_UNKNOWN) {
            lexerNext();
            leftExpr = new Node(ND_UNOP_EXPR, uop, subExpression(UNARY_PRIORITY));
        } else {
            leftExpr = simpleExpression();
        }

        for (; ; ) {
            OperatorType bop = getBOp(token);

            if (bop == OP_UNKNOWN) {
                return leftExpr;
            }

            if (priority > bop.getPriority()) return leftExpr;

            if (bop == OP_ARROW_FUNC) {
                lexerNext();

                Node rightExpr = subStatement();

                leftExpr = new Node(ND_FUNC_DEF, bop, leftExpr, rightExpr);
            } else if (bop == OP_FIELD_SEL) {
                lexerNext();

                if (token != TK_ID) throw expectedTokenException(l, token, TK_ID);

                Node rightExpr = new Node(ND_ID);
                rightExpr.setString(l.sval);
                setNodeLine(rightExpr, l);

                lexerNext();

                leftExpr = new Node(ND_FIELD_SEL, bop, leftExpr, rightExpr);
            } else if (bop == OP_CALL) {
                lexerNext();

                Node rightExpr = expressionList();

                lexerNext();

                leftExpr = new Node(ND_CALL, bop, leftExpr, rightExpr);
            } else {
                lexerNext();

                Node rightExpr = subExpression(bop.getPriority());

                leftExpr = new Node(ND_BINOP_EXPR, bop, leftExpr, rightExpr);
            }

        }
    }

    private Node expression() throws LexerException, ParserException {
        return subExpression(0);
    }

    private Node expressionStatement() throws LexerException, ParserException {
        Node exprStmt = new Node(ND_EXPR_STMT);
        Node curr = exprStmt;

        for (; ; ) {
            curr.setChild(0, expression());

            if (token == TK_COMMA) {
                lexerNext();

                Node nextExprStmt = new Node(ND_EXPR_STMT);
                curr.setChild(1, nextExprStmt);
                curr = nextExprStmt;

                continue;
            } else {
                curr.setChild(1, new Node(ND_EMPTY));

                return exprStmt;
            }
        }
    }

    private Node initVar() throws LexerException, ParserException {
        Node initNode = new Node(ND_VARINIT_STMT);
        Node curr = initNode;

        setNodeLine(curr, l);

        for (; ; ) {
            lexerNext();

            if (token != TK_ID) {
                throw expectedTokenException(l, token, TK_ID);
            }

            curr.setChild(0, new Node(ND_EMPTY));
            curr.setString(l.sval);

            lexerNext();

            if (token == TK_END_STMT) {
                curr.setChild(1, new Node(ND_EMPTY));
                return initNode;
            }

            if (token == TK_ASSIGN) {
                lexerNext();
                curr.setChild(0, expression());
            }

            if (token == TK_COMMA) {
                Node nextInitNode = new Node(ND_VARINIT_STMT);
                curr.setChild(1, nextInitNode);
                curr = nextInitNode;
                setNodeLine(curr, l);

                continue;
            }
            if (token != TK_END_STMT) {
                throw expectedTokenException(l, token, TK_END_STMT);
            }

            curr.setChild(1, new Node(ND_EMPTY));
            return initNode;
        }
    }

    private Node ifStatement() throws LexerException, ParserException {
        int ln = l.line, cn = l.column;

        lexerNext();

        if (token != TK_OPEN_PAR) {
            throw expectedTokenException(l, token, TK_OPEN_PAR);
        }
        lexerNext();

        Node cond = expression();

        if (token != TK_CLOSE_PAR) {
            throw expectedTokenException(l, token, TK_CLOSE_PAR);
        }
        lexerNext();

        Node stmt = statement();

        Node node;
        if (token == TK_ELSE) {
            lexerNext();
            Node stmtelse = statement();
            node = new Node(ND_IFELSE_STMT, cond, stmt, stmtelse);
        } else {
            node = new Node(ND_IF_STMT, cond, stmt);
        }

        setNodeLine(node, ln, cn);
        return node;
    }

    private Node whileStatement() throws LexerException, ParserException {
        int ln = l.line, cn = l.column;

        lexerNext();

        if (token != TK_OPEN_PAR) {
            throw expectedTokenException(l, token, TK_OPEN_PAR);
        }
        lexerNext();

        Node cond = expression();

        if (token != TK_CLOSE_PAR) {
            throw expectedTokenException(l, token, TK_CLOSE_PAR);
        }
        lexerNext();

        Node stmt = statement();

        Node node = new Node(ND_WHILE_STMT, cond, stmt);
        setNodeLine(node, ln, cn);

        return node;
    }

    private Node forStatement() throws LexerException, ParserException {
        int ln = l.line, cn = l.column;

        lexerNext();

        if (token != TK_OPEN_PAR) {
            throw expectedTokenException(l, token, TK_OPEN_PAR);
        }
        lexerNext();

        Node preExpr;
        if (token == TK_LET) {
            preExpr = initVar();
        } else if (token == TK_END_STMT) {
            preExpr = new Node(ND_EMPTY);
        } else {
            preExpr = expressionStatement();
        }

        if (token != TK_END_STMT) {
            throw expectedTokenException(l, token, TK_END_STMT);
        }
        lexerNext();

        Node cond;
        if (token == TK_END_STMT) {
            cond = new Node(ND_EMPTY);
        } else {
            cond = expression();
        }

        if (token != TK_END_STMT) {
            throw expectedTokenException(l, token, TK_END_STMT);
        }
        lexerNext();

        Node postExpr;
        if (token == TK_CLOSE_PAR) {
            postExpr = new Node(ND_EMPTY);
        } else {
            postExpr = expressionStatement();
        }

        if (token != TK_CLOSE_PAR) {
            throw expectedTokenException(l, token, TK_CLOSE_PAR);
        }
        lexerNext();

        Node stmt = statement();

        Node node = new Node(ND_FOR_STMT, preExpr, cond, postExpr, stmt);
        setNodeLine(node, ln, cn);

        return node;
    }

    private Node importStatement() throws LexerException, ParserException {
        lexerNext();

        if (token != TK_ID) {
            throw expectedTokenException(l, token, TK_ID);
        }

        Node node = new Node(ND_IMPORT);
        node.setString(l.sval);

        Node id = new Node(ND_ID);
        id.setString(l.sval);
        node.setChild(0, id);

        lexerNext();

        if (token == TK_AS) {
            lexerNext();
            if (token != TK_ID) {
                throw expectedTokenException(l, token, TK_ID);
            }

            id.setString(l.sval);

            lexerNext();
        }

        return node;
    }

    private Node block() throws LexerException, ParserException {
        lexerNext();

        Node node = statementList();

        if (token != TK_CLOSE_CS) {
            throw expectedTokenException(l, token, TK_CLOSE_CS);
        }

        lexerNext();

        return node;
    }

    private Node subStatement() throws LexerException, ParserException {
        Node node;

        if (token == TK_OPEN_CS) {
            node = block();
        } else {
            node = expressionStatement();
        }

        return node;
    }

    private Node statement() throws LexerException, ParserException {
        Node node = null;

        switch (token) {
            case TK_END_STMT:
                lexerNext();
                break;
            case TK_OPEN_CS:
                node = block();
                break;
            case TK_IMPORT:
                node = importStatement();
                if (token != TK_END_STMT) {
                    throw expectedTokenException(l, token, TK_END_STMT);
                }
                lexerNext();
                break;
            case TK_LET:
                node = initVar();
                if (token != TK_END_STMT) {
                    throw expectedTokenException(l, token, TK_END_STMT);
                }
                lexerNext();
                break;
            case TK_IF:
                node = ifStatement();
                break;
            case TK_WHILE:
                node = whileStatement();
                break;
            case TK_FOR:
                node = forStatement();
                break;
            case TK_BREAK:
                node = new Node(ND_BREAK);
                setNodeLine(node, l);
                lexerNext();
                if (token != TK_END_STMT) {
                    throw expectedTokenException(l, token, TK_END_STMT);
                }
                lexerNext();
                break;
            case TK_CONTINUE:
                node = new Node(ND_CONTINUE);
                setNodeLine(node, l);
                lexerNext();
                if (token != TK_END_STMT) {
                    throw expectedTokenException(l, token, TK_END_STMT);
                }
                lexerNext();
                break;
            default:
                node = subStatement();
                if (token != TK_END_STMT) {
                    throw expectedTokenException(l, token, TK_END_STMT);
                } else {
                    lexerNext();
                }
                break;
        }

        if (node == null) {
            throw unexpectedTokenException(l, token);
        }

        return node;
    }

    private Node statementList() throws LexerException, ParserException {
        Node node = new Node(ND_STMT_LIST);
        Node curr = node;

        if (token == TK_EOS) return node;
        for (; ; ) {
            curr.setChild(0, statement());

            if (token == TK_EOS || token == TK_CLOSE_CS) {
                curr.setChild(1, new Node(ND_EMPTY));
                return node;
            }

            Node child = new Node(ND_STMT_LIST);
            curr.setChild(1, curr = child);
        }
    }

    private void lexerNext() throws LexerException {
        this.token = l.next();
    }
}
