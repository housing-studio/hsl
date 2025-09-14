package org.housingstudio.hsl.compiler.error;

import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

@UtilityClass
public class NamingConvention {
    public final Convention FUNCTIONS = Convention.of("^[a-z][a-zA-Z0-9]*$");
    public final Convention LOCALS    = Convention.of("^[a-z][a-zA-Z0-9]*$");
    public final Convention CONSTANTS = Convention.of("^[A-Z][A-Z0-9]*(?:_[A-Z0-9]+)*$");

    @RequiredArgsConstructor
    public static class Convention {
        private final @NotNull Pattern pattern;

        public boolean test(@NotNull String value) {
            return pattern.matcher(value).matches();
        }

        public static @NotNull Convention of(@NotNull String pattern) {
            return new Convention(Pattern.compile(pattern));
        }
    }
}
