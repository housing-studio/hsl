package org.housingstudio.hsl.compiler.ast.impl.value;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.impl.local.Variable;
import org.housingstudio.hsl.compiler.ast.impl.type.Type;
import org.housingstudio.hsl.runtime.vm.Frame;
import org.housingstudio.hsl.std.Namespace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.MACRO_CALL)
public class MacroParameterAccessor extends Value implements Variable {
    private final @NotNull String name;
    private final @NotNull Type type;

    public MacroParameterAccessor(@NotNull String name, @NotNull Type type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public @NotNull Namespace namespace() {
        return Namespace.PLAYER; // should have no namespace
    }

    @Override
    public @NotNull String name() {
        return name;
    }

    @Override
    public @NotNull Type type() {
        return type;
    }

    @Override
    public @NotNull Type getValueType() {
        return type;
    }

    @Override
    public @NotNull String asConstantValue() {
        // try to resolve from the current execution frame
        Frame currentFrame = Frame.current();
        if (currentFrame != null) {
            Value value = currentFrame.locals().get(name);
            if (value != null)
                return value.asConstantValue();
        }

        // during compilation, macro parameters don't have constant values yet
        // TODO as of now I'm not sure if this issue could occur for legit use cases
        //  for now every once and then we'll end up here as some ast nodes may not implement Instruction yet
        throw new UnsupportedOperationException("Macro parameter '" + name + "' cannot be converted to constant value - not in execution context");
    }

    @Override
    public @NotNull Value load() {
        // try to resolve from the current execution frame
        Frame currentFrame = Frame.current();
        if (currentFrame != null) {
            Value value = currentFrame.locals().get(name);
            if (value != null)
                return value;
        }

        // during compilation, return this as a placeholder
        return this;
    }

    @Override
    public @NotNull String print() {
        return "param:" + name;
    }
}
