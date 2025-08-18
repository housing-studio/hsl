package org.housingstudio.hsl.importer.platform;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

@UtilityClass
public class Anvil {
    @SneakyThrows
    public void input(@NotNull String input) {
        Object container = Player.getContainer().container();
        Field outputSlotField = container.getClass().getDeclaredField("field_82852_f");
        outputSlotField.setAccessible(true);
        Object outputSlot = outputSlotField.get(container); // outputSlot is a net.minecraft.inventory.InventoryCraftResult

        Field outputSlotItemField = outputSlot.getClass().getDeclaredField("field_70467_a");
        outputSlotItemField.setAccessible(true);
        Object[] outputSlotItem = (Object[]) outputSlotItemField.get(outputSlot); // array with one item, net.minecraft.item.ItemStack

        outputSlotItem[0] = new Item(339).setName(input).itemStack(); // set the single item in the array an item with the name of the input

        outputSlotItemField.set(outputSlot, outputSlotItem); // actually set the outputSlot in the anvil to the new item

        Player.getContainer().click(2, false, "LEFT"); // click that new item
    }
}
