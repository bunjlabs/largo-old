package com.bunjlabs.largo;

import java.util.ArrayList;
import java.util.List;

public class InstructionSequenceBuilder {

    private final List<Instruction> instructions = new ArrayList<>(128);

    public InstructionSequenceBuilder() {
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    private Instruction add(Instruction instruction) {
        instructions.add(instruction);
        return instruction;
    }

    public Instruction nop() {
        return add(new Instruction(InstructionName.L_NOP));
    }

    public Instruction err() {
        return add(new Instruction(InstructionName.L_ERR));
    }

    public Instruction getindex(int result, int array, int index) {
        return add(new Instruction(InstructionName.L_GETINDEX, result, array, index));
    }

    public Instruction putindex(int array, int index, int value) {
        return add(new Instruction(InstructionName.L_PUTINDEX, array, index, value));
    }

    public Instruction pusha(int array, int value) {
        return add(new Instruction(InstructionName.L_PUSHA, array, value));
    }

    public Instruction getfield(int result, int object, int constant) {
        return add(new Instruction(InstructionName.L_GETFIELD, result, object, constant));
    }

    public Instruction putfield(int object, int fieldConstant, int value) {
        return add(new Instruction(InstructionName.L_SETFIELD, object, fieldConstant, value));
    }

    // import
    public Instruction imp(int result, int constant) {
        return add(new Instruction(InstructionName.L_IMPORT, result, constant));
    }

    // stack operators
    public Instruction push(int value) {
        return add(new Instruction(InstructionName.L_PUSH, value));
    }

    public Instruction pop(int result) {
        return add(new Instruction(InstructionName.L_POP, result));
    }

    // flow control
    public Instruction jmp(int offset) {
        return add(new Instruction(InstructionName.L_JMP, offset));
    }

    public Instruction jmpf(int value, int offset) {
        return add(new Instruction(InstructionName.L_JMPF, value, offset));
    }

    // load
    public Instruction load(int result, int variable) {
        return add(new Instruction(InstructionName.L_LOAD, result, variable));
    }

    public Instruction store(int variable, int reg) {
        return add(new Instruction(InstructionName.L_STORE, variable, reg));
    }

    public Instruction loado(int result, int variable) {
        return add(new Instruction(InstructionName.L_LOADO, result, variable));
    }

    public Instruction storeo(int variable, int reg) {
        return add(new Instruction(InstructionName.L_STOREO, variable, reg));
    }

    public Instruction loadc(int result, int constant) {
        return add(new Instruction(InstructionName.L_LOADC, result, constant));
    }

    public Instruction loada(int result) {
        return add(new Instruction(InstructionName.L_LOADA, result));
    }

    public Instruction loadm(int result) {
        return add(new Instruction(InstructionName.L_LOADM, result));
    }

    public Instruction loadb(int result, int b) {
        return add(new Instruction(InstructionName.L_LOADB, result, b));
    }

    public Instruction loadu(int result) {
        return add(new Instruction(InstructionName.L_LOADU, result));
    }

    public Instruction loadn(int result) {
        return add(new Instruction(InstructionName.L_LOADN, result));
    }
    public Instruction loadf(int result, int blueprint) {
        return add(new Instruction(InstructionName.L_LOADF, result, blueprint));
    }

    // arithmetical
    public Instruction add(int a, int b, int c) {
        return add(new Instruction(InstructionName.L_ADD, a, b, c));
    }

    public Instruction mul(int a, int b, int c) {
        return add(new Instruction(InstructionName.L_MUL, a, b, c));
    }

    public Instruction sub(int a, int b, int c) {
        return add(new Instruction(InstructionName.L_SUB, a, b, c));
    }

    public Instruction div(int a, int b, int c) {
        return add(new Instruction(InstructionName.L_DIV, a, b, c));
    }

    public Instruction mod(int a, int b, int c) {
        return add(new Instruction(InstructionName.L_MOD, a, b, c));
    }

    // unary
    public Instruction pos(int a, int b) {
        return add(new Instruction(InstructionName.L_POS, a, b));
    }

    public Instruction neg(int a, int b) {
        return add(new Instruction(InstructionName.L_NEG, a, b));
    }

    // inc/dec
    public Instruction inc(int a, int b) {
        return add(new Instruction(InstructionName.L_INC, a, b));
    }

    public Instruction dec(int a, int b) {
        return add(new Instruction(InstructionName.L_DEC, a, b));
    }

    // boolean
    public Instruction eq(int a, int b, int c) {
        return add(new Instruction(InstructionName.L_EQ, a, b, c));
    }

    public Instruction neq(int a, int b, int c) {
        return add(new Instruction(InstructionName.L_NOTEQ, a, b, c));
    }

    public Instruction gt(int a, int b, int c) {
        return add(new Instruction(InstructionName.L_GT, a, b, c));
    }

    public Instruction gteq(int a, int b, int c) {
        return add(new Instruction(InstructionName.L_GTEQ, a, b, c));
    }

    public Instruction lt(int a, int b, int c) {
        return add(new Instruction(InstructionName.L_LT, a, b, c));
    }

    public Instruction lteq(int a, int b, int c) {
        return add(new Instruction(InstructionName.L_LTEQ, a, b, c));
    }

    // logic
    public Instruction and(int a, int b, int c) {
        return add(new Instruction(InstructionName.L_AND, a, b, c));
    }

    public Instruction or(int a, int b, int c) {
        return add(new Instruction(InstructionName.L_OR, a, b, c));
    }

    public Instruction not(int a, int b) {
        return add(new Instruction(InstructionName.L_NOT, a, b));
    }

    // closure
    public Instruction call(int result, int closure, int args) {
        return add(new Instruction(InstructionName.L_CALL, result, closure, args));
    }

    public Instruction ret(int value) {
        return add(new Instruction(InstructionName.L_RET, value));
    }
}
