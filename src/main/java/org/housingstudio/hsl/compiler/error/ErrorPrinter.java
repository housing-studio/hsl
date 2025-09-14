package org.housingstudio.hsl.compiler.error;

import lombok.RequiredArgsConstructor;
import org.housingstudio.hsl.compiler.debug.Format;
import org.housingstudio.hsl.compiler.parser.ParserContext;
import org.housingstudio.hsl.compiler.token.Errno;
import org.housingstudio.hsl.compiler.token.Meta;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.Tokenizer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class ErrorPrinter {
    private final @NotNull ParserContext context;

    public void print(@NotNull ErrorContainer container) {
        List<TokenError> errors = container.errors();
        if (errors.isEmpty())
            throw new IllegalStateException("You must specify at least one token error");

        Errno code = container.code();
        String title = container.title();

        TokenError first = errors.get(0);

        System.err.println(
            Format.RED + "error[E" + code.code() + "]" + Format.WHITE + ": " + title
        );
        Meta firstMeta = first.tokens().get(0).meta();
        System.err.println(
            Format.CYAN + " --> " + Format.LIGHT_GRAY + context.file().getName() + ":" + firstMeta.lineNumber() +
            ":" + firstMeta.lineIndex()
        );

        int longestSize = errors.stream()
            .map(TokenError::tokens)
            .reduce(new ArrayList<>(), (total, tokens) -> {
                total.addAll(tokens);
                return total;
            })
            .stream()
            .map(Token::meta)
            .map(Meta::lineIndex)
            .map(String::valueOf)
            .map(String::length)
            .max(Integer::compare)
            .orElse(0);

        for (TokenError error : errors) {
            Meta meta = error.tokens().get(0).meta();

            int lineSize = String.valueOf(meta.lineNumber()).length();
            int padding = lineSize < longestSize ? longestSize - (longestSize - lineSize) : lineSize;

            // display the line number
            System.err.print(Format.CYAN + repeat(" ", padding + 1));
            System.err.println(" | ");

            System.err.print(" " + meta.lineNumber() + " | ");

            // get the line of the error
            String line = context.data().split("\n")[meta.lineNumber() - 1];

            // get the start and end index of the line
            int start = Math.max(0, meta.lineIndex() - Tokenizer.MAX_ERROR_LINE_LENGTH);
            int end = Math.min(line.length(), firstMeta.lineIndex() + Tokenizer.MAX_ERROR_LINE_LENGTH);

            // display the line of the error
            System.err.println(Format.LIGHT_GRAY + line.substring(start, end));

            // display the error pointer
            System.err.print(Format.CYAN + repeat(" ", lineSize + 1));
            String pointerPad = repeat(" ", lineSize + (meta.lineIndex() - start) - 1);
            System.err.println(" | " + pointerPad + Format.RED + repeat("^", meta.endIndex() - meta.beginIndex()));

            // display the expected tokens below the pointer
            System.err.print(Format.CYAN + repeat(" ", lineSize + 1));
            System.err.println(" | " + pointerPad + Format.RED + error.message());
        }

        // display a final separator
        System.err.print(Format.CYAN + repeat(" ", longestSize + 1));
        System.err.println(" | ");

        for (String note : container.notes()) {
            System.err.print(Format.GREEN + "note: ");
            System.err.println(Format.LIGHT_GRAY + note);
        }

        System.err.print(Format.DEFAULT);

        throw new IllegalStateException("AST parse error");
    }

    private @NotNull String repeat(@NotNull String value, int count) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++)
            builder.append(value);
        return builder.toString();
    }
}
