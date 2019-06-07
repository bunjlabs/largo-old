package com.bunjlabs.largo.compiler.codegen;

import com.bunjlabs.largo.Blueprint;
import com.bunjlabs.largo.compiler.SemanticException;
import com.bunjlabs.largo.compiler.parser.nodes.Node;
import com.bunjlabs.largo.compiler.parser.nodes.OperatorType;
import com.bunjlabs.largo.compiler.semantic.SemanticInfo;
import com.bunjlabs.largo.runtime.OpCode;
import com.bunjlabs.largo.runtime.OpCodeType;
import com.bunjlabs.largo.types.LargoValue;

import java.util.ArrayList;
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

    private final List<Blueprint> blueprints = new ArrayList<>();
    private final SemanticInfo semanticInfo;
    private int loopLevel = 0;
    private int maxStackSize = 0;

    public CodeGenerator(SemanticInfo semanticInfo) {
        this.semanticInfo = semanticInfo;
    }

    private void reserveStack(int size) {
        if (size > maxStackSize) {
            maxStackSize = size;
        }
    }

    private OpCode putOpCode(OpCodeType opCodeType) {
        OpCode opCode = new OpCode(opCodeType);
        opCodes.add(opCode);

        return opCode;
    }

    private OpCode putOpCode(OpCodeType opCodeType, int a) {
        OpCode opCode = new OpCode(opCodeType, a);
        opCodes.add(opCode);

        return opCode;
    }

    private OpCode putOpCode(OpCodeType opCodeType, int a, int b) {
        OpCode opCode = new OpCode(opCodeType, a, b);
        opCodes.add(opCode);

        return opCode;
    }

    private OpCode putOpCode(OpCodeType opCodeType, int a, int b, int c) {
        OpCode opCode = new OpCode(opCodeType, a, b, c);
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
            br.getOpcode().a = opCodes.size() - br.getPos();
        }
        while (!continues.isEmpty() && continues.peek().getLoopLevel() == loopLevel) {
            FlowBreak br = continues.pop();
            br.getOpcode().a = back - br.getPos() + 2; // 2 because cur op is JMP
        }
    }

    private void generateFunctionDefinition(Node node, int reg) throws SemanticException {
        Node body = node.getChild(1);

        SemanticInfo[] functions = semanticInfo.getFunctions();

        SemanticInfo closureSemanticInfo = functions[node.getArgument()];
        CodeGenerator generator = new CodeGenerator(closureSemanticInfo);

        if (checkNodeType(body, ND_STMT_LIST)) {
            generator.generateStatement(body);
        } else {
            generator.generateExpressionStatement(body, 0);
            generator.putOpCode(L_RET, 0);
        }

        OpCode[] code = generator.opCodes.toArray(new OpCode[0]);
        LargoValue[] constantPool = closureSemanticInfo.getConstPool();
        int argumentsCount = closureSemanticInfo.getArgumentsCount();
        int localVariablesCount = closureSemanticInfo.getLocalVariablesCount();
        Blueprint[] blueprintsArray = generator.blueprints.toArray(new Blueprint[0]);

        Blueprint blueprint = new Blueprint(code, constantPool, argumentsCount, localVariablesCount, localVariablesCount + generator.maxStackSize, blueprintsArray);

        reserveStack(localVariablesCount + generator.maxStackSize);

        blueprints.add(blueprint);
        putOpCode(L_CLOSURE, reg, node.getArgument());
    }

    private void generateFunctionCall(Node node, int reg) throws SemanticException {
        Node expr = node.getChild(0);
        Node args = node.getChild(1);

        int argCount;
        if (checkNodeType(args, ND_EMPTY)) {
            argCount = 0;
        } else if (checkNodeType(args, ND_EXPR_LIST)) {
            argCount = generateExpressionListStack(args);
        } else {
            generateExpression(args, 0);
            putOpCode(L_PUSH, 0);
            argCount = 1;
        }

        reserveStack(argCount);

        generateExpression(expr, 0);

        putOpCode(L_CALL, reg, 0, argCount);
    }

    private void generateFieldSelect(Node node, int reg) throws SemanticException {
        Node expr = node.getChild(0);
        generateExpression(expr, 0);

        Node id = node.getChild(1);

        putOpCode(L_GETNAME, reg, 0, id.getArgument());
    }

    private void generateBinaryOperator(Node node, int reg) throws SemanticException {
        Node leftNode = node.getChild(0);
        Node rightNode = node.getChild(1);

        if (node.getOperator() == OP_ASSIGN) {
            generateExpression(rightNode, reg);
            putOpCode(L_SETLOCAL, leftNode.getArgument(), reg);
            return;
        }

        generateExpression(rightNode, 1);
        generateExpression(leftNode, 0);

        switch (node.getOperator()) {
            case OP_PLUSEQ:
            case OP_MINUSEQ:
            case OP_MULTEQ:
            case OP_DIVEQ:
            case OP_MODEQ:
                if (node.getOperator() == OP_PLUSEQ) {
                    putOpCode(L_ADD, reg, 0, 1);
                } else if (node.getOperator() == OP_MINUSEQ) {
                    putOpCode(L_SUB, reg, 0, 1);
                } else if (node.getOperator() == OP_MULTEQ) {
                    putOpCode(L_MUL, reg, 0, 1);
                } else if (node.getOperator() == OP_DIVEQ) {
                    putOpCode(L_DIV, reg, 0, 1);
                } else {
                    putOpCode(L_MOD, reg, 0, 1);
                }

                putOpCode(L_SETLOCAL, leftNode.getArgument(), reg);
                return;
            case OP_PLUS:
                putOpCode(L_ADD, reg, 0, 1);
                break;
            case OP_MINUS:
                putOpCode(L_SUB, reg, 0, 1);
                break;
            case OP_MULT:
                putOpCode(L_MUL, reg, 0, 1);
                break;
            case OP_DIV:
                putOpCode(L_DIV, reg, 0, 1);
                break;
            case OP_MOD:
                putOpCode(L_MOD, reg, 0, 1);
                break;
            case OP_LAND:
                putOpCode(L_AND, reg, 0, 1);
                break;
            case OP_LOR:
                putOpCode(L_OR, reg, 0, 1);
                break;
            case OP_EQ:
                putOpCode(L_EQ, reg, 0, 1);
                break;
            case OP_NOTEQ:
                putOpCode(L_NOTEQ, reg, 0, 1);
                break;
            case OP_GREAT:
                putOpCode(L_GT, reg, 0, 1);
                break;
            case OP_GREATEQ:
                putOpCode(L_GTEQ, reg, 0, 1);
                break;
            case OP_LESS:
                putOpCode(L_LT, reg, 0, 1);
                break;
            case OP_LESSEQ:
                putOpCode(L_LTEQ, reg, 0, 1);
                break;
            default:
                throw unexpectedNodeException(node);
        }
    }

    private void generateUnaryOperator(Node node, int reg) throws SemanticException {
        Node rightNode = node.getChild(0);

        switch (node.getOperator()) {
            case OP_DPREPLUS:
            case OP_DPREMINUS:
            case OP_DPOSTPLUS:
            case OP_DPOSTMINUS:
                int id = rightNode.getArgument();
                if (node.getOperator() == OperatorType.OP_DPREPLUS || node.getOperator() == OperatorType.OP_DPREMINUS) {
                    putOpCode(L_GETLOCAL, reg, id);
                    putOpCode(node.getOperator() == OperatorType.OP_DPREPLUS ? L_INC : L_DEC);
                    putOpCode(L_SETLOCAL, id, reg);
                } else {
                    putOpCode(L_GETLOCAL, reg, id);
                    putOpCode(node.getOperator() == OperatorType.OP_DPOSTPLUS ? L_INC : L_DEC);
                    putOpCode(L_SETLOCAL, id, reg);
                }
                return;
            case OP_LNOT:
                generateExpression(rightNode, reg);
                putOpCode(L_NOT, reg);
                break;
            case OP_MINUS:
                generateExpression(rightNode, reg);
                putOpCode(L_NEG, reg);
                break;
            case OP_PLUS:
                generateExpression(rightNode, reg);
                putOpCode(L_POS, reg);
                break;
            default:
                throw unexpectedNodeException(node);
        }
    }

    private void generateExpression(Node node, int reg) throws SemanticException {
        switch (node.getType()) {
            case ND_BINOP_EXPR:
                generateBinaryOperator(node, reg);
                return;
            case ND_UNOP_EXPR:
                generateUnaryOperator(node, reg);
                return;
            case ND_ID_LOCAL:
                putOpCode(L_GETLOCAL, reg, node.getArgument());
                break;
            case ND_ID_OUTER:
                putOpCode(L_GETOUTER, reg, node.getArgument());
                break;
            case ND_NUMBER:
            case ND_STRING:
                putOpCode(L_CONST, reg, node.getArgument());
                break;
            case ND_BOOLEAN:
                putOpCode(L_BOOLEAN, reg, node.getArgument());
                break;
            case ND_NULL:
                putOpCode(L_NULL, reg);
                break;
            case ND_UNDEFINED:
                putOpCode(L_UNDEFINED, reg);
                break;
            case ND_CALL:
                generateFunctionCall(node, reg);
                break;
            case ND_FUNC_DEF:
                generateFunctionDefinition(node, reg);
                break;
            case ND_FIELD_SEL:
                generateFieldSelect(node, reg);
                break;
            default:
                throw unexpectedNodeException(node);
        }
    }

    private int generateExpressionListStack(Node node) throws SemanticException {
        Node curr = node;

        int count = 0;
        while (!checkNodeType(curr, ND_EMPTY)) {
            generateExpression(curr.getChild(0), 0);
            putOpCode(L_PUSH, 0);
            curr = curr.getChild(1);
            count++;
        }

        return count;
    }

    private void generateExpressionStatement(Node node, int reg) throws SemanticException {
        Node curr = node;

        while (!checkNodeType(curr, ND_EMPTY)) {
            generateExpression(curr.getChild(0), reg);
            curr = curr.getChild(1);
        }
    }

    private void generateVariableInit(Node node) throws SemanticException {
        int id = node.getArgument();

        Node initNode = node.getChild(0);
        if (!checkNodeType(initNode, ND_EMPTY)) {
            generateExpression(initNode, 0);
            putOpCode(L_SETLOCAL, id, 0);
        }

        Node nextVar = node.getChild(1);
        if (!checkNodeType(nextVar, ND_EMPTY)) {
            generateVariableInit(nextVar);
        }
    }

    private void generateIf(Node node) throws SemanticException {
        generateExpression(node.getChild(0), 0);

        OpCode jmpf = putOpCode(L_JMPF);
        jmpf.b = 0;
        int cur = opCodes.size();

        generateStatement(node.getChild(1));
        jmpf.a = opCodes.size() - cur;
    }

    private void generateIfElse(Node node) throws SemanticException {
        generateExpression(node.getChild(0), 0);

        OpCode jmpf = putOpCode(L_JMPF);
        jmpf.b = 0;
        int curf = opCodes.size();

        generateStatement(node.getChild(1));

        OpCode jmp = putOpCode(L_JMP);
        int cur = opCodes.size();

        generateStatement(node.getChild(2));
        jmpf.a = opCodes.size() - curf;
        jmp.a = opCodes.size() - cur;
    }

    private void generateBreak(Node node) throws SemanticException {
        if (loopLevel <= 0) throw semanticException(node, "break not in loop");
        putOpCode(L_JMP);
        putBreak();
    }

    private void generateContinue(Node node) throws SemanticException {
        if (loopLevel <= 0) throw semanticException(node, "continue not in loop");
        putOpCode(L_JMP);
        putContinue();
    }

    private void generateReturn(Node node) throws SemanticException {
        Node expr = node.getChild(0);
        if (!checkNodeType(expr, ND_EMPTY)) {
            generateExpression(node.getChild(0), 0);
        } else {
            putOpCode(L_UNDEFINED, 0);
        }
        putOpCode(L_RET, 0);
    }

    private void generateWhile(Node node) throws SemanticException {
        loopLevel++;

        int curback = opCodes.size();

        generateExpression(node.getChild(0), 0);

        OpCode jmp = putOpCode(L_JMPF, 0, 0);

        int cur = opCodes.size();

        generateStatement(node.getChild(1));

        putOpCode(L_JMP, curback - opCodes.size() + 1);
        jmp.a = opCodes.size() - cur;

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
                generateExpressionStatement(preExpr, 0);
            }
        }
        int curback = opCodes.size();

        OpCode jmp = null;
        Node condExpr = node.getChild(1);
        if (!checkNodeType(condExpr, ND_EMPTY)) {
            generateExpression(condExpr, 0);
            jmp = putOpCode(L_JMPF, 0);
        }

        int cur = opCodes.size();
        Node stmt = node.getChild(3);
        generateStatement(stmt);

        Node postExpr = node.getChild(2);
        if (!checkNodeType(postExpr, ND_EMPTY)) {
            generateExpressionStatement(postExpr, 0);
        }
        putOpCode(L_JMP, curback - opCodes.size() + 1);
        if (jmp != null) jmp.a = opCodes.size() - cur;

        processBrCont(curback);
        loopLevel--;
    }

    private void generateImport(Node node) throws SemanticException {
        putOpCode(L_IMPORT, node.getArgument());
        putOpCode(L_SETLOCAL, node.getChild(0).getArgument(), 0);
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
                generateFunctionCall(node, 0);
                return;
            case ND_BREAK:
                generateBreak(node);
                return;
            case ND_CONTINUE:
                generateContinue(node);
                return;
            case ND_RETURN:
                generateReturn(node);
                return;
            case ND_EXPR_STMT:
                generateExpressionStatement(node, 0);
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

    public Blueprint generate() throws SemanticException {
        generateStatement(semanticInfo.getRoot());

        OpCode[] code = opCodes.toArray(new OpCode[0]);
        LargoValue[] constantPool = semanticInfo.getConstPool();
        int argumentsCount = semanticInfo.getArgumentsCount();
        int localVariablesCount = semanticInfo.getLocalVariablesCount();
        Blueprint[] blueprintsArray = blueprints.toArray(new Blueprint[0]);

        return new Blueprint(code, constantPool, argumentsCount, localVariablesCount, localVariablesCount + maxStackSize, blueprintsArray);
    }
}
