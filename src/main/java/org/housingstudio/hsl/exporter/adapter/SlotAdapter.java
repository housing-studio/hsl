package org.housingstudio.hsl.exporter.adapter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.housingstudio.hsl.type.slot.Slot;
import org.housingstudio.hsl.type.slot.impl.CustomSlot;
import org.housingstudio.hsl.type.slot.impl.HotbarSlot;
import org.housingstudio.hsl.type.slot.impl.InventorySlot;

import java.lang.reflect.Type;

public class SlotAdapter implements JsonSerializer<Slot> {
    @Override
    public JsonElement serialize(Slot slot, Type type, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.add("type", context.serialize(slot.type()));

        switch (slot) {
            case CustomSlot custom -> json.addProperty("raw-slot", custom.rawSlot().load().asConstantValue());
            case InventorySlot inventory ->
                json.addProperty("inventory-slow", inventory.inventorySlot().load().asConstantValue());
            case HotbarSlot hotbar -> json.addProperty("hotbar-slot", hotbar.hotbarSlot().load().asConstantValue());
            default -> {}
        }

        return json;
    }
}
