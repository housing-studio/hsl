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
}
