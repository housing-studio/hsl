package org.hsl.compiler.ast.impl.scope;

import org.hsl.compiler.ast.Node;
import org.hsl.compiler.ast.NodeInfo;
import org.hsl.compiler.ast.NodeType;

/**
 * Represents an expression node in a {@link Scope} that can be executed and is associated with a value.
 */
@NodeInfo(type = NodeType.EXPRESSION)
public abstract class Expression extends Node {
}
