package org.housingstudio.hsl.compiler.ast;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents the annotation used to define the type of nodes.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface NodeInfo {
    /**
     * Retrieve the type of the node.
     */
    @NotNull NodeType type();
}
