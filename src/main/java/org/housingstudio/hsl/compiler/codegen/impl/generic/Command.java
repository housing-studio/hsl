package org.housingstudio.hsl.compiler.codegen.impl.generic;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.codegen.impl.action.Action;
import org.housingstudio.hsl.std.Executor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
@ToString
public class Command {
    /**
     * The unique name of the command.
     */
    private @NotNull String name;

    /**
     * The actions to be executed when the command is invoked.
     */
    private @NotNull List<Action> actions;

    /**
     * The target player of the command to execute against.
     */
    private @NotNull Executor executor;

    /**
     * Change the required priority so only players in a group with a priority equal or higher can use the command.
     */
    @SerializedName("required-group-priority")
    private @Nullable Integer requiredGroupPriority;

    /**
     * Whether this command should be listed to players in command tab completion.
     * <p>
     * Commands will only be listed for players who have the required group priority.
     */
    private boolean listed;
}
