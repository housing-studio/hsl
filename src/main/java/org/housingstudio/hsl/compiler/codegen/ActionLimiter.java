package org.housingstudio.hsl.compiler.codegen;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Method;
import org.housingstudio.hsl.compiler.codegen.impl.action.Action;
import org.housingstudio.hsl.compiler.codegen.impl.action.ActionType;
import org.housingstudio.hsl.compiler.error.Errno;
import org.housingstudio.hsl.compiler.error.Notification;
import org.housingstudio.hsl.compiler.parser.ParserContext;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class ActionLimiter {
    private final @NotNull ParserContext context;
    private final @NotNull Method function;
    private final @NotNull Map<ActionType, Integer> actions;

    public void validate() {
        for (Map.Entry<ActionType, Integer> entry : actions.entrySet()) {
            ActionType type = entry.getKey();
            if (entry.getValue() > type.limit()) {
                context.errorPrinter().print(
                    Notification.error(
                        Errno.TOO_MANY_ACTIONS, "Function `" + function.name().value() + "` exceeded action limit"
                    )
                    .error(
                        "Action `" + type.functionName() + "` exceeded limit of " + type.limit(), function.name()
                    )
                );
            }
        }
    }

    public static @NonNull ActionLimiter from(
        @NotNull ParserContext context, @NotNull Method function, @NotNull List<Action> actions
    ) {
        Map<ActionType, Integer> map = new HashMap<>();
        for (Action action : actions)
            map.put(action.type(), map.getOrDefault(action.type(), 0) + 1);
        return new ActionLimiter(context, function, map);
    }
}
