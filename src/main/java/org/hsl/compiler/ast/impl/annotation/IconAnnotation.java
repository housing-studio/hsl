package org.hsl.compiler.ast.impl.annotation;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.hsl.compiler.ast.NodeInfo;
import org.hsl.compiler.ast.NodeType;
import org.hsl.compiler.ast.impl.value.Annotation;
import org.hsl.compiler.ast.impl.value.Value;
import org.hsl.compiler.ast.impl.value.builtin.MaterialValue;
import org.hsl.compiler.token.Token;
import org.hsl.std.type.Material;
import org.jetbrains.annotations.NotNull;

@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.ANNOTATION)
public class IconAnnotation extends Annotation {
    private Material material;

    public IconAnnotation(@NotNull Token name, @NotNull Value value) {
        super(name, value);
    }

    @Override
    public void init() {
        if (!(value() instanceof MaterialValue value)) {
            context.syntaxError(name(), "Icon annotation expects material value");
            throw new UnsupportedOperationException("Icon annotation expects material value");
        }

        material = value.material();
    }
}
