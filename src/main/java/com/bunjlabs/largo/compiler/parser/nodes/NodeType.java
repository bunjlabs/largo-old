package com.bunjlabs.largo.compiler.parser.nodes;

public enum NodeType {
    ND_EMPTY,             // empty statement []

    ND_IMPORT,            // import statement [id]
    ND_EXPORT,            // import statement [id]

    ND_STMT_LIST,         // list of statements [left-part, right-part] (left-part may be another list)
    ND_EXPR_STMT,         // single expression as statement [expression]
    ND_EXPR_LIST,         // expression list
    ND_VARINIT_STMT,      // variable initialization (without init value)
    ND_IF_STMT,           // if statement [cond, if-part]
    ND_IFELSE_STMT,       // if statement with else [cond, if-part, else-part]
    ND_WHILE_STMT,        // while statement [cond, while-part]
    ND_FOR_STMT,          // for statement [init, cond, post, for-part]
    ND_CALL,              // function call [id, args]
    ND_BREAK,             // loop break
    ND_CONTINUE,          // loop continue
    ND_RETURN,            // function return statement [expression]

    // Expressions
    ND_BINOP_EXPR,        // Binary operator (+, -, *, +=, -=, ...)
    ND_UNOP_EXPR,         // Unary operator (-a, ++a, --a, )
    ND_ARRAY,             // array expression
    ND_OBJECT,             // array expression
    ND_FIELD_SEL,         // Field select (*.*)
    ND_FIELD_SET,         // Field select (*.*=*)
    ND_INDEX_SEL,         // Index select ([i])
    ND_INDEX_SET,         // Index select ([i]=*)

    // Expressions with data
    ND_NULL,
    ND_UNDEFINED,
    ND_ID,
    ND_NUMBER,
    ND_BOOLEAN,
    ND_STRING,
    ND_FUNC_DEF,

    // Debug
    ND_ERROR,              // error statement
}
