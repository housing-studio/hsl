package org.housingstudio.hsl.std;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public enum Enchant {
    PROTECTION("Protection"),
    FIRE_PROTECTION("FireProtection"),
    FEATHER_FALLING("FeatherFalling"),
    BLAST_PROTECTION("BlastProtection"),
    PROJECTILE_PROTECTION("ProjectileProtection"),
    RESPIRATION("Respiration"),
    AQUA_AFFINITY("AquaAffinity"),
    THORNS("Thorns"),
    DEPTH_STRIDER("DepthStrider"),
    SHARPNESS("Sharpness"),
    SMITE("Smite"),
    BANE_OF_ARTHROPODS("BaneOfArthropods"),
    KNOCKBACK("Knockback"),
    FIRE_ASPECT("FireAspect"),
    LOOTING("Looting"),
    EFFICIENCY("Efficiency"),
    SILK_TOUCH("SilkTouch"),
    UNBREAKING("Unbreaking"),
    FORTUNE("Fortune"),
    POWER("Power"),
    PUNCH("Punch"),
    FLAME("Flame"),
    INFINITY("Infinity"),;

    private final @NotNull String format;
}
