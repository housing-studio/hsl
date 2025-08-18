package org.housingstudio.hsl.importer.platform;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.housingstudio.hsl.importer.minecraft.Minecraft;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@UtilityClass
public class Client {
    @Getter
    private Class<?> type;
    private Class<?> packetType;

    private Method sendPacket, getMinecraft;
    private Field currentGui;

    @SneakyThrows
    public void sendPacket(@NotNull Object packet) {
        if (sendPacket == null)
            sendPacket = loadType().getMethod("sendPacket", loadPacketType());
        sendPacket.invoke(null, packet);
    }

    @SneakyThrows
    @Deprecated
    public @NotNull Object currentGui() {
        if (currentGui == null)
            currentGui = loadType().getField("currentGui");
        return currentGui.get(null);
    }

    @SneakyThrows
    @Deprecated
    public @NotNull Minecraft getMinecraft() {
        if (getMinecraft == null)
            getMinecraft = loadType().getMethod("getMinecraft");
        return new Minecraft(getMinecraft.invoke(null));
    }

    @SneakyThrows
    private @NotNull Class<?> loadType() {
        if (type == null)
            type = Class.forName("com.chattriggers.ctjs.engine.langs.js.JSClient");
        return type;
    }

    @SneakyThrows
    public @NotNull Class<?> loadPacketType() {
        if (packetType == null)
            packetType = Class.forName("net.minecraft.network.Packet");
        return packetType;
    }
}
