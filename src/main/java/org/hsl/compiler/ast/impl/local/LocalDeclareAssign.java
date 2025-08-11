package org.hsl.compiler.ast.impl.local;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.compiler.ast.NodeInfo;
import org.hsl.compiler.ast.NodeType;
import org.hsl.compiler.ast.builder.ActionBuilder;
import org.hsl.compiler.token.Token;
import org.hsl.export.action.Action;
import org.hsl.export.action.impl.ChangeVariable;
import org.hsl.std.type.Mode;
import org.hsl.std.type.Namespace;
import org.hsl.compiler.ast.impl.type.Type;
import org.hsl.compiler.ast.impl.value.Value;
import org.hsl.compiler.debug.Format;
import org.hsl.compiler.debug.Printable;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.LOCAL_DECLARE_ASSIGN)
public class LocalDeclareAssign extends Variable implements Printable, ActionBuilder {
    private final @NotNull Namespace namespace;
    private final @NotNull Token name;
    private final @NotNull Type type;
    private final @NotNull Value value;

    /**
     * Returns a string representation of the implementing class.
     *
     * @return the class debug information
     */
    @Override
    public @NotNull String print() {
        return Format.RED + "stat " + Format.LIGHT_YELLOW + namespace.name().toLowerCase() + " " +
            Format.WHITE + name.value() + Format.YELLOW + ": " + Format.RED + type.name().toLowerCase() +
            Format.YELLOW + " = " + Format.WHITE + value.print();
    }

    /**
     * Generate an {@link Action} based on the underlying node.
     *
     * @return the built action representing this node
     */
    @Override
    public @NotNull Action build() {
        return new ChangeVariable(namespace, name.value(), Mode.SET, value.asConstantValue(), false);
    }

    @Override
    public @NotNull String name() {
        return name.value();
    }
}
