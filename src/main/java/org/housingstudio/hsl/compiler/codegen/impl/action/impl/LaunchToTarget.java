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
import org.housingstudio.hsl.importer.interaction.InteractionTarget;
import org.housingstudio.hsl.importer.interaction.InteractionType;
import org.housingstudio.hsl.importer.interaction.defaults.DefaultInt;
import org.housingstudio.hsl.importer.interaction.defaults.Required;
import org.housingstudio.hsl.std.location.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
@ToString
public class LaunchToTarget implements Action {
    private final ActionType type = ActionType.LAUNCH_TO_TARGET;

    @InteractionTarget(type = InteractionType.LOCATION, offset = 0)
    @Required
    @SerializedName("target-location")
    @JsonAdapter(LocationAdapter.class)
    private @Nullable Location location;

    @InteractionTarget(type = InteractionType.ANVIL, offset = 1)
    @DefaultInt(2)
    @SerializedName("launch-strength")
    private int strength;

    /**
     * Retrieve the HTSL representation of this housing action.
     *
     * @return the htsl code that represents this action
     */
    @Override
    public @NotNull HtslInvocation asHTSL() {
        return HTSL.Action.LAUNCH_TARGET.invoke()
            .setLocation("target_location", location)
            .set("launch_strength", strength);
    }
}
