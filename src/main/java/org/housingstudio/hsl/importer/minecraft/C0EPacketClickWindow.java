package org.housingstudio.hsl.importer.minecraft;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;

@RequiredArgsConstructor
public class C0EPacketClickWindow {
    private static Class<?> type;
    private static Constructor<?> constructor;

    @Getter
    private final @NotNull Object handle;

    public C0EPacketClickWindow(
        int windowId, int slotId, int buttonId, int var1, @Nullable Object itemStack, short var2
    ) {
        this(createHandle(windowId, slotId, buttonId, var1, itemStack, var2));
    }

    @SneakyThrows
    private static @NotNull Object createHandle(
        int windowId, int slotId, int buttonId, int var1, @Nullable Object itemStack, short var2
    ) {
        return loadConstructor().newInstance(windowId, slotId, buttonId, var1, itemStack, var2);
    }

    @SneakyThrows
    private static @NotNull Class<?> loadType() {
        if (type == null)
            type = Class.forName("net.minecraft.network.play.client.C0EPacketClickWindow");
        return type;
    }

    @SneakyThrows
    private static @NotNull Constructor<?> loadConstructor() {
        Class<?> itemStack = Class.forName("net.minecraft.item.ItemStack");
        if (constructor == null)
            constructor = loadType().getDeclaredConstructor(
            int.class, int.class, int.class, int.class,
            itemStack, short.class
        );
        return constructor;
    }
}
