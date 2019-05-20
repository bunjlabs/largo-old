package com.bunjlabs.largo.compiler.codegen;

import com.bunjlabs.largo.runtime.OpCode;

class FlowBreak {
    private final OpCode opcode;
    private final int pos;
    private final int loopLevel;

    FlowBreak(OpCode opcode, int pos, int loopLevel) {
        this.opcode = opcode;
        this.pos = pos;
        this.loopLevel = loopLevel;
    }

    OpCode getOpcode() {
        return opcode;
    }

    int getPos() {
        return pos;
    }

    int getLoopLevel() {
        return loopLevel;
    }
}
