package org.housingstudio.hsl.compiler.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.runtime.vm.Frame;

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
    ILLEGAL_TYPE_CONVERSION(141),

    /**
     * `CANNOT_INFER_TYPE` indicates, that the declaration could not infer the value type.
     */
    CANNOT_INFER_TYPE(142),

    /**
     * `TOO_MANY_ACTIONS` indicates, that more actions were called by a function than allowed by Hypixel
     */
    TOO_MANY_ACTIONS(143),

    /**
     * `INVALID_PREFIX_OPERATOR` indicates, that the given operator is not a valid prefix operator.
     */
    INVALID_PREFIX_OPERATOR(144),

    /**
     * `INVALID_POSTFIX_OPERATOR` indicates, that the given operator is not a valid postfix operator.
     */
    INVALID_POSTFIX_OPERATOR(145),

    /**
     * `INVALID_ASSIGNMENT_OPERATOR` indicates, that the given operator is not a valid assignment operator.
     */
    INVALID_ASSIGNMENT_OPERATOR(146),

    /**
     * `EXPECTED_CONSTANT_VALUE` indicates, that a dynamic value was given, but a constant literal was expected.
     */
    EXPECTED_CONSTANT_VALUE(147),

    /**
     * `EXPECTED_COMPARATOR_OPERATOR` indicates, that a comparator operator was expected, but something else ęas given.
     */
    EXPECTED_COMPARATOR_OPERATOR(148),

    /**
     * `INVALID_INTEGER_VALUE` indicates, that an integer-like value was found with an invalid format.
     */
    INVALID_INTEGER_VALUE(149),

    /**
     * `INVALID_FLOAT_VALUE` indicates, that a float-like value was found with an invalid format.
     */
    INVALID_FLOAT_VALUE(150),

    /**
     * `RESERVED_MACRO_NAME` indicates, that the chosen macro name is reserved.
     */
    RESERVED_MACRO_NAME(151),

    /**
     * `RESERVED_FUNCTION_NAME` indicates, that the chosen function name is reserved.
     */
    RESERVED_FUNCTION_NAME(152),

    /**
     * `CANNOT_INVOKE_MAIN_MACRO` indicates, that the main macro was attempted to be called.
     */
    CANNOT_INVOKE_MAIN_MACRO(153),

    /**
     * `NOT_IMPLEMENTED_FEATURE` indicates, that a compiler feature use was syntactically correct, but the underlying
     * infrastructure is not implemented yet.
     */
    NOT_IMPLEMENTED_FEATURE(154),

    /**
     * `ENUM_MEMBER_MISMATCH` indicates, that an enum declaration mixed the use of value and sum members.
     */
    ENUM_MEMBER_MISMATCH(155),

    /**
     * `MISSING_ENUM_TYPE_ALIAS` indicates, that an enum member is a value type, but the enum itself does not declare
     * a type alias.
     */
    MISSING_ENUM_TYPE_ALIAS(156),

    /**
     * `ENUM_MEMBER_VALUE_TYPE_MISMATCH` indicates, that a value enum expected a T type, but the member's value is not
     * of type T.
     */
    ENUM_MEMBER_VALUE_TYPE_MISMATCH(157),

    /**
     * `ENUM_ALREADY_DEFINED` indicates, that an enum with the specified name is already defined.
     */
    ENUM_ALREADY_DEFINED(158),

    /**
     * `MISSING_ENUM_MEMBER` indicates, that the specified member could not be found for the target enum.
     */
    MISSING_ENUM_MEMBER(159),

    /**
     * `UNKNOWN_TYPE` indicates, that the specified type could not be resolved.
     */
    UNKNOWN_TYPE(160),

    /**
     * `CANNOT_ACCESS_ARRAY_DYNAMICALLY` indicates, that the underlying array could not be stored/loaded dynamically.
     */
    CANNOT_ACCESS_ARRAY_DYNAMICALLY(161),

    /**
     * `STACK_OVERFLOW` indicates, that the stack frame tree exceeded the maximum depth of {@link Frame#MAX_DEPTH}
     */
    STACK_OVERFLOW(162),

    /**
     * `ILLEGAL_NESTED_CONDITIONAL` indicates, that a conditional was declared within another conditional.
     */
    ILLEGAL_NESTED_CONDITIONAL(163),

    /**
     * `NON_TERMINAL_RETURN` indicates, that a `return` instruction was put before the end of a scope.
     */
    NON_TERMINAL_RETURN(164);

    /**
     * The error code of the token error.
     */
    private final int code;
}
