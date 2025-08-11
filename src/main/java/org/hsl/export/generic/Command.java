package org.hsl.export.generic;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.compiler.ast.impl.value.Argument;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
public class Command {
    /**
     * The unique name of the command.
     */
    private @NotNull String name;

    /**
     * The target player of the command to execute against.
     */
    private @NotNull Argument argument;

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
