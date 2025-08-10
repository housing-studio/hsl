package org.hsl.compiler.ast;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.hsl.compiler.ast.impl.declaration.ConstantDeclare;
import org.hsl.compiler.ast.impl.declaration.Method;

import java.util.LinkedHashMap;
import java.util.Map;

@NodeInfo(type = NodeType.GAME)
@Accessors(fluent = true)
@Getter
public class Game extends Node {
    private final Map<String, Method> methods = new LinkedHashMap<>();
    private final Map<String, ConstantDeclare> constants = new LinkedHashMap<>();
}
