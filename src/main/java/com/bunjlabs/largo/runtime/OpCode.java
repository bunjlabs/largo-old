package com.bunjlabs.largo.runtime;

public class OpCode {

    private final OpCodeType type;
    private int arg;

    public OpCode(OpCodeType type) {
        this.type = type;
        this.arg = 0;
    }

    public OpCode(OpCodeType type, int arg) {
        this.type = type;
        this.arg = arg;
    }

    public OpCodeType getType() {
        return type;
    }

    public int getArg() {
        return arg;
    }

    public void setArg(int arg) {
        this.arg = arg;
    }

    @Override
    public String toString() {
        return "OpCode{" +
                "type=" + type +
                ", arg=" + arg +
                '}';
    }
}
