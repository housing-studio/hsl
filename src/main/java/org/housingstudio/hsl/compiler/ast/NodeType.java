package org.housingstudio.hsl.compiler.ast;

/**
 * Represents an enumeration of the types of nodes that can be present in the AST.
 */
public enum NodeType {
    SCOPE,
    STATEMENT,
    EXPRESSION,

    LITERAL,
    CONSTANT,
    BUILTIN,

    LOCAL_DECLARE,
    LOCAL_DECLARE_ASSIGN,
    LOCAL_ASSIGN,

    CONSTANT_ACCESS,
    STAT_ACCESS,
    INTERPOLATED_STRING,

    METHOD_CALL,
    RETURN,

    BINARY_OPERATOR,
    PREFIX_UNARY_OPERATOR,
    POSTFIX_UNARY_OPERATOR,
    TERNARY_OPERATOR,

    ARGUMENT,
    METHOD,
    MACRO,
    COMMAND,
    EVENT,
    GAME,

    CONDITIONAL,
    ANNOTATION,

    EMPTY,
    ERROR,
    EOF
}
