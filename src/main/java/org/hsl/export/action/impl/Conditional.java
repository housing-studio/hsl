package org.hsl.export.action.impl;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.export.action.Action;
import org.hsl.export.action.ActionType;
import org.hsl.export.condition.Condition;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
public class Conditional implements Action {
    private final ActionType type = ActionType.CONDITIONAL;

    private @NotNull List<Condition> conditions;

    @SerializedName("match-any-condition")
    private boolean matchAnyCondition;

    @SerializedName("if-actions")
    private @NotNull List<Action> ifActions;

    @SerializedName("else-actions")
    private @NotNull List<Action> elseActions;
}
