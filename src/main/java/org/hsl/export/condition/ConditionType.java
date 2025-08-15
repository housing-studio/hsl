package org.hsl.export.condition;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.export.condition.impl.*;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public enum ConditionType {
    REQUIRED_GROUP                (20, RequiredGroup.class),
    VARIABLE_REQUIREMENT          (20, VariableRequirement.class),
    REQUIRED_PERMISSION           (20, RequiredPermission.class),
    WITHIN_REGION                 (20, WithinRegion.class),
    HAS_ITEM                      (20, HasItem.class),
    DOING_PARKOUR                 (1,  DoingParkour.class),
    HAS_POTION_EFFECT             (21, HasPotionEffect.class),
    PLAYER_SNEAKING               (20, PlayerSneaking.class),
    PLAYER_FLYING                 (20, PlayerFlying.class),
    PLAYER_HEALTH                 (20, PlayerHealth.class),
    MAX_PLAYER_HEALTH             (20, MaxPlayerHealth.class),
    PLAYER_HUNGER                 (20, PlayerHunger.class),
    REQUIRED_GAME_MODE            (20, RequiredGameMode.class),
    PLACEHOLDER_NUMBER_REQUIREMENT(20, PlaceholderNumberRequirement.class),
    REQUIRED_TEAM                 (20, RequiredTeam.class),;

    private final int limit;
    private final Class<? extends Condition> wrapper;
}
