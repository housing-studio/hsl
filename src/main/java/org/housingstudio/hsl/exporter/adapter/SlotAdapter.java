package org.housingstudio.hsl.exporter.adapter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.housingstudio.hsl.std.slot.Slot;
import org.housingstudio.hsl.std.slot.impl.CustomSlot;
import org.housingstudio.hsl.std.slot.impl.HotbarSlot;
import org.housingstudio.hsl.std.slot.impl.InventorySlot;

import java.lang.reflect.Type;

public class SlotAdapter implements JsonSerializer<Slot> {
    @Override
    public JsonElement serialize(Slot slot, Type type, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.add("type", context.serialize(slot.type()));


        if (slot instanceof CustomSlot)
            json.addProperty("raw-slot", ((CustomSlot) slot).rawSlot().load().asConstantValue());
        else if (slot instanceof InventorySlot)
            json.addProperty("inventory-slow", ((InventorySlot) slot).inventorySlot().load().asConstantValue());
        else if (slot instanceof HotbarSlot)
            json.addProperty("hotbar-slot", ((HotbarSlot) slot).hotbarSlot().load().asConstantValue());

        return json;
    }
}
