package org.housingstudio.hsl.compiler.ast.impl.local;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.token.Errno;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.type.Namespace;
import org.housingstudio.hsl.compiler.ast.impl.type.Type;
import org.housingstudio.hsl.compiler.debug.Format;
import org.housingstudio.hsl.compiler.debug.Printable;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.LOCAL_DECLARE)
public class LocalDeclare extends Variable implements Printable {
    private final @NotNull Namespace namespace;
    private final @NotNull Token name;
    private final @NotNull Type type;

    /**
     * Initialize node logic before the nodes are visited and the code is generated.
     */
    @Override
    public void init() {
        Variable variable = resolveName(name.value());
        if (variable != null && variable != this) {
            context.error(
                Errno.CANNOT_REDECLARE_VARIABLE,
                "Cannot redeclare variable",
                name,
                "Variable name is already declared in this scope"
            );
            throw new UnsupportedOperationException("Cannot redeclare variable: " + name.value());
        }
    }

    /**
     * Returns a string representation of the implementing class.
     *
     * @return the class debug information
     */
    @Override
    public @NotNull String print() {
        return Format.RED + "stat " +
            Format.LIGHT_YELLOW + namespace.name().toLowerCase() + " " +
            Format.WHITE + name.value() + Format.YELLOW + ": " + Format.RED + type.name().toLowerCase();
    }

    @Override
    public @NotNull String name() {
        return name.value();
    }
}
