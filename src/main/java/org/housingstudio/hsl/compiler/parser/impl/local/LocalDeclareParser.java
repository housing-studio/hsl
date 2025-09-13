package org.housingstudio.hsl.compiler.parser.impl.local;

import org.housingstudio.hsl.compiler.ast.impl.local.LocalDeclare;
import org.housingstudio.hsl.compiler.ast.impl.local.LocalDeclareAssign;
import org.housingstudio.hsl.compiler.ast.impl.local.Variable;
import org.housingstudio.hsl.compiler.token.Errno;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.std.Namespace;
import org.housingstudio.hsl.compiler.ast.impl.type.BaseType;
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
        Namespace namespace;
        switch (peek(TokenType.IDENTIFIER).value()) {
            case "player":
                namespace = Namespace.PLAYER;
                break;
            case "team":
                namespace = Namespace.TEAM;
                break;
            case "global":
                namespace = Namespace.GLOBAL;
                break;
            default:
                context.error(
                    Errno.UNEXPECTED_NAMESPACE,
                    "invalid namespace `" + peek().value() + "` for stat",
                    peek(),
                    "unexpected stat namespace"
                );
                throw new UnsupportedOperationException("Invalid stat namespace: " + peek());
        }
        get();

        // parse the local variable name
        // stat global msg: string
        //             ^^^ the name of the local variable
        Token name = get(TokenType.IDENTIFIER);

        // parse the local variable type
        BaseType type = null; // if null, infer type
        Token typeToken = null;
        if (peek().is(TokenType.COLON)) {
            get();
            typeToken = peek(TokenType.TYPE);
            switch (typeToken.value()) {
                case "int":
                    type = BaseType.INT;
                    break;
                case "float":
                    type = BaseType.FLOAT;
                    break;
                case "bool":
                    type = BaseType.BOOL;
                    break;
                case "string":
                    type = BaseType.STRING;
                    break;
                case "any":
                    type = BaseType.ANY;
                    break;
                default:
                    context.error(
                        Errno.UNEXPECTED_TYPE,
                        "unexpected stat type",
                        peek(),
                        "unrecognized stat type"
                    );
                    throw new UnsupportedOperationException("Invalid type: " + peek());
            }
            get();
        }

        // check if no explicit type is specified and the declaration does not follow an assignment
        if (type == null && !peek().is(TokenType.OPERATOR, "=")) {
            context.error(
                Errno.EXPECTED_ASSIGNMENT_TO_INFER_TYPE,
                "expected assignment to infer type",
                peek(),
                "either specify an explicit type, or specify a value to infer type"
            );
            // TODO note: specify an explicit type: stat player foo: int
            // TODO note: specify a value to infer type: stat player foo = bar
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

        // handle local declaration with assignment
        return new LocalDeclareAssign(namespace, name, type, typeToken, value);
    }
}
