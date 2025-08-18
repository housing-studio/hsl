package org.housingstudio.hsl.importer.platform;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@RequiredArgsConstructor
public class Item {
    private static Class<?> type;
    private static Constructor<?> constructor;

    private static Method getName, setName;
    private static Field itemStack;

    private final @NotNull Object handle;

    @SneakyThrows
    public Item(int itemId) {
        Constructor<?> constructor = loadConstructor();
        handle = constructor.newInstance(itemId);
    }

    @Getter
    @Setter
    private int slot;

    @SneakyThrows
    public @NotNull String getName() {
        if (getName == null)
            getName = loadType().getDeclaredMethod("getName");
        return (String) getName.invoke(handle);
    }

    @SneakyThrows
    public @NotNull Item setName(@NotNull String name) {
        if (setName == null)
            setName = loadType().getDeclaredMethod("setName", String.class);
        setName.invoke(handle, name);
        return this;
    }

    @SneakyThrows
    public @NotNull Object itemStack() {
        if (itemStack == null) {
            itemStack = loadType().getDeclaredField("itemStack");
            itemStack.setAccessible(true);
        }
        return itemStack.get(handle);
    }

    @SneakyThrows
    private static @NotNull Class<?> loadType() {
        if (type == null)
            type = Class.forName("com.chattriggers.ctjs.minecraft.wrappers.inventory.Item");
        return type;
    }

    @SneakyThrows
    private static @NotNull Constructor<?> loadConstructor() {
        if (constructor == null)
            constructor = loadType().getDeclaredConstructor(int.class);
        return constructor;
    }
}
