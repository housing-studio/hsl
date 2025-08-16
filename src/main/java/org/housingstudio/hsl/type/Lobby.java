package org.housingstudio.hsl.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public enum Lobby {
    MAIN("Main"),
    TOURNAMENT_HALL("TournamentHall"),
    BLITZ_SG("BlitzSG"),
    THE_TNT_GAMES("TheTNTGames"),
    MEGA_WALLS("MegaWalls"),
    ARCADE_GAMES("ArcadeGames"),
    COPS_AND_CRIMS("CopsAndCrims"),
    UHC_CHAMPIONS("UHCChampions"),
    WARLORDS("Warlords"),
    SMASH_HEROES("SmashHeroes"),
    HOUSING("Housing"),
    SKY_WARS("SkyWars"),
    SPEED_UHC("SpeedUHC"),
    CLASSIC_GAMES("ClassicGames"),
    PROTOTYPE("Prototype"),
    BED_WARS("BedWars"),
    MURDER_MYSTERY("MurderMystery"),
    BUILD_BATTLE("BuildBattle"),
    DUELS("Duels"),
    WOOL_WARS("WoolWars");

    private final @NotNull String format;
}
