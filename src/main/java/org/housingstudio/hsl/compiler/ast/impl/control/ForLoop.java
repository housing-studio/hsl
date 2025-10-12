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
import org.housingstudio.hsl.compiler.ast.impl.local.Variable;
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
@NodeInfo(type = NodeType.FOR_LOOP)
public class ForLoop extends ScopeContainer implements ActionBuilder, Printable {
    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    private final int id = COUNTER.incrementAndGet();

    @Children
    private final @Nullable Node decl;

    @Children
    private final @Nullable List<ConditionBuilder> conditions;

    private final boolean matchAny;

    @Children
    private final @Nullable Node step;

    @Children
    private final @NotNull Scope body;

    private boolean created;

    private String initName, tickName, mergeName;
    private @Nullable Method initFn, tickFn, mergeFn;

    @Override
    public void init() {
        initName = magnleName("init");
        tickName = magnleName("tick");
        mergeName = magnleName("merge");
        createFunctions();
    }

    @Override
    public @NotNull Action buildAction() {
        return new TriggerFunction(initName, false);
    }

    private void createFunctions() {
        if (created)
            return;

        initFn = createInitFn();
        tickFn = createTickFn();
        mergeFn = createMergeFn();

        registerFn(initFn);
        registerFn(tickFn);
        registerFn(mergeFn);

        created = true;
    }

    private @NotNull String magnleName(@NotNull String name) {
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

    private @NotNull Method createInitFn() {
        if (!(decl instanceof ActionBuilder))
            throw new IllegalStateException("For loop declaration must build actions");

        return createGenericFn(initName, new Scope(Collections.singletonList(decl)));
    }

    private @NotNull Method createTickFn() {
        // first run the loop's body
        List<Node> iteration = new ArrayList<>();
        iteration.add(body);

        // then run the "step" stage (for example i++)
        if (step != null) {
            if (!(step instanceof ActionBuilder))
                throw new IllegalStateException("For loop step must build actions");
            iteration.add(step);
        }

        // then call the next loop iteration
        iteration.add(new StaticActionBuilder(new PauseExecution(5))); // hacky workaround for function spam filter
        iteration.add(new StaticActionBuilder(new TriggerFunction(tickName, false)));

        // finally, if the for statement has a condition, wrap this logic into an if-else action
        if (conditions != null) {
            Scope elseScope = new Scope(Collections.singletonList(
                new StaticActionBuilder(new TriggerFunction(mergeName, false))
            ));

            ConditionalNode conditional = new ConditionalNode(
                conditions, matchAny, new Scope(iteration), elseScope
            );
            iteration = Collections.singletonList(conditional);
        }

        return createGenericFn(tickName, new Scope(iteration));
    }

    @RequiredArgsConstructor
    @NodeInfo(type = NodeType.BUILTIN)
    static class StaticActionBuilder extends Node implements ActionBuilder {
        private final @NotNull Action action;

        @Override
        public @NotNull Action buildAction() {
            return action;
        }
    }

    private @NotNull Method createMergeFn() {
        return createGenericFn(mergeName, Scope.EMPTY);
    }

    private @NotNull Method createGenericFn(@NotNull String name, @NotNull Scope scope) {
        return new Method(
            new ArrayList<>(), Token.of(TokenType.IDENTIFIER, name), Types.VOID, Collections.emptyList(), scope
        );
    }

    private void registerFn(@NotNull Method fn) {
        game.functions().put(fn.name().value(), fn);
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
     * Resolve a local variable or a global constant by its specified name.
     * <p>
     * If a node does not override this logic, by default it will try to resolve the value from the {@link #parent()}
     * node.
     * <p>
     * A {@link Scope} will initially try to resolve the value from itself, and then from the parent scope.
     *
     * @param name the name of the variable or constant to resolve
     * @return the value of the variable or constant, or {@code null} if the name is not found
     */
    @Override
    public @Nullable Variable resolveName(@NotNull String name) {
        if (decl instanceof Variable && ((Variable) decl).name().equals(name))
            return (Variable) decl;

        return super.resolveName(name);
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
        return "for (...) { ... }";
    }
}
