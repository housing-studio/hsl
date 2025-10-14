package org.housingstudio.hsl.compiler.ast.impl.statement;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.impl.local.Variable;
import org.housingstudio.hsl.compiler.ast.impl.scope.Statement;
import org.housingstudio.hsl.compiler.ast.impl.type.ArrayType;
import org.housingstudio.hsl.compiler.ast.impl.type.BaseType;
import org.housingstudio.hsl.compiler.ast.impl.type.Types;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.codegen.builder.ActionListBuilder;
import org.housingstudio.hsl.compiler.codegen.hierarchy.Children;
import org.housingstudio.hsl.compiler.codegen.impl.action.Action;
import org.housingstudio.hsl.compiler.codegen.impl.action.impl.ChangeVariable;
import org.housingstudio.hsl.compiler.codegen.impl.action.impl.Conditional;
import org.housingstudio.hsl.compiler.codegen.impl.action.impl.Exit;
import org.housingstudio.hsl.compiler.codegen.impl.action.impl.SendChatMessage;
import org.housingstudio.hsl.compiler.codegen.impl.condition.impl.VariableRequirement;
import org.housingstudio.hsl.compiler.debug.Printable;
import org.housingstudio.hsl.compiler.error.Errno;
import org.housingstudio.hsl.compiler.error.Notification;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.std.Comparator;
import org.housingstudio.hsl.std.Mode;
import org.housingstudio.hsl.std.Namespace;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.ARRAY_STORE)
public class ArrayStore extends Statement implements ActionListBuilder, Printable {
    private final @NotNull Token name;

    @Children
    @Setter
    private @NotNull Value index;

    @Children
    private final @NotNull Value value;

    private Variable variable;

    @Override
    public void init() {
        variable = resolveName(name.value());
        if (variable == null) {
            context.errorPrinter().print(
                Notification.error(Errno.UNKNOWN_VARIABLE, "unknown variable: " + name.value(), this)
                    .error("cannot find variable in this scope", name)
                    .note("did you misspell the name, or forgot to declare the variable?")
            );
            throw new UnsupportedOperationException("Cannot find variable: " + name.value());
        }

        if (variable.type().base() != BaseType.ARRAY) {
            context.errorPrinter().print(
                Notification.error(Errno.UNEXPECTED_TYPE, "expected array type for: " + name.value(), this)
                    .error("underlying type must be array, but found: " + variable.type().print(), name)
            );
            throw new IllegalStateException("Expected array type, but found " + variable.type().print());
        }

        BaseType elementType = ((ArrayType) variable.type()).elementType();
        if (elementType != BaseType.ANY && elementType != value.getValueType().base()) {
            context.errorPrinter().print(
                Notification.error(Errno.UNEXPECTED_TYPE, "cannot assign type " + value.getValueType().print() + " for array: " + name.value(), this)
                    .error("array type = " + variable.type().print() + ", value type = " + value.getValueType().print(), name)
            );
            throw new IllegalStateException("Cannot assign type " + value.getValueType().print() + " for array: " + name.value());
        }

        // TODO check assigned value type against array element type

        if (!value.isConstant()) {
            context.errorPrinter().print(
                Notification.error(Errno.EXPECTED_CONSTANT_VALUE, "cannot assign to non-constant value", this)
                    .error(
                        "cannot assign to this stat, because the assigned value may not be known at compile-time",
                        name // TODO use value.tokens()
                    )
                    .note("consider assigning a constant value", "foo = 1234")
            );
            throw new UnsupportedOperationException("Cannot assign to non-constant value: " + name.value());
        }
    }

    @Override
    public @NotNull List<Action> buildActionList() {
        if (index.isConstant())
            return Collections.singletonList(buildConstantLookup());
        return buildDynamicLookup();
    }

    private @NotNull Action buildConstantLookup() {
        if (!index.getValueType().matches(Types.INT))
            throw new IllegalStateException("Expected int");

        int index = Integer.parseInt(index().asConstantValue());
        if (index < 0)
            throw new IllegalStateException("Expected non-negative");

        ArrayType arrayType = (ArrayType) variable.type();
        int capacity = Integer.parseInt(arrayType.capacity().asConstantValue());
        if (index >= capacity)
            throw new IllegalStateException("Index out of bounds");

        return new ChangeVariable(
            Namespace.PLAYER, variable.name() + "_" + index, Mode.SET, value.asConstantValue(), false
        );
    }

    private @NotNull List<Action> buildDynamicLookup() {
        List<Action> actions = new ArrayList<>();

        actions.add(buildBoundsCheck());

        ArrayType arrayType = (ArrayType) variable.type();
        int capacity = Integer.parseInt(arrayType.capacity().asConstantValue());

        for (int i = 0; i < capacity; i++) {
            Conditional conditional = new Conditional(
                Collections.singletonList(
                    new VariableRequirement(false, Namespace.PLAYER, index.asConstantValue(), Comparator.EQUAL, "" + i, null)
                ),
                false,
                Collections.singletonList(
                    new ChangeVariable(
                        Namespace.PLAYER, variable.name() + "_" + i, Mode.SET, value.asConstantValue(), false
                    )
                ),
                Collections.emptyList()
            );
            actions.add(conditional);
        }

        return actions;
    }

    private @NotNull Action buildBoundsCheck() {
        ArrayType arrayType = (ArrayType) variable.type();
        int capacity = Integer.parseInt(arrayType.capacity().asConstantValue());

        return new Conditional(
            Arrays.asList(
                new VariableRequirement(
                    false, Namespace.PLAYER, variable.name(), Comparator.LESS_THAN, "0", null
                ),
                new VariableRequirement(
                    false, Namespace.PLAYER, variable.name(), Comparator.GREATER_THAN_OR_EQUAL, "" + capacity, null
                )
            ),
            true,
            Arrays.asList(
                new SendChatMessage(
                    "error: array `" + variable.name() + "` index " + index.asConstantValue() + " is out of bounds (0, " +
                    capacity + "]"
                ),
                new Exit()
            ),
            Collections.emptyList()
        );
    }

    /**
     * Returns a string representation of the implementing class.
     *
     * @return the class debug information
     */
    @Override
    public @NotNull String print() {
        return name.value() + "[" + index().print() + "] = " + value.print();
    }
}
