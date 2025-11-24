package org.housingstudio.hsl.compiler.ast.impl.placeholder;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.housingstudio.hsl.compiler.ast.impl.placeholder.impl.*;
import org.housingstudio.hsl.compiler.ast.impl.placeholder.impl.Math;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class PlaceholderRegistry {
    private final Map<String, Map<String, Placeholder>> placeholders = new HashMap<>();

    public static void init() {
        register(Date.class);
        register(House.class);
        register(Math.class);
        register(Player.class);
        register(Server.class);
        register(Stat.class);
    }

    @SneakyThrows
    private void register(@NotNull Class<?> type) {
        Struct struct = type.getDeclaredAnnotation(Struct.class);
        for (Field field : type.getDeclaredFields()) {
            if (!field.isAnnotationPresent(Fn.class))
                continue;

            Placeholder placeholder = (Placeholder) field.get(null);
            placeholders
                .computeIfAbsent(struct.value(), k -> new HashMap<>())
                .put(placeholder.name(), placeholder);
        }
    }

    public @Nullable Placeholder resolve(@NotNull String struct, @NotNull String fn) {
        Map<String, Placeholder> fnMap = placeholders.get(struct);
        if (fnMap == null)
            return null;
        return fnMap.get(fn);
    }
}
