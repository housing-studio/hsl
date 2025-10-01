package org.housingstudio.hsl.compiler;

import org.housingstudio.hsl.compiler.error.ErrorMode;
import org.housingstudio.hsl.lsp.Diagnostics;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Represents a per-source-file token parser context.
 */
public interface TokenContext {
    /**
     * Retrieve the underlying file that the token context is currently parsing.
     *
     * @return the absolute source file
     */
    @NotNull File file();

    /**
     * Retrieve the error printing strategy.
     *
     * @return whether to use pretty print or json output
     */
    @NotNull ErrorMode mode();

    /**
     * Retrieve the underlying diagnostics collector.
     *
     * @return the token error notification handler
     */
    @NotNull Diagnostics diagnostics();

    /**
     * Retrieve the underlying raw contents of the source file.
     *
     * @return the raw source code in string
     */
    @NotNull String data();
}
