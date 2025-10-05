package org.housingstudio.hsl.compiler.codegen.impl.action.impl;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.codegen.impl.action.Action;
import org.housingstudio.hsl.compiler.codegen.impl.action.ActionType;
import org.housingstudio.hsl.compiler.codegen.impl.htsl.HtslInvocation;
import org.housingstudio.hsl.compiler.codegen.impl.htsl.HTSL;
import org.housingstudio.hsl.importer.interaction.InteractionTarget;
import org.housingstudio.hsl.importer.interaction.InteractionType;
import org.housingstudio.hsl.importer.interaction.defaults.DefaultBoolean;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
@ToString
public class ToggleNameTagDisplay implements Action {
    private final ActionType type = ActionType.TOGGLE_NAME_TAG_DISPLAY;

    @InteractionTarget(type = InteractionType.TOGGLE, offset = 0)
    @DefaultBoolean(true)
    @SerializedName("display-name-tag")
    private boolean displayNameTag;

    /**
     * Retrieve the HTSL representation of this housing action.
     *
     * @return the htsl code that represents this action
     */
    @Override
    public @NotNull HtslInvocation asHTSL() {
        return HTSL.Action.DISPLAY_NAMETAG.invoke()
            .set("display_nametag", displayNameTag);
    }
}
