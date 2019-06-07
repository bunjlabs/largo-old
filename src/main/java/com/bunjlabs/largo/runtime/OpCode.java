package com.bunjlabs.largo.runtime;

public class OpCode {

    public final OpCodeType type;
    public int a, b, c;

    public OpCode(OpCodeType type) {
        this.type = type;
        this.a = 0;
        this.b = 0;
        this.c = 0;
    }

    public OpCode(OpCodeType type, int a) {
        this.type = type;
        this.a = a;
        this.b = 0;
        this.c = 0;
    }

    public OpCode(OpCodeType type, int a, int b) {
        this.type = type;
        this.a = a;
        this.b = b;
        this.c = 0;
    }

    public OpCode(OpCodeType type, int a, int b, int c) {
        this.type = type;
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public String toString() {
        return String.format("%s %d %d %d", type, a, b, c);
    }
}
