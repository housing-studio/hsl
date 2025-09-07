package org.housingstudio.hsl.compiler.ast.hierarchy;

import org.housingstudio.hsl.compiler.ast.Node;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public interface ChildrenResolver<T> {
    @NotNull List<Node> resolveChildren(@NotNull T children);

    class NoResolver implements ChildrenResolver<Object> {
        @Override
        public @NotNull List<Node> resolveChildren(@NotNull Object children) {
            return Collections.emptyList();
        }
    }
}
