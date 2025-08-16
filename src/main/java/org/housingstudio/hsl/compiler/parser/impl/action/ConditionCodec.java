package org.housingstudio.hsl.compiler.parser.impl.action;

import lombok.experimental.UtilityClass;
import org.housingstudio.hsl.exporter.condition.Condition;
import org.housingstudio.hsl.exporter.condition.impl.*;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class ConditionCodec {
    public @NotNull Condition hasGroup(@NotNull ArgAccess args) {
        return new RequiredGroup(
            args.getBoolean("inverted"),
            args.getString("group"),
            args.getBoolean("higher")
        );
    }

    public @NotNull Condition compareVariable(@NotNull ArgAccess args) {
        return new VariableRequirement(
            args.getBoolean("inverted"),
            args.getNamespace("namespace"),
            args.getString("variable"),
            args.getComparator("comparator"),
            args.getString("value"),
            args.getNullableString("fallback")
        );
    }

    public @NotNull Condition hasPermission(@NotNull ArgAccess args) {
        return new RequiredPermission(
            args.getBoolean("inverted"),
            args.getPermission("permission")
        );
    }

    public @NotNull Condition withinRegion(@NotNull ArgAccess args) {
        return new WithinRegion(
            args.getBoolean("inverted"),
            args.getString("region")
        );
    }

    public @NotNull Condition hasItem(@NotNull ArgAccess args) {
        return new HasItem(
            args.getBoolean("inverted"),
            args.getMaterial("item"),
            args.getItemComparator("comparator"),
            args.getComparatorTarget("target"),
            args.getComparatorAmount("amount")
        );
    }

    public @NotNull Condition doingParkour(@NotNull ArgAccess args) {
        return new DoingParkour(args.getBoolean("inverted"));
    }

    public @NotNull Condition hasEffect(@NotNull ArgAccess args) {
        return new HasPotionEffect(
            args.getBoolean("inverted"),
            args.getEffect("effect")
        );
    }

    public @NotNull Condition isSneaking(@NotNull ArgAccess args) {
        return new PlayerSneaking(args.getBoolean("inverted"));
    }

    public @NotNull Condition isFlying(@NotNull ArgAccess args) {
        return new PlayerFlying(args.getBoolean("inverted"));
    }

    public @NotNull Condition hasHealth(@NotNull ArgAccess args) {
        return new PlayerHealth(
            args.getBoolean("inverted"),
            args.getComparator("comparator"),
            args.getString("health")
        );
    }

    public @NotNull Condition hasMaxHealth(@NotNull ArgAccess args) {
        return new MaxPlayerHealth(
            args.getBoolean("inverted"),
            args.getComparator("comparator"),
            args.getString("health")
        );
    }

    public @NotNull Condition hasHunger(@NotNull ArgAccess args) {
        return new PlayerHunger(
            args.getBoolean("inverted"),
            args.getComparator("comparator"),
            args.getString("health")
        );
    }

    public @NotNull Condition hasGameMode(@NotNull ArgAccess args) {
        return new RequiredGameMode(
            args.getBoolean("inverted"),
            args.getGameMode("gameMode")
        );
    }

    public @NotNull Condition comparePlaceholder(@NotNull ArgAccess args) {
        return new PlaceholderNumberRequirement(
            args.getBoolean("inverted"),
            args.getString("placeholder"),
            args.getComparator("comparator"),
            args.getString("value")
        );
    }

    public @NotNull Condition hasTeam(@NotNull ArgAccess args) {
        return new RequiredTeam(
            args.getBoolean("inverted"),
            args.getString("team")
        );
    }
}
