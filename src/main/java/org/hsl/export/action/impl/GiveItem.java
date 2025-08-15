package org.hsl.export.action.impl;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.export.action.Action;
import org.hsl.export.action.ActionType;
import org.hsl.export.adapter.SlotAdapter;
import org.hsl.std.type.Material;
import org.hsl.std.type.slot.Slot;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
public class GiveItem implements Action {
    private final ActionType type = ActionType.GIVE_ITEM;

    private @NotNull Material item;

    @SerializedName("allow-multiple")
    private boolean allowMultiple;

    @JsonAdapter(SlotAdapter.class)
    private @NotNull Slot slot;

    @SerializedName("replace-existing-item")
    private boolean replaceExistingItem;
}
