package org.hsl.compiler.parser;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.compiler.ast.Game;
import org.hsl.compiler.ast.Node;
import org.hsl.compiler.ast.impl.declaration.Constant;
import org.hsl.compiler.ast.impl.declaration.Method;
import org.hsl.compiler.ast.impl.local.Variable;
import org.hsl.compiler.ast.impl.scope.Scope;
import org.hsl.compiler.ast.impl.value.Value;
import org.hsl.compiler.parser.impl.declaration.ConstantParser;
import org.hsl.compiler.parser.impl.declaration.MethodParser;
import org.hsl.compiler.parser.impl.local.LocalAssignParser;
import org.hsl.compiler.parser.impl.local.LocalDeclareParser;
import org.hsl.compiler.parser.impl.scope.ScopeParser;
import org.hsl.compiler.parser.impl.scope.StatementParser;
import org.hsl.compiler.parser.impl.value.BuiltinValueParser;
import org.hsl.compiler.parser.impl.value.ConstantAccessParser;
import org.hsl.compiler.parser.impl.value.LiteralParser;
import org.hsl.compiler.parser.impl.value.ValueParser;
import org.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

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

    public @NotNull Constant nextConstant() {
        return parse(ConstantParser.class, Constant.class);
    }

    public @NotNull Method nextMethod() {
        return parse(MethodParser.class, Method.class);
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

    public void parseGame(@NotNull Game game) {
        while (context.peek().hasNext()) {
            if (context.peek().is(TokenType.EXPRESSION, "fn")) {
                Method method = nextMethod();

                if (game.methods().containsKey(method.name().value())) {
                    context.syntaxError(method.name(), "Method name is already in use");
                    throw new UnsupportedOperationException("Method name is already in use: " + method.name().value());
                }

                game.methods().put(method.name().value(), method);
            }

            if (context.peek().is(TokenType.EXPRESSION, "const")) {
                Constant constant = nextConstant();

                if (game.constants().containsKey(constant.name().value())) {
                    context.syntaxError(constant.name(), "Constant name is already in use");
                    throw new UnsupportedOperationException("Constant name is already in use: " + constant.name().value());
                }

                game.constants().put(constant.name().value(), constant);
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
