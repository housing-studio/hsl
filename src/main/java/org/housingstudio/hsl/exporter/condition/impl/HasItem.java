package org.housingstudio.hsl.exporter.condition.impl;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.exporter.condition.Condition;
import org.housingstudio.hsl.exporter.condition.ConditionType;
import org.housingstudio.hsl.type.ComparatorAmount;
import org.housingstudio.hsl.type.ComparatorTarget;
import org.housingstudio.hsl.type.ItemComparator;
import org.housingstudio.hsl.type.Material;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
public class HasItem implements Condition {
    private final ConditionType type = ConditionType.HAS_ITEM;

    private boolean inverted;

    private @NotNull Material material;

    @SerializedName("what-to-check")
    private @NotNull ItemComparator whatToCheck;

    @SerializedName("where-to-check")
    private @NotNull ComparatorTarget whereToCheck;

    @SerializedName("required-amount")
    private @NotNull ComparatorAmount requiredAmount;
}
