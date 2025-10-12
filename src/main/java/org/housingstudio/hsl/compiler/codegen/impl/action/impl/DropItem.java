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
import org.housingstudio.hsl.compiler.codegen.impl.adapter.LocationAdapter;
import org.housingstudio.hsl.compiler.codegen.interop.htsl.HtslInvocation;
import org.housingstudio.hsl.compiler.codegen.interop.htsl.HTSL;
import org.housingstudio.hsl.std.Material;
import org.housingstudio.hsl.std.location.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
@ToString
public class DropItem implements Action {
    private final ActionType type = ActionType.DROP_ITEM;

    private @NotNull Material item;

    @JsonAdapter(LocationAdapter.class)
    private @Nullable Location location;

    @SerializedName("drop-naturally")
    private boolean dropNaturally;

    @SerializedName("disable-item-merging")
    private boolean disableItemMerging;

    @SerializedName("prioritize-player")
    private boolean prioritizePlayer;

    @SerializedName("fallback-to-inventory")
    private boolean fallbackToInventory;

    /**
     * Retrieve the HTSL representation of this housing action.
     *
     * @return the htsl code that represents this action
     */
    @Override
    public @NotNull HtslInvocation asHTSL() {
        return HTSL.Action.DROP_ITEM.invoke()
            .setMaterial("item", item) // TODO
            .setLocation("location", location)
            .set("drop_naturally", dropNaturally)
            .set("disable_item_merging", disableItemMerging)
            .set("prioritize_player", prioritizePlayer)
            .set("fallback_to_inventory", fallbackToInventory);
        // TODO implement rest of the parameters
    }
}
