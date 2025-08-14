package org.hsl.compiler.parser.impl.action;

import lombok.experimental.UtilityClass;
import org.hsl.export.action.Action;
import org.hsl.export.action.impl.*;
import org.hsl.std.type.Target;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class ActionCodec {
    // TODO conditional

    public @NotNull Action setGroup(@NotNull ActionArgs args) {
        return new ChangePlayerGroup(args.getString("group"), args.getBoolean("protection"));
    }

    public @NotNull Action kill(@NotNull ActionArgs args) {
        return new KillPlayer();
    }

    public @NotNull Action heal(@NotNull ActionArgs args) {
        return new FullHealth();
    }

    public @NotNull Action title(@NotNull ActionArgs args) {
        return new DisplayTitle(
            args.getString("title"),
            args.getString("subtitle"),
            args.getInt("fadein"),
            args.getInt("stay"),
            args.getInt("fadeout")
        );
    }

    public @NotNull Action actionbar(@NotNull ActionArgs args) {
        return new DisplayActionBar(args.getString("message"));
    }

    public @NotNull Action resetInventory(@NotNull ActionArgs args) {
        return new ResetInventory();
    }

    public @NotNull Action changeMaxHealth(@NotNull ActionArgs args) {
        return new ChangeMaxHealth(
            args.getInt("maxHealth"),
            args.getMode("mode"),
            args.getBoolean("healOnChange")
        );
    }

    public @NotNull Action parkourCheckpoint(@NotNull ActionArgs args) {
        return new ParkourCheckpoint();
    }

    public @NotNull Action giveItem(@NotNull ActionArgs args) {
        return new GiveItem(
            args.getMaterial("item"),
            args.getBoolean("allowMultiple"),
            args.getSlot("slot"),
            args.getBoolean("replace")
        );
    }

    public @NotNull Action removeItem(@NotNull ActionArgs args) {
        return new RemoveItem(args.getMaterial("item"));
    }

    public @NotNull Action chat(@NotNull ActionArgs args) {
        return new SendChatMessage(args.getString("message"));
    }

    public @NotNull Action addEffect(@NotNull ActionArgs args) {
        return new ApplyPotionEffect(
            args.getEffect("effect"),
            args.getInt("duration"),
            args.getInt("level"),
            args.getBoolean("override"),
            args.getBoolean("showIcon")
        );
    }

    public @NotNull Action clearEffects(@NotNull ActionArgs args) {
        return new ClearAllPotionEffects();
    }

    public @NotNull Action addExperience(@NotNull ActionArgs args) {
        return new GiveExperienceLevels(args.getInt("levels"));
    }

    public @NotNull Action sendToLobby(@NotNull ActionArgs args) {
        return new SendToLobby(args.getLobby("lobby"));
    }

    public @NotNull Action changeVariable(@NotNull ActionArgs args) {
        return new ChangeVariable(
            args.getNamespace("namespace"),
            args.getString("variable"),
            args.getMode("mode"),
            args.getString("value"),
            args.getBoolean("autoUnset")
        );
    }

    public @NotNull Action teleport(@NotNull ActionArgs args) {
        return new TeleportPlayer(args.getLocation("location"), args.getBoolean("prevent"));
    }

    public @NotNull Action failParkour(@NotNull ActionArgs args) {
        return new FailParkour(args.getString("reason"));
    }

    public @NotNull Action playSound(@NotNull ActionArgs args) {
        return new PlaySound(
            args.getSound("sound"),
            args.getFloat("volume"),
            args.getFloat("pitch"),
            args.getLocation("location")
        );
    }

    public @NotNull Action setCompassTarget(@NotNull ActionArgs args) {
        return new SetCompassTarget(args.getLocation("location"));
    }

    public @NotNull Action setGameMode(@NotNull ActionArgs args) {
        return new SetGameMode(args.getGameMode("gameMode"));
    }

    public @NotNull Action changeHealth(@NotNull ActionArgs args) {
        return new ChangeHealth(args.getInt("health"), args.getMode("mode"));
    }

    public @NotNull Action changeHunger(@NotNull ActionArgs args) {
        return new ChangeHungerLevel(args.getInt("hunger"), args.getMode("mode"));
    }

    // TODO random action

    public @NotNull Action triggerFunction(@NotNull ActionArgs args) {
        return new TriggerFunction(
            args.getString("function"),
            args.getTarget("target") == Target.ALL_PLAYERS
        );
    }

    public @NotNull Action applyInventoryLayout(@NotNull ActionArgs args) {
        return new ApplyInventoryLayout(args.getString("layout"));
    }

    public @NotNull Action enchantHeldItem(@NotNull ActionArgs args) {
        return new EnchantHeldItem(args.getEnchant("enchant"), args.getInt("level"));
    }

    public @NotNull Action sleep(@NotNull ActionArgs args) {
        return new PauseExecution(args.getInt("ticks"));
    }

    public @NotNull Action setTeam(@NotNull ActionArgs args) {
        return new SetPlayerTeam(args.getString("team"));
    }

    public @NotNull Action dropItem(@NotNull ActionArgs args) {
        return new DropItem(
            args.getMaterial("item"),
            args.getLocation("location"),
            args.getBoolean("naturally"),
            args.getBoolean("disableMerging"),
            args.getBoolean("prioritizePlayers"),
            args.getBoolean("fallbackToInventory")
        );
    }

    public @NotNull Action changeVelocity(@NotNull ActionArgs args) {
        return new ChangeVelocity(args.getVector("velocity"));
    }

    public @NotNull Action launchToTarget(@NotNull ActionArgs args) {
        return new LaunchToTarget(args.getLocation("location"), args.getInt("strength"));
    }

    public @NotNull Action setPlayerWeather(@NotNull ActionArgs args) {
        return new SetPlayerWeather(args.getWeather("weather"));
    }

    public @NotNull Action setPlayerTime(@NotNull ActionArgs args) {
        return new SetPlayerTime(args.getTime("time"));
    }

    public @NotNull Action toggleNameTagDisplay(@NotNull ActionArgs args) {
        return new ToggleNameTagDisplay(args.getBoolean("shown"));
    }

    public @NotNull Action cancelEvent(ActionArgs args) {
        return new CancelEvent();
    }

    // TODO open/close menu
}
