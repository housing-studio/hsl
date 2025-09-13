package org.housingstudio.hsl.compiler.debug;

import org.housingstudio.hsl.compiler.token.Token;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface TokenContainer {
    @NotNull List<Token> tokens();
}
