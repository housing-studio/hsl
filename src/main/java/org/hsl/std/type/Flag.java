package org.hsl.std.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public enum Flag {
    PVP("Pvp"),
    DOUBLE_JUMP("DoubleJump"),
    FIRE_DAMAGE("FireDamage"),
    FALL_DAMAGE("FallDamage"),
    POISON_DAMAGE("PoisonDamage"),
    SUFFOCATION("Suffocation"),
    HUNGER("Hunger"),
    NATURAL_REGENERATION("NaturalRegeneration"),
    DEATH_MESSAGES("DeathMessages"),
    INSTANT_RESPAWN("InstantRespawn"),
    KEEP_INVENTORY("KeepInventory");

    private final @NotNull String format;
}
