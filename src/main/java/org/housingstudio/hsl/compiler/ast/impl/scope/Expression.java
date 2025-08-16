package org.housingstudio.hsl.compiler.ast.impl.scope;

import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;

/**
 * Represents an expression node in a {@link Scope} that can be executed and is associated with a value.
 */
@NodeInfo(type = NodeType.EXPRESSION)
public abstract class Expression extends Node {
}
