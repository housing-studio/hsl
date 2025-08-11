package org.hsl.compiler.ast.impl.local;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.compiler.ast.NodeInfo;
import org.hsl.compiler.ast.NodeType;
import org.hsl.compiler.token.Token;
import org.hsl.std.type.Namespace;
import org.hsl.compiler.ast.impl.type.Type;
import org.hsl.compiler.debug.Format;
import org.hsl.compiler.debug.Printable;
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
            context.syntaxError(name, "Cannot redeclare variable ");
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
