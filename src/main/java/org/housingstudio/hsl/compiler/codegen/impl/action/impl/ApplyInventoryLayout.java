package org.housingstudio.hsl.compiler.codegen.impl.action.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.codegen.impl.action.Action;
import org.housingstudio.hsl.compiler.codegen.impl.action.ActionType;
import org.housingstudio.hsl.compiler.codegen.impl.htsl.HtslInvocation;
import org.housingstudio.hsl.compiler.codegen.impl.htsl.HTSL;
import org.housingstudio.hsl.importer.interaction.InteractionTarget;
import org.housingstudio.hsl.importer.interaction.InteractionType;
import org.housingstudio.hsl.importer.interaction.defaults.Required;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
@ToString
public class ApplyInventoryLayout implements Action {
    private final ActionType type = ActionType.APPLY_INVENTORY_LAYOUT;

    @InteractionTarget(type = InteractionType.DYNAMIC_OPTION, offset = 0)
    @Required
    private @NotNull String layout;

    /**
     * Retrieve the HTSL representation of this housing action.
     *
     * @return the htsl code that represents this action
     */
    @Override
    public @NotNull HtslInvocation asHTSL() {
        return HTSL.Action.APPLY_LAYOUT.invoke()
            .setString("layout", layout);
    }
}
