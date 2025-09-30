package org.housingstudio.hsl.compiler.parser;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.Game;
import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.impl.type.Type;
import org.housingstudio.hsl.compiler.codegen.builder.ConditionBuilder;
import org.housingstudio.hsl.compiler.ast.impl.control.ConditionalNode;
import org.housingstudio.hsl.compiler.ast.impl.declaration.*;
import org.housingstudio.hsl.compiler.ast.impl.local.Variable;
import org.housingstudio.hsl.compiler.ast.impl.operator.Operator;
import org.housingstudio.hsl.compiler.ast.impl.scope.Scope;
import org.housingstudio.hsl.compiler.ast.impl.value.*;
import org.housingstudio.hsl.compiler.error.Notification;
import org.housingstudio.hsl.compiler.parser.impl.action.MacroCallParser;
import org.housingstudio.hsl.compiler.parser.impl.action.MethodCallParser;
import org.housingstudio.hsl.compiler.parser.impl.annotation.AnnotationParser;
import org.housingstudio.hsl.compiler.parser.impl.conditional.ConditionParser;
import org.housingstudio.hsl.compiler.parser.impl.conditional.ConditionalParser;
import org.housingstudio.hsl.compiler.parser.impl.declaration.*;
import org.housingstudio.hsl.compiler.parser.impl.local.LocalAssignParser;
import org.housingstudio.hsl.compiler.parser.impl.local.LocalDeclareParser;
import org.housingstudio.hsl.compiler.parser.impl.operator.AssignmentOperatorParser;
import org.housingstudio.hsl.compiler.parser.impl.operator.BinaryOperatorTree;
import org.housingstudio.hsl.compiler.parser.impl.operator.OperatorParser;
import org.housingstudio.hsl.compiler.parser.impl.operator.PostfixOperatorParser;
import org.housingstudio.hsl.compiler.parser.impl.scope.ScopeParser;
import org.housingstudio.hsl.compiler.parser.impl.scope.StatementParser;
import org.housingstudio.hsl.compiler.parser.impl.value.*;
import org.housingstudio.hsl.compiler.error.Errno;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a class that simplifies calls for specific parser algorithms.
 * <p>
 * The implementations of {@link ParserAlgorithm} will implicitly use this to refer to another parser algorithm,
 * based on the parent context.
 */
