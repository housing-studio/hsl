package org.housingstudio.hsl.compiler.codegen;

import lombok.RequiredArgsConstructor;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Method;
import org.housingstudio.hsl.compiler.codegen.impl.action.Action;
import org.housingstudio.hsl.compiler.codegen.impl.action.ActionType;
import org.housingstudio.hsl.compiler.codegen.impl.action.impl.Conditional;
import org.housingstudio.hsl.compiler.error.Errno;
import org.housingstudio.hsl.compiler.error.Notification;
import org.housingstudio.hsl.compiler.parser.ParserContext;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class ActionLimiter {
    private final @NotNull Map<ActionType, Integer> count = new HashMap<>();

    private final @NotNull ParserContext context;
    private final @NotNull Method function;
    private final @NotNull List<Action> actions;

    public void process() {
        for (Action action : actions) {
            count.put(action.type(), count.getOrDefault(action.type(), 0) + 1);

            ActionType type = action.type();
            int found = count.getOrDefault(type,0);

            if (found > type.limit()) {
                context.errorPrinter().print(
                    Notification
                        .error(Errno.TOO_MANY_ACTIONS, "Function `" + function.name().value() + "` exceeded action limit")
                        .error("Action `" + type.functionName() + "` exceeded limit of " + type.limit(), function.name())
                );
            }

            if (action instanceof Conditional)
                processConditional((Conditional) action);
        }
    }

    private void processConditional(Conditional conditional) {
        if (!conditional.ifActions().isEmpty())
            ActionLimiter
                .from(context, function, conditional.ifActions())
                .process();

        if (!conditional.elseActions().isEmpty())
            ActionLimiter
                .from(context, function, conditional.elseActions())
                .process();
    }

    public static ActionLimiter from(
        @NotNull ParserContext context, @NotNull Method function, @NotNull List<Action> actions
    ) {
        return new ActionLimiter(context, function, actions);
    }
}
