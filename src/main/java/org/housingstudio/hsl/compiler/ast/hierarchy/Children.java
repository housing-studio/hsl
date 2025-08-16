package org.housingstudio.hsl.compiler.ast.hierarchy;

import org.housingstudio.hsl.compiler.ast.Node;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

/**
 * Represents an annotation, that describes to the {@link NodeVisitor}, that which fields are considered the children
 * of the node, that declared this annotation.
 * <p>
 * The children fields are used to establish the parent-child relationships in the node hierarchy.
 * <p>
 * The children fields should be of the type {@link Node} or a {@link List} of {@link Node}s.
 *
 * @see NodeVisitor
 * @see Parent
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Children {
}
