package org.housingstudio.hsl.compiler.ast.impl.value;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Constant;
import org.housingstudio.hsl.compiler.ast.impl.local.Variable;
import org.housingstudio.hsl.compiler.ast.impl.type.Type;
import org.housingstudio.hsl.compiler.ast.impl.type.Types;
import org.housingstudio.hsl.compiler.error.Errno;
import org.housingstudio.hsl.compiler.error.Notification;
import org.housingstudio.hsl.compiler.token.Token;
import org.jetbrains.annotations.NotNull;

import java.util.Stack;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.CONSTANT_ACCESS)
public class ConstantAccess extends Value {
    private final Stack<Value> accessStack = new Stack<>();
    private final @NotNull Token name;

    /**
     * Initialize node logic before the nodes are visited and the code is generated.
     */
    @Override
    public void init() {
        Variable variable = resolveName(name.value());
        if (variable != null)
            return;

        Constant constant = game.constants().get(name.value());
        if (constant != null)
            return;

        context.errorPrinter().print(
            Notification.error(Errno.UNKNOWN_VARIABLE, "unknown variable", this)
                .error("cannot find variable in this scope", name)
                .note("did you misspell the name, or forgot to declare the variable?")
        );
        throw new UnsupportedOperationException("Cannot find variable: " + name.value());
    }

    /**
     * Retrieve the type of the held value. This result will be used to inter types for untyped variables.
     *
     * @return the resolved value of the type
     */
    @Override
    public @NotNull Type getValueType() {
        // During parsing, the parent hierarchy hasn't been set up yet, so we can't resolve names.
        // In this case, return ANY type to defer type checking until later.
        if (parent() == null) {
            return Types.ANY;
        }

        Value value = load();
        if (accessStack.contains(value)) {
            context.errorPrinter().print(
                Notification.error(Errno.CIRCULAR_REFERENCE, "Illegal circular initialization flow", this)
                    .error("two or more constants refer to each other", name)
            );
            throw new IllegalStateException("Circular initialization flow: " + name.value());
        }

        accessStack.push(value);

        try {
            return value.getValueType();
        } finally {
            accessStack.pop();
        }
    }

    /**
     * Get the constant string representation of the value.
     * <p>
     * Housing variables are handled as string by default, this format is the input for housing variables.
     *
     * @return the final string value
     */
    @Override
    public @NotNull String asConstantValue() {
        Value value = load();
        if (accessStack.contains(value)) {
            context.errorPrinter().print(
                Notification.error(Errno.CIRCULAR_REFERENCE, "Illegal circular initialization flow", this)
                    .error("two or more constants refer to each other", name)
            );
            throw new IllegalStateException("Circular initialization flow: " + name.value());
        }

        accessStack.push(value);

        try {
            return value.asConstantValue();
        } finally {
            accessStack.pop();
        }
    }

    @Override
    public @NotNull Value load() {
        // TODO variables and constants should be resolved based on their priority by their depth in the AST tree
        //  basically whichever one is the "closest" to this node, should be captured in case of "shadowing".
        //  To achieve this, you might want to make resolveName "generic" for both variables and constants.
        //  The current implementation of prioritizing variables is invalid - however you need to support anonymous
        //  constants first, as they are currently package-level only.

        // Try to resolve as a variable (including macro parameters)
        Variable variable = resolveName(name.value());
        if (variable != null) {
            // If it's a macro parameter, return it directly
            if (variable instanceof MacroParameterAccessor) {
                return (MacroParameterAccessor) variable;
            }
            // Otherwise, create a StatAccess for regular variables
            return new StatAccess(name, variable);
        }

        // Try to resolve as a constant
        Constant constant = game.constants().get(name.value());
        if (constant != null)
            return constant.value();

        // If we can't resolve it, throw an error
        context.errorPrinter().print(
            Notification.error(Errno.UNKNOWN_VARIABLE, "cannot resolve name from scope", this)
                .error("unknown variable, stat or constant", name)
        );
        throw new UnsupportedOperationException("Cannot find constant: " + name.value());
    }

    /**
     * Indicate, whether the underlying value is a compile-time constant or a dynamically resolved value.
     * <p>
     * Some operations may be online applicable for compile-time constants.
     *
     * @return {@code true} if the value is a constant, {@code false} otherwise
     */
    @Override
    public boolean isConstant() {
        Variable variable = resolveName(name.value());
        if (variable != null)
            return false;

        Constant constant = game.constants().get(name.value());
        return constant != null;
    }

    /**
     * Returns a string representation of the implementing class.
     *
     * @return the class debug information
     */
    @Override
    public @NotNull String print() {
        return "load " + name.value();
    }
}
