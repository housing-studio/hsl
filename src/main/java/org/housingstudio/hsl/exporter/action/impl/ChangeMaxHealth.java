package org.housingstudio.hsl.exporter.action.impl;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.exporter.action.Action;
import org.housingstudio.hsl.exporter.action.ActionType;
import org.housingstudio.hsl.type.Mode;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
@ToString
public class ChangeMaxHealth implements Action {
    private final ActionType type = ActionType.CHANGE_MAX_HEALTH;

    @SerializedName("max-health")
    private int maxHealth;

    private Mode mode;

    @SerializedName("heal-on-change")
    private boolean healOnChange;
}
