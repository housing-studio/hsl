package org.housingstudio.hsl.compiler.parser.impl.annotation;

import org.housingstudio.hsl.compiler.ast.impl.annotation.*;
import org.housingstudio.hsl.compiler.ast.impl.value.Annotation;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.parser.AstParser;
import org.housingstudio.hsl.compiler.parser.ParserAlgorithm;
import org.housingstudio.hsl.compiler.parser.ParserContext;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

public class AnnotationParser extends ParserAlgorithm<Annotation> {
    /**
     * Parse the next {@link Annotation} node from the token stream.
     *
     * @param parser the AST node parser
     * @param context the token parser context
     * @return the next {@link Annotation} node from the token stream
     */
    @Override
    public @NotNull Annotation parse(@NotNull AstParser parser, @NotNull ParserContext context) {
        Token name = get(TokenType.ANNOTATION);

        get(TokenType.LPAREN);
        Value value = parser.nextValue();
        get(TokenType.RPAREN);

        // skip the semicolon after the annotation
        // @foo("hello");
        //              ^ the (auto-inserted) semicolon does not mean anything here, we just have to remove it
        //                because it will be an unexpected token in the next parsing step
        if (peek().is(TokenType.SEMICOLON))
            get();

        return switch (name.value()) {
            case "description" -> new DescriptionAnnotation(name, value);
            case "loop" -> new LoopAnnotation(name, value);
            case "icon" -> new IconAnnotation(name, value);
            case "executor" -> new ExecutorAnnotation(name, value);
            case "priority" -> new PriorityAnnotation(name, value);
            case "listed" -> new ListedAnnotation(name, value);
            default -> new Annotation(name, value);
        };
    }
}
