package org.housingstudio.hsl.compiler.ast.impl.declaration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.impl.type.Type;
import org.housingstudio.hsl.compiler.codegen.builder.ActionBuilder;
import org.housingstudio.hsl.compiler.codegen.builder.ActionListBuilder;
import org.housingstudio.hsl.compiler.codegen.hierarchy.Children;
import org.housingstudio.hsl.compiler.ast.impl.scope.Scope;
import org.housingstudio.hsl.compiler.ast.impl.scope.ScopeContainer;
import org.housingstudio.hsl.compiler.error.ErrorContainer;
import org.housingstudio.hsl.compiler.error.NamingConvention;
import org.housingstudio.hsl.compiler.error.Warning;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.runtime.Frame;
import org.housingstudio.hsl.runtime.Instruction;
import org.housingstudio.hsl.runtime.Invocable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.MACRO)
public class Macro extends ScopeContainer implements Invocable {
    private final @NotNull Token name;
    private final @NotNull Type returnType;

    @Children(resolver = MethodParameterChildrenResolver.class)
    private final List<Parameter> parameters;

    @Children
    private final @NotNull Scope scope;

    @Override
    public void invoke(@NotNull Frame parent) {
        List<Node> statements = scope.statements();
        int length = statements.size();

        Frame frame = new Frame(parent, name.value(), 0, 0, length, this);

        for (; frame.cursor().get() < length; frame.cursor().incrementAndGet()) {
            Node statement = statements.get(frame.cursor().get());
            if (statement instanceof Instruction)
                ((Instruction) statement).execute(frame);
            else if (statement instanceof ActionBuilder)
                frame.actions().add(((ActionBuilder) statement).buildAction());
            else if (statement instanceof ActionListBuilder)
                frame.actions().addAll(((ActionListBuilder) statement).buildActionList());
        }

        parent.actions().addAll(frame.actions());
    }

    @Override
    public void init() {
        if (!NamingConvention.FUNCTIONS.test(name.value())) {
            context.errorPrinter().print(
                ErrorContainer.warning(Warning.INVALID_NAMING_CONVENTION, "invalid naming convention", this)
                    .error("not preferred macro name", name)
                    .note("use `lowerCamelCase` style to name macros")
            );
        }
    }

    /**
     * Retrieve the parent scope of this scope.
     * <p>
     * This method will return {@code null}, only if {@code this} scope is the root scope.
     *
     * @return the parent scope of this scope, or {@code null} if {@code this} scope is the root scope
     */
    @Override
    public @Nullable ScopeContainer getParentScope() {
        return null;
    }

    /**
     * Retrieve the list of child scopes of this scope.
     * <p>
     * If {@code this} scope has no child scopes, this method will return an empty list.
     *
     * @return the list of child scopes of this scope
     */
    @Override
    public @NotNull List<ScopeContainer> getChildrenScopes() {
        return Collections.singletonList(scope);
    }
}
