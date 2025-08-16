package org.housingstudio.hsl.compiler.parser;

import lombok.experimental.UtilityClass;
import org.housingstudio.hsl.compiler.parser.impl.action.MethodCallParser;
import org.housingstudio.hsl.compiler.parser.impl.annotation.AnnotationParser;
import org.housingstudio.hsl.compiler.parser.impl.conditional.ConditionParser;
import org.housingstudio.hsl.compiler.parser.impl.conditional.ConditionalParser;
import org.housingstudio.hsl.compiler.parser.impl.declaration.CommandParser;
import org.housingstudio.hsl.compiler.parser.impl.declaration.ConstantParser;
import org.housingstudio.hsl.compiler.parser.impl.declaration.EventParser;
import org.housingstudio.hsl.compiler.parser.impl.declaration.MethodParser;
import org.housingstudio.hsl.compiler.parser.impl.local.LocalAssignParser;
import org.housingstudio.hsl.compiler.parser.impl.local.LocalDeclareParser;
import org.housingstudio.hsl.compiler.parser.impl.operator.OperatorParser;
import org.housingstudio.hsl.compiler.parser.impl.scope.ScopeParser;
import org.housingstudio.hsl.compiler.parser.impl.scope.StatementParser;
import org.housingstudio.hsl.compiler.parser.impl.value.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a utility class that registers and looks up parser algorithm implementations.
 */
@UtilityClass
public class ParserRegistry {
    /**
     * The lookup table that maps the parse targets to the parser algorithms.
     */
    private final @NotNull Map<@NotNull Class<?>, @NotNull ParserAlgorithm<?>> PARSERS = new HashMap<>();

    // initialize the lookup table with the parser algorithms
    static {
        Class<?>[] parsers = {
            MethodParser.class,
            CommandParser.class,
            EventParser.class,
            ScopeParser.class,
            StatementParser.class,
            LiteralParser.class,
            ValueParser.class,
            LocalDeclareParser.class,
            LocalAssignParser.class,
            ConstantParser.class,
            BuiltinValueParser.class,
            ConstantAccessParser.class,
            TypeParser.class,
            ArgumentListParser.class,
            OperatorParser.class,
            AnnotationParser.class,
            MethodCallParser.class,
            ConditionParser.class,
            ConditionalParser.class,
        };

        for (Class<?> parser : parsers) {
            // try to instantiate the parser algorithm
            ParserAlgorithm<?> value;
            try {
                value = (ParserAlgorithm<?>) parser.getDeclaredConstructors()[0].newInstance();
            } catch (Exception e) {
                throw new ParserException("Failed to instantiate parser: " + parser, e);
            }
            // register the parser algorithm in the lookup table
            PARSERS.put(parser, value);
        }
    }

    /**
     * Retrieve the parser algorithm for the specified parse target.
     *
     * @param target the target of the parser algorithm
     * @return the parser algorithm for the specified target
     *
     * @throws ParserException if no parser algorithm is found for the target
     */
    public static @NotNull ParserAlgorithm<?> of(@NotNull Class<? extends ParserAlgorithm<?>> target) {
        ParserAlgorithm<?> parser = PARSERS.get(target);
        if (parser == null)
            throw new ParserException("No parser found for target: " + target);
        return parser;
    }
}
