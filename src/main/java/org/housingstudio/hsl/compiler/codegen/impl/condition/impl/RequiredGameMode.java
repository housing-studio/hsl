package org.housingstudio.hsl.compiler.codegen.impl.condition.impl;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.codegen.impl.condition.Condition;
import org.housingstudio.hsl.compiler.codegen.impl.condition.ConditionType;
import org.housingstudio.hsl.compiler.codegen.interop.htsl.HTSL;
import org.housingstudio.hsl.compiler.codegen.interop.htsl.HtslInvocation;
import org.housingstudio.hsl.std.GameMode;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
@ToString
public class RequiredGameMode implements Condition {
    private final ConditionType type = ConditionType.REQUIRED_GAME_MODE;

    private boolean inverted;

    @SerializedName("required-game-mode")
    private @NotNull GameMode requiredGameMode;

    /**
     * Retrieve the HTSL representation of this housing condition.
     *
     * @return the htsl code that represents this condition
     */
    @Override
    public @NotNull HtslInvocation asHTSL() {
        return HTSL.Condition.GAMEMODE.invoke()
            .setGameMode("required_gamemode", requiredGameMode);
    }
}
