package org.housingstudio.hsl.compiler.ast.impl.value;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.runtime.natives.NativeDefinitions;
import org.housingstudio.hsl.compiler.ast.impl.local.Variable;
import org.housingstudio.hsl.compiler.ast.impl.scope.Scope;
import org.housingstudio.hsl.compiler.ast.impl.type.Type;
import org.housingstudio.hsl.compiler.ast.impl.type.Types;
import org.housingstudio.hsl.compiler.codegen.builder.ActionListBuilder;
import org.housingstudio.hsl.compiler.codegen.hierarchy.Children;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Macro;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Parameter;
import org.housingstudio.hsl.compiler.ast.impl.value.builtin.NullValue;
import org.housingstudio.hsl.compiler.error.Notification;
import org.housingstudio.hsl.compiler.parser.impl.value.ArgumentParser;
import org.housingstudio.hsl.compiler.error.Errno;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.codegen.impl.action.Action;
import org.housingstudio.hsl.runtime.vm.Frame;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    @Override
    public void init() {
        if (name.value().equals("main")) {
            context.errorPrinter().print(
                Notification.error(Errno.CANNOT_INVOKE_MAIN_MACRO, "cannot invoke main macro", this)
                    .error("you must not invoke the main macro", name)
                    .note("this macro is invoked automatically by the runtime")
            );
            throw new UnsupportedOperationException("Cannot invoke main macro");
        }

        resolveMacro();
    }

    /**
     * Retrieve the type of the held value. This result will be used to inter types for untyped variables.
     *
     * @return the resolved value of the type
     */
    @Override
    public @NotNull Type getValueType() {
        return resolveMacro().returnType();
    }

    private @NotNull Macro resolveMacro() {
        Macro macro = game.macros().get(name.value());
        if (macro == null) {
            context.errorPrinter().print(
                Notification.error(Errno.UNKNOWN_MACRO, "macro not found", this)
                    .error("cannot find macro: " + name.value(), name)
                    .note("did you misspell the name, or forgot to declare the macro?")
            );
            throw new UnsupportedOperationException("Cannot find macro: " + name.value());
        }

        return macro;
    }

    public @NotNull Frame invoke(@NotNull Macro macro) {
        Map<String, Value> args = ArgumentParser.parseArguments(context, name, macro.parameters(), arguments);
        validateArgumentTypes(macro.parameters(), args);

        Frame mainFrame = new Frame(null, "main", 0, 0, 0, null);
        for (Map.Entry<String, Value> entry : args.entrySet())
            mainFrame.locals().set(entry.getKey(), entry.getValue());

        macro.invoke(mainFrame);
        return mainFrame;
    }

    @Override
    public @NotNull Value load() {
        Macro macro = resolveMacro();
        Frame mainFrame = invoke(macro);

        // pull return value from the call stack, if the macro returns a value
        if (!macro.returnType().matches(Types.VOID))
            return mainFrame.stack().pop();

        return new NullValue();
    }

    @Override
    public @NotNull List<Action> buildActionList() {
        Macro macro = resolveMacro();
        Frame mainFrame = invoke(macro);
        return mainFrame.actions();
    }

    /**
     * Resolve a local variable or a global constant by its specified name.
     * <p>
     * If a node does not override this logic, by default it will try to resolve the value from the {@link #parent()}
     * node.
     * <p>
     * A {@link Scope} will initially try to resolve the value from itself, and then from the parent scope.
     *
     * @param name the name of the variable or constant to resolve
     * @return the value of the variable or constant, or {@code null} if the name is not found
     */
    @Override
    public @Nullable Variable resolveName(@NotNull String name) {
        Macro macro = resolveMacro();
        Map<String, Value> args = ArgumentParser.parseArguments(context, this.name, macro.parameters(), arguments);
        validateArgumentTypes(macro.parameters(), args);
        Value value = args.get(name);
        if (value != null)
            return new ParameterAccessor(name, value.getValueType(), value);
        return super.resolveName(name);
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
        return "call! " + name.value();
    }

    public void validateArgumentTypes(@NotNull List<Parameter> parameters, @NotNull Map<String, Value> args) {
        for (Parameter parameter : parameters) {
            Value value = args.get(parameter.name().value());
            if (!parameter.type().matches(Types.ANY))
                continue;

            if (!parameter.type().matches(value.getValueType())) {
                context.errorPrinter().print(
                    Notification.error(Errno.INVALID_ARGUMENT_TYPE, "argument type mismatch", this)
                        .error(
                            "parameter `" + parameter.name().value() + "` expects type " + parameter.type() +
                                 " but found " + value.getValueType(),
                            name
                        )
                        .note("did you misspell the name, or forgot to declare the variable?")
                );
                throw new UnsupportedOperationException(
                    String.format("Invalid parameter type: (given: %s, expected: %s)",
                    value.getValueType(), parameter.type())
                );
            }
        }
    }
}
