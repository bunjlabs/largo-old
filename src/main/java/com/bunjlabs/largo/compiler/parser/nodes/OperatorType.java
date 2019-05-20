package com.bunjlabs.largo.compiler.parser.nodes;

public enum OperatorType {
    // Assignment operators
    OP_ASSIGN(0),    // =
    OP_PLUSEQ(0),    // +=
    OP_MINUSEQ(0),   // -=
    OP_MULTEQ(0),    // *=
    OP_DIVEQ(0),     // /=
    OP_MODEQ(0),     // %=

    // Arithmetic operators
    OP_PLUS(10),      // +
    OP_MINUS(10),     // -
    OP_MULT(11),      // *
    OP_DIV(11),       // /
    OP_MOD(11),       // %

    // Logical operators
    OP_LAND(3),     // &&
    OP_LOR(2),      // ||
    OP_LNOT(11),     // !

    // Unary inc/dec
    OP_DPREPLUS(12),
    OP_DPREMINUS(12),

    OP_DPOSTPLUS(12),
    OP_DPOSTMINUS(12),

    // Comparsion operators
    OP_EQ(1),       // ==
    OP_NOTEQ(1),    // !=
    OP_GREAT(1),    // >
    OP_GREATEQ(1),  // >=
    OP_LESS(1),     // <
    OP_LESSEQ(1),   // <=

    OP_CALL(13),
    OP_FIELD_SEL(13),
    OP_ARROW_FUNC(14),

    OP_UNKNOWN(0);

    private final int priority;

    OperatorType(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
