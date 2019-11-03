package com.bunjlabs.largo.compiler.codegen;

import com.bunjlabs.largo.*;
import com.bunjlabs.largo.compiler.SemanticException;
import com.bunjlabs.largo.compiler.parser.nodes.Node;
import com.bunjlabs.largo.compiler.parser.nodes.NodeType;
import com.bunjlabs.largo.compiler.parser.nodes.OperatorType;
import com.bunjlabs.largo.compiler.semantic.SemanticInfo;
import com.bunjlabs.largo.types.LargoValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static com.bunjlabs.largo.compiler.Utils.*;
import static com.bunjlabs.largo.compiler.parser.nodes.NodeType.*;
import static com.bunjlabs.largo.compiler.parser.nodes.OperatorType.*;

public class CodeGenerator {

    private final InstructionSequenceBuilder inst = new InstructionSequenceBuilder();
    private final Stack<FlowBreak> breaks = new Stack<>();
    private final Stack<FlowBreak> continues = new Stack<>();

    private final List<Blueprint> blueprints = new ArrayList<>();
    private final SemanticInfo semanticInfo;
    private int loopLevel = 0;
    private int maxRegistersCount = 0;
    private int maxVariablesCount = 0;
    private int maxCallStackSize = 0;

    public CodeGenerator(SemanticInfo semanticInfo) {
        this.semanticInfo = semanticInfo;
        reserveVariables(semanticInfo.getLocalVariablesCount());
    }

    private void reserveRegisters(int count) {
        if (count > maxRegistersCount) {
            maxRegistersCount = count;
        }
    }

    private void reserveVariables(int count) {
        maxVariablesCount += count;
    }

    private void reserveCallStack(int size) {
        if (size > maxCallStackSize) {
            maxCallStackSize = size;
        }
    }

    private void putBreak() {
        List<Instruction> instructions = inst.getInstructions();
        breaks.push(new FlowBreak(instructions.get(instructions.size() - 1), instructions.size() - 1, loopLevel));
    }

    private void putContinue() {
        List<Instruction> instructions = inst.getInstructions();
        continues.push(new FlowBreak(instructions.get(instructions.size() - 1), instructions.size() - 1, loopLevel));
    }

    private void processBrCont(int back) {
        List<Instruction> instructions = inst.getInstructions();
        while (!breaks.isEmpty() && breaks.peek().getLoopLevel() == loopLevel) {
            FlowBreak br = breaks.pop();
            br.getInstruction().a = instructions.size() - br.getPos();
        }
        while (!continues.isEmpty() && continues.peek().getLoopLevel() == loopLevel) {
            FlowBreak br = continues.pop();
            br.getInstruction().a = back - br.getPos() + 2; // 2 because cur op is JMP
        }
    }

    private void generateVarLoad(int reg, int variable, boolean outer) {
        if (outer) {
            inst.loado(reg, variable);
        } else {
            inst.load(reg, variable);
        }
    }

    private void generateVarStore(int variable, int reg, boolean outer) {
        if (outer) {
            inst.storeo(variable, reg);
        } else {
            inst.store(variable, reg);
        }
    }

    private void generateArray(Node node, int reg) throws SemanticException {
        reserveRegisters(reg + 1);

        inst.loada(reg);

        Node body = node.getChild(0);
        if (checkNodeType(body, ND_EXPR_LIST)) {
            Node curr = body;
            while (!checkNodeType(curr, ND_EMPTY)) {
                generateExpression(curr.getChild(0), reg + 1);
                inst.pusha(reg, reg + 1);
                curr = curr.getChild(1);
            }
        } else if (!checkNodeType(body, ND_EMPTY)) {
            generateExpression(body, reg + 1);
            inst.pusha(reg, reg + 1);
        }
    }

    private void generateObject(Node node, int reg) throws SemanticException {
        reserveRegisters(reg + 1);
        inst.loadm(reg);
    }

    private void generateFunctionDefinition(Node node, int reg) throws SemanticException {
        reserveRegisters(reg + 1);

        Node body = node.getChild(1);

        SemanticInfo[] functions = semanticInfo.getFunctions();
        SemanticInfo closureSemanticInfo = functions[node.getArgument()];
        CodeGenerator generator = new CodeGenerator(closureSemanticInfo);

        if (checkNodeType(body, ND_STMT_LIST)) {
            generator.generateStatement(body);
        } else {
            generator.generateExpressionStatement(body, reg);
            generator.inst.ret(reg);
        }

        Instruction[] code = generator.inst.getInstructions().toArray(new Instruction[0]);
        LargoValue[] constantPool = closureSemanticInfo.getConstPool();

        int registersCount = generator.maxRegistersCount;
        int argumentsCount = closureSemanticInfo.getArgumentsCount();
        int variablesCount = generator.maxVariablesCount;
        int localVariablesCount = closureSemanticInfo.getLocalVariablesCount();
        int callStackSize = generator.maxCallStackSize;

        Blueprint[] blueprintsArray = generator.blueprints.toArray(new Blueprint[0]);

        Blueprint blueprint = new Blueprint(code, constantPool, registersCount, argumentsCount, variablesCount, localVariablesCount, callStackSize, blueprintsArray);

        reserveRegisters(generator.maxRegistersCount);
        reserveVariables(variablesCount);
        reserveCallStack(callStackSize);

        blueprints.add(blueprint);
        inst.loadf(reg, node.getArgument());
    }

