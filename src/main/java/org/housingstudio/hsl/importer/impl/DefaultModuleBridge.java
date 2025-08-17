package org.housingstudio.hsl.importer.impl;

import org.housingstudio.hsl.exporter.Metadata;
import org.housingstudio.hsl.importer.ModuleBridge;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DefaultModuleBridge implements ModuleBridge {
    /**
     * Handle module post load event.
     */
    @Override
    public void onLoad() {
    }

    /**
     * Handle module post unload event.
     */
    @Override
    public void onUnload() {
    }

    /**
     * Import a game into a Hypixel House.
     *
     * @param id the unique name of the name
     * @return {@code true} if the game was imported, {@code false} otherwise
     */
    @Override
    public boolean importGame(@NotNull String id) {
        return false;
    }

    /**
     * Retrieve the list of games placed in the `games` directory.
     *
     * @return the identifiers of the recognized games
     */
    @Override
    public @NotNull List<String> listGames() {
        return new ArrayList<>();
    }

    /**
     * Retrieve the metadata of the specified game.
     *
     * @param id the unique name of the name
     * @return the metadata of the game, or {@code null} if the game is not recognized
     */
    @Override
    public @Nullable Metadata loadMetadata(@NotNull String id) {
        return null;
    }
}
