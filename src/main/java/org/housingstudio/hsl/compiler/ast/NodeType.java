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
    ENUM,
    ENUM_MEMBER,
    ENUM_LOOKUP,
    BUILTIN,

    LOCAL_DECLARE,
    LOCAL_DECLARE_ASSIGN,
    LOCAL_ASSIGN,

    CONSTANT_ACCESS,
    STAT_ACCESS,
    INTERPOLATED_STRING,
    GROUP,
    COMPARATOR,

    METHOD_CALL,
    MACRO_CALL,
    RETURN,
    RETURN_VALUE,
    PARAMETER_ACCESSOR,
    ARRAY_LOAD,
    ARRAY_STORE,

    BINARY_OPERATOR,
    PREFIX_UNARY_OPERATOR,
    POSTFIX_UNARY_OPERATOR,
    ASSIGNMENT_OPERATOR,
    TERNARY_OPERATOR,

    ARGUMENT,
    METHOD,
    MACRO,
    COMMAND,
    EVENT,
    RANDOM,
    GAME,

    CONDITIONAL,
    FOR_LOOP,
    WHILE_LOOP,
    ANNOTATION,

    EMPTY,
    ERROR,
    EOF
}
