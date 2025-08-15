package org.hsl.compiler.ast.impl.value.builtin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.compiler.ast.NodeInfo;
import org.hsl.compiler.ast.NodeType;
import org.hsl.compiler.ast.impl.type.Type;
import org.hsl.compiler.ast.impl.value.Value;
import org.hsl.std.type.slot.Slot;
import org.hsl.std.type.slot.impl.CustomSlot;
import org.hsl.std.type.slot.impl.HotbarSlot;
import org.hsl.std.type.slot.impl.InventorySlot;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.BUILTIN)
public class SlotValue extends Value {
    private final @NotNull Slot slot;

    /**
     * Retrieve the type of the held value. This result will be used to inter types for untyped variables.
     *
     * @return the resolved value of the type
     */
    @Override
    public @NotNull Type getValueType() {
        return Type.SLOT;
    }

    /**
     * Get the constant string representation of the value.
     * <p>
     * Housing variables are handled as string by default, this format is the input for housing variables.
     *
     * @return the final string value
     */
    @Override
    public @NotNull String asConstantValue() {
        return slot.asConstantValue();
    }

    /**
     * Returns a string representation of the implementing class.
     *
     * @return the class debug information
     */
    @Override
    public @NotNull String print() {
        return switch (slot.type()) {
            case HELMET, CHESTPLATE, LEGGINGS, BOOTS, FIRST_AVAILABLE, HAND -> "Slot::" + slot.type().format();
            case CUSTOM -> {
                CustomSlot custom = (CustomSlot) slot;
                yield "Slot::Custom(%s)".formatted(custom.rawSlot());
            }
            case INVENTORY -> {
                InventorySlot inventory = (InventorySlot) slot;
                yield "Slot::Inventory(%s)".formatted(inventory.inventorySlot());
            }
            case HOTBAR -> {
                HotbarSlot hotbar = (HotbarSlot) slot;
                yield "Slot::Hotbar(%s)".formatted(hotbar.hotbarSlot());
            }
        };
    }
}
