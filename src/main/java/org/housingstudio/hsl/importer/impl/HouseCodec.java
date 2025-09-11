package org.housingstudio.hsl.importer.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.experimental.UtilityClass;
import org.housingstudio.hsl.compiler.codegen.impl.house.House;
import org.housingstudio.hsl.compiler.codegen.impl.action.Action;
import org.housingstudio.hsl.compiler.codegen.impl.adapter.ActionAdapter;
import org.housingstudio.hsl.compiler.codegen.impl.adapter.ConditionAdapter;
import org.housingstudio.hsl.compiler.codegen.impl.adapter.LocationAdapter;
import org.housingstudio.hsl.compiler.codegen.impl.condition.Condition;
import org.housingstudio.hsl.importer.platform.FileLib;
import org.housingstudio.hsl.std.location.Location;
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
