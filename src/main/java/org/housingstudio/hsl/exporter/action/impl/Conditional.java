package org.housingstudio.hsl.exporter.action.impl;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.exporter.action.Action;
import org.housingstudio.hsl.exporter.action.ActionType;
import org.housingstudio.hsl.exporter.condition.Condition;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
@ToString
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
