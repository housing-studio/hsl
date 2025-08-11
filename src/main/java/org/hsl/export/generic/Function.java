package org.hsl.export.generic;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.export.action.Action;
import org.hsl.std.type.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Represents a wrapper for a Housing function, that is capable of executing a set of actions when triggered.
 * <p>
 * Functions can also act as timers, as they can be triggered automatically for all players.
 */
@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
public class Function {
    /**
     * The exposed unique name of the function.
     */
    private @NotNull String name;

    /**
     * The actions to be executed when the function is triggered.
     */
    private @NotNull List<Action> actions;

    /**
     * The short description for the function
     */
    private @Nullable String description;

    /**
     * The icon of the function that is displayed on the Systems Menu.
     */
    private @Nullable Material icon;

    /**
     * Functions can be enabled to automatically execute for all players in the house every X amount of ticks.
     * <p>
     * If this is {@code null}, then the function will not loop.
     */
    @SerializedName("automatic-execution")
    private @Nullable Integer automaticExecution;
}
