package org.hsl.compiler.parser.impl.action;

import lombok.experimental.UtilityClass;
import org.hsl.export.condition.Condition;
import org.hsl.export.condition.impl.*;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class ConditionCodec {
    public @NotNull Condition hasGroup(@NotNull ActionArgs args) {
        return new RequiredGroup(args.getString("group"), args.getBoolean("higher"));
    }

    public @NotNull Condition compareVariable(@NotNull ActionArgs args) {
        return new VariableRequirement(
            args.getNamespace("namespace"),
            args.getString("variable"),
            args.getComparator("comparator"),
            args.getString("value"),
            args.getNullableString("fallback")
        );
    }

    public @NotNull Condition hasPermission(@NotNull ActionArgs args) {
        return new RequiredPermission(args.getPermission("permission"));
    }

    public @NotNull Condition withinRegion(@NotNull ActionArgs args) {
        return new WithinRegion(args.getString("region"));
    }

    public @NotNull Condition hasItem(@NotNull ActionArgs args) {
        return new HasItem(
            args.getMaterial("item"),
            args.getItemComparator("comparator"),
            args.getComparatorTarget("target"),
            args.getComparatorAmount("amount")
        );
    }

    public @NotNull Condition doingParkour(@NotNull ActionArgs args) {
        return new DoingParkour();
    }

    public @NotNull Condition hasEffect(@NotNull ActionArgs args) {
        return new HasPotionEffect(args.getEffect("effect"));
    }

    public @NotNull Condition isSneaking(@NotNull ActionArgs args) {
        return new PlayerSneaking();
    }

    public @NotNull Condition isFlying(@NotNull ActionArgs args) {
        return new PlayerFlying();
    }

    public @NotNull Condition hasHealth(@NotNull ActionArgs args) {
        return new PlayerHealth(
            args.getComparator("comparator"),
            args.getString("health")
        );
    }

    public @NotNull Condition hasMaxHealth(@NotNull ActionArgs args) {
        return new MaxPlayerHealth(
            args.getComparator("comparator"),
            args.getString("health")
        );
    }

    public @NotNull Condition hasHunger(@NotNull ActionArgs args) {
        return new PlayerHunger(
            args.getComparator("comparator"),
            args.getString("health")
        );
    }

    public @NotNull Condition hasGameMode(@NotNull ActionArgs args) {
        return new RequiredGameMode(args.getGameMode("gameMode"));
    }

    public @NotNull Condition comparePlaceholder(@NotNull ActionArgs args) {
        return new PlaceholderNumberRequirement(
            args.getString("placeholder"),
            args.getComparator("comparator"),
            args.getString("value")
        );
    }

    public @NotNull Condition hasTeam(@NotNull ActionArgs args) {
        return new RequiredTeam(args.getString("team"));
    }
}
