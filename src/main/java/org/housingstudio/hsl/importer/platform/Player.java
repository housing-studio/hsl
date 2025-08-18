package org.housingstudio.hsl.importer.platform;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

@UtilityClass
public class Player {
    private Class<?> type;
    private Method getContainer;

    @SneakyThrows
    public @Nullable Inventory getContainer() {
        if (getContainer == null)
            getContainer = loadType().getDeclaredMethod("getContainer");

        Object handle = getContainer.invoke(null);
        if (handle == null)
            return null;

        return new Inventory(handle);
    }

    @SneakyThrows
    private @NotNull Class<?> loadType() {
        if (type == null)
            type = Class.forName("com.chattriggers.ctjs.minecraft.wrappers.Player");
        return type;
    }
}
