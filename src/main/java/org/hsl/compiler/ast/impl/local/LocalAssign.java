package org.hsl.compiler.ast.impl.local;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.compiler.ast.NodeInfo;
import org.hsl.compiler.ast.NodeType;
import org.hsl.compiler.ast.builder.ActionBuilder;
import org.hsl.compiler.ast.hierarchy.Parent;
import org.hsl.compiler.ast.impl.scope.Scope;
import org.hsl.compiler.ast.impl.scope.Statement;
import org.hsl.compiler.ast.impl.value.Value;
import org.hsl.compiler.debug.Format;
import org.hsl.compiler.debug.Printable;
import org.hsl.compiler.token.Token;
import org.hsl.export.action.Action;
import org.hsl.export.action.impl.ChangeVariable;
import org.hsl.std.type.Mode;
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
    public @NotNull Action build() {
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
