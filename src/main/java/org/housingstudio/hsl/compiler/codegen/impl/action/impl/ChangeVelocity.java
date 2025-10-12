package org.housingstudio.hsl.compiler.codegen.impl.action.impl;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.codegen.impl.action.Action;
import org.housingstudio.hsl.compiler.codegen.impl.action.ActionType;
import org.housingstudio.hsl.compiler.codegen.interop.htsl.HtslInvocation;
import org.housingstudio.hsl.compiler.codegen.interop.htsl.HTSL;
import org.housingstudio.hsl.importer.interaction.InteractionTarget;
import org.housingstudio.hsl.importer.interaction.InteractionType;
import org.housingstudio.hsl.importer.interaction.defaults.DefaultFloat;
import org.jetbrains.annotations.NotNull;

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

    /**
     * Retrieve the HTSL representation of this housing action.
     *
     * @return the htsl code that represents this action
     */
    @Override
    public @NotNull HtslInvocation asHTSL() {
        return HTSL.Action.CHANGE_VELOCITY.invoke()
            .set("x_direction", xDirection)
            .set("y_direction", yDirection)
            .set("z_direction", zDirection);
    }
}
