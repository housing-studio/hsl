package org.housingstudio.hsl.exporter.adapter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.housingstudio.hsl.type.Vector;

import java.lang.reflect.Type;

public class VectorAdapter implements JsonSerializer<Vector> {
    @Override
    public JsonElement serialize(Vector vector, Type type, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty("x", vector.x().load().asConstantValue());
        json.addProperty("y", vector.y().load().asConstantValue());
        json.addProperty("z", vector.z().load().asConstantValue());
        return json;
    }
}
