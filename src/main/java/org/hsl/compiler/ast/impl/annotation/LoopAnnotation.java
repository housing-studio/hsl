package org.hsl.compiler.ast.impl.annotation;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.hsl.compiler.ast.NodeInfo;
import org.hsl.compiler.ast.NodeType;
import org.hsl.compiler.ast.impl.type.Type;
import org.hsl.compiler.ast.impl.value.Annotation;
import org.hsl.compiler.ast.impl.value.ConstantLiteral;
import org.hsl.compiler.ast.impl.value.Value;
import org.hsl.compiler.token.Token;
import org.jetbrains.annotations.NotNull;

@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.ANNOTATION)
public class LoopAnnotation extends Annotation {
    private long ticks;

    public LoopAnnotation(@NotNull Token name, @NotNull Value value) {
        super(name, value);
    }

    @Override
    public void init() {
        if (!(value() instanceof ConstantLiteral literal)) {
            context.syntaxError(name(), "Description annotation expects constant literal");
            throw new UnsupportedOperationException("Description annotation expects constant literal");
        }

        if (literal.getValueType() != Type.INT) {
            context.syntaxError(name(), "Loop annotation expects int literal");
            throw new UnsupportedOperationException("Loop annotation expects int literal");
        }

        ticks = Long.parseLong(literal.asConstantValue());
    }
}
