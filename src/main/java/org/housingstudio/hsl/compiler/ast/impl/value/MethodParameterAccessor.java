package org.housingstudio.hsl.compiler.ast.impl.value;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Method;
import org.housingstudio.hsl.compiler.ast.impl.local.Variable;
import org.housingstudio.hsl.compiler.ast.impl.type.Type;
import org.housingstudio.hsl.std.Namespace;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.METHOD_CALL)
public class MethodParameterAccessor extends Value implements Variable {
    private final @NotNull String name;
    private final @NotNull Type type;
    private final @NotNull Method method;

    @Override
    public @NotNull Namespace namespace() {
        return Namespace.PLAYER;
    }

    public @NotNull String name() {
        return encodeName(method, name);
    }

    /**
     * Retrieve the type of the held value. This result will be used to inter types for untyped variables.
     *
     * @return the resolved value of the type
     */
    @Override
    public @NotNull Type getValueType() {
        return type;
    }

    /**
     * Get the constant string representation of the value.
     * <p>
     * Housing variables are handled as string by default, this format is the input for housing variables.
     *
     * @return the final string value
     */
    @Override
    public @NotNull String asConstantValue() {
        return "%%stat_" + encodeName(method, name) + "%%";
    }

    /**
     * Returns a string representation of the implementing class.
     *
     * @return the class debug information
     */
    @Override
    public @NotNull String print() {
        return "param:" + name;
    }

    public static @NotNull String encodeName(@NotNull Method targetMethod, @NotNull String paramName) {
        return String.format("p:%s", paramName);
    }
}
