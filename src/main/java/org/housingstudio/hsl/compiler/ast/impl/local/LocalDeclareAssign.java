package org.housingstudio.hsl.compiler.ast.impl.local;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.builder.ActionBuilder;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.export.action.Action;
import org.housingstudio.hsl.export.action.impl.ChangeVariable;
import org.housingstudio.hsl.type.Mode;
import org.housingstudio.hsl.type.Namespace;
import org.housingstudio.hsl.compiler.ast.impl.type.Type;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.debug.Format;
import org.housingstudio.hsl.compiler.debug.Printable;
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
    public @NotNull Action buildAction() {
        return new ChangeVariable(namespace, name.value(), Mode.SET, value.asConstantValue(), false);
    }

    @Override
    public @NotNull String name() {
        return name.value();
    }
}
