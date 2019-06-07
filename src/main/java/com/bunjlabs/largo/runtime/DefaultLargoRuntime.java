package com.bunjlabs.largo.runtime;

import com.bunjlabs.largo.Blueprint;
import com.bunjlabs.largo.types.LargoValue;

public class DefaultLargoRuntime implements LargoRuntime {

    public void execute(LargoEnvironment environment, Blueprint blueprint) throws LargoRuntimeException {
        LargoRuntimeConstraints constraints = environment.getConstraints();

        if (blueprint.getCode().length > constraints.getMaxCodeLength()) {
            throw new LargoRuntimeException("Maximum code length exceeded " +
                    "(limit: " + constraints.getMaxCodeLength() +
                    " actual: " + blueprint.getCode().length);
        }

        if (blueprint.getConstantPool().length > constraints.getMaxConstantPoolSize()) {
            throw new LargoRuntimeException("Maximum constant pool size exceeded " +
                    "(limit: " + constraints.getMaxConstantPoolSize() +
                    " actual: " + blueprint.getConstantPool().length);
        }

        if (blueprint.getLocalVariablesCount() > constraints.getMaxVariables()) {
            throw new LargoRuntimeException("Maximum variables count exceeded " +
                    "(limit: " + constraints.getMaxVariables() +
                    " actual: " + blueprint.getLocalVariablesCount());
        }

        LargoState state = new DefaultLargoState(constraints);

        state.init(blueprint.getLocalVariablesCount());

        runProgram(environment, state, blueprint);

        state.clear();
    }

    private void runProgram(LargoEnvironment env, LargoState state, Blueprint blueprint) throws LargoRuntimeException {
        LargoRuntimeConstraints constraints = env.getConstraints();
        OpCode[] opCodes = blueprint.getCode();
        LargoValue[] constPool = blueprint.getConstantPool();
        long startTime = System.currentTimeMillis();

        int jmp;
        LargoValue c;
        LargoValue v1, v2;
        for (int index = 0, count = 0; index < opCodes.length; index++, count++) {
            OpCode opCode = opCodes[index];


            if (count > constraints.getMaxInstructions()) {
                throw new LargoRuntimeException("Instructions count reached its limit: " + constraints.getMaxInstructions());
            }

            if (System.currentTimeMillis() - startTime > constraints.getMaxExecutionTime()) {
                throw new LargoRuntimeException("Execute time reached its limit: " + constraints.getMaxExecutionTime());
            }
        }
    }
}
