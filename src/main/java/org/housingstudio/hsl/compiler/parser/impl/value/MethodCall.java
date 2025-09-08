package org.housingstudio.hsl.compiler.parser.impl.value;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.builder.ActionBuilder;
import org.housingstudio.hsl.compiler.ast.hierarchy.Children;
import org.housingstudio.hsl.compiler.ast.impl.action.BuiltinActions;
import org.housingstudio.hsl.compiler.ast.impl.action.BuiltinConditions;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Method;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Parameter;
import org.housingstudio.hsl.compiler.ast.impl.type.Type;
import org.housingstudio.hsl.compiler.ast.impl.value.Argument;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.parser.impl.action.ArgAccess;
import org.housingstudio.hsl.compiler.parser.impl.action.ActionCodec;
import org.housingstudio.hsl.compiler.token.Errno;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.exporter.action.Action;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.METHOD_CALL)
@ToString
public class MethodCall extends Value implements ActionBuilder {
    /**
     * The name of the method to call.
     */
    private final @NotNull Token name;

    /**
     * The list of arguments to pass to the method.
     */
    @Children(resolver = MethodCallChildrenResolver.class)
    private final @NotNull List<Argument> arguments;

    /**
     * Retrieve the type of the held value. This result will be used to inter types for untyped variables.
     *
     * @return the resolved value of the type
     */
    @Override
    public @NotNull Type getValueType() {
        if (BuiltinConditions.LOOKUP.containsKey(name.value()))
            return Type.BOOL;

        Method method = BuiltinActions.LOOKUP.get(name.value());
        if (method == null)
            method = game.functions().get(name.value());

        if (method == null) {
            context.error(
                Errno.UNKNOWN_MACRO,
                "method not found",
                name,
                "cannot find method: " + name.value()
            );
            throw new UnsupportedOperationException("Cannot find method: " + name.value());
        }

        return Type.VOID; // TODO resolve method return type
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
        context.error(
            Errno.FUNCTION_TRIGGER_AS_EXPRESSION,
            "function trigger used as expression",
            name,
            "function triggers cannot be treated as expressions"
        );
        throw new UnsupportedOperationException("Cannot use method call as an expression: " + name.value());
    }

    /**
     * Returns a string representation of the implementing class.
     *
     * @return the class debug information
     */
    @Override
    public @NotNull String print() {
        return name.value() + "(" + arguments.stream().map(Argument::print).collect(Collectors.joining(", ")) + ")";
    }

    @Override
    public @NotNull Action buildAction() {
        if (BuiltinConditions.LOOKUP.containsKey(name.value())) {
            context.error(
                Errno.UNEXPECTED_CONDITION_TARGET,
                "unexpected condition target",
                name,
                "cannot use condition functions as action calls or method calls"
            );
            throw new UnsupportedOperationException("Cannot use condition as action or method call: " + name.value());
        }

        Method method = BuiltinActions.LOOKUP.get(name.value());
        if (method == null)
            method = game.functions().get(name.value());

        if (method == null) {
            context.error(
                Errno.UNKNOWN_METHOD,
                "method not found",
                name,
                "cannot find method"
            );
            throw new UnsupportedOperationException("Cannot find method: " + name.value());
        }

        if (BuiltinActions.LOOKUP.containsKey(name.value())) {
            Map<String, Value> args = ArgumentParser.parseArguments(context, name, method.parameters(), arguments);
            validateArgumentTypes(method.parameters(), args);
            return buildBuiltinAction(new ArgAccess(args));
        }

        if (!arguments.isEmpty()) {
            context.error(
                Errno.FUNCTION_TRIGGER_WITH_ARGUMENTS,
                "function triggered with arguments",
                name,
                "cannot trigger functions with arguments"
            );
            throw new UnsupportedOperationException("Function trigger cannot be used with arguments: " + name.value());
        }

        return buildFunctionTrigger();
    }

    public void validateArgumentTypes(@NotNull List<Parameter> parameters, @NotNull Map<String, Value> args) {
        for (Parameter parameter : parameters) {
            Value value = args.get(parameter.name().value());
            if (parameter.type() == Type.ANY)
                continue;

            if (parameter.type() != value.getValueType()) {
                context.error(
                    Errno.INVALID_ARGUMENT_TYPE,
                    "argument type mismatch",
                    name,
                    "parameter `" + parameter.name().value() + "` expects " + parameter.type() +
                    " but found " + value.getValueType()
                );
                throw new UnsupportedOperationException(
                    String.format(
                        "Invalid parameter type: (given: %s, expected: %s)", value.getValueType(), parameter.type()
                    )
                );
            }
        }
    }

    private @NotNull Action buildBuiltinAction(@NotNull ArgAccess args) {
        switch (name.value()) {
            case "setGroup":
                return ActionCodec.setGroup(args);
            case "kill":
                return ActionCodec.kill(args);
            case "heal":
                return ActionCodec.heal(args);
            case "title":
                return ActionCodec.title(args);
            case "actionbar":
                return ActionCodec.actionbar(args);
            case "resetInventory":
                return ActionCodec.resetInventory(args);
            case "changeMaxHealth":
                return ActionCodec.changeMaxHealth(args);
            case "parkourCheckpoint":
                return ActionCodec.parkourCheckpoint(args);
            case "giveItem":
                return ActionCodec.giveItem(args);
            case "removeItem":
                return ActionCodec.removeItem(args);
            case "chat":
                return ActionCodec.chat(args);
            case "addEffect":
                return ActionCodec.addEffect(args);
            case "clearEffects":
                return ActionCodec.clearEffects(args);
            case "addExperience":
                return ActionCodec.addExperience(args);
            case "sendToLobby":
                return ActionCodec.sendToLobby(args);
            case "changeVariable":
                return ActionCodec.changeVariable(args);
            case "teleport":
                return ActionCodec.teleport(args);
            case "failParkour":
                return ActionCodec.failParkour(args);
            case "playSound":
                return ActionCodec.playSound(args);
            case "setCompassTarget":
                return ActionCodec.setCompassTarget(args);
            case "setGameMode":
                return ActionCodec.setGameMode(args);
            case "changeHealth":
                return ActionCodec.changeHealth(args);
            case "changeHunger":
                return ActionCodec.changeHunger(args);
            // TODO random action
            case "triggerFunction":
                return ActionCodec.triggerFunction(args);
            case "applyInventoryLayout":
                return ActionCodec.applyInventoryLayout(args);
            case "enchantHeldItem":
                return ActionCodec.enchantHeldItem(args);
            case "sleep":
                return ActionCodec.sleep(args);
            case "setTeam":
                return ActionCodec.setTeam(args);
            case "dropItem":
                return ActionCodec.dropItem(args);
            case "changeVelocity":
                return ActionCodec.changeVelocity(args);
            case "launchToTarget":
                return ActionCodec.launchToTarget(args);
            case "setPlayerWeather":
                return ActionCodec.setPlayerWeather(args);
            case "setPlayerTime":
                return ActionCodec.setPlayerTime(args);
            case "toggleNameTagDisplay":
                return ActionCodec.toggleNameTagDisplay(args);
            case "cancelEvent":
                return ActionCodec.cancelEvent(args);
            default:
                throw new UnsupportedOperationException(String.format("Action `%s` is not implemented yet", name.value()));
        }
    }

    private @NotNull Action buildFunctionTrigger() {
        throw new UnsupportedOperationException("function trigger not implemented yet");
    }
}
