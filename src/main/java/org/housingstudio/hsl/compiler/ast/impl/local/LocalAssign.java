package org.housingstudio.hsl.compiler.ast.impl.local;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.impl.type.Types;
import org.housingstudio.hsl.compiler.codegen.builder.ActionBuilder;
import org.housingstudio.hsl.compiler.ast.impl.scope.Statement;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.codegen.hierarchy.Children;
import org.housingstudio.hsl.compiler.debug.Format;
import org.housingstudio.hsl.compiler.debug.Printable;
import org.housingstudio.hsl.compiler.error.Notification;
import org.housingstudio.hsl.compiler.error.Errno;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.codegen.impl.action.Action;
import org.housingstudio.hsl.compiler.codegen.impl.action.impl.ChangeVariable;
import org.housingstudio.hsl.std.Mode;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.LOCAL_ASSIGN)
public class LocalAssign extends Statement implements Printable, ActionBuilder {
    private final @NotNull Token name;
    private final @NotNull Token operator;

    @Children
    private final @NotNull Value value;

    private Variable variable;

    /**
     * Initialize node logic before the nodes are visited and the code is generated.
     */
    @Override
    public void init() {
        variable = resolveName(name.value());
        if (variable == null) {
            context.errorPrinter().print(
                Notification.error(Errno.UNKNOWN_VARIABLE, "unknown variable: " + name.value(), this)
                    .error("cannot find variable in this scope", name)
                    .note("did you misspell the name, or forgot to declare the variable?")
            );
            throw new UnsupportedOperationException("Cannot find variable: " + name.value());
        }

        if (!variable.type().matches(Types.ANY) && !variable.type().matches(value.getValueType())) {
            context.errorPrinter().print(
                Notification.error(Errno.UNEXPECTED_TYPE, "invalid assignment type", this)
                    .error(
                        "cannot assign " + value.getValueType().print() + " value to " +
                            variable.type().print() + " stat",
                        name
                    )
                    .note("make sure the stat's type matches the assigned value's type")
            );

            throw new IllegalStateException("Explicit type does not match inferred type");
        }

        if (!value.isConstant()) {
            context.errorPrinter().print(
                Notification.error(Errno.EXPECTED_CONSTANT_VALUE, "cannot assign to non-constant value", this)
                    .error(
                        "cannot assign to this stat, because the assigned value may not be known at compile-time",
                        name // TODO use value.tokens()
                    )
                    .note("consider assigning a constant value", "foo = 1234")
            );
            throw new UnsupportedOperationException("Cannot assign to non-constant value: " + name.value());
        }
    }

    /**
     * Generate an {@link Action} based on the underlying node.
     *
     * @return the built action representing this node
     */
    @Override
    public @NotNull Action buildAction() {
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
