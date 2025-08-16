package org.housingstudio.hsl.export.action.impl;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.export.action.Action;
import org.housingstudio.hsl.export.action.ActionType;
import org.housingstudio.hsl.export.adapter.LocationAdapter;
import org.housingstudio.hsl.type.Material;
import org.housingstudio.hsl.type.location.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
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
}
