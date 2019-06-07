package com.bunjlabs.largo.runtime;

public enum OpCodeType {
    // Variable
    L_GETLOCAL,
    L_SETLOCAL,
    L_GETOUTER,
    L_CLOSURE,

    // Object
    L_GETNAME,

    // Export and constants
    L_IMPORT,
    L_CONST,

    // Stack operators
    L_PUSH,
    L_POP,

    // Jumpers
    L_JMP,
    L_JMPF,

    // Constants
    L_BOOLEAN,
    L_UNDEFINED,
    L_NULL,

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

    // Misc
    L_NOP,
    L_ERR,

    // Call
    L_CALL,
    L_RET
}