    private void generateFunctionCall(Node node, int reg) throws SemanticException {
        reserveRegisters(reg + 1);

        Node expr = node.getChild(0);
        Node args = node.getChild(1);

        int argCount;
        if (checkNodeType(args, ND_EMPTY)) {
            argCount = 0;
        } else if (checkNodeType(args, ND_EXPR_LIST)) {
            argCount = generateExpressionListStack(args, reg);
        } else {
            generateExpression(args, reg);
            inst.push(reg);
            argCount = 1;
        }

        reserveCallStack(argCount);
        generateExpression(expr, reg);
        inst.call(reg, reg, argCount);
    }

    private void generateIndexSelect(Node node, int reg) throws SemanticException {
        reserveRegisters(reg + 1);

        Node expr = node.getChild(0);
        Node index = node.getChild(1);

        generateExpression(index, reg + 1);
        generateExpression(expr, reg);

        inst.getindex(reg, reg, reg + 1);
    }

    private void generateIndexSet(Node node, int reg) throws SemanticException {
        reserveRegisters(reg + 1);

        Node leftExpr = node.getChild(0);
        Node index = node.getChild(1);
        Node rightExpr = node.getChild(2);

        generateExpression(rightExpr, reg + 2);
        generateExpression(index, reg + 1);
        generateExpression(leftExpr, reg);

        inst.putindex(reg, reg + 1, reg + 2);
    }

    private void generateFieldSelect(Node node, int reg) throws SemanticException {
        reserveRegisters(reg + 1);

        Node expr = node.getChild(0);
        Node id = node.getChild(1);

        generateExpression(expr, reg);

        inst.getfield(reg, reg, id.getArgument());
    }

    private void generateFieldSet(Node node, int reg) throws SemanticException {
        reserveRegisters(reg + 1);

        Node leftExpr = node.getChild(0);
        Node id = node.getChild(1);
        Node rightExpr = node.getChild(2);

        generateExpression(rightExpr, reg + 1);
        generateExpression(leftExpr, reg);

        inst.putfield(reg, id.getArgument(), reg + 1);
    }

    private void generateBinaryOperator(Node node, int reg) throws SemanticException {
        reserveRegisters(reg + 1);

        Node leftNode = node.getChild(0);
        Node rightNode = node.getChild(1);

        if (node.getOperator() == OP_ASSIGN) {
            generateExpression(rightNode, reg);
            if (leftNode.getType() == ND_ID_LOCAL) {
                inst.store(leftNode.getArgument(), reg);
            } else {
                inst.storeo(leftNode.getArgument(), reg);
            }
            return;
        }

        generateExpression(leftNode, reg);
        generateExpression(rightNode, reg + 1);

        switch (node.getOperator()) {
            case OP_PLUSEQ:
            case OP_MINUSEQ:
            case OP_MULTEQ:
            case OP_DIVEQ:
            case OP_MODEQ:
                if (node.getOperator() == OP_PLUSEQ) {
                    inst.add(reg, reg, reg + 1);
                } else if (node.getOperator() == OP_MINUSEQ) {
                    inst.sub(reg, reg, reg + 1);
                } else if (node.getOperator() == OP_MULTEQ) {
                    inst.mul(reg, reg, reg + 1);
                } else if (node.getOperator() == OP_DIVEQ) {
                    inst.div(reg, reg, reg + 1);
                } else {
                    inst.mod(reg, reg, reg + 1);
                }

                generateVarStore(leftNode.getArgument(), reg, leftNode.getType() == ND_ID_OUTER);
                return;
            case OP_PLUS:
                inst.add(reg, reg, reg + 1);
                break;
            case OP_MINUS:
                inst.sub(reg, reg, reg + 1);
                break;
            case OP_MULT:
                inst.mul(reg, reg, reg + 1);
                break;
            case OP_DIV:
                inst.div(reg, reg, reg + 1);
                break;
            case OP_MOD:
                inst.mod(reg, reg, reg + 1);
                break;
            case OP_LAND:
                inst.and(reg, reg, reg + 1);
                break;
            case OP_LOR:
                inst.or(reg, reg, reg + 1);
                break;
            case OP_EQ:
                inst.eq(reg, reg, reg + 1);
                break;
            case OP_NOTEQ:
                inst.neq(reg, reg, reg + 1);
                break;
            case OP_GREAT:
                inst.gt(reg, reg, reg + 1);
                break;
            case OP_GREATEQ:
                inst.gteq(reg, reg, reg + 1);
                break;
            case OP_LESS:
                inst.lt(reg, reg, reg + 1);
                break;
            case OP_LESSEQ:
                inst.lteq(reg, reg, reg + 1);
                break;
            default:
                throw unexpectedNodeException(node);
        }
    }

