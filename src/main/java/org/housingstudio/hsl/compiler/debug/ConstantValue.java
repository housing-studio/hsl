package org.housingstudio.hsl.compiler.debug;

import org.jetbrains.annotations.NotNull;

public interface ConstantValue {
    /**
     * Get the constant string representation of the value.
     * <p>
     * Housing variables are handled as string by default, this format is the input for housing variables.
     *
     * @return the final string value
     */
    @NotNull String asConstantValue();
}
