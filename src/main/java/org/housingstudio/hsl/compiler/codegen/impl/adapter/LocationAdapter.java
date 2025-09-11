package org.housingstudio.hsl.compiler.codegen.impl.adapter;

import com.google.gson.*;
import org.housingstudio.hsl.compiler.ast.impl.value.ConstantLiteral;
import org.housingstudio.hsl.std.location.Location;
import org.housingstudio.hsl.std.location.LocationType;
import org.housingstudio.hsl.std.location.impl.CustomLocation;
import org.housingstudio.hsl.std.location.impl.StaticLocation;

import java.lang.reflect.Type;

public class LocationAdapter implements JsonSerializer<Location>, JsonDeserializer<Location> {
    @Override
    public JsonElement serialize(Location location, Type type, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.add("type", context.serialize(location.type()));

        if (location instanceof CustomLocation) {
            CustomLocation custom = (CustomLocation) location;
            json.addProperty("x", custom.x().load().asConstantValue());
            json.addProperty("y", custom.y().load().asConstantValue());
            json.addProperty("z", custom.z().load().asConstantValue());
        }

        return json;
    }

    @Override
    public Location deserialize(JsonElement src, Type type, JsonDeserializationContext ctx) throws JsonParseException {
        JsonElement kindValue = src.getAsJsonObject().get("type");
        LocationType kind = ctx.deserialize(kindValue, LocationType.class);
        switch (kind) {
            case SPAWN:
            case INVOKER:
            case CURRENT:
                return new StaticLocation(kind);
            case CUSTOM:
                float x = src.getAsJsonObject().get("x").getAsFloat();
                float y = src.getAsJsonObject().get("y").getAsFloat();
                float z = src.getAsJsonObject().get("z").getAsFloat();
                return new CustomLocation(
                    ConstantLiteral.ofFloat(x),
                    ConstantLiteral.ofFloat(y),
                    ConstantLiteral.ofFloat(z)
                );
            default:
                throw new JsonParseException("Unknown location type: " + kind);
        }
    }
}
