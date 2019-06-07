package com.bunjlabs.largo.types;

import com.bunjlabs.largo.Blueprint;
import com.bunjlabs.largo.runtime.OpCode;

public class LargoClosure extends LargoFunction {

    private final Blueprint blueprint;
    private final LargoValue[] stack;
    private final int stackIndex;

    public LargoClosure(Blueprint blueprint) {
        this.blueprint = blueprint;
        this.stack = createStack();
        this.stackIndex = 0;
    }

    public LargoClosure(LargoValue context, Blueprint blueprint) {
        super(context);
        this.blueprint = blueprint;
        this.stack = createStack();
        this.stackIndex = 0;
    }

    public LargoClosure(Blueprint blueprint, LargoValue[] stack, int stackIndex) {
        this.blueprint = blueprint;
        this.stack = stack;
        this.stackIndex = stackIndex;
    }

    @Override
    protected LargoValue call(LargoValue context, LargoValue... args) {
        OpCode[] code = blueprint.getCode();
        LargoValue[] constants = blueprint.getConstantPool();

        // Closure call stack scheme example
        // [registers: a b c] [local variables] [tail stack]
        // [registers: a b c] [outer variables #1] [local variables] [tail stack]
        // [registers: a b c] [outer variables #1] [outer variables #2] [local variables] [tail stack]

        int o = 3;
        int v = 3 + stackIndex;
        int s = 3 + stackIndex + blueprint.getLocalVariablesCount();

        for (int i = 0; i < blueprint.getArgumentsCount(); i++) {
            if (i >= args.length) {
                stack[v + i] = LargoUndefined.UNDEFINED;
            } else {
                stack[v + i] = args[i];
            }
        }

        for (int index = 0, count = 0; index < code.length; index++, count++) {
            OpCode op = code[index];

            switch (op.type) {
                case L_IMPORT:
                    stack[op.a] = context.get(constants[op.b]);
                    break;
                case L_GETLOCAL:
                    stack[op.a] = stack[v + op.b];
                    break;
                case L_GETOUTER:
                    stack[op.a] = stack[o + op.b];
                    break;
                case L_SETLOCAL:
                    stack[v + op.a] = stack[op.b];
                    break;
                case L_CLOSURE: {
                    stack[op.a] = new LargoClosure(
                            blueprint.getBlueprints()[op.b],
                            stack,
                            stackIndex + blueprint.getLocalVariablesCount());
                    break;
                }
                case L_GETNAME:
                    stack[op.a] = stack[op.b].get(constants[op.c]);
                    break;
                case L_CONST:
                    stack[op.a] = constants[op.b];
                    break;
                case L_PUSH:
                    stack[s++] = stack[op.a];
                    break;
                case L_POP:
                    stack[op.a] = stack[s++];
                    break;
                case L_JMP:
                    index += op.a < 0 ? op.a - 2 : op.a;
                    break;
                case L_JMPF:
                    if (!stack[op.b].asJBoolean()) index += op.a < 0 ? op.a - 2 : op.a;
                    break;
                case L_BOOLEAN:
                    stack[op.a] = op.b > 0 ? LargoBoolean.TRUE : LargoBoolean.FALSE;
                    break;
                case L_NULL:
                    stack[op.a] = LargoNull.NULL;
                    break;
                case L_UNDEFINED:
                    stack[op.a] = LargoUndefined.UNDEFINED;
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
                case L_NOP:
                case L_ERR:
                    break;
                case L_CALL:
                    LargoValue[] callValues = new LargoValue[op.c];
                    for (int j = callValues.length - 1; j >= 0; j--) callValues[j] = stack[--s];

                    stack[op.b].call(callValues);
                    break;
                case L_RET:
                    return stack[op.a];
            }
        }

        return LargoUndefined.UNDEFINED;
    }

    private LargoValue[] createStack() {
        return new LargoValue[3 + blueprint.getMaxStackSize()];
    }
}
