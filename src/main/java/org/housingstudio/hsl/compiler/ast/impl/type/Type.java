package org.housingstudio.hsl.compiler.ast.impl.type;

import org.housingstudio.hsl.compiler.debug.TokenContainer;
import org.housingstudio.hsl.compiler.debug.Printable;
import org.jetbrains.annotations.NotNull;

public interface Type extends Matcher<Type>, Printable, TokenContainer {
    @NotNull BaseType base();

    default boolean numeric() {
        switch (base()) {
            case INT:
            case FLOAT:
                return true;
            default:
                return false;
        }
    }
}
