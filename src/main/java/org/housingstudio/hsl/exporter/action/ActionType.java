package org.housingstudio.hsl.exporter.action;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.exporter.action.impl.*;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public enum ActionType {
    CONDITIONAL             (30, "Conditional",              Conditional.class),
    CHANGE_PLAYER_GROUP     (1,  "Change Player's Group",    ChangePlayerGroup.class),
    KILL_PLAYER             (1,  "Kill Player",              KillPlayer.class),
    FULL_HEALTH             (5,  "Full Heal",                FullHealth.class),
    DISPLAY_TITLE           (5,  "Display Title",            DisplayTitle.class),
    DISPLAY_ACTION_BAR      (5,  "Display Action Bar",       DisplayActionBar.class),
    RESET_INVENTORY         (1,  "Reset Inventory",          ResetInventory.class),
    CHANGE_MAX_HEALTH       (5,  "Change Max Health",        ChangeMaxHealth.class),
    PARKOUR_CHECKPOINT      (1,  "Parkour Checkpoint",       ParkourCheckpoint.class),
    GIVE_ITEM               (20, "Give Item",                GiveItem.class),
    REMOVE_ITEM             (20, "Remove Item",              RemoveItem.class),
    SEND_CHAT_MESSAGE       (20, "Send a Chat Message",      SendChatMessage.class),
    APPLY_POTION_EFFECT     (22, "Apply Potion Effect",      ApplyPotionEffect.class),
    CLEAR_ALL_POTION_EFFECTS(5,  "Clear All Potion Effects", ClearAllPotionEffects.class),
    GIVE_EXPERIENCE_LEVELS  (5,  "Give Experience Levels",   GiveExperienceLevels.class),
    SEND_TO_LOBBY           (1,  "Send to Lobby",            SendToLobby.class),
    CHANGE_VARIABLE         (25, "Change Variable",          ChangeVariable.class),
    TELEPORT_PLAYER         (5,  "Teleport Player",          TeleportPlayer.class),
    FAIL_PARKOUR            (1,  "Fail Parkour",             FailParkour.class),
    PLAY_SOUND              (25, "Play Sound",               PlaySound.class),
    SET_COMPASS_TARGET      (5,  "Set Compass Target",       SetCompassTarget.class),
    SET_GAME_MODE           (1,  "Set Gamemode",             SetGameMode.class),
    CHANGE_HEALTH           (5,  "Change Health",            ChangeHealth.class),
    CHANGE_HUNGER_LEVEL     (5,  "Change Hunger Level",      ChangeHungerLevel.class),
    RANDOM_ACTION           (5,  "Random Action",            RandomAction.class),
    TRIGGER_FUNCTION        (10, "Trigger Function",         TriggerFunction.class),
    APPLY_INVENTORY_LAYOUT  (5,  "Apply Inventory Layout",   ApplyInventoryLayout.class),
    ENCHANT_HELD_ITEM       (23, "Enchant Held Item",        EnchantHeldItem.class),
    PAUSE_EXECUTION         (30, "Pause Execution",          PauseExecution.class),
    SET_PLAYER_TEAM         (1,  "Set Player Team",          SetPlayerTeam.class),
    DISPLAY_MENU            (10, "Display Menu",             DisplayMenu.class),
    CLOSE_MENU              (1,  "TODO",                     CloseMenu.class),
    DROP_ITEM               (5,  "Drop Item",                DropItem.class),
    CHANGE_VELOCITY         (5,  "Change Velocity",          ChangeVelocity.class),
    LAUNCH_TO_TARGET        (5,  "Launch to Target",         LaunchToTarget.class),
    SET_PLAYER_WEATHER      (5,  "Set Player Weather",       SetPlayerWeather.class),
    SET_PLAYER_TIME         (5,  "Set Player Time",          SetPlayerTime.class),
    TOGGLE_NAME_TAG_DISPLAY (5,  "Toggle Nametag Display",   ToggleNameTagDisplay.class),
    CANCEL_EVENT            (1,  "TODO",                     CancelEvent.class),
    EXIT                    (1,  "TODO",                     Exit.class);

    // USE_HELD_ITEM, BALANCE_PLAYER_TEAM,

    private final int limit;
    private final @NotNull String itemName;
    private final Class<? extends Action> wrapper;
}
