package org.housingstudio.hsl.importer.platform;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

@UtilityClass
public class ChatLib {
    private Class<?> type;

    private Method chat, command, say;

    @SneakyThrows
    public void chat(Object message) {
        if (chat == null)
            chat = loadType().getDeclaredMethod("chat", Object.class);
        chat.invoke(null, message);
    }

    public void prefixChat(String message) {
        chat("&e[HSL] &r" + message);
    }

    @SneakyThrows
    public void command(String text, boolean clientSide) {
        if (command == null)
            command = loadType().getDeclaredMethod("command", String.class, boolean.class);
        command.invoke(null, text, clientSide);
    }

    @SneakyThrows
    public void say(@NotNull String text) {
        if (say == null)
            say = loadType().getDeclaredMethod("say", String.class);
        say.invoke(null, text);
    }

    @SneakyThrows
    private @NotNull Class<?> loadType() {
        if (type == null)
            type = Class.forName("com.chattriggers.ctjs.minecraft.libs.ChatLib");
        return type;
    }
}
