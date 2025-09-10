package org.housingstudio.hsl.compiler.ast.impl.declaration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.builder.CommandBuilder;
import org.housingstudio.hsl.compiler.ast.hierarchy.Children;
import org.housingstudio.hsl.compiler.ast.impl.annotation.ExecutorAnnotation;
import org.housingstudio.hsl.compiler.ast.impl.annotation.ListedAnnotation;
import org.housingstudio.hsl.compiler.ast.impl.annotation.PriorityAnnotation;
import org.housingstudio.hsl.compiler.ast.impl.scope.Scope;
import org.housingstudio.hsl.compiler.ast.impl.value.Annotation;
import org.housingstudio.hsl.compiler.debug.Format;
import org.housingstudio.hsl.compiler.debug.Printable;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.exporter.generic.Command;
import org.housingstudio.hsl.std.Executor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.COMMAND)
public class CommandNode extends Node implements Printable, CommandBuilder {
    private final List<Annotation> annotations;
    private final @NotNull Token name;

    @Children
    private final @NotNull Scope scope;

    @Override
    public @NotNull Command buildCommand() {
        return new Command(
            name.value(),
            scope.buildActionList(),
            resolveExecutor(),
            resolvePriority(),
            resolveListed()
        );
    }

    private @NotNull Executor resolveExecutor() {
        for (Annotation annotation : annotations) {
            if (annotation instanceof ExecutorAnnotation)
                return ((ExecutorAnnotation) annotation).executor();
        }

        return Executor.SELF;
    }

    private @Nullable Integer resolvePriority() {
        for (Annotation annotation : annotations) {
            if (annotation instanceof PriorityAnnotation)
                return ((PriorityAnnotation) annotation).priority();
        }

        return null;
    }

    private boolean resolveListed() {
        for (Annotation annotation : annotations) {
            if (annotation instanceof ListedAnnotation)
                return ((ListedAnnotation) annotation).listed();
        }

        return true;
    }

    /**
     * Returns a string representation of the implementing class.
     *
     * @return the class debug information
     */
    @Override
    public @NotNull String print() {
        StringBuilder builder = new StringBuilder(Format.RED + "command " + Format.BLUE + name.value());
        builder.append(Format.CYAN).append("()").append(Format.LIGHT_GRAY).append(" {").append('\n');

        for (Node statement : scope.statements()) {
            if (statement instanceof Printable)
                builder.append('\t').append(Format.WHITE).append(((Printable) statement).print()).append('\n');
            else
                builder.append('\t').append(Format.WHITE).append(statement.getClass().getSimpleName()).append('\n');
        }

        builder.append(Format.LIGHT_GRAY).append('}');
        return builder.toString();
    }
}
