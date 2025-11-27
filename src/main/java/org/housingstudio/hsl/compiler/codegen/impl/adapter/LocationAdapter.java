package org.housingstudio.hsl.compiler.codegen.impl.adapter;

import com.google.gson.*;
import org.housingstudio.hsl.compiler.ast.impl.value.ConstantLiteral;
import org.housingstudio.hsl.std.location.Location;
import org.housingstudio.hsl.std.location.LocationType;
import org.housingstudio.hsl.std.location.impl.PosLocation;
import org.housingstudio.hsl.std.location.impl.PosLookLocation;
import org.housingstudio.hsl.std.location.impl.StaticLocation;

import java.lang.reflect.Type;

public class LocationAdapter implements JsonSerializer<Location>, JsonDeserializer<Location> {
    @Override
    public JsonElement serialize(Location location, Type type, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.add("type", context.serialize(location.type()));

        if (location instanceof PosLocation) {
            PosLocation pos = (PosLocation) location;

            json.addProperty("x", pos.x().load().asConstantValue());
            json.addProperty("y", pos.y().load().asConstantValue());
            json.addProperty("z", pos.z().load().asConstantValue());
        } else if (location instanceof PosLookLocation) {
            PosLookLocation posLook = (PosLookLocation) location;

            json.addProperty("x", posLook.x().load().asConstantValue());
            json.addProperty("y", posLook.y().load().asConstantValue());
            json.addProperty("z", posLook.z().load().asConstantValue());

            json.addProperty("yaw", posLook.yaw().load().asConstantValue());
            json.addProperty("pitch", posLook.pitch().load().asConstantValue());
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
            case POSITION:
            case POSITION_LOOK:
                float x = src.getAsJsonObject().get("x").getAsFloat();
                float y = src.getAsJsonObject().get("y").getAsFloat();
                float z = src.getAsJsonObject().get("z").getAsFloat();

                if (kind == LocationType.POSITION) {
                    return new PosLocation(
                        ConstantLiteral.ofFloat(x),
                        ConstantLiteral.ofFloat(y),
                        ConstantLiteral.ofFloat(z)
                    );
                } else {
                    float yaw = src.getAsJsonObject().get("yaw").getAsFloat();
                    float pitch = src.getAsJsonObject().get("pitch").getAsFloat();

                    return new PosLookLocation(
                        ConstantLiteral.ofFloat(x),
                        ConstantLiteral.ofFloat(y),
                        ConstantLiteral.ofFloat(z),
                        ConstantLiteral.ofFloat(yaw),
                        ConstantLiteral.ofFloat(pitch)
                    );
                }
            default:
                throw new JsonParseException("Unknown location type: " + kind);
        }
    }
}
