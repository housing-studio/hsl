package org.housingstudio.hsl.compiler.codegen.hierarchy;

import org.housingstudio.hsl.compiler.ast.Node;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents an annotation, that describes to the {@link NodeVisitor}, that which field is considered the parent
 * of the node, that declared this annotation.
 * <p>
 * The parent field is used to establish the parent-child relationships in the node hierarchy.
 * <p>
 * The parent field should be of the type {@link Node}.
 * <p>
 * The field will be assigned implicitly by the {@link NodeVisitor} during the initialization of the node hierarchy.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Parent {
}
