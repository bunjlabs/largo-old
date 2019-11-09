package com.bunjlabs.largo.compiler.lexer;

public enum Token {
    // keywords
    TK_IMPORT,
    TK_EXPORT,
    TK_AS,
    TK_IF,
    TK_ELSE,
    TK_FOR,
    TK_WHILE,
    TK_LET,
    TK_DO,
    TK_BREAK,
    TK_CONTINUE,
    TK_RETURN,

    // Data
    TK_STRING,
    TK_ID,
    TK_NUMBER,
    TK_TRUE,
    TK_FALSE,
    TK_UNDEFINED,
    TK_NULL,

    // Operators
    TK_ASSIGN,    // ==
    TK_PLUSEQ,    // +=
    TK_MINUSEQ,   // -=
    TK_MULTEQ,    // *=
    TK_DIVEQ,     // /=
    TK_MODEQ,     // %=

    TK_PLUS,      // +
    TK_MINUS,     // -
    TK_MULT,      // *
    TK_DIV,       // /
    TK_MOD,       // %

    TK_AND,       // &
    TK_DAND,      // %%
    TK_OR,        // |
    TK_DOR,       // ||
    TK_NOT,       // !

    TK_EQ,        // =
    TK_NOTEQ,     // !=
    TK_GREAT,     // >
    TK_GREATEQ,   // >=
    TK_LESS,      // <
    TK_LESSEQ,    // <=

    TK_DPLUS,     // ++
    TK_DMINUS,    // --

    TK_END_STMT,  // ;
    TK_OPEN_PAR,  // (
    TK_CLOSE_PAR, // )
    TK_OPEN_CS,   // {
    TK_CLOSE_CS,  // }
    TK_OPEN_BR,   // [
    TK_CLOSE_BR,  // ]
    TK_COMMA,     // ,
    TK_DOT,       // .

    TK_ARROW,     // ->

    // Misc
    TK_EOS,
    TK_ERROR
}
