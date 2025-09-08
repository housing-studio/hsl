package org.housingstudio.hsl.compiler.ast.impl.declaration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.impl.scope.Scope;
import org.housingstudio.hsl.compiler.ast.impl.type.Type;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.runtime.Frame;
import org.housingstudio.hsl.runtime.Instruction;
import org.housingstudio.hsl.runtime.Invocable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.MACRO)
public class Macro extends Node implements Invocable {
    private final @NotNull Token name;
    private final @NotNull Type returnType;
    private final List<Parameter> parameters;
    private final @NotNull Scope scope;

    @Override
    public void invoke(@NotNull Frame parent) {
        List<Node> statements = scope.statements();
        int length = statements.size();

        Frame frame = new Frame(parent, name.value(), 0, 0, length, this);

        for (int index = parameters.size() - 1; index >= 0; index--)
            frame.locals().set(index, parent.stack().pop());

        for (; frame.cursor().get() < length; frame.cursor().incrementAndGet()) {
            Node statement = statements.get(frame.cursor().get());
            if (statement instanceof Instruction)
                ((Instruction) statement).execute(frame);
        }
    }
}
