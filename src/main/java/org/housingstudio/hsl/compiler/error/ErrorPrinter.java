package org.housingstudio.hsl.compiler.error;

import lombok.RequiredArgsConstructor;
import org.housingstudio.hsl.compiler.TokenContext;
import org.housingstudio.hsl.compiler.debug.Format;
import org.housingstudio.hsl.compiler.token.Meta;
import org.housingstudio.hsl.compiler.token.Tokenizer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class ErrorPrinter {
    private final Map<Integer, Integer> warnings = new HashMap<>();
    private final Map<Integer, Integer> errors = new HashMap<>();

    private final @NotNull TokenContext context;

    private int computePadding(@NotNull List<TokenError> errors) {
        return errors.stream()
            .map(TokenError::tokens)
            .reduce(new ArrayList<>(), (total, tokens) -> {
                total.addAll(tokens);
                return total;
            })
            .stream()
            .map(token -> token.meta().lineNumber())
            .map(String::valueOf)
            .map(String::length)
            .max(Integer::compare)
            .orElse(0);
    }

    public void print(@NotNull Notification container) {
        if (isDuplicate(container))
            return;

        container.file(context.file().getAbsolutePath());

        if (context.mode() == ErrorMode.PRETTY_PRINT)
            printPretty(container);
        else
            printJson(container);
    }

    private void printJson(@NotNull Notification container) {
        context.diagnostics().submit(container);
    }

    private void printPretty(@NotNull Notification container) {
        List<TokenError> errors = container.errors();
        if (errors.isEmpty())
            throw new IllegalStateException("You must specify at least one token error");

        Format color = container.type() == ErrorType.ERROR ? Format.RED : Format.YELLOW;
        String prefix = container.type() == ErrorType.ERROR ? "error" : "warning";

        System.err.println(
            color + prefix + "[E" + container.code() + "]" + Format.WHITE + ": " + container.title()
        );

        Meta firstMeta = errors.get(0).tokens().get(0).meta();
        System.err.println(
            Format.CYAN + " --> " + Format.LIGHT_GRAY + context.file().getName() + ":" + firstMeta.lineNumber() +
            ":" + firstMeta.lineIndex()
        );

        int longestSize = computePadding(errors);

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
            System.err.println(" | " + pointerPad + color + repeat("^", meta.endIndex() - meta.beginIndex()));

            // display the expected tokens below the pointer
            System.err.print(Format.CYAN + repeat(" ", lineSize + 1));
            System.err.println(" | " + pointerPad + color + error.message());
        }

        // display a final separator
        System.err.print(Format.CYAN + repeat(" ", longestSize + 1));
        System.err.println(" | ");

        printNotes(container);

        System.err.print(Format.DEFAULT);

        if (container.type() == ErrorType.ERROR)
            throw new IllegalStateException("AST parse error");
    }

    private boolean isDuplicate(@NotNull Notification container) {
        // cannot check for untagged errors, that were not emitted by a node
        if (container.id() == -1)
            return false;

        switch (container.type()) {
            case WARNING: {
                Integer code = warnings.get(container.id());
                if (code != null && code == container.code())
                    return true;

                warnings.put(container.id(), container.code());
                break;
            }
            case ERROR: {
                Integer code = errors.get(container.id());
                if (code != null && code == container.code())
                    return true;

                errors.put(container.id(), container.code());
                break;
            }
        }

        return false;
    }

    private void printNotes(@NotNull Notification container) {
        for (String note : container.notes()) {
            System.err.print(Format.GREEN + "note: ");
            System.err.println(Format.LIGHT_GRAY + note);
        }
    }

    private @NotNull String repeat(@NotNull String value, int count) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++)
            builder.append(value);
        return builder.toString();
    }
}
