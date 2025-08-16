package org.housingstudio.hsl.compiler.ast.impl.declaration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.builder.FunctionBuilder;
import org.housingstudio.hsl.compiler.ast.impl.scope.Scope;
import org.housingstudio.hsl.compiler.ast.impl.type.Type;
import org.housingstudio.hsl.compiler.ast.impl.value.Annotation;
import org.housingstudio.hsl.compiler.ast.impl.annotation.DescriptionAnnotation;
import org.housingstudio.hsl.compiler.ast.impl.annotation.IconAnnotation;
import org.housingstudio.hsl.compiler.ast.impl.annotation.LoopAnnotation;
import org.housingstudio.hsl.compiler.debug.Format;
import org.housingstudio.hsl.compiler.debug.Printable;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.export.generic.Function;
import org.housingstudio.hsl.type.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.METHOD)
public class Method extends Node implements Printable, FunctionBuilder {
    private final List<Annotation> annotations;
    private final @NotNull Token name;
    private final @NotNull Type returnType;
    private final List<Parameter> parameters;
    private final @NotNull Scope scope;

    /**
     * Initialize node logic before the nodes are visited and the code is generated.
     */
    @Override
    public void init() {
        for (Annotation annotation : annotations) {
            if (!annotation.isAllowedForFunctions()) {
                context.syntaxError(annotation.name(), "This annotation is not allowed in functions");
                throw new UnsupportedOperationException(
                    "Annotation `%s` is not allowed in functions".formatted(annotation.name().value())
                );
            }
        }
    }

    @Override
    public @NotNull Function buildFunction() {
        return new Function(
            name.value(),
            scope.buildActionList(),
            resolveDescription(),
            resolveIcon(),
            resolveAutomaticExecution()
        );
    }

    private @Nullable String resolveDescription() {
        for (Annotation annotation : annotations) {
            if (annotation instanceof DescriptionAnnotation description)
                return description.description();
        }

        return null;
    }

    private @Nullable Material resolveIcon() {
        for (Annotation annotation : annotations) {
            if (annotation instanceof IconAnnotation icon)
                return icon.material();
        }

        return null;
    }

    private @Nullable Integer resolveAutomaticExecution() {
        for (Annotation annotation : annotations) {
            if (annotation instanceof LoopAnnotation loop)
                return (int) loop.ticks();
        }

        return null;
    }

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
