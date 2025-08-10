package org.hsl.compiler.ast.impl.local;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.compiler.ast.NodeInfo;
import org.hsl.compiler.ast.NodeType;
import org.hsl.compiler.ast.impl.scope.Statement;
import org.hsl.compiler.ast.impl.value.Value;
import org.hsl.compiler.debug.Format;
import org.hsl.compiler.debug.Printable;
import org.hsl.compiler.token.Token;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.LOCAL_ASSIGN)
public class LocalAssign extends Statement implements Printable {
    private final @NotNull String name;
    private final @NotNull Token operator;
    private final @NotNull Value value;

    /**
     * Returns a string representation of the implementing class.
     *
     * @return the class debug information
     */
    @Override
    public @NotNull String print() {
        return Format.WHITE + name + Format.YELLOW + " = " + Format.WHITE + value.print();
    }
}
