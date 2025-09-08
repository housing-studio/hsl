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
import org.housingstudio.hsl.exporter.adapter.LocationAdapter;
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
}
