package org.hsl.compiler.parser;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.compiler.ast.Game;
import org.hsl.compiler.ast.Node;
import org.hsl.compiler.ast.builder.ConditionBuilder;
import org.hsl.compiler.ast.impl.control.ConditionalNode;
import org.hsl.compiler.ast.impl.declaration.CommandNode;
import org.hsl.compiler.ast.impl.declaration.ConstantDeclare;
import org.hsl.compiler.ast.impl.declaration.Event;
import org.hsl.compiler.ast.impl.declaration.Method;
import org.hsl.compiler.ast.impl.local.Variable;
import org.hsl.compiler.ast.impl.operator.Operator;
import org.hsl.compiler.ast.impl.scope.Scope;
import org.hsl.compiler.ast.impl.type.Type;
import org.hsl.compiler.ast.impl.value.Annotation;
import org.hsl.compiler.ast.impl.value.Argument;
import org.hsl.compiler.parser.impl.action.MethodCallParser;
import org.hsl.compiler.ast.impl.value.Value;
import org.hsl.compiler.parser.impl.annotation.AnnotationParser;
import org.hsl.compiler.parser.impl.conditional.ConditionParser;
import org.hsl.compiler.parser.impl.conditional.ConditionalParser;
import org.hsl.compiler.parser.impl.declaration.CommandParser;
import org.hsl.compiler.parser.impl.declaration.ConstantParser;
import org.hsl.compiler.parser.impl.declaration.EventParser;
import org.hsl.compiler.parser.impl.declaration.MethodParser;
import org.hsl.compiler.parser.impl.local.LocalAssignParser;
import org.hsl.compiler.parser.impl.local.LocalDeclareParser;
import org.hsl.compiler.parser.impl.operator.BinaryOperatorTree;
import org.hsl.compiler.parser.impl.operator.OperatorParser;
import org.hsl.compiler.parser.impl.scope.ScopeParser;
import org.hsl.compiler.parser.impl.scope.StatementParser;
import org.hsl.compiler.parser.impl.value.*;
import org.hsl.compiler.token.TokenType;
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

    @SuppressWarnings("unchecked")
    public @NotNull List<Argument> nextArgumentList() {
        return parse(ArgumentListParser.class, List.class);
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

    public @NotNull ConditionBuilder nextCondition() {
        return parse(ConditionParser.class, ConditionBuilder.class);
    }

    public @NotNull ConditionalNode nextConditional() {
        return parse(ConditionalParser.class, ConditionalNode.class);
    }

    public void parseGame(@NotNull Game game) {
        while (context.peek().hasNext()) {
            if (context.peek().is(TokenType.EXPRESSION, "fn")) {
                Method method = nextMethod();

                if (game.functions().containsKey(method.name().value())) {
                    context.syntaxError(method.name(), "Method name is already in use");
                    throw new UnsupportedOperationException("Method name is already in use: " + method.name().value());
                }

                game.functions().put(method.name().value(), method);
            }

            else if (context.peek().is(TokenType.EXPRESSION, "command")) {
                CommandNode command = nextCommand();

                if (game.commands().containsKey(command.name().value())) {
                    context.syntaxError(command.name(), "Command name is already in use");
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
                    context.syntaxError(constant.name(), "Constant name is already in use");
                    throw new UnsupportedOperationException("Constant name is already in use: " + constant.name().value());
                }

                game.constants().put(constant.name().value(), constant);
            }

            else if (context.peek().is(TokenType.ANNOTATION)) {
                Annotation annotation = nextAnnotation();

                if (
                    context.currentAnnotations().stream().anyMatch(a -> a.name().value().equals(annotation.name().value()))
                ) {
                    context.syntaxError(annotation.name(), "Annotation name is already in use");
                    throw new UnsupportedOperationException("Annotation name is already in use: " + annotation.name().value());
                }

                context.currentAnnotations().add(annotation);
            }

            else {
                context.syntaxError(context.peek(), "Expected declaration, got " + context.peek());
                throw new UnsupportedOperationException("Expected declaration but got " + context.peek());
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
