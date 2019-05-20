package com.bunjlabs.largo.compiler.codegen;

import com.bunjlabs.largo.compiler.SemanticException;
import com.bunjlabs.largo.compiler.parser.nodes.Node;
import com.bunjlabs.largo.compiler.parser.nodes.OperatorType;
import com.bunjlabs.largo.compiler.parser.nodes.RootNode;
import com.bunjlabs.largo.compiler.semantic.SemanticInfo;
import com.bunjlabs.largo.runtime.OpCode;
import com.bunjlabs.largo.runtime.OpCodeType;
import com.bunjlabs.largo.runtime.Program;
import com.bunjlabs.largo.types.LargoValue;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import static com.bunjlabs.largo.compiler.Utils.*;
import static com.bunjlabs.largo.compiler.parser.nodes.NodeType.*;
import static com.bunjlabs.largo.compiler.parser.nodes.OperatorType.*;
import static com.bunjlabs.largo.runtime.OpCodeType.*;

public class CodeGenerator {

    private final List<OpCode> opCodes = new LinkedList<>();
    private final Stack<FlowBreak> breaks = new Stack<>();
    private final Stack<FlowBreak> continues = new Stack<>();

    private int loopLevel = 0;

    private OpCode putOpCode(OpCodeType opCodeType) {
        OpCode opCode = new OpCode(opCodeType);
        opCodes.add(opCode);

        return opCode;
    }

    private OpCode putOpCode(OpCodeType opCodeType, int arg) {
        OpCode opCode = new OpCode(opCodeType, arg);
        opCodes.add(opCode);

        return opCode;
    }

    private void putBreak() {
        breaks.push(new FlowBreak(opCodes.get(opCodes.size() - 1), opCodes.size() - 1, loopLevel));
    }

    private void putContinue() {
        continues.push(new FlowBreak(opCodes.get(opCodes.size() - 1), opCodes.size() - 1, loopLevel));
    }

    private void processBrCont(int back) {
        while (!breaks.isEmpty() && breaks.peek().getLoopLevel() == loopLevel) {
            FlowBreak br = breaks.pop();
            br.getOpcode().setArg(opCodes.size() - br.getPos());
        }
        while (!continues.isEmpty() && continues.peek().getLoopLevel() == loopLevel) {
            FlowBreak br = continues.pop();
            br.getOpcode().setArg(back - br.getPos() + 2); // 2 because cur op is JMP
        }
    }

    private void generateFunctionDefinition(Node node) throws SemanticException {

    }

    private void generateFunctionCall(Node node) throws SemanticException {
        Node expr = node.getChild(0);

        generateExpression(expr);

        Node args = node.getChild(1);

        int argCount;
        if (checkNodeType(args, ND_EMPTY)) {
            argCount = 0;
        } else if (checkNodeType(args, ND_EXPR_LIST)) {
            argCount = generateExpressionList(args);
        } else {
            generateExpression(args);
            argCount = 1;
        }

        putOpCode(L_CALL, argCount);
    }

    private void generateFieldSelect(Node node) throws SemanticException {
        Node expr = node.getChild(0);
        generateExpression(expr);

        Node id = node.getChild(1);

        putOpCode(L_GETNAME, id.getArgument());
    }

    private void generateBinaryOperator(Node node, boolean isStmt) throws SemanticException {
        Node leftNode = node.getChild(0);
        Node rightNode = node.getChild(1);

        if (node.getOperator() == OP_ASSIGN) {
            generateExpression(rightNode);
            putOpCode(isStmt ? L_SETLOCAL_S : L_SETLOCAL, leftNode.getArgument());
            return;
        }

        generateExpression(leftNode);
        generateExpression(rightNode);

        switch (node.getOperator()) {
            case OP_PLUSEQ:
            case OP_MINUSEQ:
            case OP_MULTEQ:
            case OP_DIVEQ:
            case OP_MODEQ:
                if (node.getOperator() == OP_PLUSEQ) {
                    putOpCode(L_ADD);
                } else if (node.getOperator() == OP_MINUSEQ) {
                    putOpCode(L_SUB);
                } else if (node.getOperator() == OP_MULTEQ) {
                    putOpCode(L_MUL);
                } else if (node.getOperator() == OP_DIVEQ) {
                    putOpCode(L_DIV);
                } else {
                    putOpCode(L_MOD);
                }

                putOpCode(isStmt ? L_SETLOCAL_S : L_SETLOCAL, leftNode.getArgument());
                return;
            case OP_PLUS:
                putOpCode(L_ADD);
                break;
            case OP_MINUS:
                putOpCode(L_SUB);
                break;
            case OP_MULT:
                putOpCode(L_MUL);
                break;
            case OP_DIV:
                putOpCode(L_DIV);
                break;
            case OP_MOD:
                putOpCode(L_MOD);
                break;
            case OP_LAND:
                putOpCode(L_AND);
                break;
            case OP_LOR:
                putOpCode(L_OR);
                break;
            case OP_LNOT:
                putOpCode(L_NOT);
                break;
            case OP_EQ:
                putOpCode(L_EQ);
                break;
            case OP_NOTEQ:
                putOpCode(L_NOTEQ);
                break;
            case OP_GREAT:
                putOpCode(L_GREAT);
                break;
            case OP_GREATEQ:
                putOpCode(L_GREATEQ);
                break;
            case OP_LESS:
                putOpCode(L_LESS);
                break;
            case OP_LESSEQ:
                putOpCode(L_LESSEQ);
                break;
            default:
                throw unexpectedNodeException(node);
        }

        if (isStmt) putOpCode(L_POP);
    }

