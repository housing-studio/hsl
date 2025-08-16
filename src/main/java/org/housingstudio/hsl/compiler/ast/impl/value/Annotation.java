package org.housingstudio.hsl.compiler.ast.impl.value;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.impl.annotation.DescriptionAnnotation;
import org.housingstudio.hsl.compiler.ast.impl.annotation.IconAnnotation;
import org.housingstudio.hsl.compiler.ast.impl.annotation.LoopAnnotation;
import org.housingstudio.hsl.compiler.debug.Printable;
import org.housingstudio.hsl.compiler.token.Token;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.ANNOTATION)
public class Annotation extends Node implements Printable {
    private @NotNull Token name;
    private @NotNull Value value;

    /**
     * Returns a string representation of the implementing class.
     *
     * @return the class debug information
     */
    @Override
    public @NotNull String print() {
        return "@" + name.value() + "(" + value.print() + ")";
    }

    public boolean isAllowedForFunctions() {
        return this instanceof DescriptionAnnotation || this instanceof IconAnnotation || this instanceof LoopAnnotation;
    }
}
