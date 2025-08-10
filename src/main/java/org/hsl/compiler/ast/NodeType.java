package org.hsl.compiler.ast;

/**
 * Represents an enumeration of the types of nodes that can be present in the AST.
 */
public enum NodeType {
    SCOPE,
    STATEMENT,
    EXPRESSION,

    LITERAL,

    LOCAL_DECLARE,
    LOCAL_DECLARE_ASSIGN,
    LOCAL_ASSIGN,

    NAME_ACCESS,

    METHOD_CALL,

    BINARY_OPERATOR,
    PREFIX_UNARY_OPERATOR,
    POSTFIX_UNARY_OPERATOR,
    TERNARY_OPERATOR,

    METHOD,
    CONSTANT,

    EMPTY,
    ERROR,
    EOF
}
