package org.housingstudio.hsl.compiler.codegen.impl.action.impl;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.codegen.impl.action.Action;
import org.housingstudio.hsl.compiler.codegen.impl.action.ActionType;
import org.housingstudio.hsl.compiler.codegen.impl.adapter.SlotAdapter;
import org.housingstudio.hsl.compiler.codegen.impl.htsl.HtslInvocation;
import org.housingstudio.hsl.compiler.codegen.impl.htsl.HTSL;
import org.housingstudio.hsl.std.Material;
import org.housingstudio.hsl.std.slot.Slot;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
@ToString
public class GiveItem implements Action {
    private final ActionType type = ActionType.GIVE_ITEM;

    private @NotNull Material item;

    @SerializedName("allow-multiple")
    private boolean allowMultiple;

    @JsonAdapter(SlotAdapter.class)
    private @NotNull Slot slot;

    @SerializedName("replace-existing-item")
    private boolean replaceExistingItem;

    /**
     * Retrieve the HTSL representation of this housing action.
     *
     * @return the htsl code that represents this action
     */
    @Override
    public @NotNull HtslInvocation asHTSL() {
        return HTSL.Action.GIVE_ITEM.invoke()
            .setMaterial("item", item)
            .set("allow_multiple", allowMultiple)
            .setSlot("inventory_slot", slot) // TODO
            .set("replace_existing_item", replaceExistingItem);
    }
}
