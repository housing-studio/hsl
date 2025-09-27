package org.housingstudio.hsl.compiler.parser.impl.operator;

import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.impl.operator.AssignmentOperator;
import org.housingstudio.hsl.compiler.ast.impl.operator.Operator;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.parser.AstParser;
import org.housingstudio.hsl.compiler.parser.ParserAlgorithm;
import org.housingstudio.hsl.compiler.parser.ParserContext;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

public class AssignmentOperatorParser extends ParserAlgorithm<Node> {
    /**
     * Parse the next {@link Node} node from the token stream.
     *
     * @param parser  the AST node parser
     * @param context the token parser context
     * @return the next {@link Node} node from the token stream
     */
    @Override
    public @NotNull Node parse(@NotNull AstParser parser, @NotNull ParserContext context) {
        Token identifier = get(TokenType.IDENTIFIER);

        Token token = peek(TokenType.OPERATOR);
        Operator operator = parser.nextOperator();
        Value value = parser.nextValue();

        return new AssignmentOperator(identifier, operator, value);
    }
}
