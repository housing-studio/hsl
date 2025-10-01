package org.housingstudio.hsl.runtime.natives;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Macro;
import org.housingstudio.hsl.runtime.natives.io.Print;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class NativeDefinitions {
    public final Map<String, Macro> MACROS = new HashMap<>();

    public void init() {
        register(Print.class);
    }

    @SneakyThrows
    private void register(@NotNull Class<?> registry) {
        for (Field field : registry.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers()))
                continue;

            if (!field.isAnnotationPresent(RegisterNative.class))
                continue;

            field.setAccessible(true);
            Object handle = field.get(null);

            if (handle instanceof Macro) {
                Macro macro = (Macro) handle;
                MACROS.put(macro.name().value(), macro);
            }

            else
                throw new IllegalStateException("Unexpected native target: " + field);
        }
    }
}
