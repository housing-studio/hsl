package org.housingstudio.hsl.compiler.ast.impl.declaration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.impl.type.Type;
import org.housingstudio.hsl.compiler.ast.impl.type.Types;
import org.housingstudio.hsl.compiler.codegen.builder.ActionBuilder;
import org.housingstudio.hsl.compiler.codegen.builder.ActionListBuilder;
import org.housingstudio.hsl.compiler.codegen.hierarchy.Children;
import org.housingstudio.hsl.compiler.ast.impl.local.Variable;
import org.housingstudio.hsl.compiler.ast.impl.scope.Scope;
import org.housingstudio.hsl.compiler.ast.impl.scope.ScopeContainer;
import org.housingstudio.hsl.compiler.ast.impl.value.MacroParameterAccessor;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.debug.Format;
import org.housingstudio.hsl.compiler.error.Errno;
import org.housingstudio.hsl.compiler.error.Notification;
import org.housingstudio.hsl.compiler.error.NamingConvention;
import org.housingstudio.hsl.compiler.error.Warning;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.runtime.vm.Frame;
import org.housingstudio.hsl.runtime.vm.Instruction;
import org.housingstudio.hsl.runtime.vm.Invocable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.MACRO)
public class Macro extends ScopeContainer implements Invocable {
    private final @NotNull Token name;
    private final @NotNull Type returnType;

    @Children(resolver = MethodParameterChildrenResolver.class)
    private final List<Parameter> parameters;

    @Children
    private final @NotNull Scope scope;

    @Override
    public void invoke(@NotNull Frame parent) {
        List<Node> statements = scope.statements();
        int length = statements.size();

        Frame frame = new Frame(parent, name.value(), 0, 0, length, this, Frame.Kind.MACRO);

        if (frame.depth() > Frame.MAX_DEPTH) {
            try {
                context.errorPrinter().print(
                    Notification.error(Errno.STACK_OVERFLOW, "stack overflow", this)
                        .error("macro `" + name.value() + "` exceeded maximum stack depth of " + Frame.MAX_DEPTH, name)
                        .note("did you misspell the name, or forgot to declare the macro?")
                );
            } finally {
                System.err.println(
                    Format.RED + "exception in thread `" + Thread.currentThread().getName() + "`: stack overflow"
                );
                frame.printStackTrace();
            }
            throw new UnsupportedOperationException("Stack overflow caused by macro call: " + name.value());
        }
        // Copy macro parameter values from parent frame's locals to this frame's locals
        for (Parameter parameter : parameters) {
            Value argValue = parent.locals().get(parameter.name().value());
            if (argValue != null)
                frame.locals().set(parameter.name().value(), argValue);
        }

        // Set this frame as the current frame for macro parameter resolution
        Frame previousFrame = Frame.current();
        try {
            Frame.setCurrent(frame);

            for (; frame.cursor().get() < length; frame.cursor().incrementAndGet()) {
                Node statement = statements.get(frame.cursor().get());
                if (statement instanceof Instruction)
                    ((Instruction) statement).execute(frame);
                else if (statement instanceof ActionBuilder)
                    frame.actions().add(((ActionBuilder) statement).buildAction());
                else if (statement instanceof ActionListBuilder)
                    frame.actions().addAll(((ActionListBuilder) statement).buildActionList());
            }
        } finally {
            Frame.setCurrent(previousFrame);
        }

        parent.actions().addAll(frame.actions());
    }

    @Override
    public void init() {
        validateName();
        validateParameters();
    }

    private void validateName() {
        if (!NamingConvention.FUNCTIONS.test(name.value())) {
            context.errorPrinter().print(
                Notification.warning(Warning.INVALID_NAMING_CONVENTION, "invalid naming convention", this)
                    .error("not preferred macro name", name)
                    .note("use `lowerCamelCase` style to name macros")
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
                return new MacroParameterAccessor(name, parameter.type());
        }

        // if not a parameter, delegate to parent scope
        return super.resolveName(name);
    }
}
