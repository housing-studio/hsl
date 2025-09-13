package org.housingstudio.hsl.compiler.parser.impl.declaration;

import org.housingstudio.hsl.compiler.ast.impl.declaration.Macro;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Parameter;
import org.housingstudio.hsl.compiler.ast.impl.scope.Scope;
import org.housingstudio.hsl.compiler.ast.impl.type.BaseType;
import org.housingstudio.hsl.compiler.parser.AstParser;
import org.housingstudio.hsl.compiler.parser.ParserAlgorithm;
import org.housingstudio.hsl.compiler.parser.ParserContext;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents the parser algorithm for parsing a {@link Macro} node from the token stream.
 * <p>
 * A macro is a special function that runs during compile time and produces actions.
 * <p>
 * For example:
 * <pre>
 *     macro add(a: int, b: int) -> int {
 *         return a + b
 *     }
 * </pre>
 * The macro `add` is defined the statement {@code return a + b}.
 * <p>
 * The macro is defined by the return type, the name of the macro, the parameter list, and the body of the macro.
 * <p>
 * The macro can have multiple return types, that are placed in between parenthesis.
 *
 * @see Macro
 */
public class MacroParser extends ParserAlgorithm<Macro> {
    /**
     * Parse the next {@link Macro} node from the token stream.
     *
     * @param parser  the AST node parser
     * @param context the token parser context
     *
     * @return the next {@link Macro} node from the token stream
     */
    @Override
    public @NotNull Macro parse(@NotNull AstParser parser, @NotNull ParserContext context) {
        // skip the method declaration specifier
        get(TokenType.EXPRESSION, "macro");

        // parse the method name
        Token name = context.get(TokenType.IDENTIFIER);

        // parse the parameter list
        List<Parameter> parameters = parser.nextParameterList();

        BaseType returnType = BaseType.VOID;
        if (peek().is(TokenType.OPERATOR, "-") && at(cursor() + 1).is(TokenType.OPERATOR, ">")) {
            get();
            get();
            returnType = parser.nextType();
        }

        Scope scope = parser.nextScope();

        return new Macro(name, returnType, parameters, scope);
    }
}
