package org.housingstudio.hsl.runtime;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;

import java.util.concurrent.atomic.AtomicInteger;

@Accessors(fluent = true)
@Getter
public class Frame {
    private final Frame parent;
    private final String name;

    private final Stack stack;
    private final Storage locals;

    private final AtomicInteger cursor;
    private final int length;

    private final Invocable target;

    public Frame(Frame parent, String name, int stackSize, int localsSize, int length, Invocable target) {
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
        parent.stack.push(value);
    }
}
