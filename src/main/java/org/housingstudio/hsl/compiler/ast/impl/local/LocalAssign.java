package org.housingstudio.hsl.compiler.ast.impl.local;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.builder.ActionBuilder;
import org.housingstudio.hsl.compiler.ast.impl.scope.Statement;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.debug.Format;
import org.housingstudio.hsl.compiler.debug.Printable;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.exporter.action.Action;
import org.housingstudio.hsl.exporter.action.impl.ChangeVariable;
import org.housingstudio.hsl.type.Mode;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.LOCAL_ASSIGN)
public class LocalAssign extends Statement implements Printable, ActionBuilder {
    private final @NotNull Token name;
    private final @NotNull Token operator;
    private final @NotNull Value value;

    /**
     * Generate an {@link Action} based on the underlying node.
     *
     * @return the built action representing this node
     */
    @Override
    public @NotNull Action buildAction() {
        Variable variable = resolveName(name.value());
        if (variable == null) {
            context.syntaxError(name, "Cannot find variable");
            throw new UnsupportedOperationException("Cannot find variable: " + name.value());
        }

        return new ChangeVariable(
            variable.namespace(), name.value(), Mode.SET, value.asConstantValue(), false
        );
    }

    /**
     * Returns a string representation of the implementing class.
     *
     * @return the class debug information
     */
    @Override
    public @NotNull String print() {
        return Format.WHITE + name.value() + Format.YELLOW + " = " + Format.WHITE + value.print();
    }
}
