package org.housingstudio.hsl.compiler.parser.impl.declaration;

import org.housingstudio.hsl.compiler.ast.impl.declaration.Event;
import org.housingstudio.hsl.compiler.ast.impl.scope.Scope;
import org.housingstudio.hsl.compiler.error.Notification;
import org.housingstudio.hsl.compiler.parser.AstParser;
import org.housingstudio.hsl.compiler.parser.ParserAlgorithm;
import org.housingstudio.hsl.compiler.parser.ParserContext;
import org.housingstudio.hsl.compiler.error.Errno;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.housingstudio.hsl.compiler.codegen.impl.generic.EventType;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public class EventParser extends ParserAlgorithm<Event> {
    /**
     * Parse the next {@link Event} node from the token stream.
     *
     * @param parser  the AST node parser
     * @param context the token parser context
     * @return the next {@link Event} node from the token stream
     */
    @Override
    public @NotNull Event parse(@NotNull AstParser parser, @NotNull ParserContext context) {
        // skip the method declaration specifier
        get(TokenType.EXPRESSION, "event");

        Token typeToken = get(TokenType.IDENTIFIER);

        // skip the parameter list
        get(TokenType.LPAREN);
        get(TokenType.RPAREN);

        EventType type = Stream.of(EventType.values())
            .filter(v -> v.format().equals(typeToken.value()))
            .findFirst()
            .orElse(null);

        if (type == null) {
            context.errorPrinter().print(
                Notification.error(Errno.UNEXPECTED_EVENT_TYPE, "invalid event kind")
                    .error("unexpected event type", typeToken)
                    .note(
                        "read about event triggers at",
                        "https://docs.housing-studio.org/documentation/events"
                    )
            );
            throw new UnsupportedOperationException("Invalid event kind: " + typeToken.value());
        }

        Scope scope = parser.nextScope();

        if (!context.currentAnnotations().isEmpty()) {
            Token token = context.currentAnnotations().get(0).name();
            context.errorPrinter().print(
                Notification.error(Errno.UNEXPECTED_ANNOTATION_TARGET, "unexpected annotation target")
                    .error("cannot declare annotations for event actions", token)
                    .note("remove @" + token.value() + " from this event")
            );
            throw new UnsupportedOperationException("Cannot specify annotations for event handler");
        }

        return new Event(type, scope);
    }
}
