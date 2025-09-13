package org.housingstudio.hsl.compiler.ast.impl.type;

import org.jetbrains.annotations.NotNull;

/**
 * Represents an interface that defines a loose comparator for nodes of the specific {@link T} type.
 * <p>
 * Node comparing should be tested with this interface, because {@link Object#equals(Object)} may not be defined
 * correctly for all nodes, and this interface allows for a more flexible comparison. For instance, when comparing,
 * maybe not all fields should be compared, or maybe the comparison should be done in a different way.
 *
 * @param <T> the type of the node that should be compared
 */
public interface Matcher<T> {
    /**
     * Indicate, whether the specified node matches the criteria of the matcher.
     *
     * @param other the node to compare to
     * @return {@code true} if the node matches the criteria, {@code false} otherwise
     */
    boolean matches(@NotNull T other);
}
