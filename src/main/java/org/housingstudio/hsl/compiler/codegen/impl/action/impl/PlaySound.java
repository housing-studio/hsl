package org.housingstudio.hsl.compiler.codegen.impl.action.impl;

import com.google.gson.annotations.JsonAdapter;
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
import org.housingstudio.hsl.importer.interaction.defaults.DefaultFloat;
import org.housingstudio.hsl.importer.interaction.defaults.Required;
import org.housingstudio.hsl.std.Sound;
import org.housingstudio.hsl.std.location.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
@ToString
public class PlaySound implements Action {
    private final ActionType type = ActionType.PLAY_SOUND;

    // TODO
    private @NotNull Sound sound;

    @InteractionTarget(type = InteractionType.ANVIL, offset = 1)
    @DefaultFloat(0.7F)
    private float volume;

    @InteractionTarget(type = InteractionType.ANVIL, offset = 2)
    @DefaultFloat(1.0F)
    private float pitch;

    @InteractionTarget(type = InteractionType.LOCATION, offset = 3)
    @Required
    // TODO find out if it should be @Optional and @Nullable if this works without location
    //  or should it be @Required and @NotNull
    @JsonAdapter(LocationAdapter.class)
    private @Nullable Location location;

    /**
     * Retrieve the HTSL representation of this housing action.
     *
     * @return the htsl code that represents this action
     */
    @Override
    public @NotNull HtslInvocation asHTSL() {
        return HTSL.Action.SOUND.invoke()
            .setSound("sound", sound)
            .set("volume", volume)
            .set("pitch", pitch)
            .setLocation("location", location);
    }
}
