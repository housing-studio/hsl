package org.housingstudio.hsl.compiler.parser.impl.declaration;

import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Enum;
import org.housingstudio.hsl.compiler.ast.impl.type.Type;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.error.Errno;
import org.housingstudio.hsl.compiler.error.Notification;
import org.housingstudio.hsl.compiler.parser.AstParser;
import org.housingstudio.hsl.compiler.parser.ParserAlgorithm;
import org.housingstudio.hsl.compiler.parser.ParserContext;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EnumParser extends ParserAlgorithm<Enum> {
    /**
     * Parse the next {@link Enum} node from the token stream.
     *
     * @param parser  the AST node parser
     * @param context the token parser context
     * @return the next {@link Enum} node from the token stream
     */
    @Override
    public @NotNull Enum parse(@NotNull AstParser parser, @NotNull ParserContext context) {
        get(TokenType.EXPRESSION, "enum");
        Token name = get(TokenType.IDENTIFIER);

        Enum.Kind kind = null;

        Type alias = null;
        if (peek().is(TokenType.COLON)) {
            get(TokenType.COLON);
            alias = parser.nextType();
            kind = Enum.Kind.VALUE;
        }

        List<Enum.Member> members = new ArrayList<>();
        get(TokenType.LBRACE);

        while (!peek().is(TokenType.RBRACE)) {
            Enum.Member member = parseMember(parser, context);
            if (peek().is(TokenType.SEMICOLON, "auto"))
                get(TokenType.SEMICOLON);

            if (kind == null)
                kind = member.kind();
            else if (member.kind() != kind) {
                context.errorPrinter().print(
                    Notification.error(Errno.ENUM_MEMBER_MISMATCH, "enum member mismatch")
                        .error("cannot mix sum and value enum members", member.name())
                );
                throw new IllegalStateException("Enum type mismatch: " + name.value());
            }

            if (member.kind() == Enum.Kind.VALUE && alias == null) {
                context.errorPrinter().print(
                    Notification.error(Errno.MISSING_ENUM_TYPE_ALIAS, "missing enum type alias")
                        .error("expected an enum type alias", name)
                        .note("consider specifying type alias", "enum Foo: string { ... }")
                );
                throw new IllegalStateException("Missing enum type alias: " + name.value());
            }

            members.add(member);

            if (peek().is(TokenType.COMMA))
                get(TokenType.COMMA);
            else
                break;
        }

        if (kind == null)
            kind = Enum.Kind.SUM;

        get(TokenType.RBRACE);

        if (peek().is(TokenType.SEMICOLON, "auto"))
            get(TokenType.SEMICOLON);

        return new Enum(name, alias, kind, members);
    }

    private @NotNull Enum.Member parseMember(@NotNull AstParser parser, @NotNull ParserContext context) {
        Token name = get(TokenType.IDENTIFIER);
        if (peek().is(TokenType.OPERATOR, "=")) {
            get(TokenType.OPERATOR, "=");
            Value value = parser.nextValue();
            return new Enum.Member(name, Enum.Kind.VALUE, Collections.emptyList(), value);
        }

        if (!peek().is(TokenType.LPAREN))
            return new Enum.Member(name, Enum.Kind.SUM, Collections.emptyList(), null);

        List<Type> fields = new ArrayList<>();
        get(TokenType.LPAREN);

        while (!peek().is(TokenType.RPAREN)) {
            fields.add(parser.nextType());

            if (peek().is(TokenType.COMMA))
                get(TokenType.COMMA);
            else
                break;
        }

        get(TokenType.RPAREN);
        return new Enum.Member(name, Enum.Kind.SUM, fields, null);
    }
}
