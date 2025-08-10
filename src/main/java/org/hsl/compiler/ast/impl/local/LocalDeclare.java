package org.hsl.compiler.ast.impl.local;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.compiler.ast.NodeInfo;
import org.hsl.compiler.ast.NodeType;
import org.hsl.compiler.ast.impl.type.Namespace;
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
    private final @NotNull String name;
    private final @NotNull Type type;

    /**
     * Returns a string representation of the implementing class.
     *
     * @return the class debug information
     */
    @Override
    public @NotNull String print() {
        return Format.RED + "stat " +
            Format.LIGHT_YELLOW + namespace.name().toLowerCase() + " " +
            Format.WHITE + name + Format.YELLOW + ": " + Format.RED + type.name().toLowerCase();
    }
}
