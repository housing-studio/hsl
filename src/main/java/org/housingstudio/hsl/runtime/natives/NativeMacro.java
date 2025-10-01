package org.housingstudio.hsl.runtime.natives;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Macro;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Parameter;
import org.housingstudio.hsl.compiler.ast.impl.scope.Scope;
import org.housingstudio.hsl.compiler.ast.impl.type.Type;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.runtime.vm.Frame;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.MACRO)
public class NativeMacro extends Macro {
    private final @NotNull Consumer<InvocationContext> callback;

    public NativeMacro(
        @NotNull Token name, @NotNull Type returnType, List<Parameter> parameters,
        @NotNull Consumer<InvocationContext> callback
    ) {
        super(name, returnType, parameters, Scope.EMPTY);
        this.callback = callback;
    }

    @Override
    public void invoke(@NotNull Frame parent) {
        Frame frame = new Frame(parent, name().value(), 0, 0, 0, this);
        InvocationContext context = new InvocationContext(parent, frame);
        callback.accept(context);
    }
}
