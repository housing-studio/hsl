package org.housingstudio.hsl.compiler.codegen.impl.action.impl;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.codegen.impl.action.Action;
import org.housingstudio.hsl.compiler.codegen.impl.action.ActionType;
import org.housingstudio.hsl.compiler.codegen.impl.condition.Condition;
import org.housingstudio.hsl.compiler.codegen.impl.htsl.HtslInvocation;
import org.housingstudio.hsl.importer.interaction.InteractionTarget;
import org.housingstudio.hsl.importer.interaction.InteractionType;
import org.housingstudio.hsl.importer.interaction.defaults.DefaultBoolean;
import org.housingstudio.hsl.importer.interaction.defaults.DefaultEmpty;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
@ToString
public class Conditional implements Action {
    private final ActionType type = ActionType.CONDITIONAL;

    @InteractionTarget(type = InteractionType.CONDITIONS, offset = 0)
    @DefaultEmpty
    private @NotNull List<Condition> conditions;

    @InteractionTarget(type = InteractionType.TOGGLE, offset = 1)
    @DefaultBoolean(false)
    @SerializedName("match-any-condition")
    private boolean matchAnyCondition;

    @InteractionTarget(type = InteractionType.ACTIONS, offset = 2)
    @DefaultEmpty
    @SerializedName("if-actions")
    private @NotNull List<Action> ifActions;

    @InteractionTarget(type = InteractionType.ACTIONS, offset = 3)
    @DefaultEmpty
    @SerializedName("else-actions")
    private @NotNull List<Action> elseActions;

    /**
     * Retrieve the HTSL representation of this housing action.
     *
     * @return the htsl code that represents this action
     */
    @Override
    public @NotNull HtslInvocation asHTSL() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
