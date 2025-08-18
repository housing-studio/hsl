package org.housingstudio.hsl.exporter.action.impl;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.exporter.action.Action;
import org.housingstudio.hsl.exporter.action.ActionType;
import org.housingstudio.hsl.importer.interaction.InteractionTarget;
import org.housingstudio.hsl.importer.interaction.InteractionType;
import org.housingstudio.hsl.importer.interaction.defaults.DefaultFloat;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
@ToString
public class ChangeVelocity implements Action {
    private final ActionType type = ActionType.CHANGE_VELOCITY;

    @InteractionTarget(type = InteractionType.ANVIL, offset = 0)
    @DefaultFloat(10)
    @SerializedName("x-direction")
    private float xDirection;

    @InteractionTarget(type = InteractionType.ANVIL, offset = 1)
    @DefaultFloat(10)
    @SerializedName("y-direction")
    private float yDirection;

    @InteractionTarget(type = InteractionType.ANVIL, offset = 0)
    @DefaultFloat(10)
    @SerializedName("z-direction")
    private float zDirection;
}
