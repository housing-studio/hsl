package org.hsl.compiler.ast.impl.declaration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.compiler.ast.Node;
import org.hsl.compiler.ast.NodeInfo;
import org.hsl.compiler.ast.NodeType;
import org.hsl.compiler.ast.impl.scope.Scope;
import org.hsl.compiler.ast.impl.type.Type;
import org.hsl.compiler.debug.Format;
import org.hsl.compiler.debug.Printable;
import org.hsl.compiler.token.Token;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.METHOD)
public class Method extends Node implements Printable {
    private final @NotNull Token name;
    private final @NotNull Type returnType;
    private final List<Parameter> parameters;
    private final @NotNull Scope scope;

    /**
     * Returns a string representation of the implementing class.
     *
     * @return the class debug information
     */
    @Override
    public @NotNull String print() {
        StringBuilder builder = new StringBuilder(Format.RED + "fn " + Format.BLUE + name.value());
        builder.append(Format.CYAN).append("()");

        if (returnType != Type.VOID)
            builder.append(" -> ").append(Format.RED).append(returnType.name().toLowerCase());

        builder.append(Format.LIGHT_GRAY).append(" {").append('\n');

        for (Node statement : scope.statements()) {
            if (statement instanceof Printable printable)
                builder.append('\t').append(Format.WHITE).append(printable.print()).append('\n');
            else
                builder.append('\t').append(Format.WHITE).append(statement.getClass().getSimpleName()).append('\n');
        }

        builder.append(Format.LIGHT_GRAY).append('}');
        return builder.toString();
    }
}
