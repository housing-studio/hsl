package org.housingstudio.hsl.compiler.ast.impl.annotation;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.impl.value.Annotation;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.ast.impl.value.builtin.ExecutorValue;
import org.housingstudio.hsl.compiler.token.Errno;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.std.Executor;
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
        if (!(value() instanceof ExecutorValue)) {
            context.error(
                Errno.UNEXPECTED_ANNOTATION_VALUE,
                "unexpected annotation value",
                name(),
                "executor annotation expects executor value"
            );
            throw new UnsupportedOperationException("Executor annotation expects executor value");
        }

        executor = ((ExecutorValue) value()).executor();
    }
}
