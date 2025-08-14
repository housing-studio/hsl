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
public class ListedAnnotation extends Annotation {
    private boolean listed;

    public ListedAnnotation(@NotNull Token name, @NotNull Value value) {
        super(name, value);
    }

    @Override
    public void init() {
        if (!(value() instanceof ConstantLiteral value) || value.getValueType() != Type.BOOL) {
            context.syntaxError(name(), "Listed annotation expects bool value");
            throw new UnsupportedOperationException("Listed annotation expects bool value");
        }

        listed = Boolean.parseBoolean(value.asConstantValue());
    }
}
