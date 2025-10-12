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
import org.housingstudio.hsl.importer.interaction.defaults.DefaultMode;
import org.housingstudio.hsl.importer.interaction.defaults.DefaultNamespace;
import org.housingstudio.hsl.importer.interaction.defaults.DefaultString;
import org.housingstudio.hsl.std.Mode;
import org.housingstudio.hsl.std.Namespace;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
@ToString
public class ChangeVariable implements Action {
    private final ActionType type = ActionType.CHANGE_VARIABLE;

    @InteractionTarget(type = InteractionType.NAMESPACE, offset = 0)
    @DefaultNamespace(Namespace.PLAYER)
    private @NotNull Namespace namespace;

    @InteractionTarget(type = InteractionType.CHAT, offset = 1)
    @DefaultString("Kills")
    private @NotNull String variable;

    @InteractionTarget(type = InteractionType.MODE_WITH_UNSET, offset = 2)
    @DefaultMode(Mode.INCREMENT)
    private @NotNull Mode mode;

    @InteractionTarget(type = InteractionType.CHAT, offset = 3)
    @DefaultString("1")
    private @NotNull String value;

    @SerializedName("automatic-unset")
    @InteractionTarget(type = InteractionType.TOGGLE, offset = 4)
    @DefaultBoolean(false)
    private boolean automaticUnset;

    /**
     * Retrieve the HTSL representation of this housing action.
     *
     * @return the htsl code that represents this action
     */
    @Override
    public @NotNull HtslInvocation asHTSL() {
        switch (namespace) {
            case PLAYER:
                return HTSL.Action.VAR.invoke()
                    .setString("variable", variable)
                    .setMode("operation", mode)
                    .set("value", value)
                    .set("automatic_unset", automaticUnset);
            case TEAM:
                return HTSL.Action.TEAM_VAR.invoke()
                    .setString("variable", variable)
                    .setString("team", "TODO")
                    .setMode("operation", mode)
                    .set("value", value)
                    .set("automatic_unset", automaticUnset);
            case GLOBAL:
                return HTSL.Action.GLOBAL_VAR.invoke()
                    .setString("variable", variable)
                    .setMode("operation", mode)
                    .set("value", value)
                    .set("automatic_unset", automaticUnset);
            default:
                throw new IllegalStateException("Unexpected namespace: " + namespace);
        }
    }
}
