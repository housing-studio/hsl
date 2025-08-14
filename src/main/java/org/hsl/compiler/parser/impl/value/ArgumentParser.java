package org.hsl.compiler.parser.impl.value;

import lombok.experimental.UtilityClass;
import org.hsl.compiler.ast.impl.declaration.Parameter;
import org.hsl.compiler.ast.impl.value.Argument;
import org.hsl.compiler.ast.impl.value.Value;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class ArgumentParser {
    public @NotNull Map<String, Value> parseArguments(
        @NotNull List<Parameter> parameters, @NotNull List<Argument> arguments
    ) {
        Map<String, Value> result = new LinkedHashMap<>(); // preserve parameter order
        Map<String, Integer> paramIndex = new HashMap<>();
        for (int i = 0; i < parameters.size(); i++)
            paramIndex.put(parameters.get(i).name().value(), i);

        boolean seenNamed = false;
        int nextPosParam = 0;                    // next parameter index to use for a positional arg
        Map<String, Value> namedArgs = new LinkedHashMap<>();

        // 1) iterate call arguments in order
        for (Argument arg : arguments) {
            if (arg.name() == null) {
                // positional
                if (seenNamed)
                    throw new IllegalArgumentException("Positional argument after a named argument is not allowed.");

                // find next parameter index that isn't assigned (should be nextPosParam)
                while (nextPosParam < parameters.size() && result.containsKey(parameters.get(nextPosParam).name().value()))
                    nextPosParam++;

                if (nextPosParam >= parameters.size())
                    throw new IllegalArgumentException("Too many positional arguments.");

                Parameter p = parameters.get(nextPosParam);
                result.put(p.name().value(), arg.value());
                nextPosParam++;
            } else {
                // named
                seenNamed = true;
                if (!paramIndex.containsKey(arg.name().value()))
                    throw new IllegalArgumentException("Unknown named argument: " + arg.name().value());

                if (namedArgs.containsKey(arg.name().value()))
                    throw new IllegalArgumentException("Duplicate named argument: " + arg.name().value());

                namedArgs.put(arg.name().value(), arg.value());
            }
        }

        // 2) apply named args (must not overwrite positional assignments)
        for (Map.Entry<String, Value> e : namedArgs.entrySet()) {
            if (result.containsKey(e.getKey()))
                throw new IllegalArgumentException("Multiple values for argument: " + e.getKey());

            result.put(e.getKey(), e.getValue());
        }

        // 3) fill defaults or error on missing required params
        for (Parameter p : parameters) {
            if (!result.containsKey(p.name().value())) {
                if (p.defaultValue() != null)
                    result.put(p.name().value(), p.defaultValue());
                else
                    throw new IllegalArgumentException("Missing required argument: " + p.name().value());
            }
        }

        return result;
    }
}
