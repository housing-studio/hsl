package org.housingstudio.hsl.compiler.ast.impl.placeholder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public class Result {
    private final Map<String, Object> values = new HashMap<>();
    private final @NotNull String pattern;

    public @NotNull Result set(@NotNull String key, @NotNull Object value) {
        key = "{" + key + "}";
        values.put(key, value);
        return this;
    }

    public @NotNull String build() {
        String result = pattern;
        for (Map.Entry<String, Object> entry : values.entrySet())
            result = result.replace(entry.getKey(), String.valueOf(entry.getValue()));
        return result;
    }
}
