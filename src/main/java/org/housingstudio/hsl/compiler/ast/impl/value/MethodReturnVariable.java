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

/**
 * Represents the reserved variable for storing a method's return value.
 * <p>
 * This variable is used internally to store return values from method calls. The variable name follows the pattern
 * {@code r:methodName}.
 */
@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.METHOD_CALL)
public class MethodReturnVariable implements Variable {
    private final @NotNull Method method;

    @Override
    public @NotNull Namespace namespace() {
        return Namespace.PLAYER;
    }

    @Override
    public @NotNull String name() {
        return encodeName(method);
    }

    @Override
    public @NotNull Type type() {
        return method.returnType();
    }

    public static @NotNull String encodeName(@NotNull Method targetMethod) {
        return String.format("r:%s", targetMethod.name().value());
    }
}
