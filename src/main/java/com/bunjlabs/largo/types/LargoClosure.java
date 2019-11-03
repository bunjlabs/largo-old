package com.bunjlabs.largo.types;

import com.bunjlabs.largo.Blueprint;
import com.bunjlabs.largo.Instruction;

public class LargoClosure extends LargoFunction {

    private final Blueprint blueprint;
    private final LargoValue[] stack;
    private final int stackIndex;

    private LargoClosure(Blueprint blueprint) {
        this.blueprint = blueprint;
        this.stack = createStack();
        this.stackIndex = 0;
    }

    private LargoClosure(Blueprint blueprint, LargoValue[] stack, int stackIndex) {
        this.blueprint = blueprint;
        this.stack = stack;
        this.stackIndex = stackIndex;
    }

    public static LargoClosure from(Blueprint blueprint) {
        return new LargoClosure(blueprint);
    }

    @Override
    public LargoValue call(LargoValue context, LargoValue... args) {
        Instruction[] code = blueprint.getCode();
        LargoValue[] constants = blueprint.getConstantPool();
        Blueprint[] blueprints = blueprint.getBlueprints();

        // Closure call stack scheme
        // [registers] [outer variables]... [local variables] [tail stack]

        LargoValue currentContext = context;
        final int o = blueprint.getRegistersCount();
        final int v = blueprint.getRegistersCount() + stackIndex;
        int s = blueprint.getRegistersCount() + stackIndex + blueprint.getLocalVariablesCount();

        for (int i = 0; i < blueprint.getArgumentsCount(); i++) {
            if (i >= args.length) {
                stack[v + i] = LargoUndefined.UNDEFINED;
            } else {
                stack[v + i] = args[i];
            }
        }

        for (int pc = 0, count = 0; pc < code.length; pc++, count++) {
            Instruction op = code[pc];

            switch (op.name) {
                case L_IMPORT:
                    stack[op.a] = context.get(constants[op.b]);
                    break;
                case L_LOAD:
                    stack[op.a] = stack[v + op.b];
                    break;
                case L_LOADO:
                    stack[op.a] = stack[o + op.b];
                    break;
                case L_LOADA:
                    stack[op.a] = new LargoArray();
                    break;
                case L_LOADM:
                    stack[op.a] = new LargoObject();
                    break;
                case L_LOADC:
                    stack[op.a] = constants[op.b];
                    break;
                case L_LOADB:
                    stack[op.a] = op.b != 0 ? LargoBoolean.TRUE : LargoBoolean.FALSE;
                    break;
                case L_LOADN:
                    stack[op.a] = LargoNull.NULL;
                    break;
                case L_LOADU:
                    stack[op.a] = LargoUndefined.UNDEFINED;
                    break;
                case L_LOADF:
                    stack[op.a] = new LargoClosure(blueprints[op.b], stack, stackIndex + blueprint.getLocalVariablesCount());
                    break;
                case L_STORE:
                    stack[v + op.a] = stack[op.b];
                    break;
                case L_STOREO:
                    stack[o + op.a] = stack[op.b];
                    break;
                case L_GETINDEX:
                    stack[op.a] = stack[op.b].get(stack[op.c]);
                    break;
                case L_PUTINDEX:
                    stack[op.a].set(stack[op.b], stack[op.c]);
                    break;
                case L_PUSHA:
                    stack[op.a].push(stack[op.b]);
                    break;
                case L_GETFIELD:
                    stack[op.a] = stack[op.b].get(constants[op.c]);
                    break;
                case L_SETFIELD:
                    stack[op.a].set(constants[op.b], stack[op.c]);
                    break;
                case L_PUSH:
                    stack[s++] = stack[op.a];
                    break;
                case L_POP:
                    stack[op.a] = stack[s++];
                    break;
                case L_JMP:
                    pc += op.a < 0 ? op.a - 2 : op.a;
                    break;
                case L_JMPF:
                    if (!stack[op.a].asJBoolean()) pc += op.b < 0 ? op.b - 2 : op.b;
                    break;
                case L_ADD:
                    stack[op.a] = stack[op.b].add(stack[op.c]);
                    break;
                case L_SUB:
                    stack[op.a] = stack[op.b].sub(stack[op.c]);
                    break;
                case L_MUL:
                    stack[op.a] = stack[op.b].mul(stack[op.c]);
                    break;
                case L_DIV:
                    stack[op.a] = stack[op.b].div(stack[op.c]);
                    break;
                case L_MOD:
                    stack[op.a] = stack[op.b].mod(stack[op.c]);
                    break;
                case L_POS:
                    stack[op.a] = stack[op.b].pos();
                    break;
                case L_NEG:
                    stack[op.a] = stack[op.b].neg();
                    break;
                case L_INC:
                    stack[op.a] = stack[op.b].addTo(1);
                    break;
                case L_DEC:
                    stack[op.a] = stack[op.b].addTo(-1);
                    break;
                case L_EQ:
                    stack[op.a] = stack[op.b].eq(stack[op.c]);
                    break;
                case L_NOTEQ:
                    stack[op.a] = stack[op.b].eq(stack[op.c]).not();
                    break;
                case L_GT:
                    stack[op.a] = stack[op.b].gt(stack[op.c]);
                    break;
                case L_GTEQ:
                    stack[op.a] = stack[op.b].gteq(stack[op.c]);
                    break;
                case L_LT:
                    stack[op.a] = stack[op.b].lt(stack[op.c]);
                    break;
                case L_LTEQ:
                    stack[op.a] = stack[op.b].lteq(stack[op.c]);
                    break;
                case L_AND:
                    stack[op.a] = stack[op.b].and(stack[op.c]);
                    break;
                case L_OR:
                    stack[op.a] = stack[op.b].or(stack[op.c]);
                    break;
                case L_NOT:
                    stack[op.a] = stack[op.b].not();
                    break;
                case L_CALL:
                    LargoValue[] callValues = new LargoValue[op.c];
                    for (int j = callValues.length - 1; j >= 0; j--) callValues[j] = stack[--s];
                    stack[op.a] = stack[op.b].call(context, callValues);
                    break;
                case L_RET:
                    return stack[op.a];
                case L_NOP:
                case L_ERR:
                    break;
            }
        }

        return LargoUndefined.UNDEFINED;
    }

    private LargoValue[] createStack() {
        int fullStackSize = blueprint.getRegistersCount() + blueprint.getVariablesCount() + blueprint.getCallStackSize();
        return new LargoValue[fullStackSize];
    }
}
