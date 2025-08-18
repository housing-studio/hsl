package org.housingstudio.hsl.importer.platform;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class Inventory {
    private static Class<?> type;

    private static Method getName, getItems, click, getWindowId;
    private static Field container;

    private final @NotNull Object handle;

    @SneakyThrows
    public @NotNull String getName() {
        if (getName == null)
            getName = loadType().getDeclaredMethod("getName");
        return (String) getName.invoke(handle);
    }

    @SneakyThrows
    public @NotNull List<Item> getItems() {
        if (getItems == null)
            getItems = loadType().getDeclaredMethod("getItems");

        @SuppressWarnings("rawtypes")
        List list = (List) getItems.invoke(handle);

        List<Item> items = new ArrayList<>(list.size());
        for (int i = 0; i < list.size(); i++) {
            Object item = list.get(i);
            if (item == null)
                continue;

            Item itemWrapper = new Item(item);
            itemWrapper.setSlot(i);
            items.add(itemWrapper);
        }
        return items;
    }

    @SneakyThrows
    public void click(int slot, boolean shift, String button) {
        if (click == null)
            click = loadType().getDeclaredMethod("click", int.class, boolean.class, String.class);
        click.invoke(handle, slot, shift, button);
    }

    @SneakyThrows
    public int getWindowId() {
        if (getWindowId == null)
            getWindowId = loadType().getDeclaredMethod("getWindowId");
        return (int) getWindowId.invoke(handle);
    }

    @SneakyThrows
    public @NotNull Object container() {
        if (container == null)
            container = loadType().getDeclaredField("container");
        container.setAccessible(true);
        return container.get(handle);
    }

    @SneakyThrows
    private static @NotNull Class<?> loadType() {
        if (type == null)
            type = Class.forName("com.chattriggers.ctjs.minecraft.wrappers.inventory.Inventory");
        return type;
    }
}
