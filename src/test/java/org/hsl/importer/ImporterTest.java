package org.hsl.importer;

import com.google.gson.GsonBuilder;
import lombok.SneakyThrows;
import org.housingstudio.hsl.exporter.House;
import org.housingstudio.hsl.exporter.action.Action;
import org.housingstudio.hsl.exporter.adapter.ActionAdapter;
import org.housingstudio.hsl.exporter.adapter.ConditionAdapter;
import org.housingstudio.hsl.exporter.condition.Condition;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ImporterTest {
    public static void main(String[] args) {
        String resource = getResource();
        House house = new GsonBuilder()
            .registerTypeAdapter(Action.class, new ActionAdapter())
            .registerTypeAdapter(Condition.class, new ConditionAdapter())
            .create()
            .fromJson(resource, House.class);
        System.out.println(house);
    }

    @SneakyThrows
    private static @NotNull String getResource() {
        InputStream stream = ImporterTest.class.getClassLoader().getResourceAsStream("example.json");
        if (stream == null)
            throw new RuntimeException("Could not find resource example.json");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }

            return sb.toString();
        }
    }
}
