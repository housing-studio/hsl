package org.housingstudio.hsl.compiler.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.debug.Format;
import org.housingstudio.hsl.compiler.token.Token;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public class Notification {
    private final List<TokenError> errors = new ArrayList<>();
    private final List<String> notes = new ArrayList<>();

    private final int code;
    private final int id;
    private final @NotNull String title;
    private final @NotNull ErrorType type;

    public @NotNull Notification error(@NotNull String message, @NotNull Token @NotNull ... tokens) {
        errors.add(new TokenError(Arrays.asList(tokens), message));
        return this;
    }

    public @NotNull Notification error(@NotNull String message, @NotNull List<Token> tokens) {
        errors.add(new TokenError(tokens, message));
        return this;
    }

    public @NotNull Notification note(@NotNull String message) {
        notes.add(message);
        return this;
    }

    public @NotNull Notification note(@NotNull String message, @NotNull String example) {
        notes.add(message + ": " + Format.LIGHT_BLUE + example);
        return this;
    }

    public static @NotNull Notification error(@NotNull Errno error, @NotNull String title, @NotNull Node node) {
        return new Notification(error.code(), node.id(), title, ErrorType.ERROR);
    }

    public static @NotNull Notification error(@NotNull Errno error, @NotNull String title) {
        return new Notification(error.code(), -1, title, ErrorType.ERROR);
    }

    public static @NotNull Notification warning(@NotNull Warning warning, @NotNull String title, @NotNull Node node) {
        return new Notification(warning.code(), node.id(), title, ErrorType.WARNING);
    }
}
