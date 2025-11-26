package org.housingstudio.hsl.compiler.ast.impl.local;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.impl.type.Type;
import org.housingstudio.hsl.compiler.error.Errno;
import org.housingstudio.hsl.compiler.error.Notification;
import org.housingstudio.hsl.compiler.error.NamingConvention;
import org.housingstudio.hsl.compiler.error.Warning;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.std.Namespace;
import org.housingstudio.hsl.compiler.debug.Format;
import org.housingstudio.hsl.compiler.debug.Printable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.LOCAL_DECLARE)
public class LocalDeclare extends Node implements Variable, Printable {
    private final @NotNull Namespace namespace;
    private final @NotNull Token name;
    private final @Nullable Token alias;
    private final @Nullable Token team;
    private final @NotNull Type type;

    /**
     * Initialize node logic before the nodes are visited and the code is generated.
     */
    @Override
    public void init() {
        validateName();
        validateConvention();
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
        return Format.RED + "stat " +
            Format.LIGHT_YELLOW + namespace.name().toLowerCase() + " " +
            Format.WHITE + name.value() + Format.YELLOW + ": " + Format.RED + type.print();
    }

    @Override
    public @NotNull String name() {
        return name.value();
    }
}
