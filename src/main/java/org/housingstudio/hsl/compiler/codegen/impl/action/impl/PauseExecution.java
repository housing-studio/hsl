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
import org.housingstudio.hsl.importer.interaction.defaults.DefaultInt;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
@ToString
public class PauseExecution implements Action {
    private final ActionType type = ActionType.PAUSE_EXECUTION;

    @SerializedName("ticks-to-wait")
    @InteractionTarget(type = InteractionType.ANVIL, offset = 0)
    @DefaultInt(20)
    private int ticksToWait;

    /**
     * Retrieve the HTSL representation of this housing action.
     *
     * @return the htsl code that represents this action
     */
    @Override
    public @NotNull HtslInvocation asHTSL() {
        return HTSL.Action.PAUSE.invoke()
            .set("ticks_to_wait", ticksToWait);
    }
}
