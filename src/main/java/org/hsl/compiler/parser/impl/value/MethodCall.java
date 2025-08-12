package org.hsl.compiler.parser.impl.value;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.compiler.ast.NodeInfo;
import org.hsl.compiler.ast.NodeType;
import org.hsl.compiler.ast.impl.declaration.Method;
import org.hsl.compiler.ast.impl.type.Type;
import org.hsl.compiler.ast.impl.value.Argument;
import org.hsl.compiler.ast.impl.value.Value;
import org.hsl.compiler.token.Token;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.METHOD_CALL)
public class MethodCall extends Value {
    /**
     * The name of the method to call.
     */
    private final @NotNull Token name;

    /**
     * The list of arguments to pass to the method.
     */
    private final @NotNull List<Argument> arguments;

    /**
     * Retrieve the type of the held value. This result will be used to inter types for untyped variables.
     *
     * @return the resolved value of the type
     */
    @Override
    public @NotNull Type getValueType() {
        Method method = game.methods().get(name.value());

        if (method == null) {
            context.syntaxError(name, "Method not found");
            throw new UnsupportedOperationException("Cannot find method: " + name.value());
        }

        return Type.VOID;//TODO
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
        context.syntaxError(name, "Cannot use method call as an expression");
        throw new UnsupportedOperationException("Cannot use method call as an expression: " + name.value());
    }

    /**
     * Returns a string representation of the implementing class.
     *
     * @return the class debug information
     */
    @Override
    public @NotNull String print() {
        return "call " + name.value() + "(" + arguments.stream().map(Argument::print).collect(Collectors.joining(", ")) + ")";
    }
}
