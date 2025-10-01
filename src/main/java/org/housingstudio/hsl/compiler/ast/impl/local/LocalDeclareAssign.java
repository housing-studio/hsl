package org.housingstudio.hsl.compiler.ast.impl.local;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.impl.type.Type;
import org.housingstudio.hsl.compiler.codegen.builder.ActionBuilder;
import org.housingstudio.hsl.compiler.codegen.hierarchy.Children;
import org.housingstudio.hsl.compiler.error.NamingConvention;
import org.housingstudio.hsl.compiler.error.Notification;
import org.housingstudio.hsl.compiler.error.Errno;
import org.housingstudio.hsl.compiler.error.Warning;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.codegen.impl.action.Action;
import org.housingstudio.hsl.compiler.codegen.impl.action.impl.ChangeVariable;
import org.housingstudio.hsl.std.Mode;
import org.housingstudio.hsl.std.Namespace;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.debug.Format;
import org.housingstudio.hsl.compiler.debug.Printable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.LOCAL_DECLARE_ASSIGN)
public class LocalDeclareAssign extends Node implements Variable, Printable, ActionBuilder {
    private final @NotNull Namespace namespace;
    private final @NotNull Token name;
    private final @Nullable Type explicitType;

    @Children
    private final @NotNull Value value;

    private Type type;

    private void inferType() {
        Type valueType = value.load().getValueType();

        // check if an explicit type is specified and it does not match the inferred type
        if (explicitType != null && !valueType.matches(explicitType)) {
            List<Token> tokens = explicitType.tokens();
            context.errorPrinter().print(
                Notification.error(Errno.INFER_TYPE_MISMATCH, "infer type mismatch", this)
                    .error("the explicit type does not match the inferred type", tokens.get(tokens.size() - 1))
                    .note("make sure the stat's type matches the assigned value's type")
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

    @Override
    public void init() {
        validateName();
        validateConvention();
        inferType();

        if (type == null) {
            context.errorPrinter().print(
                Notification.error(Errno.CANNOT_INFER_TYPE, "cannot infer type", this)
                    .error("cannot infer type", name)
                    .note("did you recursively assign a stat?")
            );
            throw new IllegalStateException("Cannot infer type for stat: " + name.value());
        }
    }

    private void validateName() {
        Variable variable = resolveName(name.value());
        if (variable != null && variable != this) {
            context.errorPrinter().print(
                Notification.error(Errno.CANNOT_REDECLARE_VARIABLE, "cannot redeclare variable", this)
                    .error("variable name is already declared in this scope", name)
                    .note("you may choose a different variable name")
            );
            throw new UnsupportedOperationException("Cannot redeclare variable: " + name.value());
        }
    }

    private void validateConvention() {
        if (!NamingConvention.LOCALS.test(name.value())) {
            context.errorPrinter().print(
                Notification.warning(Warning.INVALID_NAMING_CONVENTION, "invalid naming convention", this)
                    .error("not preferred stat name", name)
                    .note("use `lowerCamelCase` style to name stats")
            );
        }
    }

    /**
     * Returns a string representation of the implementing class.
     *
     * @return the class debug information
     */
    @Override
    public @NotNull String print() {
        return Format.RED + "stat " + Format.LIGHT_YELLOW + namespace.name().toLowerCase() + " " +
            Format.WHITE + name.value() + Format.YELLOW + ": " + Format.RED + type.print() +
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
