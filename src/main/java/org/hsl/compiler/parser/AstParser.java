package org.hsl.compiler.parser;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
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
