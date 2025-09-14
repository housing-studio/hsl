package org.housingstudio.hsl.compiler.token;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Represents an enumeration of token errors that can occur during tokenization.
 *
 * @author AdvancedAntiSkid
 */
@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public enum Errno {
    /**
     * `UNEXPECTED_CHARACTER` indicates, that the tokenizer encountered an unexpected character.
     */
    UNEXPECTED_CHARACTER(101),

    /**
     * `INVALID_ESCAPE_SEQUENCE` indicates, that the tokenizer detected an invalid character that follows a backslash.
     */
    INVALID_ESCAPE_SEQUENCE(102),

    /**
     * `MISSING_STRING_TERMINATOR` indicates, that the tokenizer did not find a closing quote for a string literal,
     * or the beginning and ending quotes are not the same.
     */
    MISSING_STRING_TERMINATOR(103),

    /**
     * `INVALID_UNSIGNED_LITERAL` indicates, that the tokenizer detected an invalid unsigned literal.
     */
    INVALID_UNSIGNED_LITERAL(104),

    /**
     * `MULTIPLE_DECIMAL_POINTS` indicates, that the tokenizer detected multiple decimal points in a number literal.
     */
    MULTIPLE_DECIMAL_POINTS(105),

    /**
     * `CANNOT_HAVE_DECIMAL_POINT` indicates, that the tokenizer detected a decimal point in a number literal that
     * cannot have a decimal point.
     */
    CANNOT_HAVE_DECIMAL_POINT(106),

    /**
     * `UNEXPECTED_TOKEN` indicates, that the tokenizer encountered an unexpected token.
     */
    UNEXPECTED_TOKEN(107),

    /**
     * `UNKNOWN_VARIABLE` indicates, that a variable is being accessed that is not found.
     */
    UNKNOWN_VARIABLE(108),

    /**
     * `UNKNOWN_VARIABLE` indicates, that a duration value has invalid format.
     */
    INVALID_DURATION_VALUE(109),

    /**
     * `UNKNOWN_METHOD` indicates, that a method is being called that is not found.
     */
    UNKNOWN_METHOD(110),

    /**
     * `UNEXPECTED_ANNOTATION_VALUE` indicates, that an annotation was used for a target that is not allowed on.
     */
    UNEXPECTED_ANNOTATION_TARGET(111),

    /**
     * `UNEXPECTED_ANNOTATION_VALUE` indicates, that an annotation received an unexpected value.
     */
    UNEXPECTED_ANNOTATION_VALUE(112),

    /**
     * `CIRCULAR_REFERENCE` indicates, that two or more nodes refer to each other, creating an irresolvable circular
     * dependency.
     */
    CIRCULAR_REFERENCE(113),

    /**
     * `CANNOT_REDECLARE_VARIABLE` indicates, that two or more variables have the same name in the same scope.
     */
    CANNOT_REDECLARE_VARIABLE(114),

    /**
     * `UNEXPECTED_CONDITION_TARGET` indicates, that a condition function was used as an action or method call.
     */
    UNEXPECTED_CONDITION_TARGET(115),

    /**
     * `FUNCTION_TRIGGER_AS_EXPRESSION` indicates, that a function call was used as an expression.
     */
    FUNCTION_TRIGGER_AS_EXPRESSION(116),

    /**
     * `FUNCTION_TRIGGER_WITH_ARGUMENTS`, indicates that a function was called with arguments.
     */
    FUNCTION_TRIGGER_WITH_ARGUMENTS(117),

    /**
     * `FUNCTION_TRIGGER_WITH_ARGUMENTS` indicates, that a required parameter did not receive an argument.
     */
    MISSING_REQUIRED_ARGUMENT(118),

    /**
     * `INVALID_ARGUMENT_TYPE` indicates, that a parameter expected a given type, but expected another kind.
     */
    INVALID_ARGUMENT_TYPE(119),

    /**
     * `MULTIPLE_ARGUMENT_VALUES` indicates, that a named argument overridden a position argument.
     */
    MULTIPLE_ARGUMENT_VALUES(120),

    /**
     * `UNKNOWN_NAMED_ARGUMENT` indicates, that no such parameter found for a named argument.
     */
    UNKNOWN_NAMED_ARGUMENT(121),

    /**
     * `DUPLICATE_NAMED_ARGUMENT` indicates, that multiple named arguments are specified for the same parameter.
     */
    DUPLICATE_NAMED_ARGUMENT(122),

    /**
     * `TOO_MANY_POSITIONAL_ARGUMENTS` indicates, that more positional arguments are given, than expected.
     */
    TOO_MANY_POSITIONAL_ARGUMENTS(123),

    /**
     * `POSITIONAL_ARGUMENT_AFTER_NAMED_ARGUMENT` indicates, that a positional argument was specified after a named
     * argument.
     */
    POSITIONAL_ARGUMENT_AFTER_NAMED_ARGUMENT(124),

    /**
     * `METHOD_ALREADY_DEFINED` indicates, that a method is already declared in that scope with the same name.
     */
    METHOD_ALREADY_DEFINED(125),

    /**
     * `COMMAND_ALREADY_DEFINED` indicates, that a command is already declared in that scope with the same name.
     */
    COMMAND_ALREADY_DEFINED(126),

    /**
     * `CONSTANT_ALREADY_DEFINED` indicates, that a constant is already declared in that scope with the same name.
     */
    CONSTANT_ALREADY_DEFINED(127),

    /**
     * `MACRO_ALREADY_DEFINED` indicates, that a macro is already declared in that scope with the same name.
     */
    MACRO_ALREADY_DEFINED(128),

    /**
     * `DUPLICATE_ANNOTATION` indicates, that an annotation of the same kind is already defined for the current target.
     */
    DUPLICATE_ANNOTATION(129),

    /**
     * `UNEXPECTED_NAMESPACE` indicates, that a stat has specified an invalid namespace.
     */
    UNEXPECTED_NAMESPACE(130),

    /**
     * `UNEXPECTED_TYPE` indicates, that an invalid type name was specified.
     */
    UNEXPECTED_TYPE(131),

    /**
     * `EXPECTED_ASSIGNMENT_TO_INFER_TYPE` indicates, that the stat declaration did not follow assignment, and no
     * type information was specified, so no type can be inferred.
     */
    EXPECTED_ASSIGNMENT_TO_INFER_TYPE(132),

    /**
     * `INFER_TYPE_MISMATCH` indicates, that the inferred type does not match the type of the assigned value.
     */
    INFER_TYPE_MISMATCH(133),

    /**
     * `OPERATOR_TYPE_MISMATCH` indicates, that the LHS and RHS of a binary operator has mismatching types.
     */
    OPERATOR_TYPE_MISMATCH(134),

    /**
     * `UNEXPECTED_OPERAND` indicates, that an operation was attempted against unsupported operands.
     */
    UNEXPECTED_OPERAND(135),

    /**
     * `UNEXPECTED_EVENT_TYPE` indicates, that an unrecognized event group was specified.
     */
    UNEXPECTED_EVENT_TYPE(136),

    /**
     * `UNKNOWN_CONDITION` indicates, that an unknown condition method was specified.
     */
    UNKNOWN_CONDITION(137),

    /**
     * `CANNOT_MIX_OPERATORS` indicates, that multiple operator kinds were specified in a function condition.
     */
    CANNOT_MIX_OPERATORS(138),

    /**
     * `INVALID_BUILTIN_TYPE` indicates, that malformed or invalid builtin type was specified.
     */
    INVALID_BUILTIN_TYPE(139),

    /**
     * `UNKNOWN_MACRO` indicates, that the macro could not be found.
     */
    UNKNOWN_MACRO(140),

    /**
     * `ILLEGAL_TYPE_CONVERSION` indicates, that the target value cannot be converted to the specified type.
     */
    ILLEGAL_TYPE_CONVERSION(141),;

    /**
     * The error code of the token error.
     */
    private final int code;
}
