package org.housingstudio.hsl.importer;

import org.housingstudio.hsl.exporter.Metadata;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Represents a bridge between the HSL implementation and a ChatTriggers module.
 */
public interface ModuleBridge {
    /**
     * Handle module post load event.
     */
    void onLoad();

    /**
     * Handle module post unload event.
     */
    void onUnload();

    /**
     * Import a game into a Hypixel House.
     *
     * @param id the unique name of the name
     * @return {@code true} if the game was imported, {@code false} otherwise
     */
    boolean importGame(@NotNull String id);

    /**
     * Retrieve the list of games placed in the `games` directory.
     *
     * @return the identifiers of the recognized games
     */
    @NotNull List<String> listGames();

    /**
     * Retrieve the metadata of the specified game.
     *
     * @param id the unique name of the name
     * @return the metadata of the game, or {@code null} if the game is not recognized
     */
    @Nullable Metadata loadMetadata(@NotNull String id);

    /**
     * Handle client chat message receive event.
     *
     * @param event the message event container
     * @param message the unformatted message content
     */
    void onMessage(@NotNull Object event, @NotNull String message);

    /**
     * Handle client GUI open event.
     *
     * @param event the gui open event container
     */
    void onContainerOpen(@NotNull Object event);

    /**
     * Handle client GUI render tick.
     *
     * @param event the gui render event container
     */
    void onContainerRender(@NotNull Object event);

    /**
     * Handle client GUI close event.
     *
     * @param event the gui close event container
     */
    void onContainerClose(@NotNull Object event);
}
