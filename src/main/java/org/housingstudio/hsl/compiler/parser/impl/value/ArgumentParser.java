package org.housingstudio.hsl.compiler.parser.impl.value;

import lombok.experimental.UtilityClass;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Parameter;
import org.housingstudio.hsl.compiler.ast.impl.value.Argument;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.error.Notification;
import org.housingstudio.hsl.compiler.parser.ParserContext;
import org.housingstudio.hsl.compiler.error.Errno;
import org.housingstudio.hsl.compiler.token.Token;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class ArgumentParser {
    public @NotNull Map<String, Value> parseArguments(
        @NotNull ParserContext context, @NotNull Token name, @NotNull List<Parameter> parameters, @NotNull List<Argument> arguments
    ) {
        Map<String, Value> result = new LinkedHashMap<>(); // preserve parameter order
        Map<String, Integer> paramIndex = new HashMap<>();
        for (int i = 0; i < parameters.size(); i++)
            paramIndex.put(parameters.get(i).name().value(), i);

        boolean seenNamed = false;
        int nextPosParam = 0; // next parameter index to use for a positional arg
        Map<String, Value> namedArgs = new LinkedHashMap<>();

        // 1) iterate call arguments in order
        for (Argument arg : arguments) {
            if (arg.name() == null) {
                // positional
                if (seenNamed) {
                    context.errorPrinter().print(
                        Notification.error(Errno.POSITIONAL_ARGUMENT_AFTER_NAMED_ARGUMENT, "positional argument after named argument")
                            .error("positional arguments are not allowed after named arguments", name)
                    );
                    throw new IllegalArgumentException("Positional argument after a named argument is not allowed.");
                }

                // find next parameter index that isn't assigned (should be nextPosParam)
                while (nextPosParam < parameters.size() && result.containsKey(parameters.get(nextPosParam).name().value()))
                    nextPosParam++;

                if (nextPosParam >= parameters.size()) {
                    context.errorPrinter().print(
                        Notification.error(Errno.TOO_MANY_POSITIONAL_ARGUMENTS, "too many positional arguments")
                            .error("expected " + parameters.size() + " positional arguments, but found " + arguments.size(), name)
                    );
                    throw new IllegalArgumentException("Too many positional arguments.");
                }

                Parameter p = parameters.get(nextPosParam);
                result.put(p.name().value(), arg.value());
                nextPosParam++;
            } else {
                // named
                seenNamed = true;
                if (!paramIndex.containsKey(arg.name().value())) {
                    context.errorPrinter().print(
                        Notification.error(Errno.UNKNOWN_NAMED_ARGUMENT, "unknown named argument")
                            .error("unknown named argument: `" + arg.name().value() + "`", name)
                    );
                    throw new IllegalArgumentException("Unknown named argument: " + arg.name().value());
                }

                if (namedArgs.containsKey(arg.name().value())) {
                    context.errorPrinter().print(
                        Notification.error(Errno.DUPLICATE_NAMED_ARGUMENT, "duplicate named argument")
                            .error("duplicate named argument: `" + arg.name().value() + "`", name)
                    );
                    throw new IllegalArgumentException("Duplicate named argument: " + arg.name().value());
                }

                namedArgs.put(arg.name().value(), arg.value());
            }
        }

        // 2) apply named args (must not overwrite positional assignments)
        for (Map.Entry<String, Value> e : namedArgs.entrySet()) {
            if (result.containsKey(e.getKey())) {
                context.errorPrinter().print(
                    Notification.error(Errno.MULTIPLE_ARGUMENT_VALUES, "multiple argument values")
                        .error("parameter `" + e.getKey() + "` received multiple arguments", name)
                );
                throw new IllegalArgumentException("Multiple values for argument: " + e.getKey());
            }

            result.put(e.getKey(), e.getValue());
        }

        // 3) fill defaults or error on missing required params
        for (Parameter p : parameters) {
            if (!result.containsKey(p.name().value())) {
                if (p.defaultValue() != null)
                    result.put(p.name().value(), p.defaultValue());
                else {
                    context.errorPrinter().print(
                        Notification.error(Errno.MISSING_REQUIRED_ARGUMENT, "missing required argument")
                            .error("missing required argument: " + p.name().value(), name)
                    );
                    throw new IllegalArgumentException("Missing required argument: " + p.name().value());
                }
            }
        }

        return result;
    }
}
