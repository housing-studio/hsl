package org.hsl.export.action.impl;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hsl.export.action.Action;
import org.hsl.export.action.ActionType;
import org.hsl.std.type.Mode;
import org.hsl.std.type.Namespace;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
@ToString
public class ChangeVariable implements Action {
    private final ActionType type = ActionType.CHANGE_VARIABLE;

    private @NotNull Namespace namespace;
    private @NotNull String variable;
    private @NotNull Mode mode;
    private @NotNull String value;

    @SerializedName("automatic-unset")
    private boolean automaticUnset;
}
