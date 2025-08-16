package org.housingstudio.hsl.exporter.adapter;

import com.google.gson.*;
import org.housingstudio.hsl.type.location.Location;
import org.housingstudio.hsl.type.location.impl.CustomLocation;

import java.lang.reflect.Type;

public class LocationAdapter implements JsonSerializer<Location> {
    @Override
    public JsonElement serialize(Location location, Type type, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.add("type", context.serialize(location.type()));
        if (location instanceof CustomLocation custom) {
            json.addProperty("x", custom.x().load().asConstantValue());
            json.addProperty("y", custom.y().load().asConstantValue());
            json.addProperty("z", custom.z().load().asConstantValue());
        }
        return json;
    }
}
