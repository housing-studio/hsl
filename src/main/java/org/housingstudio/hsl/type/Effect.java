package org.housingstudio.hsl.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public enum Effect {
    SLOWNESS("Slowness"),
    HASTE("Haste"),
    MINING_FATIGUE("MiningFatigue"),
    STRENGTH("Strength"),
    INSTANT_HEALTH("InstantHealth"),
    INSTANT_DAMAGE("InstantDamage"),
    JUMP_BOOST("JumpBoost"),
    NAUSEA("Nausea"),
    REGENERATION("Regeneration"),
    RESISTANCE("Resistance"),
    FIRE_RESISTANCE("FireResistance"),
    WATER_BREATHING("WaterBreathing"),
    INVISIBILITY("Invisibility"),
    BLINDNESS("Blindness"),
    NIGHT_VISION("NightVision"),
    HUNGER("Hunger"),
    WEAKNESS("Weakness"),
    POISON("Poison"),
    WITHER("Wither"),
    HEALTH_BOOST("HealthBoost"),
    ABSORPTION("Absorption");

    private final @NotNull String format;
}
