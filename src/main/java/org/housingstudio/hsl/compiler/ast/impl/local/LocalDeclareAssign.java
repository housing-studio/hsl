package org.housingstudio.hsl.compiler.ast.impl.local;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.builder.ActionBuilder;
import org.housingstudio.hsl.compiler.ast.hierarchy.Children;
import org.housingstudio.hsl.compiler.token.Errno;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.exporter.action.Action;
import org.housingstudio.hsl.exporter.action.impl.ChangeVariable;
import org.housingstudio.hsl.std.Mode;
import org.housingstudio.hsl.std.Namespace;
import org.housingstudio.hsl.compiler.ast.impl.type.Type;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.debug.Format;
import org.housingstudio.hsl.compiler.debug.Printable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.LOCAL_DECLARE_ASSIGN)
public class LocalDeclareAssign extends Variable implements Printable, ActionBuilder {
    private final @NotNull Namespace namespace;
    private final @NotNull Token name;
    private final @Nullable Type explicitType;
    private final @Nullable Token typeToken;

    @Children
    private final @NotNull Value value;

    private Type type;

    @Override
    public void init() {
        Type valueType = value.load().getValueType();

        // check if an explicit type is specified and it does not match the inferred type
        if (explicitType != null && valueType != explicitType) {
            assert typeToken != null;
            context.error(
                Errno.INFER_TYPE_MISMATCH,
                "infer type mismatch",
                typeToken,
                "the explicit type does not match the inferred type"
            );
            throw new IllegalStateException("Explicit type does not match inferred type");
        }

        // this will be important later, when type hierarchy will exist
        // for example TInfer inherits from TExplicit
        // always prioritize explicit type, if specified
        if (explicitType != null)
            type = explicitType;
        // infer type from value if no explicit type is specified
        else
            type = valueType;
    }

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
