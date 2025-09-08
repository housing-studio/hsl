package org.housingstudio.hsl.std;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public enum Lobby {
    MAIN("Main", 0),
    TOURNAMENT_HALL("TournamentHall", 1),
    BLITZ_SG("BlitzSG", 2),
    THE_TNT_GAMES("TheTNTGames", 3),
    MEGA_WALLS("MegaWalls", 4),
    ARCADE_GAMES("ArcadeGames", 5),
    COPS_AND_CRIMS("CopsAndCrims", 6),
    UHC_CHAMPIONS("UHCChampions", 7),
    WARLORDS("Warlords", 8),
    SMASH_HEROES("SmashHeroes", 9),
    HOUSING("Housing", 10),
    SKY_WARS("SkyWars", 11),
    SPEED_UHC("SpeedUHC", 12),
    CLASSIC_GAMES("ClassicGames", 13),
    PROTOTYPE("Prototype", 14),
    BED_WARS("BedWars", 15),
    MURDER_MYSTERY("MurderMystery", 16),
    BUILD_BATTLE("BuildBattle", 17),
    DUELS("Duels", 18),
    WOOL_WARS("WoolWars", 19);

    private final @NotNull String format;
    private final int offset;
}
