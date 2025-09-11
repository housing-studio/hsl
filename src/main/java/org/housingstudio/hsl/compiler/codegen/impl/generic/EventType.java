package org.housingstudio.hsl.compiler.codegen.impl.generic;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public enum EventType {
    /**
     * Executes when a player joins the house.
     */
    PLAYER_JOIN("join"),

    /**
     * Executes when a player leaves the house.
     */
    PLAYER_QUIT("quit"),

    /**
     * Executes when a player dies.
     */
    PLAYER_DEATH("death"),

    /**
     * Executes for a player when they kill another player.
     */
    PLAYER_KILL("kill"),

    /**
     * Executes when a player respawns.
     */
    PLAYER_RESPAWN("respawn"),

    /**
     * Executes when a players group changes.
     */
    GROUP_CHANGE("groupChange"),

    /**
     * Executes when a player has their PvP state change, such as changing between regions.
     */
    PVP_STATE_CHANGE("pvpStateChange"),

    /**
     * Executes when a player catches a fish while fishing.
     */
    FISH_CAUGHT("fishCaught"),

    /**
     * Executes when a player uses a nether or end portal.
     */
    PLAYER_ENTER_PORTAL("enterPortal"),

    /**
     * Executes when a player takes damage.
     */
    PLAYER_DAMAGE("damage"),

    /**
     * Executes when a player attempts to break a block in the plot.
     * <p>
     * This event does not run if a player successfully breaks a block due to build permissions.
     */
    PLAYER_BLOCK_BREAK("blockBreak"),

    /**
     * Executes when a player starts doing parkour.
     */
    START_PARKOUR("startParkour"),

    /**
     * Executes when a player successfully completes the parkour.
     */
    COMPLETE_PARKOUR("completeParkour"),

    /**
     * Executes when a player drops an item.
     */
    PLAYER_DROP_ITEM("dropItem"),

    /**
     * Executes when a player picks up an item.
     */
    PLAYER_PICK_UP_ITEM("pickUpItem"),

    /**
     * Executes when a player changes their held item slot.
     */
    PLAYER_CHANGE_HELD_ITEM("changeHeldItem"),

    /**
     * Executes when a player toggles sneak.
     */
    PLAYER_TOGGLE_SNEAK("toggleSneak"),

    /**
     * Executes when a player toggles flight.
     */
    PLAYER_TOGGLE_FLIGHT("toggleFlight");

    private final @NotNull String format;
}
