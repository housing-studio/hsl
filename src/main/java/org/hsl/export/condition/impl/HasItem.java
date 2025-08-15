package org.hsl.export.condition.impl;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.export.condition.Condition;
import org.hsl.export.condition.ConditionType;
import org.hsl.std.type.ComparatorAmount;
import org.hsl.std.type.ComparatorTarget;
import org.hsl.std.type.ItemComparator;
import org.hsl.std.type.Material;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
public class HasItem extends Condition {
    private final ConditionType type = ConditionType.HAS_ITEM;

    private @NotNull Material material;

    @SerializedName("what-to-check")
    private @NotNull ItemComparator whatToCheck;

    @SerializedName("where-to-check")
    private @NotNull ComparatorTarget whereToCheck;

    @SerializedName("required-amount")
    private @NotNull ComparatorAmount requiredAmount;
}
