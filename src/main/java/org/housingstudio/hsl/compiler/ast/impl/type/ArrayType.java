package org.housingstudio.hsl.compiler.ast.impl.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.token.Token;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public class ArrayType implements Type {
    private final @NotNull BaseType base = BaseType.ARRAY;

    private final @NotNull BaseType elementType;
    private final @NotNull Token elementTypeToken;

    private final int dimensions;
    private final @NotNull List<Token> dimensionTokens;

    /**
     * Indicate, whether the specified node matches the criteria of the matcher.
     *
     * @param other the node to compare to
     * @return {@code true} if the node matches the criteria, {@code false} otherwise
     */
    @Override
    public boolean matches(@NotNull Type other) {
        if (!(other instanceof ArrayType))
            return false;

        ArrayType otherType = (ArrayType) other;
        return dimensions == otherType.dimensions;
    }

    /**
     * Returns a string representation of the implementing class.
     *
     * @return the class debug information
     */
    @Override
    public @NotNull String print() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < dimensions; i++) {
            builder.append("[");
            builder.append("]");
        }
        return builder.append(elementType.name()).toString();
    }

    @Override
    public @NotNull List<Token> tokens() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(elementTypeToken);
        tokens.addAll(dimensionTokens);
        return tokens;
    }
}
