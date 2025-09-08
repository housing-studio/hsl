package org.housingstudio.hsl.compiler.ast.impl.value.builtin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.impl.type.Type;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.std.slot.Slot;
import org.housingstudio.hsl.std.slot.impl.CustomSlot;
import org.housingstudio.hsl.std.slot.impl.HotbarSlot;
import org.housingstudio.hsl.std.slot.impl.InventorySlot;
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
        switch (slot.type()) {
            case HELMET:
            case CHESTPLATE:
            case LEGGINGS:
            case BOOTS:
            case FIRST_AVAILABLE:
            case HAND:
                return "Slot::" + slot.type().format();
            case CUSTOM: {
                CustomSlot custom = (CustomSlot) slot;
                return String.format("Slot::Custom(%s)", custom.rawSlot());
            }
            case INVENTORY: {
                InventorySlot inventory = (InventorySlot) slot;
                return String.format("Slot::Inventory(%s)", inventory.inventorySlot());
            }
            case HOTBAR: {
                HotbarSlot hotbar = (HotbarSlot) slot;
                return String.format("Slot::Hotbar(%s)", hotbar.hotbarSlot());
            }
            default:
                throw new UnsupportedOperationException("Unexpected slot type: " + slot.type());
        }
    }
}
