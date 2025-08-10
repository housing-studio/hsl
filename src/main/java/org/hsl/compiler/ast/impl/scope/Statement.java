package org.hsl.compiler.ast.impl.scope;

import org.hsl.compiler.ast.Node;
import org.hsl.compiler.ast.NodeInfo;
import org.hsl.compiler.ast.NodeType;

/**
 * Represents an instruction node in a {@link Scope} that can be executed, but is not associated with a value.
 */
@NodeInfo(type = NodeType.STATEMENT)
public abstract class Statement extends Node {
}
