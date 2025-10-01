package org.housingstudio.hsl.runtime.vm;

import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class Storage {
    private final Map<String, Value> data;

    public Storage(int capacity) {
        this.data = new HashMap<>(capacity);
    }

    public void set(@NotNull String key, @NotNull Value value) {
        data.put(key, value);
    }

    public @Nullable Value get(@NotNull String key) {
        return data.get(key);
    }
}
