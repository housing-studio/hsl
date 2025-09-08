package org.housingstudio.hsl.std;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public enum Effect {
    SPEED("Speed", 1, 0),
    SLOWNESS("Slowness", 1, 1),
    HASTE("Haste", 1, 2),
    MINING_FATIGUE("MiningFatigue", 1, 3),
    STRENGTH("Strength", 1, 4),
    INSTANT_HEALTH("InstantHealth", 1, 5),
    INSTANT_DAMAGE("InstantDamage", 1, 6),
    JUMP_BOOST("JumpBoost", 1, 7),
    NAUSEA("Nausea", 1, 8),
    REGENERATION("Regeneration", 1, 9),
    RESISTANCE("Resistance", 1, 10),
    FIRE_RESISTANCE("FireResistance", 1, 11),
    WATER_BREATHING("WaterBreathing", 1, 12),
    INVISIBILITY("Invisibility", 1, 13),
    BLINDNESS("Blindness", 1, 14),
    NIGHT_VISION("NightVision", 1, 15),
    HUNGER("Hunger", 1, 16),
    WEAKNESS("Weakness", 1, 17),
    POISON("Poison", 1, 18),
    WITHER("Wither", 1, 19),
    HEALTH_BOOST("HealthBoost", 1, 20),
    ABSORPTION("Absorption", 2, 0);

    private final @NotNull String format;
    private final int page;
    private final int offset;
}
