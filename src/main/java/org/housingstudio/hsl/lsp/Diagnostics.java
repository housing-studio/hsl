package org.housingstudio.hsl.lsp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.housingstudio.hsl.compiler.error.Notification;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Diagnostics {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final @NotNull List<Notification> notifications = new ArrayList<>();

    public void submit(@NotNull Notification notification) {
        notifications.add(notification);
    }

    public @NotNull String export() {
        JsonArray array = new JsonArray();
        for (Notification notification : notifications) {
            JsonObject json = GSON.toJsonTree(notification).getAsJsonObject();
            array.add(json);
        }
        return GSON.toJson(array);
    }
}
