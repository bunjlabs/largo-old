package com.bunjlabs.largo.runtime;

import com.bunjlabs.largo.types.*;

public class DefaultLargoRuntime implements LargoRuntime {

    public void execute(LargoEnvironment environment, Program program) throws LargoRuntimeException {
        LargoRuntimeConstraints constraints = environment.getConstraints();

        if (program.getOpCodes().length > constraints.getMaxCodeLength()) {
            throw new LargoRuntimeException("Maximum code length exceeded " +
                    "(limit: " + constraints.getMaxCodeLength() +
                    " actual: " + program.getOpCodes().length);
        }

        if (program.getConstPool().length > constraints.getMaxConstantPoolSize()) {
            throw new LargoRuntimeException("Maximum constant pool size exceeded " +
                    "(limit: " + constraints.getMaxConstantPoolSize() +
                    " actual: " + program.getConstPool().length);
        }

        if (program.getLocalVariablesCount() > constraints.getMaxVariables()) {
            throw new LargoRuntimeException("Maximum variables count exceeded " +
                    "(limit: " + constraints.getMaxVariables() +
                    " actual: " + program.getLocalVariablesCount());
        }

        LargoState state = new DefaultLargoState(constraints);

        state.init(program.getLocalVariablesCount());

        runProgram(environment, state, program);

        state.clear();
    }

    private void runProgram(LargoEnvironment env, LargoState state, Program program) throws LargoRuntimeException {
        LargoRuntimeConstraints constraints = env.getConstraints();
        OpCode[] opCodes = program.getOpCodes();
        LargoValue[] constPool = program.getConstPool();
        long startTime = System.currentTimeMillis();

        int jmp;
        LargoValue c;
        LargoValue v1, v2;
        for (int index = 0, count = 0; index < opCodes.length; index++, count++) {
            OpCode opCode = opCodes[index];

            switch (opCode.getType()) {
                case L_IMPORT:
                    c = constPool[opCode.getArg()];
                    if (c.getType() == LargoType.STRING) {
                        String id = c.asJString();

                        v1 = env.getExported(id);

                        state.stackPush(v1);
                    } else {
                        state.stackPush(LargoUndefined.UNDEFINED);
                    }
                    break;
                case L_GETLOCAL:
                    v1 = state.getLocal(opCode.getArg());
                    state.stackPush(v1 == null ? LargoUndefined.UNDEFINED : v1);
                    break;
                case L_SETLOCAL:
                    v1 = state.stackPeek();
                    state.setLocal(opCode.getArg(), v1);
                    break;
                case L_SETLOCAL_S:
                    v1 = state.stackPop();
                    state.setLocal(opCode.getArg(), v1);
                    break;
                case L_GETNAME:
                    v1 = state.stackPop();
                    state.stackPush(v1.get(constPool[opCode.getArg()]));
                    break;
                case L_CONST:
                    state.stackPush(constPool[opCode.getArg()]);
                    break;
                case L_POP:
                    state.stackPop();
                    break;
                case L_JUMP:
                    jmp = opCode.getArg();
                    index += jmp < 0 ? jmp - 2 : jmp;
                    break;
                case L_JUMP_F:
                    v1 = state.stackPop();
                    if (!v1.asJBoolean()) {
                        jmp = opCode.getArg();
                        index += jmp < 0 ? jmp - 2 : jmp;
                    }
                    break;
                case L_TRUE:
                    state.stackPush(LargoBoolean.TRUE);
                    break;
                case L_FALSE:
                    state.stackPush(LargoBoolean.FALSE);
                    break;
                case L_NULL:
                    state.stackPush(LargoNull.NULL);
                    break;
                case L_UNDEFINED:
                    state.stackPush(LargoUndefined.UNDEFINED);
                    break;
                case L_ADD:
                    v2 = state.stackPop();
                    v1 = state.stackPop();

                    state.stackPush(v1.add(v2));
                    break;
                case L_MUL:
                    v2 = state.stackPop();
                    v1 = state.stackPop();

                    state.stackPush(v1.mul(v2));
                    break;
                case L_SUB:
                    v2 = state.stackPop();
                    v1 = state.stackPop();

                    state.stackPush(v1.sub(v2));
                    break;
                case L_DIV:
                    v2 = state.stackPop();
                    v1 = state.stackPop();

                    state.stackPush(v1.div(v2));
                    break;
                case L_MOD:
                    v2 = state.stackPop();
                    v1 = state.stackPop();

                    state.stackPush(v1.mod(v2));
                    break;
                case L_POS:
                    v1 = state.stackPop();
                    state.stackPush(v1.pos());
                    break;
                case L_NEG:
                    v1 = state.stackPop();
                    state.stackPush(v1.neg());
                    break;
                case L_INC:
                    v1 = state.stackPop();
                    state.stackPush(v1.addTo(1));
                    break;
                case L_DEC:
                    v1 = state.stackPop();
                    state.stackPush(v1.addTo(-1));
                    break;
                case L_EQ:
                    v2 = state.stackPop();
                    v1 = state.stackPop();
                    state.stackPush(v1.eq(v2));
                    break;
                case L_NOTEQ:
                    v2 = state.stackPop();
                    v1 = state.stackPop();
                    state.stackPush(v1.eq(v2).not());
                    break;
                case L_GREAT:
                    v2 = state.stackPop();
                    v1 = state.stackPop();
                    state.stackPush(v1.gt(v2));
                    break;
                case L_GREATEQ:
                    v2 = state.stackPop();
                    v1 = state.stackPop();
                    state.stackPush(v1.gteq(v2));
                    break;
                case L_LESS:
                    v2 = state.stackPop();
                    v1 = state.stackPop();
                    state.stackPush(v1.lt(v2));
                    break;
                case L_LESSEQ:
                    v2 = state.stackPop();
                    v1 = state.stackPop();
                    state.stackPush(v1.lteq(v2));
                    break;
                case L_AND:
                    v2 = state.stackPop();
                    v1 = state.stackPop();
                    state.stackPush(v1.and(v2));
                    break;
                case L_OR:
                    v2 = state.stackPop();
                    v1 = state.stackPop();
                    state.stackPush(v1.or(v2));
                    break;
                case L_NOT:
                    v1 = state.stackPop();
                    state.stackPush(v1.not());
                    break;
                case L_NOP:
                case L_ERR:
                    break;
                case L_CALL:
                    LargoValue[] callValues = new LargoValue[opCode.getArg()];

                    for (int j = callValues.length - 1; j >= 0; j--) {
                        callValues[j] = state.stackPop();
                    }

                    v1 = state.stackPop();

                    state.stackPush(v1.call(callValues));
                    break;
            }

            if (count > constraints.getMaxInstructions()) {
                throw new LargoRuntimeException("Instructions count reached its limit: " + constraints.getMaxInstructions());
            }

            if (System.currentTimeMillis() - startTime > constraints.getMaxExecutionTime()) {
                throw new LargoRuntimeException("Execute time reached its limit: " + constraints.getMaxExecutionTime());
            }
        }
    }
}
