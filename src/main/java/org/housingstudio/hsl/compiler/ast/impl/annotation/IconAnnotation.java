package org.housingstudio.hsl.compiler.ast.impl.annotation;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.impl.value.Annotation;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.ast.impl.value.builtin.MaterialValue;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.type.Material;
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
