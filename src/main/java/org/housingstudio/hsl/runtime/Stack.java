package org.housingstudio.hsl.runtime;

public class Stack {
    private Object[] data;
    private int top;

    public Stack(int initSize) {
        this.data = new Object[initSize];
        this.top = -1;
    }

    public void push(Object value) {
        if (top + 1 >= data.length) {
            resize(top + 1 + 1); // make room for one more element
        }
        top++;
        data[top] = value;
    }

    private void resize(int newSize) {
        Object[] newData = new Object[newSize];
        System.arraycopy(data, 0, newData, 0, data.length);
        data = newData;
    }

    public Object pop() {
        if (isEmpty()) {
            throw new RuntimeException("stack underflow");
        }
        Object value = data[top];
        data[top] = null; // help GC
        top--;
        return value;
    }

    public Object peek() {
        if (isEmpty()) {
            throw new RuntimeException("empty stack");
        }
        return data[top];
    }

    public boolean isEmpty() {
        return top == -1;
    }

    public int size() {
        return top + 1;
    }
}
