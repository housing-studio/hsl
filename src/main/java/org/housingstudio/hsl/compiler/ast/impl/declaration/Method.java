package org.housingstudio.hsl.compiler.ast.impl.declaration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.impl.type.Type;
import org.housingstudio.hsl.compiler.ast.impl.type.Types;
import org.housingstudio.hsl.compiler.codegen.builder.FunctionBuilder;
import org.housingstudio.hsl.compiler.codegen.hierarchy.Children;
import org.housingstudio.hsl.compiler.ast.impl.scope.Scope;
import org.housingstudio.hsl.compiler.ast.impl.scope.ScopeContainer;
import org.housingstudio.hsl.compiler.ast.impl.type.BaseType;
import org.housingstudio.hsl.compiler.ast.impl.value.Annotation;
import org.housingstudio.hsl.compiler.ast.impl.annotation.DescriptionAnnotation;
import org.housingstudio.hsl.compiler.ast.impl.annotation.IconAnnotation;
import org.housingstudio.hsl.compiler.ast.impl.annotation.LoopAnnotation;
import org.housingstudio.hsl.compiler.debug.Format;
import org.housingstudio.hsl.compiler.debug.Printable;
import org.housingstudio.hsl.compiler.token.Errno;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.codegen.impl.generic.Function;
import org.housingstudio.hsl.std.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.METHOD)
public class Method extends ScopeContainer implements Printable, FunctionBuilder {
    private final List<Annotation> annotations;
    private final @NotNull Token name;
    private final @NotNull Type returnType;

    @Children(resolver = MethodParameterChildrenResolver.class)
    private final List<Parameter> parameters;

    @Children
    private final @NotNull Scope scope;

    /**
     * Initialize node logic before the nodes are visited and the code is generated.
     */
    @Override
    public void init() {
        for (Annotation annotation : annotations) {
            if (!annotation.isAllowedForFunctions()) {
                context.error(
                    Errno.UNEXPECTED_ANNOTATION_TARGET,
                    "unexpected annotation target",
                    annotation.name(),
                    "this annotation is not allowed for functions"
                );
                throw new UnsupportedOperationException(
                    String.format("Annotation '%s' is not allowed for functions", annotation.name().value())
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
            if (annotation instanceof DescriptionAnnotation)
                return ((DescriptionAnnotation) annotation).description();
        }

        return null;
    }

    private @Nullable Material resolveIcon() {
        for (Annotation annotation : annotations) {
            if (annotation instanceof IconAnnotation)
                return ((IconAnnotation) annotation).material();
        }

        return null;
    }

    private @Nullable Integer resolveAutomaticExecution() {
        for (Annotation annotation : annotations) {
            if (annotation instanceof LoopAnnotation)
                return (int) ((LoopAnnotation) annotation).ticks();
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

        if (!returnType.matches(Types.VOID))
            builder.append(" -> ").append(Format.RED).append(returnType.print());

        builder.append(Format.LIGHT_GRAY).append(" {").append('\n');

        for (Node statement : scope.statements()) {
            if (statement instanceof Printable)
                builder.append('\t').append(Format.WHITE).append(((Printable) statement).print()).append('\n');
            else
                builder.append('\t').append(Format.WHITE).append(statement.getClass().getSimpleName()).append('\n');
        }

        builder.append(Format.LIGHT_GRAY).append('}');
        return builder.toString();
    }

    /**
     * Retrieve the parent scope of this scope.
     * <p>
     * This method will return {@code null}, only if {@code this} scope is the root scope.
     *
     * @return the parent scope of this scope, or {@code null} if {@code this} scope is the root scope
     */
    @Override
    public @Nullable ScopeContainer getParentScope() {
        return null;
    }

    /**
     * Retrieve the list of child scopes of this scope.
     * <p>
     * If {@code this} scope has no child scopes, this method will return an empty list.
     *
     * @return the list of child scopes of this scope
     */
    @Override
    public @NotNull List<ScopeContainer> getChildrenScopes() {
        return Collections.singletonList(scope);
    }
}
