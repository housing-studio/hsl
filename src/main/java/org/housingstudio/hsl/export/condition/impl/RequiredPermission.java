package org.housingstudio.hsl.export.condition.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.export.condition.Condition;
import org.housingstudio.hsl.export.condition.ConditionType;
import org.housingstudio.hsl.type.Permission;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
public class RequiredPermission implements Condition {
    private final ConditionType type = ConditionType.REQUIRED_PERMISSION;

    private boolean inverted;

    private @NotNull Permission permission;
}
