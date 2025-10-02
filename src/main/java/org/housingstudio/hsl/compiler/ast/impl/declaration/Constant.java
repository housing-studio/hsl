package org.housingstudio.hsl.compiler.ast.impl.declaration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.codegen.hierarchy.Children;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.debug.Format;
import org.housingstudio.hsl.compiler.debug.Printable;
import org.housingstudio.hsl.compiler.error.Notification;
import org.housingstudio.hsl.compiler.error.NamingConvention;
import org.housingstudio.hsl.compiler.error.Warning;
import org.housingstudio.hsl.compiler.token.Token;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.CONSTANT)
public class Constant extends Node implements Printable {
    private final @NotNull Token name;

    @Children
    private final @NotNull Value value;

    @Override
    public void init() {
        if (!NamingConvention.CONSTANTS.test(name.value())) {
            context.errorPrinter().print(
                Notification.warning(Warning.INVALID_NAMING_CONVENTION, "invalid naming convention", this)
                    .error("not preferred constant name", name)
                    .note("use `UPPER_CASE_WITH_UNDERSCORES` style to name constants")
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
        return Format.RED + "const " + Format.WHITE + name.value() + Format.YELLOW + " = " + Format.WHITE + value.print();
    }
}
