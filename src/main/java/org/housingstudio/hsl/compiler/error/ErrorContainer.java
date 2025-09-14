package org.housingstudio.hsl.compiler.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.debug.Format;
import org.housingstudio.hsl.compiler.token.Token;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public class ErrorContainer {
    private final List<TokenError> errors = new ArrayList<>();
    private final List<String> notes = new ArrayList<>();

    private final int code;
    private final @NotNull String title;
    private final @NotNull ErrorType type;

    public @NotNull ErrorContainer error(@NotNull String message, @NotNull Token @NotNull ... tokens) {
        errors.add(new TokenError(Arrays.asList(tokens), message));
        return this;
    }

    public @NotNull ErrorContainer error(@NotNull String message, @NotNull List<Token> tokens) {
        errors.add(new TokenError(tokens, message));
        return this;
    }

    public @NotNull ErrorContainer note(@NotNull String message) {
        notes.add(message);
        return this;
    }

    public @NotNull ErrorContainer note(@NotNull String message, @NotNull String example) {
        notes.add(message + ": " + Format.LIGHT_BLUE + example);
        return this;
    }

    public static @NotNull ErrorContainer error(@NotNull Errno error, @NotNull String title) {
        return new ErrorContainer(error.code(), title, ErrorType.ERROR);
    }

    public static @NotNull ErrorContainer warning(@NotNull Warning warning, @NotNull String title) {
        return new ErrorContainer(warning.code(), title, ErrorType.WARNING);
    }
}
