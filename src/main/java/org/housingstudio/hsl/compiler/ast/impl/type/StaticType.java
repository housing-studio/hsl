package org.housingstudio.hsl.compiler.ast.impl.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.ast.impl.value.builtin.NullValue;
import org.housingstudio.hsl.compiler.token.Token;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public class StaticType implements Type {
    private final @NotNull BaseType base;
    private final @Nullable Token baseToken;
    private final @Nullable Value defaultValue;

    public StaticType(@NotNull BaseType base) {
        this.base = base;
        baseToken = null;
        defaultValue = new NullValue();
    }

    public StaticType(@NotNull BaseType base, @NotNull Value defaultValue) {
        this.base = base;
        baseToken = null;
        this.defaultValue = defaultValue;
    }

    /**
     * Indicate, whether the specified node matches the criteria of the matcher.
     *
     * @param other the node to compare to
     * @return {@code true} if the node matches the criteria, {@code false} otherwise
     */
    @Override
    public boolean matches(@NotNull Type other) {
        return base.equals(other.base());
    }

    /**
     * Returns a string representation of the implementing class.
     *
     * @return the class debug information
     */
    @Override
    public @NotNull String print() {
        return base.format();
    }

    @Override
    public @NotNull List<Token> tokens() {
        return baseToken != null ? Collections.singletonList(baseToken) : Collections.emptyList();
    }

    @Override
    public @NotNull Value defaultValue() {
        assert defaultValue != null : "missing type default value";
        return defaultValue;
    }
}
