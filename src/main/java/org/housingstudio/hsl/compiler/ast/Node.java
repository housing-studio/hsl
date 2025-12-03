package org.housingstudio.hsl.compiler.ast;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.impl.declaration.CommandNode;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Event;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Macro;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Method;
import org.housingstudio.hsl.compiler.ast.impl.type.Type;
import org.housingstudio.hsl.compiler.codegen.hierarchy.Children;
import org.housingstudio.hsl.compiler.codegen.hierarchy.ChildrenResolver;
import org.housingstudio.hsl.compiler.codegen.hierarchy.NodeVisitor;
import org.housingstudio.hsl.compiler.codegen.hierarchy.Parent;
import org.housingstudio.hsl.compiler.ast.impl.local.Variable;
import org.housingstudio.hsl.compiler.ast.impl.scope.Scope;
import org.housingstudio.hsl.compiler.debug.SoftOverride;
import org.housingstudio.hsl.compiler.parser.ParserContext;
import org.housingstudio.hsl.compiler.token.Token;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents the base class for all element of the Abstract Syntax Tree.
 * <p>
 * It is used to represent the structure of the code in a tree-like structure.
 * The tree is composed of nodes, where each node represents a single element of the code.
 * <p>
 * For example the code {@code 1 + (2 - 3)} can be represented as {@code ADD(1, SUB(2, 3))}.
 */
@Accessors(fluent = true)
@Getter
public abstract class Node {
    @Setter
    protected static Game game;

    @Setter
    protected static ParserContext context;

    private static final AtomicInteger counter = new AtomicInteger();
    private final int id = counter.incrementAndGet();

    /**
     * The type of the node.
     */
    private final @NotNull NodeType nodeType;

    /**
     * The parent node of the overriding node.
     * <p>
     * This field is initially {@code null}, and it is set be the {@link NodeVisitor} after initialization.
     */
    @Parent
    private @Nullable Node parent;

    /**
     * The cache of the list of children nodes of the overriding node.
     * <p>
     * This field is initially {@code null}, and is resolved, when {@link #children()} is called.
     * <p>
     * Note that, this field is dynamically resolved later, and does not implicitly use the {@link NodeVisitor} class.
     * Rather, it tries to access the children from the overriding node.
     */
    private @Nullable Set<@NotNull Node> children;

    private int start, end;

    /**
     * Initialize the node and retrieve the type from the annotation.
     */
    public Node() {
        // resolve the metadata of the node
        NodeInfo info = getClass().getAnnotation(NodeInfo.class);
        if (info == null)
            throw new IllegalStateException(getClass().getSimpleName() + " does not have @NodeInfo");
        nodeType = info.type();

        // register the node for the node hierarchy
        NodeVisitor.register(this);

        if (context == null)
            return;

        start = context.pointer() + 1;
        end = context.cursor();
        context.pointer(context.cursor());
    }

    public @NotNull List<Token> tokens() {
        List<Token> tokens = new ArrayList<>();
        for (int i = start; i <= end; i++)
            tokens.add(context.at(i));
        return tokens;
    }

    /**
     * Initialize node logic before the nodes are visited and the code is generated.
     */
    @SoftOverride
    public void init() {
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
    public @Nullable Variable resolveName(@NotNull String name) {
        return parent != null ? parent.resolveName(name) : null;
    }

    /**
     * Resolve a user-defined type declaration by its specified name.
     * <p>
     * If a node does not override this logic, by default it will try to resolve the type from the {@link #parent()}
     * node.
     *
     * @param name the name of the type to resolve
     * @return the type definition or {@code null} if the type is not found
     */
    public @Nullable Type resolveType(@NotNull String name) {
        return parent != null ? parent.resolveType(name) : null;
    }

    /**
     * Indicate, whether this node is not a finish node.
     *
     * @return {@code true} if there are more nodes to be parsed
     */
    public boolean hasNext() {
        return nodeType != NodeType.ERROR && nodeType != NodeType.EOF;
    }

    public boolean isTopLevelNode() {
        return this instanceof Method || this instanceof Macro || this instanceof CommandNode || this instanceof Event;
    }

    /**
     * Retrieve the list of child nodes of the overriding node.
     * <p>
     * If the children are not resolved yet, it will try to resolve them by checking the fields of the node.
     * Otherwise, it the children will be resolved from the cache.
     *
     * @return the set of child nodes of the overriding node
     */
    @SuppressWarnings("unchecked")
    @SneakyThrows
    public @NotNull Set<Node> children() {
        if (children != null)
            return children;

        Set<Node> result = new CopyOnWriteArraySet<>();

        for (Field field : NodeVisitor.getFields(getClass())) {
            if (!field.isAnnotationPresent(Children.class))
                continue;

            field.setAccessible(true);

            Object children;
            try {
                children = field.get(this);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Cannot access children field", e);
            }

            if (children == null)
                continue;

            Children annotation = field.getDeclaredAnnotation(Children.class);
            if (annotation.resolver() != ChildrenResolver.NoResolver.class) {
                Constructor<?> constructor = annotation.resolver().getDeclaredConstructors()[0];
                constructor.setAccessible(true);
                @SuppressWarnings("rawtypes")
                ChildrenResolver resolver = (ChildrenResolver) constructor.newInstance();
                result.addAll(resolver.resolveChildren(children));
            }

            else if (children instanceof Node)
                result.add((Node) children);

            else if (children instanceof Collection<?>) {
                for (Object child : ((Collection<?>) children)) {
                    if (child instanceof Node)
                        result.add((Node) child);
                }
            }

            else
                throw new IllegalStateException(
                    "Children field `" + field.getName() + "` of node `" + this +
                    "` must be a node or a list of nodes, not " + children
                );
        }

        return this.children = result;
    }
}
