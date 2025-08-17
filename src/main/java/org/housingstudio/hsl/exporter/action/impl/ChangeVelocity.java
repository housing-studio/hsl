package org.housingstudio.hsl.exporter.action.impl;

import com.google.gson.annotations.JsonAdapter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.exporter.action.Action;
import org.housingstudio.hsl.exporter.action.ActionType;
import org.housingstudio.hsl.exporter.adapter.VectorAdapter;
import org.housingstudio.hsl.type.Vector;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
@ToString
public class ChangeVelocity implements Action {
    private final ActionType type = ActionType.CHANGE_VELOCITY;

    @JsonAdapter(VectorAdapter.class)
    private @NotNull Vector velocity;
}