    private void generateUnaryOperator(Node node, boolean isStmt) throws SemanticException {
        Node rightNode = node.getChild(0);

        switch (node.getOperator()) {
            case OP_DPREPLUS:
            case OP_DPREMINUS:
            case OP_DPOSTPLUS:
            case OP_DPOSTMINUS:
                int id = rightNode.getArgument();
                if (node.getOperator() == OperatorType.OP_DPREPLUS || node.getOperator() == OperatorType.OP_DPREMINUS) {
                    putOpCode(L_GETLOCAL, id);
                    putOpCode(node.getOperator() == OperatorType.OP_DPREPLUS ? L_INC : L_DEC);
                    putOpCode(isStmt ? L_SETLOCAL_S : L_SETLOCAL, id);
                } else {
                    putOpCode(L_GETLOCAL, id);
                    putOpCode(node.getOperator() == OperatorType.OP_DPOSTPLUS ? L_INC : L_DEC);
                    putOpCode(isStmt ? L_SETLOCAL_S : L_SETLOCAL, id);
                }
                return;
            case OP_LNOT:
                generateExpression(rightNode);
                putOpCode(L_NOT);
                break;
            case OP_MINUS:
                generateExpression(rightNode);
                putOpCode(L_NEG);
                break;
            case OP_PLUS:
                generateExpression(rightNode);
                putOpCode(L_POS);
                break;
            default:
                throw unexpectedNodeException(node);
        }

        if (isStmt) putOpCode(L_POP);
    }

    private void generateExpression(Node node) throws SemanticException {
        generateExpression(node, false);
    }

    private void generateExpression(Node node, boolean isStmt) throws SemanticException {
        switch (node.getType()) {
            case ND_BINOP_EXPR:
                generateBinaryOperator(node, isStmt);
                return;
            case ND_UNOP_EXPR:
                generateUnaryOperator(node, isStmt);
                return;
            case ND_ID:
                putOpCode(L_GETLOCAL, node.getArgument());
                break;
            case ND_NUMBER:
            case ND_STRING:
                putOpCode(L_CONST, node.getArgument());
                break;
            case ND_BOOL:
                putOpCode(node.getArgument() > 0 ? L_TRUE : L_FALSE);
                break;
            case ND_NULL:
                putOpCode(L_NULL);
                break;
            case ND_UNDEFINED:
                putOpCode(L_UNDEFINED);
                break;
            case ND_CALL:
                generateFunctionCall(node);
                break;
            case ND_FUNC_DEF:
                generateFunctionDefinition(node);
                break;
            case ND_FIELD_SEL:
                generateFieldSelect(node);
                break;
            default:
                throw unexpectedNodeException(node);
        }

        if (isStmt) putOpCode(L_POP);
    }

    private int generateExpressionList(Node node) throws SemanticException {
        Node curr = node;

        int count = 0;
        while (!checkNodeType(curr, ND_EMPTY)) {
            generateExpression(curr.getChild(0));
            curr = curr.getChild(1);
            count++;
        }

        return count;
    }

    private void generateExpressionStatement(Node node) throws SemanticException {
        Node curr = node;

        while (!checkNodeType(curr, ND_EMPTY)) {
            generateExpression(curr.getChild(0), true);
            curr = curr.getChild(1);
        }
    }

    private void generateVariableInit(Node node) throws SemanticException {
        int id = node.getArgument();

        Node initNode = node.getChild(0);
        if (!checkNodeType(initNode, ND_EMPTY)) {
            generateExpression(initNode);
            putOpCode(L_SETLOCAL_S, id);
        }

        Node nextVar = node.getChild(1);
        if (!checkNodeType(nextVar, ND_EMPTY)) {
            generateVariableInit(nextVar);
        }
    }

