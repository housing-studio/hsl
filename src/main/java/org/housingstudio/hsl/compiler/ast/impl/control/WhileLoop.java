package org.housingstudio.hsl.compiler.ast.impl.control;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.impl.declaration.CommandNode;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Event;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Macro;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Method;
import org.housingstudio.hsl.compiler.ast.impl.scope.Scope;
import org.housingstudio.hsl.compiler.ast.impl.scope.ScopeContainer;
import org.housingstudio.hsl.compiler.ast.impl.type.Types;
import org.housingstudio.hsl.compiler.codegen.builder.ActionBuilder;
import org.housingstudio.hsl.compiler.codegen.builder.ConditionBuilder;
import org.housingstudio.hsl.compiler.codegen.hierarchy.Children;
import org.housingstudio.hsl.compiler.codegen.impl.action.Action;
import org.housingstudio.hsl.compiler.codegen.impl.action.impl.PauseExecution;
import org.housingstudio.hsl.compiler.codegen.impl.action.impl.TriggerFunction;
import org.housingstudio.hsl.compiler.debug.Printable;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.WHILE_LOOP)
public class WhileLoop extends ScopeContainer implements ActionBuilder, Printable {
    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    private final int id = COUNTER.incrementAndGet();

    @Children
    private final @NotNull List<ConditionBuilder> conditions;

    private final boolean matchAny;

    @Children
    private final @NotNull Scope body;

    private boolean created;

    private String tickName, mergeName;
    private @Nullable Method tickFn, mergeFn;

    /**
     * Initialize node logic before the nodes are visited and the code is generated.
     */
    @Override
    public void init() {
        tickName = mangleName("tick");
        mergeName = mangleName("merge");
        createFunctions();
    }

    private void createFunctions() {
        if (created)
            return;

        tickFn = createTickFn();
        mergeFn = createMergeFn();

        registerFn(tickFn);
        registerFn(mergeFn);

        created = true;
    }

    @Override
    public @NotNull Action buildAction() {
        return new TriggerFunction(tickName, false);
    }

    private @NotNull Method createTickFn() {
        // first run the loop's body
        List<Node> bodyNodes = new ArrayList<>();
        bodyNodes.add(body);

        // then call the next loop iteration
        bodyNodes.add(new StaticActionBuilder(new PauseExecution(5))); // hacky workaround for function spam filter
        bodyNodes.add(new StaticActionBuilder(new TriggerFunction(tickName, false)));

        // finally, wrap this logic into an if-else action
        Scope elseScope = new Scope(Collections.singletonList(
            new StaticActionBuilder(new TriggerFunction(mergeName, false))
        ));

        ConditionalNode conditional = new ConditionalNode(
            conditions, matchAny, new Scope(bodyNodes), elseScope
        );

        return createGenericFn(tickName, new Scope(Collections.singletonList(conditional)));
    }

    private void registerFn(@NotNull Method fn) {
        game.functions().put(fn.name().value(), fn);
    }

    private @NotNull Method createMergeFn() {
        return createGenericFn(mergeName, Scope.EMPTY);
    }

    private @NotNull Method createGenericFn(@NotNull String name, @NotNull Scope scope) {
        return new Method(
            new ArrayList<>(), Token.of(TokenType.IDENTIFIER, name), Types.VOID, Collections.emptyList(), scope
        );
    }

    private @NotNull String mangleName(@NotNull String name) {
        ScopeContainer node = getUnderlyingMethod();
        String prefix = "";
        String nodeName = "";
        if (node instanceof Method) {
            prefix = "method";
            nodeName = ((Method) node).name().value();
        } else if (node instanceof Macro) {
            prefix = "macro";
            nodeName = ((Macro) node).name().value();
        } else if (node instanceof CommandNode) {
            prefix = "command";
            nodeName = ((CommandNode) node).name().value();
        }
        return String.format("%s:%s:for:%d:%s", prefix, nodeName, id, name);
    }

    private @NotNull ScopeContainer getUnderlyingMethod() {
        Node parent = parent();
        while (parent != null) {
            if (parent instanceof Method || parent instanceof Macro || parent instanceof CommandNode)
                return (ScopeContainer) parent;
            if (parent instanceof Event || parent instanceof Random)
                break;
            parent = parent.parent();
        }
        throw new IllegalStateException("Unable to resolve underlying method");
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
        return (ScopeContainer) parent();
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
        return Collections.singletonList(body);
    }

    /**
     * Returns a string representation of the implementing class.
     *
     * @return the class debug information
     */
    @Override
    public @NotNull String print() {
        return "while (...) { ... }";
    }
}
