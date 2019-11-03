package com.bunjlabs.largo;

public enum InstructionName {
    L_NOP,
    L_ERR,

    // Variable
    L_LOAD,
    L_STORE,
    L_LOADO,
    L_STOREO,

    // Arrays
    L_GETINDEX,
    L_PUTINDEX,
    L_PUSHA,

    // Objects
    L_GETFIELD,
    L_SETFIELD,

    // Export and constants
    L_IMPORT,

    // Stack operators
    L_PUSH,
    L_POP,

    // Flow control
    L_JMP,
    L_JMPF,

    // Constants
    L_LOADC,
    L_LOADA,
    L_LOADM,
    L_LOADB,
    L_LOADU,
    L_LOADN,

    // Arithmetical
    L_ADD,
    L_MUL,
    L_SUB,
    L_DIV,
    L_MOD,

    // Unary
    L_POS,
    L_NEG,

    // Inc/Dec
    L_INC,
    L_DEC,

    // Boolean
    L_EQ,
    L_NOTEQ,
    L_GT,
    L_GTEQ,
    L_LT,
    L_LTEQ,

    // Logic
    L_AND,
    L_OR,
    L_NOT,

    // Closure
    L_LOADF,
    L_CALL,
    L_RET

}
