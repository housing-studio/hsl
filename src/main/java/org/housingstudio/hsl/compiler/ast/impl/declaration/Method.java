package org.housingstudio.hsl.compiler.ast.impl.declaration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.impl.local.Variable;
import org.housingstudio.hsl.compiler.ast.impl.type.Type;
import org.housingstudio.hsl.compiler.ast.impl.type.Types;
import org.housingstudio.hsl.compiler.ast.impl.value.MethodParameterAccessor;
import org.housingstudio.hsl.compiler.ast.impl.value.MethodReturnVariable;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.codegen.ActionLimiter;
import org.housingstudio.hsl.compiler.codegen.builder.FunctionBuilder;
import org.housingstudio.hsl.compiler.codegen.hierarchy.Children;
import org.housingstudio.hsl.compiler.ast.impl.scope.Scope;
import org.housingstudio.hsl.compiler.ast.impl.scope.ScopeContainer;
import org.housingstudio.hsl.compiler.ast.impl.value.Annotation;
import org.housingstudio.hsl.compiler.ast.impl.annotation.DescriptionAnnotation;
import org.housingstudio.hsl.compiler.ast.impl.annotation.IconAnnotation;
import org.housingstudio.hsl.compiler.ast.impl.annotation.LoopAnnotation;
import org.housingstudio.hsl.compiler.codegen.impl.action.Action;
import org.housingstudio.hsl.compiler.debug.Format;
import org.housingstudio.hsl.compiler.debug.Printable;
import org.housingstudio.hsl.compiler.error.Errno;
import org.housingstudio.hsl.compiler.error.Notification;
import org.housingstudio.hsl.compiler.error.NamingConvention;
import org.housingstudio.hsl.compiler.error.Warning;
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
     * The reserved variable for storing the return value.
     * <p>
     * This is only set if the return type is not void. It represents the variable
     * where return values are stored when the method is called.
     */
    @Setter
    private @Nullable Variable returnVariable;

    /**
     * Initialize node logic before the nodes are visited and the code is generated.
     */
    @Override
    public void init() {
        validateAnnotations();
        validateName();
        validateParameters();

        // reserve a variable for the return value if the return type is not void
        if (!returnType.matches(Types.VOID))
            returnVariable = new MethodReturnVariable(this);
    }

    private void validateAnnotations() {
        for (Annotation annotation : annotations) {
            if (!annotation.isAllowedForFunctions()) {
                context.errorPrinter().print(
                    Notification.error(Errno.UNEXPECTED_ANNOTATION_TARGET, "unexpected annotation target", this)
                        .error("this annotation is not allowed for functions", annotation.name())
                );
                throw new UnsupportedOperationException(
                    String.format("Annotation '%s' is not allowed for functions", annotation.name().value())
                );
            }
        }
    }

    private void validateName() {
        if (!NamingConvention.FUNCTIONS.test(name.value())) {
            context.errorPrinter().print(
                Notification.warning(Warning.INVALID_NAMING_CONVENTION, "invalid naming convention", this)
                    .error("not preferred function name", name)
                    .note("use `lowerCamelCase` style to name functions")
            );
        }
    }

    private void validateParameters() {
        for (Parameter parameter : parameters) {
            Type type = parameter.type();
            if (type.matches(Types.ANY))
                continue;

            Value value = parameter.defaultValue();
            if (value == null)
                continue;

            if (!type.matches(value.getValueType())) {
                context.errorPrinter().print(
                    Notification.error(Errno.INFER_TYPE_MISMATCH, "infer type mismatch", this)
                        .error("default value does not match parameter type", parameter.name())
                );
            }
        }
    }

    @Override
    public @NotNull Function buildFunction() {
        List<Action> actions = scope.buildActionList();
        Function function = new Function(
            name.value(),
            actions,
            resolveDescription(),
            resolveIcon(),
            resolveAutomaticExecution()
        );
        ActionLimiter limiter = ActionLimiter.from(context, this, actions);
        limiter.process();
        return function;
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
     * Resolve a local variable or a global constant by its specified name.
     * <p>
     * For macros, this method first checks if the name matches any of the macro parameters,
     * and if so, returns a MacroParameterAccessor. Otherwise, it delegates to the parent scope.
     *
     * @param name the name of the variable or constant to resolve
     * @return the value of the variable or constant, or {@code null} if the name is not found
     */
    @Override
    public @Nullable Variable resolveName(@NotNull String name) {
        // first check if this is a macro parameter
        for (Parameter parameter : parameters) {
            if (parameter.name().value().equals(name))
                return new MethodParameterAccessor(name, parameter.type(), this);
        }

        // if not a parameter, delegate to parent scope
        return super.resolveName(name);
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
