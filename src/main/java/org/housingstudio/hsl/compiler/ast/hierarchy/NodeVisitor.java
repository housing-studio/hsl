package org.housingstudio.hsl.compiler.ast.hierarchy;

import lombok.experimental.UtilityClass;
import org.housingstudio.hsl.compiler.ast.Node;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;

/**
 * Represents a utility class that visits the node hierarchy and initializes the parent-child relationships.
 *
 * @see Children
 * @see Parent
 */
@UtilityClass
public class NodeVisitor {
    /**
     * The set of nodes that are registered for the node hierarchy.
     */
    private final @NotNull Set<Node> NODES = new CopyOnWriteArraySet<>();

    /**
     * Register the node to the node hierarchy.
     *
     * @param node the node to register
     */
    public void register(@NotNull Node node) {
        NODES.add(node);
    }

    /**
     * Initialize the parent-child relationships for the node hierarchy.
     */
    public void initHierarchy() {
        // iterate each node registered in the hierarchy
        for (Node node : NODES) {
            // resolve the child nodes of the node, that is now considered a parent
            for (Node child : node.children())
                processParent(node, child);
        }
    }

    /**
     * Initialize the lifecycle of each node in the node hierarchy.
     */
    public void initLifecycle() {
        visit(Node::init);
    }

    /**
     * Recursively visit each node in the node hierarchy, and apply the {@param visitor} function to each node.
     * <p>
     * The nodes are visited in the depth-first order, starting from the root node and recursively visiting each
     * child node of the parent node.
     *
     * @param visitor the function to apply to each node
     */
    public void visit(@NotNull Consumer<@NotNull Node> visitor) {
        visitChildren(NODES, visitor);
    }

    /**
     * Recursively visit each node in the {@param nodes} list, and apply the {@param visitor} function to each node.
     * <p>
     * The nodes are visited in the depth-first order, starting from the root node and recursively visiting each
     * child node of the parent node.
     *
     * @param nodes the list of nodes to visit
     * @param visitor the function to apply to each node
     */
    private void visitChildren(@NotNull Set<@NotNull Node> nodes, @NotNull Consumer<@NotNull Node> visitor) {
        for (Node node : nodes) {
            visitor.accept(node);
            visitChildren(node.children(), visitor);
        }
    }

    /**
     * Initialize each field annotated with {@link Parent} of the {@param child} node with the {@code parent} node.
     *
     * @param parent the parent node
     * @param child the child node
     */
    private void processParent(@NotNull Node parent, @NotNull Node child) {
        // iterate each field of the child node, including the inherited fields
        for (Field field : getFields(child.getClass())) {
            // check if the field is annotated with the parent annotation
            if (!field.isAnnotationPresent(Parent.class))
                continue;

            // assign the parent node to the field
            field.setAccessible(true);
            try {
                field.set(child, parent);
            } catch (Exception e) {
                throw new IllegalStateException("Cannot access parent field", e);
            }
        }
    }

    /**
     * Clear each node registered in the node hierarchy.
     */
    public void clear() {
        NODES.clear();
    }

    /**
     * Resolve the declared and inherited fields of the specified {@param type}.
     *
     * @param type the type to resolve the fields for
     * @return the list of declared and inherited fields of the type
     */
    public @NotNull List<@NotNull Field> getFields(@NotNull Class<?> type) {
        List<Field> fields = new ArrayList<>();
        while (type != null) {
            Collections.addAll(fields, type.getDeclaredFields());
            type = type.getSuperclass();
        }
        return fields;
    }
}
