package com.bunjlabs.largo;

public class Instruction {

    public final InstructionName name;
    public int a, b, c;

    public Instruction(InstructionName name) {
        this.name = name;
    }

    public Instruction(InstructionName name, int a) {
        this.name = name;
        this.a = a;
    }

    public Instruction(InstructionName name, int a, int b) {
        this.name = name;
        this.a = a;
        this.b = b;
    }

    public Instruction(InstructionName name, int a, int b, int c) {
        this.name = name;
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public String toString() {
        return String.format("%s %d %d %d", this.name, a, b, c);
    }
}
