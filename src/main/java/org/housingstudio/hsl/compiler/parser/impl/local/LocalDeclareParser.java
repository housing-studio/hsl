package org.housingstudio.hsl.compiler.parser.impl.local;

import org.housingstudio.hsl.compiler.ast.impl.local.LocalDeclare;
import org.housingstudio.hsl.compiler.ast.impl.local.LocalDeclareAssign;
import org.housingstudio.hsl.compiler.ast.impl.local.Variable;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.type.Namespace;
import org.housingstudio.hsl.compiler.ast.impl.type.Type;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.parser.AstParser;
import org.housingstudio.hsl.compiler.parser.ParserAlgorithm;
import org.housingstudio.hsl.compiler.parser.ParserContext;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

public class LocalDeclareParser extends ParserAlgorithm<Variable> {
    /**
     * Parse the next {@link Variable} node from the token stream.
     *
     * @param parser the AST node parser
     * @param context the token parser context
     * @return the next {@link Variable} node from the token stream
     */
    @Override
    public @NotNull Variable parse(@NotNull AstParser parser, @NotNull ParserContext context) {
        // skip the `stat` keyword
        // stat player balance = 2000
        // ^^^ the `stat` keyword indicates, that a local variable is being declared
        get(TokenType.EXPRESSION, "stat");

        // parse the stat namespace
        // stat team kills: int
        //      ^^^^ the identifier after `stat` describes the namespace of the stat
        Namespace namespace = switch (peek(TokenType.IDENTIFIER).value()) {
            case "player" -> Namespace.PLAYER;
            case "team" -> Namespace.TEAM;
            case "global" -> Namespace.GLOBAL;
            default -> {
                context.syntaxError(peek(), "Invalid stat namespace");
                throw new UnsupportedOperationException("Invalid stat namespace: " + peek());
            }
        };
        get();

        // parse the local variable name
        // stat global msg: string
        //             ^^^ the name of the local variable
        Token name = get(TokenType.IDENTIFIER);

        // parse the local variable type
        Type type = null; // if null, infer type
        if (peek().is(TokenType.COLON)) {
            get();
            type = switch (peek(TokenType.TYPE).value()) {
                case "int" -> Type.INT;
                case "float" -> Type.FLOAT;
                case "bool" -> Type.BOOL;
                case "string" -> Type.STRING;
                case "any" -> Type.ANY;
                default -> {
                    context.syntaxError(peek(), "Invalid type");
                    throw new UnsupportedOperationException("Invalid type: " + peek());
                }
            };
            get();
        }

        // check if no explicit type is specified and the declaration does not follow an assignment
        if (type == null && !peek().is(TokenType.OPERATOR, "=")) {
            context.syntaxError(peek(), "Expected assignment to infer type");
            throw new UnsupportedOperationException("Expected assignment to infer type: " + peek());
        }

        // parse the assignment after declaration
        Value value = null;
        if (peek().is(TokenType.OPERATOR, "=")) {
            get();
            value = parser.nextValue();
        }

        // skip the semicolon after the declaration
        if (peek().is(TokenType.SEMICOLON))
            get();

        // handle local declaration without assignment
        if (value == null) {
            assert type != null;
            return new LocalDeclare(namespace, name, type);
        }

        // infer type from value if no explicit type is specified
        if (type == null)
            type = value.getValueType();

        // TODO handle infer-explicit type mismatch

        // handle local declaration with assignment
        return new LocalDeclareAssign(namespace, name, type, value);
    }
}
