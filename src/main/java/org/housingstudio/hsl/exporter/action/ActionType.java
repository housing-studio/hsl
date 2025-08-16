package org.housingstudio.hsl.exporter.action;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.exporter.action.impl.*;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public enum ActionType {
    CONDITIONAL             (30, Conditional.class),
    CHANGE_PLAYER_GROUP     (1,  ChangePlayerGroup.class),
    KILL_PLAYER             (1,  KillPlayer.class),
    FULL_HEALTH             (5,  FullHealth.class),
    DISPLAY_TITLE           (5,  DisplayTitle.class),
    DISPLAY_ACTION_BAR      (5,  DisplayActionBar.class),
    RESET_INVENTORY         (1,  ResetInventory.class),
    CHANGE_MAX_HEALTH       (5,  ChangeMaxHealth.class),
    PARKOUR_CHECKPOINT      (1,  ParkourCheckpoint.class),
    GIVE_ITEM               (20, GiveItem.class),
    REMOVE_ITEM             (20, RemoveItem.class),
    SEND_CHAT_MESSAGE       (20, SendChatMessage.class),
    APPLY_POTION_EFFECT     (22, ApplyPotionEffect.class),
    CLEAR_ALL_POTION_EFFECTS(5,  ClearAllPotionEffects.class),
    GIVE_EXPERIENCE_LEVELS  (5,  GiveExperienceLevels.class),
    SEND_TO_LOBBY           (1,  SendToLobby.class),
    CHANGE_VARIABLE         (25, ChangeVariable.class),
    TELEPORT_PLAYER         (5,  TeleportPlayer.class),
    FAIL_PARKOUR            (1,  FailParkour.class),
    PLAY_SOUND              (25, PlaySound.class),
    SET_COMPASS_TARGET      (5,  SetCompassTarget.class),
    SET_GAME_MODE           (1,  SetGameMode.class),
    CHANGE_HEALTH           (5,  ChangeHealth.class),
    CHANGE_HUNGER_LEVEL     (5,  ChangeHungerLevel.class),
    RANDOM_ACTION           (5,  RandomAction.class),
    TRIGGER_FUNCTION        (10, TriggerFunction.class),
    APPLY_INVENTORY_LAYOUT  (5,  ApplyInventoryLayout.class),
    ENCHANT_HELD_ITEM       (23, EnchantHeldItem.class),
    PAUSE_EXECUTION         (30, PauseExecution.class),
    SET_PLAYER_TEAM         (1,  SetPlayerTeam.class),
    DISPLAY_MENU            (10, DisplayMenu.class),
    CLOSE_MENU              (1,  CloseMenu.class),
    DROP_ITEM               (5,  DropItem.class),
    CHANGE_VELOCITY         (5,  ChangeVelocity.class),
    LAUNCH_TO_TARGET        (5,  LaunchToTarget.class),
    SET_PLAYER_WEATHER      (5,  SetPlayerWeather.class),
    SET_PLAYER_TIME         (5,  SetPlayerTime.class),
    TOGGLE_NAME_TAG_DISPLAY (5,  ToggleNameTagDisplay.class),
    CANCEL_EVENT            (1,  CancelEvent.class),
    EXIT                    (1,  Exit.class);

    // USE_HELD_ITEM, BALANCE_PLAYER_TEAM,

    private final int limit;
    private final Class<? extends Action> wrapper;
}
