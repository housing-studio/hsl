package org.housingstudio.hsl.runtime.vm;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.codegen.impl.action.Action;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Accessors(fluent = true)
@Getter
public class Frame {
    private static final ThreadLocal<Frame> CURRENT_FRAME = new ThreadLocal<>();

    private final List<Action> actions = new ArrayList<>();

    private final @Nullable Frame parent;
    private final @NotNull String name;

    private final @NotNull Stack stack;
    private final @NotNull Storage locals;

    private final @NotNull AtomicInteger cursor;
    private final int length;

    private final @Nullable Invocable target;

    public Frame(
        @Nullable Frame parent, @NotNull String name, int stackSize, int localsSize, int length,
        @Nullable Invocable target
    ) {
        this.parent = parent;
        this.name = name;
        stack = new Stack(stackSize);
        locals = new Storage(localsSize);
        this.length = length;
        this.target = target;
        this.cursor = new AtomicInteger(0);
    }

    public void returnValue(Value value) {
        cursor.set(length);
        if (parent != null)
            parent.stack.push(value);
    }

    public static @Nullable Frame current() {
        return CURRENT_FRAME.get();
    }

    public static void setCurrent(@Nullable Frame frame) {
        if (frame == null)
            CURRENT_FRAME.remove();
        else
            CURRENT_FRAME.set(frame);
    }
}
