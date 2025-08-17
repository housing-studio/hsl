package org.housingstudio.hsl.exporter.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.housingstudio.hsl.exporter.action.Action;
import org.housingstudio.hsl.exporter.action.ActionType;

import java.lang.reflect.Type;

public class ActionAdapter implements JsonDeserializer<Action> {
    @Override
    public Action deserialize(JsonElement src, Type type, JsonDeserializationContext ctx) throws JsonParseException {
        JsonElement kindValue = src.getAsJsonObject().get("type");
        ActionType kind = ctx.deserialize(kindValue, ActionType.class);
        return ctx.deserialize(src, kind.wrapper());
    }
}
