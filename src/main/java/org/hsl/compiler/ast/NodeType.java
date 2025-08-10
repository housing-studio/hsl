package org.hsl.compiler.ast;

/**
 * Represents an enumeration of the types of nodes that can be present in the AST.
 */
public enum NodeType {
    SCOPE,
    STATEMENT,
    EXPRESSION,

    LITERAL,

    STAT_DECLARE,
    STAT_DECLARE_ASSIGN,
    LOCAL_ASSIGN,

    NAME_ACCESS,

    METHOD_CALL,

    BINARY_OPERATOR,
    PREFIX_UNARY_OPERATOR,
    POSTFIX_UNARY_OPERATOR,
    TERNARY_OPERATOR,

    RETURN,

    METHOD,

    EMPTY,
    ERROR,
    EOF
}
