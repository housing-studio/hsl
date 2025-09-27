package org.housingstudio.hsl.compiler.codegen.impl.action;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.codegen.impl.action.impl.*;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public enum ActionType {
    CONDITIONAL             (25, "Conditional",              "TODO",                 Conditional.class),
    CHANGE_PLAYER_GROUP     (1,  "Change Player's Group",    "setGroup",             ChangePlayerGroup.class),
    KILL_PLAYER             (1,  "Kill Player",              "kill",                 KillPlayer.class),
    FULL_HEALTH             (5,  "Full Heal",                "heal",                 FullHealth.class),
    DISPLAY_TITLE           (5,  "Display Title",            "title",                DisplayTitle.class),
    DISPLAY_ACTION_BAR      (5,  "Display Action Bar",       "actionbar",            DisplayActionBar.class),
    RESET_INVENTORY         (1,  "Reset Inventory",          "resetInventory",       ResetInventory.class),
    CHANGE_MAX_HEALTH       (5,  "Change Max Health",        "changeMaxHealth",      ChangeMaxHealth.class),
    PARKOUR_CHECKPOINT      (1,  "Parkour Checkpoint",       "parkourCheckpoint",    ParkourCheckpoint.class),
    GIVE_ITEM               (40, "Give Item",                "giveItem",             GiveItem.class),
    REMOVE_ITEM             (40, "Remove Item",              "removeItem",           RemoveItem.class),
    SEND_CHAT_MESSAGE       (20, "Send a Chat Message",      "chat",                 SendChatMessage.class),
    APPLY_POTION_EFFECT     (22, "Apply Potion Effect",      "addEffect",            ApplyPotionEffect.class),
    CLEAR_ALL_POTION_EFFECTS(5,  "Clear All Potion Effects", "clearEffects",         ClearAllPotionEffects.class),
    GIVE_EXPERIENCE_LEVELS  (5,  "Give Experience Levels",   "addExperience",        GiveExperienceLevels.class),
    SEND_TO_LOBBY           (1,  "Send to Lobby",            "sendToLobby",          SendToLobby.class),
    CHANGE_VARIABLE         (25, "Change Variable",          "changeVariable",       ChangeVariable.class),
    TELEPORT_PLAYER         (5,  "Teleport Player",          "teleport",             TeleportPlayer.class),
    FAIL_PARKOUR            (1,  "Fail Parkour",             "failParkour",          FailParkour.class),
    PLAY_SOUND              (25, "Play Sound",               "playSound",            PlaySound.class),
    SET_COMPASS_TARGET      (5,  "Set Compass Target",       "setCompassTarget",     SetCompassTarget.class),
    SET_GAME_MODE           (1,  "Set Gamemode",             "setGameMode",          SetGameMode.class),
    CHANGE_HEALTH           (5,  "Change Health",            "changeHealth",         ChangeHealth.class),
    CHANGE_HUNGER_LEVEL     (5,  "Change Hunger Level",      "changeHunger",         ChangeHungerLevel.class),
    RANDOM_ACTION           (25, "Random Action",            "TODO",                 RandomAction.class),
    TRIGGER_FUNCTION        (10, "Trigger Function",         "triggerFunction",      TriggerFunction.class),
    APPLY_INVENTORY_LAYOUT  (5,  "Apply Inventory Layout",   "applyInventoryLayout", ApplyInventoryLayout.class),
    ENCHANT_HELD_ITEM       (23, "Enchant Held Item",        "enchantHeldItem",      EnchantHeldItem.class),
    PAUSE_EXECUTION         (30, "Pause Execution",          "sleep",                PauseExecution.class),
    SET_PLAYER_TEAM         (1,  "Set Player Team",          "setTeam",              SetPlayerTeam.class),
    DISPLAY_MENU            (10, "Display Menu",             "openMenu",             DisplayMenu.class),
    CLOSE_MENU              (1,  "TODO",                     "closeMenu",            CloseMenu.class),
    DROP_ITEM               (5,  "Drop Item",                "dropItem",             DropItem.class),
    CHANGE_VELOCITY         (5,  "Change Velocity",          "changeVelocity",       ChangeVelocity.class),
    LAUNCH_TO_TARGET        (5,  "Launch to Target",         "launchToTarget",       LaunchToTarget.class),
    SET_PLAYER_WEATHER      (5,  "Set Player Weather",       "setPlayerWeather",     SetPlayerWeather.class),
    SET_PLAYER_TIME         (5,  "Set Player Time",          "setPlayerTime",        SetPlayerTime.class),
    TOGGLE_NAME_TAG_DISPLAY (5,  "Toggle Nametag Display",   "toggleNameTagDisplay", ToggleNameTagDisplay.class),
    CANCEL_EVENT            (1,  "TODO",                     "cancelEvent",          CancelEvent.class),
    EXIT                    (1,  "TODO",                     "TODO",                 Exit.class);

    // USE_HELD_ITEM, BALANCE_PLAYER_TEAM,

    private final int limit;
    private final @NotNull String itemName;
    private final @NotNull String functionName;
    private final Class<? extends Action> wrapper;
}
