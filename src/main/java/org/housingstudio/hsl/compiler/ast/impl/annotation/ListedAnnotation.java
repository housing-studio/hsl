package org.housingstudio.hsl.compiler.ast.impl.annotation;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.impl.type.Types;
import org.housingstudio.hsl.compiler.ast.impl.value.Annotation;
import org.housingstudio.hsl.compiler.ast.impl.value.ConstantLiteral;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.error.Errno;
import org.housingstudio.hsl.compiler.error.Notification;
import org.housingstudio.hsl.compiler.token.Token;
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
        if (!(value() instanceof ConstantLiteral) || !value().getValueType().matches(Types.BOOL)) {
            context.errorPrinter().print(
                Notification.error(Errno.UNEXPECTED_ANNOTATION_VALUE, "unexpected annotation value", this)
                    .error("listed annotation expects bool value", name())
            );
            throw new UnsupportedOperationException("Listed annotation expects bool value");
        }

        listed = Boolean.parseBoolean(value().asConstantValue());
    }
}
