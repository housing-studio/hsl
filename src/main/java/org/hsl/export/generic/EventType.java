package org.hsl.export.generic;

public enum EventType {
    /**
     * Executes when a player joins the house.
     */
    PLAYER_JOIN,

    /**
     * Executes when a player leaves the house.
     */
    PLAYER_QUIT,

    /**
     * Executes when a player dies.
     */
    PLAYER_DEATH,

    /**
     * Executes for a player when they kill another player.
     */
    PLAYER_KILL,

    /**
     * Executes when a player respawns.
     */
    PLAYER_RESPAWN,

    /**
     * Executes when a players group changes.
     */
    GROUP_CHANGE,

    /**
     * Executes when a player has their PvP state change, such as changing between regions.
     */
    PVP_STATE_CHANGE,

    /**
     * Executes when a player catches a fish while fishing.
     */
    FISH_CAUGHT,

    /**
     * Executes when a player uses a nether or end portal.
     */
    PLAYER_ENTER_PORTAL,

    /**
     * Executes when a player takes damage.
     */
    PLAYER_DAMAGE,

    /**
     * Executes when a player attempts to break a block in the plot.
     * <p>
     * This event does not run if a player successfully breaks a block due to build permissions.
     */
    PLAYER_BLOCK_BREAK,

    /**
     * Executes when a player starts doing parkour
     */
    START_PARKOUR,

    /**
     * Executes when a player successfully completes the parkour.
     */
    COMPLETE_PARKOUR,

    /**
     * Executes when a player drops an item.
     */
    PLAYER_DROP_ITEM,

    /**
     * Executes when a player picks up an item.
     */
    PLAYER_PICK_UP_ITEM,

    /**
     * Executes when a player changes their held item slot.
     */
    PLAYER_CHANGE_HELD_ITEM,

    /**
     * Executes when a player toggles sneak.
     */
    PLAYER_TOGGLE_SNEAK,

    /**
     * Executes when a player toggles flight.
     */
    PLAYER_TOGGLE_FLIGHT
}
