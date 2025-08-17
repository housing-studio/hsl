package org.housingstudio.hsl.exporter.action.impl;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.exporter.action.Action;
import org.housingstudio.hsl.exporter.action.ActionType;
import org.housingstudio.hsl.exporter.adapter.SlotAdapter;
import org.housingstudio.hsl.type.Material;
import org.housingstudio.hsl.type.slot.Slot;
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
}