    private void generateUnaryOperator(Node node, int reg) throws SemanticException {
        reserveRegisters(reg + 1);

        Node rightNode = node.getChild(0);

        switch (node.getOperator()) {
            case OP_DPREPLUS:
            case OP_DPREMINUS:
            case OP_DPOSTPLUS:
            case OP_DPOSTMINUS:
                int id = rightNode.getArgument();
                NodeType type = rightNode.getType();
                OperatorType op = node.getOperator();

                switch (op) {
                    case OP_DPREPLUS:
                        generateVarLoad(reg, id, type == ND_ID_OUTER);
                        inst.inc(reg, reg);
                        generateVarStore(id, reg, type == ND_ID_OUTER);
                        break;
                    case OP_DPOSTPLUS:
                        generateVarLoad(reg, id, type == ND_ID_OUTER);
                        inst.inc(reg, reg);
                        generateVarStore(id, reg, type == ND_ID_OUTER);
                        break;
                    case OP_DPREMINUS:
                        generateVarLoad(reg, id, type == ND_ID_OUTER);
                        inst.dec(reg, reg);
                        generateVarStore(id, reg, type == ND_ID_OUTER);
                        break;
                    case OP_DPOSTMINUS:
                        generateVarLoad(reg, id, type == ND_ID_OUTER);
                        inst.dec(reg, reg);
                        generateVarStore(id, reg, type == ND_ID_OUTER);
                        break;
                }
                return;
            case OP_LNOT:
                generateExpression(rightNode, reg);
                inst.not(reg, reg);
                break;
            case OP_MINUS:
                generateExpression(rightNode, reg);
                inst.neg(reg, reg);
                break;
            case OP_PLUS:
                generateExpression(rightNode, reg);
                inst.pos(reg, reg);
                break;
            default:
                throw unexpectedNodeException(node);
        }
    }

    private void generateExpression(Node node, int reg) throws SemanticException {
        reserveRegisters(reg + 1);

        switch (node.getType()) {
            case ND_BINOP_EXPR:
                generateBinaryOperator(node, reg);
                return;
            case ND_UNOP_EXPR:
                generateUnaryOperator(node, reg);
                return;
            case ND_ID_LOCAL:
                inst.load(reg, node.getArgument());
                break;
            case ND_ID_OUTER:
                inst.loado(reg, node.getArgument());
                break;
            case ND_ARRAY:
                generateArray(node, reg);
                break;
            case ND_OBJECT:
                generateObject(node, reg);
                break;
            case ND_NUMBER:
            case ND_STRING:
                inst.loadc(reg, node.getArgument());
                break;
            case ND_BOOLEAN:
                inst.loadb(reg, node.getArgument());
                break;
            case ND_NULL:
                inst.loadn(reg);
                break;
            case ND_UNDEFINED:
                inst.loadu(reg);
                break;
            case ND_CALL:
                generateFunctionCall(node, reg);
                break;
            case ND_FUNC_DEF:
                generateFunctionDefinition(node, reg);
                break;
            case ND_INDEX_SEL:
                generateIndexSelect(node, reg);
                break;
            case ND_INDEX_SET:
                generateIndexSet(node, reg);
                break;
            case ND_FIELD_SEL:
                generateFieldSelect(node, reg);
                break;
            case ND_FIELD_SET:
                generateFieldSet(node, reg);
                break;
            default:
                throw unexpectedNodeException(node);
        }
    }

    private int generateExpressionListStack(Node node, int reg) throws SemanticException {
        reserveRegisters(reg + 1);

        Node curr = node;

        int count = 0;
        while (!checkNodeType(curr, ND_EMPTY)) {
            generateExpression(curr.getChild(0), reg);
            inst.push(reg);
            curr = curr.getChild(1);
            count++;
        }

        return count;
    }

    private void generateExpressionStatement(Node node, int reg) throws SemanticException {
        reserveRegisters(reg + 1);

        Node curr = node;

        while (!checkNodeType(curr, ND_EMPTY)) {
            generateExpression(curr.getChild(0), reg);
            curr = curr.getChild(1);
        }
    }

