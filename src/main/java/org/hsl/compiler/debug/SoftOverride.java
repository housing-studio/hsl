package org.hsl.compiler.debug;

/**
 * Represents an annotation that indicates the annotated method may be overridden by a subclass, to implement
 * custom behavior.
 * <p>
 * If the method is not overridden, the default behavior of the method will be used.
 */
public @interface SoftOverride {
}
