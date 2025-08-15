package org.hsl.compiler.parser.impl.action;

import lombok.experimental.UtilityClass;
import org.hsl.export.action.Action;
import org.hsl.export.action.impl.*;
import org.hsl.std.type.Target;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class ActionCodec {
    // TODO conditional

    public @NotNull Action setGroup(@NotNull ArgAccess args) {
        return new ChangePlayerGroup(args.getString("group"), args.getBoolean("protection"));
    }

    public @NotNull Action kill(@NotNull ArgAccess args) {
        return new KillPlayer();
    }

    public @NotNull Action heal(@NotNull ArgAccess args) {
        return new FullHealth();
    }

    public @NotNull Action title(@NotNull ArgAccess args) {
        return new DisplayTitle(
            args.getString("title"),
            args.getString("subtitle"),
            args.getInt("fadein"),
            args.getInt("stay"),
            args.getInt("fadeout")
        );
    }

    public @NotNull Action actionbar(@NotNull ArgAccess args) {
        return new DisplayActionBar(args.getString("message"));
    }

    public @NotNull Action resetInventory(@NotNull ArgAccess args) {
        return new ResetInventory();
    }

    public @NotNull Action changeMaxHealth(@NotNull ArgAccess args) {
        return new ChangeMaxHealth(
            args.getInt("maxHealth"),
            args.getMode("mode"),
            args.getBoolean("healOnChange")
        );
    }

    public @NotNull Action parkourCheckpoint(@NotNull ArgAccess args) {
        return new ParkourCheckpoint();
    }

    public @NotNull Action giveItem(@NotNull ArgAccess args) {
        return new GiveItem(
            args.getMaterial("item"),
            args.getBoolean("allowMultiple"),
            args.getSlot("slot"),
            args.getBoolean("replace")
        );
    }

    public @NotNull Action removeItem(@NotNull ArgAccess args) {
        return new RemoveItem(args.getMaterial("item"));
    }

    public @NotNull Action chat(@NotNull ArgAccess args) {
        return new SendChatMessage(args.getString("message"));
    }

    public @NotNull Action addEffect(@NotNull ArgAccess args) {
        return new ApplyPotionEffect(
            args.getEffect("effect"),
            args.getInt("duration"),
            args.getInt("level"),
            args.getBoolean("override"),
            args.getBoolean("showIcon")
        );
    }

    public @NotNull Action clearEffects(@NotNull ArgAccess args) {
        return new ClearAllPotionEffects();
    }

    public @NotNull Action addExperience(@NotNull ArgAccess args) {
        return new GiveExperienceLevels(args.getInt("levels"));
    }

    public @NotNull Action sendToLobby(@NotNull ArgAccess args) {
        return new SendToLobby(args.getLobby("lobby"));
    }

    public @NotNull Action changeVariable(@NotNull ArgAccess args) {
        return new ChangeVariable(
            args.getNamespace("namespace"),
            args.getString("variable"),
            args.getMode("mode"),
            args.getString("value"),
            args.getBoolean("autoUnset")
        );
    }

    public @NotNull Action teleport(@NotNull ArgAccess args) {
        return new TeleportPlayer(args.getLocation("location"), args.getBoolean("prevent"));
    }

    public @NotNull Action failParkour(@NotNull ArgAccess args) {
        return new FailParkour(args.getString("reason"));
    }

    public @NotNull Action playSound(@NotNull ArgAccess args) {
        return new PlaySound(
            args.getSound("sound"),
            args.getFloat("volume"),
            args.getFloat("pitch"),
            args.getLocation("location")
        );
    }

    public @NotNull Action setCompassTarget(@NotNull ArgAccess args) {
        return new SetCompassTarget(args.getLocation("location"));
    }

    public @NotNull Action setGameMode(@NotNull ArgAccess args) {
        return new SetGameMode(args.getGameMode("gameMode"));
    }

    public @NotNull Action changeHealth(@NotNull ArgAccess args) {
        return new ChangeHealth(args.getInt("health"), args.getMode("mode"));
    }

    public @NotNull Action changeHunger(@NotNull ArgAccess args) {
        return new ChangeHungerLevel(args.getInt("hunger"), args.getMode("mode"));
    }

    // TODO random action

    public @NotNull Action triggerFunction(@NotNull ArgAccess args) {
        return new TriggerFunction(
            args.getString("function"),
            args.getTarget("target") == Target.ALL_PLAYERS
        );
    }

    public @NotNull Action applyInventoryLayout(@NotNull ArgAccess args) {
        return new ApplyInventoryLayout(args.getString("layout"));
    }

    public @NotNull Action enchantHeldItem(@NotNull ArgAccess args) {
        return new EnchantHeldItem(args.getEnchant("enchant"), args.getInt("level"));
    }

    public @NotNull Action sleep(@NotNull ArgAccess args) {
        return new PauseExecution(args.getInt("ticks"));
    }

    public @NotNull Action setTeam(@NotNull ArgAccess args) {
        return new SetPlayerTeam(args.getString("team"));
    }

    public @NotNull Action dropItem(@NotNull ArgAccess args) {
        return new DropItem(
            args.getMaterial("item"),
            args.getLocation("location"),
            args.getBoolean("naturally"),
            args.getBoolean("disableMerging"),
            args.getBoolean("prioritizePlayers"),
            args.getBoolean("fallbackToInventory")
        );
    }

    public @NotNull Action changeVelocity(@NotNull ArgAccess args) {
        return new ChangeVelocity(args.getVector("velocity"));
    }

    public @NotNull Action launchToTarget(@NotNull ArgAccess args) {
        return new LaunchToTarget(args.getLocation("location"), args.getInt("strength"));
    }

    public @NotNull Action setPlayerWeather(@NotNull ArgAccess args) {
        return new SetPlayerWeather(args.getWeather("weather"));
    }

    public @NotNull Action setPlayerTime(@NotNull ArgAccess args) {
        return new SetPlayerTime(args.getTime("time"));
    }

    public @NotNull Action toggleNameTagDisplay(@NotNull ArgAccess args) {
        return new ToggleNameTagDisplay(args.getBoolean("shown"));
    }

    public @NotNull Action cancelEvent(ArgAccess args) {
        return new CancelEvent();
    }

    // TODO open/close menu
}
