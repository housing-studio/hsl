package org.hsl.compiler.ast.impl.annotation;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.hsl.compiler.ast.NodeInfo;
import org.hsl.compiler.ast.NodeType;
import org.hsl.compiler.ast.impl.value.Annotation;
import org.hsl.compiler.ast.impl.value.Value;
import org.hsl.compiler.ast.impl.value.builtin.ExecutorValue;
import org.hsl.compiler.token.Token;
import org.hsl.std.type.Executor;
import org.jetbrains.annotations.NotNull;

@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.ANNOTATION)
public class ExecutorAnnotation extends Annotation {
    private Executor executor;

    public ExecutorAnnotation(@NotNull Token name, @NotNull Value value) {
        super(name, value);
    }

    @Override
    public void init() {
        if (!(value() instanceof ExecutorValue value)) {
            context.syntaxError(name(), "Executor annotation expects executor value");
            throw new UnsupportedOperationException("Executor annotation expects executor value");
        }

        executor = value.executor();
    }
}
