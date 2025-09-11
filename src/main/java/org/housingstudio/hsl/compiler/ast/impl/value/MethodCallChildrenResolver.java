package org.housingstudio.hsl.compiler.ast.impl.value;

import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.codegen.hierarchy.ChildrenResolver;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class MethodCallChildrenResolver implements ChildrenResolver<List<Argument>> {
    @Override
    public @NotNull List<Node> resolveChildren(@NotNull List<Argument> children) {
        return children
            .stream()
            .map(Argument::value)
            .collect(Collectors.toList());
    }
}
