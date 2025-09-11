package org.housingstudio.hsl.compiler.codegen.impl.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.housingstudio.hsl.compiler.codegen.impl.condition.Condition;
import org.housingstudio.hsl.compiler.codegen.impl.condition.ConditionType;

import java.lang.reflect.Type;

public class ConditionAdapter implements JsonDeserializer<Condition> {
    @Override
    public Condition deserialize(JsonElement src, Type type, JsonDeserializationContext ctx) throws JsonParseException {
        JsonElement kindValue = src.getAsJsonObject().get("type");
        ConditionType kind = ctx.deserialize(kindValue, ConditionType.class);
        return ctx.deserialize(src, kind.wrapper());
    }
}
