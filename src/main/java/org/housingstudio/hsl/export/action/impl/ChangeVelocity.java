package org.housingstudio.hsl.export.action.impl;

import com.google.gson.annotations.JsonAdapter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.export.action.Action;
import org.housingstudio.hsl.export.action.ActionType;
import org.housingstudio.hsl.export.adapter.VectorAdapter;
import org.housingstudio.hsl.type.Vector;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
public class ChangeVelocity implements Action {
    private final ActionType type = ActionType.CHANGE_VELOCITY;

    @JsonAdapter(VectorAdapter.class)
    private @NotNull Vector velocity;
}
