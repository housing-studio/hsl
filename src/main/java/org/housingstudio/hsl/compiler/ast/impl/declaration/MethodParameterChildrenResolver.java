package org.housingstudio.hsl.compiler.ast.impl.declaration;

import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.codegen.hierarchy.ChildrenResolver;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MethodParameterChildrenResolver implements ChildrenResolver<List<Parameter>> {
    @Override
    public @NotNull List<Node> resolveChildren(@NotNull List<Parameter> children) {
        return children
            .stream()
            .map(Parameter::defaultValue)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }
}
