package com.bunjlabs.largo.compiler.codegen;

import com.bunjlabs.largo.Instruction;

class FlowBreak {
    private final Instruction opcode;
    private final int pos;
    private final int loopLevel;

    FlowBreak(Instruction opcode, int pos, int loopLevel) {
        this.opcode = opcode;
        this.pos = pos;
        this.loopLevel = loopLevel;
    }

    Instruction getInstruction() {
        return opcode;
    }

    int getPos() {
        return pos;
    }

    int getLoopLevel() {
        return loopLevel;
    }
}
