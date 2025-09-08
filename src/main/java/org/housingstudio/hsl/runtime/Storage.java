package org.housingstudio.hsl.runtime;

import org.housingstudio.hsl.compiler.ast.impl.value.Value;

public class Storage {
    private Value[] data;
    private int capacity;
    private int size;

    public Storage(int initSize) {
        this.data = new Value[initSize];
        this.capacity = initSize;
        this.size = 0;
    }

    public Value get(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException(
                String.format("storage unit out of bounds (size: %d, index %d)", size, index)
            );

        return data[index];
    }

    public void set(int index, Value value) {
        if (index >= capacity)
            resize(index + 1);

        if (index >= size)
            size = index + 1;

        data[index] = value;
    }

    private void resize(int newSize) {
        Value[] newData = new Value[newSize];
        System.arraycopy(data, 0, newData, 0, data.length);
        data = newData;
        capacity = newSize;
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return capacity;
    }
}