@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public class AstParser {
    /**
     * The context of the token stream parsing.
     */
    private final @NotNull ParserContext context;

    public @NotNull ConstantDeclare nextConstant() {
        return parse(ConstantParser.class, ConstantDeclare.class);
    }

    public @NotNull Method nextMethod() {
        return parse(MethodParser.class, Method.class);
    }

    public @NotNull Macro nextMacro() {
        return parse(MacroParser.class, Macro.class);
    }

    public @NotNull CommandNode nextCommand() {
        return parse(CommandParser.class, CommandNode.class);
    }

    public @NotNull Event nextEvent() {
        return parse(EventParser.class, Event.class);
    }

    public @NotNull Scope nextScope() {
        return parse(ScopeParser.class, Scope.class);
    }

    public @NotNull Node nextStatement() {
        return parse(StatementParser.class, Node.class);
    }

    public @NotNull Value nextLiteral() {
        return parse(LiteralParser.class, Value.class);
    }

    public @NotNull Value nextBuiltinValue() {
        return parse(BuiltinValueParser.class, Value.class);
    }

    public @NotNull Value nextValue() {
        return parse(ValueParser.class, Value.class);
    }

    public @NotNull Variable nextLocalDeclaration() {
        return parse(LocalDeclareParser.class, Variable.class);
    }

    public @NotNull Node nextLocalAssignment() {
        return parse(LocalAssignParser.class, Node.class);
    }

    public @NotNull Value nextConstantAccess() {
        return parse(ConstantAccessParser.class, Value.class);
    }

    public @NotNull MethodCall nextMethodCall() {
        return parse(MethodCallParser.class, MethodCall.class);
    }

    public @NotNull MacroCall nextMacroCall() {
        return parse(MacroCallParser.class, MacroCall.class);
    }

    @SuppressWarnings("unchecked")
    public @NotNull List<Argument> nextArgumentList() {
        return parse(ArgumentListParser.class, List.class);
    }

    @SuppressWarnings("unchecked")
    public @NotNull List<Parameter> nextParameterList() {
        return parse(ParameterListParser.class, List.class);
    }

    public @NotNull Type nextType() {
        return parse(TypeParser.class, Type.class);
    }

    public @NotNull Operator nextOperator() {
        return parse(OperatorParser.class, Operator.class);
    }

    public @NotNull Annotation nextAnnotation() {
        return parse(AnnotationParser.class, Annotation.class);
    }

    public @NotNull Value nextBinaryOperation(@NotNull Value lhs, @NotNull Operator operator, @NotNull Value rhs) {
        return BinaryOperatorTree.makeBinaryOperator(lhs, operator, rhs);
    }

    public @NotNull Value nextInterpolation() {
        return parse(InterpolationParser.class, Value.class);
    }

    public @NotNull ConditionBuilder nextCondition() {
        return parse(ConditionParser.class, ConditionBuilder.class);
    }

    public @NotNull ConditionalNode nextConditional() {
        return parse(ConditionalParser.class, ConditionalNode.class);
    }

    public @NotNull Value nextGroup() {
        return parse(GroupParser.class, Value.class);
    }

    public @NotNull Value nextConversion() {
        return parse(ConversionParser.class, Value.class);
    }

    public @NotNull Node nextPostfixOperator() {
        return parse(PostfixOperatorParser.class, Node.class);
    }

    public @NotNull Node nextAssignmentOperator() {
        return parse(AssignmentOperatorParser.class, Node.class);
    }

    public void parseGame(@NotNull Game game) {
        while (context.peek().hasNext()) {
            if (context.peek().is(TokenType.EXPRESSION, "fn")) {
                Method method = nextMethod();

                if (game.functions().containsKey(method.name().value())) {
                    context.errorPrinter().print(
                        Notification.error(Errno.METHOD_ALREADY_DEFINED, "method already defined")
                            .error("method is already declared in this scope", method.name())
                    );
                    throw new UnsupportedOperationException("Method name is already in use: " + method.name().value());
                }

                game.functions().put(method.name().value(), method);
            }

            else if (context.peek().is(TokenType.EXPRESSION, "macro")) {
                Macro macro = nextMacro();

                if (game.functions().containsKey(macro.name().value())) {
                    context.errorPrinter().print(
                        Notification.error(Errno.MACRO_ALREADY_DEFINED, "macro already defined")
                            .error("macro is already declared in this scope", macro.name())
                    );
                    throw new UnsupportedOperationException("Macro name is already in use: " + macro.name().value());
                }

                game.macros().put(macro.name().value(), macro);
            }

            else if (context.peek().is(TokenType.EXPRESSION, "command")) {
                CommandNode command = nextCommand();

                if (game.commands().containsKey(command.name().value())) {
                    context.errorPrinter().print(
                        Notification.error(Errno.COMMAND_ALREADY_DEFINED, "command already defined")
                            .error("command is already declared in this scope", command.name())
                    );
                    throw new UnsupportedOperationException("Command name is already in use: " + command.name().value());
                }

                game.commands().put(command.name().value(), command);
            }

            else if (context.peek().is(TokenType.EXPRESSION, "event")) {
                Event event = nextEvent();

                game.events().computeIfAbsent(event.type(), t -> new ArrayList<>()).add(event);
            }

            else if (context.peek().is(TokenType.EXPRESSION, "const")) {
                ConstantDeclare constant = nextConstant();

                if (game.constants().containsKey(constant.name().value())) {
                    context.errorPrinter().print(
                        Notification.error(Errno.CONSTANT_ALREADY_DEFINED, "constant already defined")
                            .error("constant is already declared in this scope", constant.name())
                    );
                    throw new UnsupportedOperationException("Constant name is already in use: " + constant.name().value());
                }

                game.constants().put(constant.name().value(), constant);
            }

            else if (context.peek().is(TokenType.ANNOTATION)) {
                Annotation annotation = nextAnnotation();

                if (
                    context.currentAnnotations()
                        .stream()
                        .anyMatch(a -> a.name().value().equals(annotation.name().value()))
                ) {
                    context.errorPrinter().print(
                        Notification.error(Errno.DUPLICATE_ANNOTATION, "duplicate annotation")
                            .error("annotation is already declared for this target", annotation.name())
                            .note("you can only define @" + annotation.name().value() + " only once for this target")
                    );
                    throw new UnsupportedOperationException("Annotation name is already in use: " + annotation.name().value());
                }

                context.currentAnnotations().add(annotation);
            }

            else {
                context.errorPrinter().print(
                    Notification.error(Errno.UNEXPECTED_TOKEN, "unexpected token: " + context.peek())
                        .error("expected declaration, but found token " + context.peek().type(), context.peek())
                );
                throw new UnsupportedOperationException("Expected declaration but found " + context.peek().print());
            }
        }
    }

    /**
     * Resolve the implementation of the specified {@param target} parser algorithm and parse the result as a
     * {@param type} node.
     *
     * @param target the parser algorithm implementation to resolve
     * @param type the type of the node that the parser algorithm should return
     * @return the next parsed node
     * @param <T> the type of the node that the parser algorithm should return
     */
    @SuppressWarnings("unchecked")
    private <T> T parse(@NotNull Class<? extends ParserAlgorithm<?>> target, @NotNull Class<T> type) {
        // resolve the parser algorithm implementation
        ParserAlgorithm<?> parser = ParserRegistry.of(target);
        // update the context, therefore the algorithm can implicitly mock the calls for the ParseContext class
        parser.setContext(context);
        // parse the result as the specified node type
        try {
            return (T) parser.parse(this, context);
        } catch (ClassCastException e) {
            throw new ParserException(
                "Cannot parse " + type + " from target " + target + " (" + parser.getClass().getSimpleName() + ")"
            );
        }
    }
}
