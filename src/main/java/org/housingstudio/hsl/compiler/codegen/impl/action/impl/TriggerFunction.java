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
import org.housingstudio.hsl.importer.interaction.defaults.Required;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
@ToString
public class TriggerFunction implements Action {
    private final ActionType type = ActionType.TRIGGER_FUNCTION;

    @InteractionTarget(type = InteractionType.DYNAMIC_OPTION, offset = 0)
    @Required
    private @NotNull String function;

    @InteractionTarget(type = InteractionType.TOGGLE, offset = 1)
    @DefaultBoolean(false)
    @SerializedName("trigger-for-all-players")
    private boolean triggerForAllPlayers;

    /**
     * Retrieve the HTSL representation of this housing action.
     *
     * @return the htsl code that represents this action
     */
    @Override
    public @NotNull HtslInvocation asHTSL() {
        return HTSL.Action.FUNCTION.invoke()
            .set("function", function)
            .set("trigger_for_all_players", triggerForAllPlayers);
    }
}