    private void generateIf(Node node) throws SemanticException {
        generateExpression(node.getChild(0));

        OpCode jmp = putOpCode(L_JUMP_F);
        int cur = opCodes.size();

        generateStatement(node.getChild(1));
        jmp.setArg(opCodes.size() - cur);
    }

    private void generateIfElse(Node node) throws SemanticException {
        generateExpression(node.getChild(0));

        OpCode jmpf = putOpCode(L_JUMP_F);
        int curf = opCodes.size();

        generateStatement(node.getChild(1));

        OpCode jmp = putOpCode(L_JUMP);
        int cur = opCodes.size();

        generateStatement(node.getChild(2));
        jmpf.setArg(opCodes.size() - curf);
        jmp.setArg(opCodes.size() - cur);
    }

    private void generateBreak(Node node) throws SemanticException {
        if (loopLevel <= 0) throw semanticException(node, "break not in loop");
        putOpCode(L_JUMP);
        putBreak();
    }

    private void generateContinue(Node node) throws SemanticException {
        if (loopLevel <= 0) throw semanticException(node, "continue not in loop");
        putOpCode(L_JUMP);
        putContinue();
    }

    private void generateWhile(Node node) throws SemanticException {
        loopLevel++;

        int curback = opCodes.size();

        generateExpression(node.getChild(0));

        OpCode jmp = putOpCode(L_JUMP_F);

        int cur = opCodes.size();

        generateStatement(node.getChild(1));

        putOpCode(L_JUMP, curback - opCodes.size() + 1);
        jmp.setArg(opCodes.size() - cur);

        processBrCont(curback);

        loopLevel--;
    }

    private void generateFor(Node node) throws SemanticException {
        loopLevel++;
        Node preExpr = node.getChild(0);
        if (!checkNodeType(preExpr, ND_EMPTY)) {
            if (preExpr.getType() == ND_VARINIT_STMT) {
                generateVariableInit(preExpr);
            } else {
                generateExpressionStatement(preExpr);
                putOpCode(L_POP);
            }
        }
        int curback = opCodes.size();

        OpCode jmp = null;
        Node condExpr = node.getChild(1);
        if (!checkNodeType(condExpr, ND_EMPTY)) {
            generateExpression(condExpr);
            jmp = putOpCode(L_JUMP_F);
        }

        int cur = opCodes.size();
        Node stmt = node.getChild(3);
        generateStatement(stmt);

        Node postExpr = node.getChild(2);
        if (!checkNodeType(postExpr, ND_EMPTY)) {
            generateExpressionStatement(postExpr);
        }
        putOpCode(L_JUMP, curback - opCodes.size() + 1);
        if (jmp != null) jmp.setArg(opCodes.size() - cur);

        processBrCont(curback);
        loopLevel--;
    }

    private void generateImport(Node node) throws SemanticException {
        putOpCode(L_IMPORT, node.getArgument());
        putOpCode(L_SETLOCAL_S, node.getChild(0).getArgument());
    }

    private void generateStatement(Node node) throws SemanticException {
        switch (node.getType()) {
            case ND_STMT_LIST:
                generateStatementList(node);
                return;
            case ND_EMPTY:
                return;
            case ND_IMPORT:
                generateImport(node);
                return;
            case ND_VARINIT_STMT:
                generateVariableInit(node);
                return;
            case ND_IF_STMT:
                generateIf(node);
                return;
            case ND_IFELSE_STMT:
                generateIfElse(node);
                return;
            case ND_WHILE_STMT:
                generateWhile(node);
                return;
            case ND_FOR_STMT:
                generateFor(node);
                return;
            case ND_CALL:
                generateFunctionCall(node);
                return;
            case ND_BREAK:
                generateBreak(node);
                return;
            case ND_CONTINUE:
                generateContinue(node);
                return;
            case ND_EXPR_STMT:
                generateExpressionStatement(node);
                return;
            default:
                throw unexpectedNodeException(node);
        }
    }

    private void generateStatementList(Node node) throws SemanticException {
        Node curr = node;
        for (; ; ) {
            Node leftStmt = curr.getChild(0);
            if (leftStmt != null) {
                generateStatement(leftStmt);
            }

            Node rightStmt = curr.getChild(1);
            if (rightStmt == null) return;
            if (rightStmt.getType() == ND_STMT_LIST) {
                curr = rightStmt;
            } else {
                generateStatement(rightStmt);
                break;
            }
        }
    }

    public Program generate(RootNode root, SemanticInfo semanticInfo) throws SemanticException {
        if (root.getType() != ND_ROOT) {
            throw semanticException(root, "Root node is not a " + ND_ROOT);
        }

        generateStatementList(root);

        LargoValue[] constPool = semanticInfo.getConstPool();
        return new Program(opCodes.toArray(new OpCode[0]), constPool, semanticInfo.getLocalVariablesCount());
    }
}
