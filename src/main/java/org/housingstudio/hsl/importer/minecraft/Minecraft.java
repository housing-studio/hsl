package org.housingstudio.hsl.importer.minecraft;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class Minecraft {
    private static Class<?> type;

    private final @NotNull Object handle;

    @SneakyThrows
    public static @NotNull Class<?> loadType() {
        if (type == null)
            type = Class.forName("net.minecraft.client.Minecraft");
        return type;
    }
}
