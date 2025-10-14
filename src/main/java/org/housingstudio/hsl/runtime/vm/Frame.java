package org.housingstudio.hsl.runtime.vm;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.codegen.impl.action.Action;
import org.housingstudio.hsl.compiler.debug.Format;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Accessors(fluent = true)
@Getter
public class Frame {
    public static final int MAX_DEPTH = 1000;
    private static final int MAX_PRINT_DEPTH = 15;

    private static final ThreadLocal<Frame> CURRENT_FRAME = new ThreadLocal<>();

    private final List<Action> actions = new ArrayList<>();

    private final int depth;

    private final @Nullable Frame parent;
    private final @NotNull String name;

    private final @NotNull Stack stack;
    private final @NotNull Storage locals;

    private final @NotNull AtomicInteger cursor;
    private final int length;

    private final @Nullable Invocable target;

    private final @NotNull Kind kind;

    public Frame(
        @Nullable Frame parent, @NotNull String name, int stackSize, int localsSize, int length,
        @Nullable Invocable target, @NotNull Kind kind
    ) {
        this.parent = parent;
        this.name = name;
        stack = new Stack(stackSize);
        locals = new Storage(localsSize);
        this.length = length;
        this.target = target;
        this.cursor = new AtomicInteger(0);
        depth = parent != null ? parent.depth + 1 : 0;
        this.kind = kind;
    }

    public void returnValue(Value value) {
        cursor.set(length);
        if (parent != null)
            parent.stack.push(value);
    }

    public void printStackTrace() {
        System.out.println(Format.RED + "stack trace:");
        Frame frame = this;
        for (int i = 0; i < MAX_PRINT_DEPTH && frame != null; i++) {
            String element = "  at " + frame.name + "()";
            System.out.println(element);
            frame = frame.parent;
        }
    }

    @RequiredArgsConstructor
    public enum Kind {
        MACRO("macro"),
        MACRO_CALL("macro call");

        private final String name;
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
