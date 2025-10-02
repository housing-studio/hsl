package org.housingstudio.hsl.runtime.natives;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.housingstudio.hsl.compiler.ast.Game;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Constant;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Macro;
import org.housingstudio.hsl.runtime.natives.impl.io.Print;
import org.housingstudio.hsl.runtime.natives.impl.math.Math;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class NativeDefinitions {
    public final Map<String, Macro> MACROS = new HashMap<>();
    public final Map<String, Constant> CONSTANTS = new HashMap<>();

    public void init() {
        register(Print.class);
        register(Math.class);
    }

    public void apply(@NotNull Game game) {
        for (Map.Entry<String, Macro> entry : MACROS.entrySet())
            game.macros().put(entry.getKey(), entry.getValue());

        for (Map.Entry<String, Constant> entry : CONSTANTS.entrySet())
            game.constants().put(entry.getKey(), entry.getValue());
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

            else if (handle instanceof Constant) {
                Constant constant = (Constant) handle;
                CONSTANTS.put(constant.name().value(), constant);
            }

            else
                throw new IllegalStateException("Unexpected native target: " + field);
        }
    }
}
