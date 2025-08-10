package org.hsl.compiler.debug;

import org.jetbrains.annotations.NotNull;

/**
 * Represents an interface that indicates, that the implementing class can be printed for debugging purposes.
 *
 * @author AdvancedAntiSkid
 */
public interface Printable {
    /**
     * Returns a string representation of the implementing class.
     *
     * @return the class debug information
     */
    @NotNull String print();
}
