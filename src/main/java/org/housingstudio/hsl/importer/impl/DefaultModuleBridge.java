package org.housingstudio.hsl.importer.impl;

import lombok.SneakyThrows;
import org.housingstudio.hsl.exporter.House;
import org.housingstudio.hsl.exporter.Metadata;
import org.housingstudio.hsl.importer.ModuleBridge;
import org.housingstudio.hsl.importer.platform.ChatLib;
import org.housingstudio.hsl.importer.platform.Exec;
import org.housingstudio.hsl.importer.platform.FileLib;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultModuleBridge implements ModuleBridge {
    private final AtomicReference<Importer> importerRef = new AtomicReference<>();

    /**
     * Handle module post load event.
     */
    @Override
    @SneakyThrows
    public void onLoad() {
        ChatLib.chat("&e[HSL] &fLoaded successfully!");
    }

    /**
     * Handle module post unload event.
     */
    @Override
    public void onUnload() {
        ChatLib.chat("&e[HSL] &fUnloaded successfully!");
    }

    /**
     * Import a game into a Hypixel House.
     *
     * @param id the unique name of the name
     * @return {@code true} if the game was imported, {@code false} otherwise
     */
    @Override
    public boolean importGame(@NotNull String id) {
        House house = HouseCodec.load(id);
        if (house == null)
            return false;

        Importer importer = new Importer(house);
        importerRef.set(importer);

        Exec.async(importer::importHouse);
        return true;
    }

    /**
     * Retrieve the list of games placed in the `games` directory.
     *
     * @return the identifiers of the recognized games
     */
    @Override
    public @NotNull List<String> listGames() {
        File gamesDir = FileLib.getModuleFile("games");
        if (!gamesDir.exists())
            return new ArrayList<>();

        File[] files = gamesDir.listFiles();
        if (files == null)
            return new ArrayList<>();

        return Stream.of(files)
            .map(File::getName)
            .filter(name -> name.endsWith(".json"))
            .collect(Collectors.toList());
    }

    /**
     * Retrieve the metadata of the specified game.
     *
     * @param id the unique name of the name
     * @return the metadata of the game, or {@code null} if the game is not recognized
     */
    @Override
    public @Nullable Metadata loadMetadata(@NotNull String id) {
        House house = HouseCodec.load(id);
        return house != null ? house.metadata() : null;
    }

    /**
     * Handle client chat message receive event.
     *
     * @param event the message event container
     * @param message the unformatted message content
     */
    @Override
    public void onMessage(@NotNull Object event, @NotNull String message) {
        Importer importer = importerRef.get();
        if (importer != null)
            importer.onMessage(event, message);
    }

    /**
     * Handle client GUI open event.
     *
     * @param event the gui open event container
     */
    @Override
    public void onContainerOpen(@NotNull Object event) {
        Importer importer = importerRef.get();
        if (importer != null)
            importer.onContainerOpen(event);
    }

    /**
     * Handle client GUI render tick.
     *
     * @param event the gui render event container
     */
    @Override
    public void onContainerRender(@NotNull Object event) {
        Importer importer = importerRef.get();
        if (importer != null)
            importer.onContainerRender(event);
    }

    /**
     * Handle client GUI close event.
     *
     * @param event the gui close event container
     */
    @Override
    public void onContainerClose(@NotNull Object event) {
        Importer importer = importerRef.get();
        if (importer != null)
            importer.onContainerClose(event);
    }
}
