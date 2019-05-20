package com.bunjlabs.largo.runtime;

public enum OpCodeType {
    // Stack operators
    L_GETLOCAL,
    L_SETLOCAL,
    L_SETLOCAL_S,
    L_GETNAME,
    L_IMPORT,
    L_CONST,
    L_POP,

    // Jumpers
    L_JUMP,
    L_JUMP_F,

    // Constants
    L_TRUE,
    L_FALSE,
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
    L_GREAT,
    L_GREATEQ,
    L_LESS,
    L_LESSEQ,

    // Logic
    L_AND,
    L_OR,
    L_NOT,

    // Misc
    L_NOP,
    L_ERR,

    // Call
    L_CALL
}
