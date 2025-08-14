package org.hsl.compiler.parser.impl.value;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.compiler.ast.NodeInfo;
import org.hsl.compiler.ast.NodeType;
import org.hsl.compiler.ast.builder.ActionBuilder;
import org.hsl.compiler.ast.impl.action.BuiltinActions;
import org.hsl.compiler.ast.impl.declaration.Method;
import org.hsl.compiler.ast.impl.declaration.Parameter;
import org.hsl.compiler.ast.impl.type.Type;
import org.hsl.compiler.ast.impl.value.Argument;
import org.hsl.compiler.ast.impl.value.Value;
import org.hsl.compiler.parser.impl.action.ActionArgs;
import org.hsl.compiler.parser.impl.action.ActionCodec;
import org.hsl.compiler.token.Token;
import org.hsl.export.action.Action;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.METHOD_CALL)
public class MethodCall extends Value implements ActionBuilder {
    /**
     * The name of the method to call.
     */
    private final @NotNull Token name;

    /**
     * The list of arguments to pass to the method.
     */
    private final @NotNull List<Argument> arguments;

    /**
     * Retrieve the type of the held value. This result will be used to inter types for untyped variables.
     *
     * @return the resolved value of the type
     */
    @Override
    public @NotNull Type getValueType() {
        Method method = BuiltinActions.LOOKUP.get(name.value());
        if (method == null)
            method = game.functions().get(name.value());

        if (method == null) {
            context.syntaxError(name, "Method not found: `%s`".formatted(name.value()));
            throw new UnsupportedOperationException("Cannot find method: " + name.value());
        }

        return Type.VOID;//TODO
    }

    /**
     * Get the constant string representation of the value.
     * <p>
     * Housing variables are handled as string by default, this format is the input for housing variables.
     *
     * @return the final string value
     */
    @Override
    public @NotNull String asConstantValue() {
        context.syntaxError(name, "Cannot use method call as an expression");
        throw new UnsupportedOperationException("Cannot use method call as an expression: " + name.value());
    }

    /**
     * Returns a string representation of the implementing class.
     *
     * @return the class debug information
     */
    @Override
    public @NotNull String print() {
        return "call " + name.value() + "(" + arguments.stream().map(Argument::print).collect(Collectors.joining(", ")) + ")";
    }

    @Override
    public @NotNull Action buildAction() {
        Method method = BuiltinActions.LOOKUP.get(name.value());
        if (method == null)
            method = game.functions().get(name.value());

        if (method == null) {
            context.syntaxError(name, "Method not found: `%s`".formatted(name.value()));
            throw new UnsupportedOperationException("Cannot find method: " + name.value());
        }

        if (BuiltinActions.LOOKUP.containsKey(name.value())) {
            Map<String, Value> args = ArgumentParser.parseArguments(method.parameters(), arguments);
            validateArgumentTypes(method.parameters(), args);
            return buildBuiltinAction(args);
        }

        if (!arguments.isEmpty()) {
            context.syntaxError(name, "Function trigger cannot be used with arguments");
            throw new UnsupportedOperationException("Function trigger cannot be used with arguments: " + name.value());
        }

        return buildFunctionTrigger();
    }

    private void validateArgumentTypes(@NotNull List<Parameter> parameters, @NotNull Map<String, Value> args) {
        for (Parameter parameter : parameters) {
            Value value = args.get(parameter.name().value());
            if (parameter.type() == Type.ANY)
                continue;

            if (parameter.type() != value.getValueType()) {
                context.syntaxError(
                    name, "Invalid parameter type (given: %s, expected: %s)".formatted(value.getValueType(), parameter.type())
                );
                throw new UnsupportedOperationException(
                    "Invalid parameter type (given: %s, expected: %s)".formatted(value.getValueType(), parameter.type())
                );
            }
        }
    }

    private @NotNull Action buildBuiltinAction(@NotNull Map<String, Value> rawArgs) {
        ActionArgs args = new ActionArgs(rawArgs);
        return switch (name.value()) {
            case "setGroup" -> ActionCodec.setGroup(args);
            case "kill" -> ActionCodec.kill(args);
            case "heal" -> ActionCodec.heal(args);
            case "title" -> ActionCodec.title(args);
            case "actionbar" -> ActionCodec.actionbar(args);
            case "resetInventory" -> ActionCodec.resetInventory(args);
            case "changeMaxHealth" -> ActionCodec.changeMaxHealth(args);
            case "parkourCheckpoint" -> ActionCodec.parkourCheckpoint(args);
            case "giveItem" -> ActionCodec.giveItem(args);
            case "removeItem" -> ActionCodec.removeItem(args);
            case "chat" -> ActionCodec.chat(args);
            case "addEffect" -> ActionCodec.addEffect(args);
            case "clearEffects" -> ActionCodec.clearEffects(args);
            case "addExperience" -> ActionCodec.addExperience(args);
            case "sendToLobby" -> ActionCodec.sendToLobby(args);
            case "changeVariable" -> ActionCodec.changeVariable(args);
            case "teleport" -> ActionCodec.teleport(args);
            case "failParkour" -> ActionCodec.failParkour(args);
            case "playSound" -> ActionCodec.playSound(args);
            case "setCompassTarget" -> ActionCodec.setCompassTarget(args);
            case "setGameMode" -> ActionCodec.setGameMode(args);
            case "changeHealth" -> ActionCodec.changeHealth(args);
            case "changeHunger" -> ActionCodec.changeHunger(args);
            // TODO random action
            case "triggerFunction" -> ActionCodec.triggerFunction(args);
            case "applyInventoryLayout" -> ActionCodec.applyInventoryLayout(args);
            case "enchantHeldItem" -> ActionCodec.enchantHeldItem(args);
            case "sleep" -> ActionCodec.sleep(args);
            case "setTeam" -> ActionCodec.setTeam(args);
            case "dropItem" -> ActionCodec.dropItem(args);
            case "changeVelocity" -> ActionCodec.changeVelocity(args);
            case "launchToTarget" -> ActionCodec.launchToTarget(args);
            case "setPlayerWeather" -> ActionCodec.setPlayerWeather(args);
            case "setPlayerTime" -> ActionCodec.setPlayerTime(args);
            case "toggleNameTagDisplay" -> ActionCodec.toggleNameTagDisplay(args);
            case "cancelEvent" -> ActionCodec.cancelEvent(args);
            default -> throw new UnsupportedOperationException("Action `%s` not implemented yet".formatted(name.value()));
        };
    }

    private @NotNull Action buildFunctionTrigger() {
        throw new UnsupportedOperationException("function trigger not implemented yet");
    }
}
