package org.housingstudio.hsl.compiler.codegen;

import lombok.experimental.UtilityClass;
import org.housingstudio.hsl.compiler.codegen.impl.action.Action;
import org.housingstudio.hsl.compiler.codegen.impl.generic.Function;
import org.housingstudio.hsl.compiler.codegen.impl.house.House;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

@UtilityClass
public class HtslExporter {
    public @NotNull String export(@NotNull House house) {
        StringBuilder builder = new StringBuilder();

        Iterator<Function> functions = house.functions().iterator();
        while (functions.hasNext()) {
            Function function = functions.next();
            builder.append(exportFunction(function));
            if (functions.hasNext()) {
                builder.append("\n");
            }
        }

        builder.append("\n");

        return builder.toString();
    }

    private @NotNull String exportFunction(@NotNull Function function) {
        StringBuilder builder = new StringBuilder();
        builder.append("goto function \"").append(function.name()).append("\"").append("\n");

        Iterator<Action> actions = function.actions().iterator();
        while (actions.hasNext()) {
            Action action = actions.next();
            builder.append(action.asHTSL().build());
            if (actions.hasNext())
                builder.append("\n");
        }

        return builder.toString();
    }
}
