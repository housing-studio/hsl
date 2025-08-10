package org.hsl.compiler.ast.impl.declaration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.compiler.ast.NodeInfo;
import org.hsl.compiler.ast.NodeType;
import org.hsl.compiler.ast.impl.value.Value;
import org.hsl.compiler.debug.Format;
import org.hsl.compiler.debug.Printable;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.CONSTANT)
public class Constant implements Printable {
    private final @NotNull String name;
    private final @NotNull Value value;

    /**
     * Returns a string representation of the implementing class.
     *
     * @return the class debug information
     */
    @Override
    public @NotNull String print() {
        return Format.RED + "const " + Format.WHITE + name + Format.YELLOW + " = " + Format.WHITE + value.print();
    }
}
