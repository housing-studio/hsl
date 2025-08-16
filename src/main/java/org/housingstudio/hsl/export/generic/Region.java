package org.housingstudio.hsl.export.generic;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.export.action.Action;
import org.housingstudio.hsl.type.Flag;
import org.housingstudio.hsl.type.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
public class Region {
    /**
     * The unique name of the region.
     */
    private @NotNull String name;

    /**
     * The pos1 selection of the region area.
     */
    private @NotNull Vector from;

    /**
     * The pos2 selection of the region area.
     */
    private @NotNull Vector to;

    /**
     * The overridden settings of the region.
     */
    @SerializedName("pvp-settings")
    private @NotNull Map<Flag, Boolean> pvpSettings;

    /**
     * The list of actions to invoke upon region entry.
     * <p>
     * These may not trigger when teleporting into region.
     */
    @SerializedName("entry-actions")
    private @NotNull List<Action> entryActions;

    /**
     * The list of actions to invoke upon region exit.
     */
    @SerializedName("exit-actions")
    private @NotNull List<Action> exitActions;
}
