package org.housingstudio.hsl.std;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public enum Permission {
    FLY("Fly"),
    WOOD_DOOR("WoodDoor"),
    IRON_DOOR("IronDoor"),
    WOOD_TRAP_DOOR("WoodTrapDoor"),
    IRON_TRAP_DOOR("IronTrapDoor"),
    FENCE_GATE("FenceGate"),
    BUTTON("Button"),
    LEVER("Lever"),
    USE_LAUNCH_PADS("UseLaunchPads"),
    TELEPORT("Teleport"),
    TELEPORT_OTHERS("TeleportOthers"),
    JUKEBOX("Jukebox"),
    KICK("Kick"),
    BAN("Ban"),
    MUTE("Mute"),
    PET_SPAWNING("PetSpawning"),
    BUILD("Build"),
    OFFLINE_BUILD("OfflineBuild"),
    FLUID("Fluid"),
    PRO_TOOLS("ProTools"),
    USE_CHESTS("UseChests"),
    USE_ENDER_CHEST("UseEnderChests"),
    ITEM_EDITOR("ItemEditor"),
    SWITCH_GAME_MODE("SwitchGameMode"),
    EDIT_VARIABLES("EditVariables"),
    CHANGE_PLAYER_GROUP("ChangePlayerGroup"),
    CHANGE_GAME_RULES("ChangeGameRules"),
    HOUSING_MENU("HousingMenu"),
    TEAM_CHAT_SPY("TeamChatSpy"),
    EDIT_ACTIONS("EditActions"),
    EDIT_REGIONS("EditRegions"),
    EDIT_SCOREBOARD("EditScoreboard"),
    EDIT_EVENT_ACTIONS("EditEventActions"),
    EDIT_COMMANDS("EditCommands"),
    EDIT_FUNCTIONS("EditFunctions"),
    EDIT_INVENTORY_LAYOUTS("EditInventoryLayouts"),
    EDIT_TEAMS("EditTeams"),
    EDIT_CUSTOM_MENUS("EditCustomMenus"),
    VIEW_ANALYTICS("ViewAnalytics"),
    ITEM_MAILBOX("ItemMailbox"),
    ITEM_EGG_HUNT("ItemEggHunt"),
    ITEM_TELEPORT_PAD("ItemTeleportPad"),
    ITEM_LAUNCH_PAD("ItemLaunchPad"),
    ITEM_ACTION_PAD("ItemActionPad"),
    ITEM_HOLOGRAM("ItemHologram"),
    ITEM_NPC("ItemNPC"),
    ITEM_ACTION_BUTTON("ItemActionButton"),
    ITEM_LEADERBOARD("ItemLeaderboard"),
    ITEM_TRASH_CAN("ItemTrashCan"),
    ITEM_BIOME_STICK("ItemBiomeStick");

    private final @NotNull String format;
}
