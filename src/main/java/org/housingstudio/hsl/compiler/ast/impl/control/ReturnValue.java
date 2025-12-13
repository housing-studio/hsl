package org.housingstudio.hsl.compiler.ast.impl.control;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Macro;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Method;
import org.housingstudio.hsl.compiler.ast.impl.local.Variable;
import org.housingstudio.hsl.compiler.ast.impl.type.Types;
import org.housingstudio.hsl.compiler.ast.impl.value.ConstantLiteral;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.codegen.builder.ActionListBuilder;
import org.housingstudio.hsl.compiler.codegen.hierarchy.Children;
import org.housingstudio.hsl.compiler.codegen.impl.action.Action;
import org.housingstudio.hsl.compiler.codegen.impl.action.impl.ChangeVariable;
import org.housingstudio.hsl.compiler.codegen.impl.action.impl.Exit;
import org.housingstudio.hsl.compiler.debug.Printable;
import org.housingstudio.hsl.runtime.vm.Frame;
import org.housingstudio.hsl.runtime.vm.Instruction;
import org.housingstudio.hsl.std.Mode;
import org.housingstudio.hsl.std.Namespace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.RETURN_VALUE)
public class ReturnValue extends Node implements Instruction, Printable, ActionListBuilder {
    @Children
    @Setter
    private @NotNull Value value;

    @Setter
    private boolean trailing;

    /**
     * Returns a string representation of the implementing class.
     *
     * @return the class debug information
     */
    @Override
    public @NotNull String print() {
        return "return " + value.print();
    }

    @Override
    public void execute(@NotNull Frame frame) {
        // For macros, evaluate the value to a constant and create a new ConstantLiteral
        // This ensures macro parameters are resolved before the frame is destroyed
        if (frame.target() instanceof Macro) {
            String constantValue = value.asConstantValue();
            // Parse the constant value based on the value type
            if (value.getValueType().matches(Types.INT))
                frame.returnValue(ConstantLiteral.ofInt(Integer.parseInt(constantValue)));
            else if (value.getValueType().matches(Types.FLOAT))
                frame.returnValue(ConstantLiteral.ofFloat(Float.parseFloat(constantValue)));
            else
                frame.returnValue(ConstantLiteral.ofString(constantValue));
        } else
            frame.returnValue(value);
    }

    @Override
    public @NotNull List<Action> buildActionList() {
        Method method = resolveParent();
        if (method == null)
            return Collections.emptyList();

        // use the reserved return variable from the method if available
        // TODO should probably check if return type != void then assert that returnVar != null
        Variable returnVar = method.returnVariable();
        if (returnVar == null)
            // fallback to old behavior for void methods or if returnVariable is not set
            return Collections.emptyList();

        String returnName = returnVar.name();
        ChangeVariable changeVariable = new ChangeVariable(
            Namespace.PLAYER, returnName, Mode.SET, value.asConstantValue(), false
        );

        List<Action> actions = new ArrayList<>();
        actions.add(changeVariable);
        if (!trailing)
            actions.add(new Exit());

        return actions;
    }

    private @Nullable Method resolveParent() {
        Node node = this;
        while (node != null) {
            if (node instanceof Method)
                return (Method) node;
            node = node.parent();
        }
        return null;
    }
}
