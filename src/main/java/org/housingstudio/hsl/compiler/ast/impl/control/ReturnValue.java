package org.housingstudio.hsl.compiler.ast.impl.control;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.debug.Printable;
import org.housingstudio.hsl.runtime.vm.Frame;
import org.housingstudio.hsl.runtime.vm.Instruction;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.RETURN_VALUE)
public class ReturnValue extends Node implements Instruction, Printable {
    private final @NotNull Value value;

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
        frame.returnValue(value);
    }
}
