package org.hsl.export.generic;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.export.action.Action;
import org.hsl.std.type.Flag;
import org.hsl.std.type.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
public class Region {
    private @NotNull Vector from, to;

    @SerializedName("pvp-settings")
    private @NotNull Map<Flag, Boolean> pvpSettings;

    @SerializedName("entry-actions")
    private @NotNull List<Action> entryActions;

    @SerializedName("exit-actions")
    private @NotNull List<Action> exitActions;
}
