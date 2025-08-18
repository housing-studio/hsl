package org.housingstudio.hsl.importer.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.experimental.UtilityClass;
import org.housingstudio.hsl.exporter.House;
import org.housingstudio.hsl.exporter.action.Action;
import org.housingstudio.hsl.exporter.adapter.ActionAdapter;
import org.housingstudio.hsl.exporter.adapter.ConditionAdapter;
import org.housingstudio.hsl.exporter.adapter.LocationAdapter;
import org.housingstudio.hsl.exporter.condition.Condition;
import org.housingstudio.hsl.importer.platform.FileLib;
import org.housingstudio.hsl.type.location.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@UtilityClass
public class HouseCodec {
    public @Nullable House load(@NotNull String id) {
        File gamesDir = FileLib.getModuleFile("games");
        File gameFile = new File(gamesDir, id + ".json");

        if (!gameFile.exists())
            return null;

        Gson gson = new GsonBuilder()
            .registerTypeAdapter(Action.class, new ActionAdapter())
            .registerTypeAdapter(Condition.class, new ConditionAdapter())
            .registerTypeAdapter(Location.class, new LocationAdapter())
            .create();

        try {
            return gson.fromJson(new FileReader(gameFile), House.class);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