    private void generateVariableInit(Node node) throws SemanticException {
        reserveRegisters(1);

        Node initNode = node.getChild(0);
        if (!checkNodeType(initNode, ND_EMPTY)) {
            generateExpression(initNode, 0);
            inst.store(node.getArgument(), 0);
        }

        Node nextVar = node.getChild(1);
        if (!checkNodeType(nextVar, ND_EMPTY)) {
            generateVariableInit(nextVar);
        }
    }

    private void generateIf(Node node) throws SemanticException {
        reserveRegisters(1);

        generateExpression(node.getChild(0), 0);

        Instruction jmpf = inst.jmpf(0, 0);
        int cur = inst.getInstructions().size();

        generateStatement(node.getChild(1));
        jmpf.b = inst.getInstructions().size() - cur;
    }

    private void generateIfElse(Node node) throws SemanticException {
        reserveRegisters(1);

        generateExpression(node.getChild(0), 0);

        Instruction jmpf = inst.jmpf(0, 0);
        int curf = inst.getInstructions().size();

        generateStatement(node.getChild(1));

        Instruction jmp = inst.jmp(0);
        int cur = inst.getInstructions().size();

        generateStatement(node.getChild(2));
        jmpf.b = inst.getInstructions().size() - curf;
        jmp.a = inst.getInstructions().size() - cur;
    }

    private void generateBreak(Node node) throws SemanticException {
        if (loopLevel <= 0) throw semanticException(node, "break not in loop");
        inst.jmp(0);
        putBreak();
    }

    private void generateContinue(Node node) throws SemanticException {
        if (loopLevel <= 0) throw semanticException(node, "continue not in loop");
        inst.jmp(0);
        putContinue();
    }

    private void generateReturn(Node node) throws SemanticException {
        Node expr = node.getChild(0);
        if (!checkNodeType(expr, ND_EMPTY)) {
            generateExpression(node.getChild(0), 0);
        } else {
            inst.loadu(0);
        }
        inst.ret(0);
    }

    private void generateWhile(Node node) throws SemanticException {
        reserveRegisters(1);

        loopLevel++;
        int curback = inst.getInstructions().size();

        generateExpression(node.getChild(0), 0);

        Instruction jmpf = inst.jmpf(0, 0);
        int cur = inst.getInstructions().size();

        generateStatement(node.getChild(1));

        inst.jmp(curback - inst.getInstructions().size() + 1);
        jmpf.b = inst.getInstructions().size() - cur;

        processBrCont(curback);
        loopLevel--;
    }

    private void generateFor(Node node) throws SemanticException {
        reserveRegisters(1);

        loopLevel++;
        Node preExpr = node.getChild(0);
        if (!checkNodeType(preExpr, ND_EMPTY)) {
            if (preExpr.getType() == ND_VARINIT_STMT) {
                generateVariableInit(preExpr);
            } else {
                generateExpressionStatement(preExpr, 0); // ignore statement result
            }
        }
        int curback = inst.getInstructions().size();

        Instruction jmpf = null;
        Node condExpr = node.getChild(1);
        if (!checkNodeType(condExpr, ND_EMPTY)) {
            generateExpression(condExpr, 0);
            jmpf = inst.jmpf(0,  0);
        }

        int cur = inst.getInstructions().size();
        Node stmt = node.getChild(3);
        generateStatement(stmt);

        Node postExpr = node.getChild(2);
        if (!checkNodeType(postExpr, ND_EMPTY)) {
            generateExpressionStatement(postExpr, 0);
        }
        inst.jmp(curback - inst.getInstructions().size() + 1);
        if (jmpf != null) jmpf.b = inst.getInstructions().size() - cur;

        processBrCont(curback);
        loopLevel--;
    }

    private void generateImport(Node node) throws SemanticException {
        reserveRegisters(1);

        inst.imp(0, node.getArgument());
        inst.store(node.getChild(0).getArgument(), 0);
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
                generateFunctionCall(node, 0); // ignore statement result
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
                generateExpressionStatement(node, 0); // ignore statement result
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

        Instruction[] code = inst.getInstructions().toArray(new Instruction[0]);
        LargoValue[] constantPool = semanticInfo.getConstPool();
        int registersCount = maxRegistersCount;
        int argumentsCount = semanticInfo.getArgumentsCount();
        int variablesCount = maxVariablesCount;
        int localVariablesCount = semanticInfo.getLocalVariablesCount();
        int callStackSize = maxCallStackSize;
        Blueprint[] blueprintsArray = blueprints.toArray(new Blueprint[0]);


        return new Blueprint(code, constantPool, registersCount, argumentsCount, variablesCount, localVariablesCount, callStackSize, blueprintsArray);
    }
}
