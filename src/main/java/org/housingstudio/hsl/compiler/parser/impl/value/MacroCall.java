package org.housingstudio.hsl.compiler.parser.impl.value;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.builder.ActionListBuilder;
import org.housingstudio.hsl.compiler.ast.hierarchy.Children;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Macro;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Parameter;
import org.housingstudio.hsl.compiler.ast.impl.type.Type;
import org.housingstudio.hsl.compiler.ast.impl.value.Argument;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.ast.impl.value.builtin.NullValue;
import org.housingstudio.hsl.compiler.token.Errno;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.exporter.action.Action;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.MACRO_CALL)
public class MacroCall extends Value implements ActionListBuilder {
    /**
     * The name of the macro to call.
     */
    private final @NotNull Token name;

    /**
     * The list of arguments to pass to the method.
     */
    @Children(resolver = MethodCallChildrenResolver.class)
    private final @NotNull List<Argument> arguments;

    /**
     * Retrieve the type of the held value. This result will be used to inter types for untyped variables.
     *
     * @return the resolved value of the type
     */
    @Override
    public @NotNull Type getValueType() {
        return load().getValueType();
    }

    @Override
    public @NotNull Value load() {
        Macro macro = game.macros().get(name.value());
        if (macro == null) {
            context.error(
                Errno.UNKNOWN_MACRO, "macro not found",
                name,
                "cannot find macro: " + name.value()
            );
            throw new UnsupportedOperationException("Cannot find macro: " + name.value());
        }

        Map<String, Value> args = ArgumentParser.parseArguments(context, name, macro.parameters(), arguments);
        validateArgumentTypes(macro.parameters(), args);

        return new NullValue();
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
        return load().asConstantValue();
    }

    /**
     * Returns a string representation of the implementing class.
     *
     * @return the class debug information
     */
    @Override
    public @NotNull String print() {
        return "";
    }

    @Override
    public @NotNull List<Action> buildActionList() {
        Value value = load();
        return Collections.emptyList();
    }

    public void validateArgumentTypes(@NotNull List<Parameter> parameters, @NotNull Map<String, Value> args) {
        for (Parameter parameter : parameters) {
            Value value = args.get(parameter.name().value());
            if (parameter.type() == Type.ANY)
                continue;

            if (parameter.type() != value.getValueType()) {
                context.error(
                    Errno.INVALID_ARGUMENT_TYPE,
                    "argument type mismatch",
                    name,
                    "parameter `" + parameter.name().value() + "` expects " + parameter.type() +
                    " but found " + value.getValueType()
                );
                throw new UnsupportedOperationException(
                    String.format("Invalid parameter type: (given: %s, expected: %s)", value.getValueType(), parameter.type())
                );
            }
        }
    }
}
