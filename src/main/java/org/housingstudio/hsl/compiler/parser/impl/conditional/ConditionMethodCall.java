package org.housingstudio.hsl.compiler.parser.impl.conditional;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.builder.ConditionBuilder;
import org.housingstudio.hsl.compiler.ast.impl.action.BuiltinConditions;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Method;
import org.housingstudio.hsl.compiler.ast.impl.value.ConstantLiteral;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.debug.Printable;
import org.housingstudio.hsl.compiler.parser.impl.action.ArgAccess;
import org.housingstudio.hsl.compiler.parser.impl.action.ConditionCodec;
import org.housingstudio.hsl.compiler.parser.impl.value.ArgumentParser;
import org.housingstudio.hsl.compiler.ast.impl.value.MethodCall;
import org.housingstudio.hsl.compiler.token.Errno;
import org.housingstudio.hsl.exporter.condition.Condition;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.METHOD_CALL)
public class ConditionMethodCall extends Node implements ConditionBuilder, Printable {
    private final @NotNull MethodCall call;
    private boolean inverted;

    @Override
    public @NotNull Condition buildCondition() {
        Method method = BuiltinConditions.LOOKUP.get(call.name().value());
        if (method == null) {
            context.error(
                Errno.UNKNOWN_CONDITION,
                "unexpected condition method",
                call.name(),
                "unrecognized condition name"
            );
            // TODO note: read about conditions at %docs%
            throw new UnsupportedOperationException("Cannot find condition: " + call.name().value());
        }

        Map<String, Value> args = ArgumentParser.parseArguments(context, call.name(), method.parameters(), call.arguments());
        call.validateArgumentTypes(method.parameters(), args);

        return buildBuiltinCondition(new ArgAccess(args));
    }

    @Override
    public void invert() {
        inverted = !inverted;
    }

    private @NotNull Condition buildBuiltinCondition(@NotNull ArgAccess args) {
        args.args().put("inverted", ConstantLiteral.ofBool(inverted));
        switch (call.name().value()) {
            case "hasGroup":
                return ConditionCodec.hasGroup(args);
            case "compareVariable":
                return ConditionCodec.compareVariable(args);
            case "hasPermission":
                return ConditionCodec.hasPermission(args);
            case "withinRegion":
                return ConditionCodec.withinRegion(args);
            case "hasItem":
                return ConditionCodec.hasItem(args);
            case "doingParkour":
                return ConditionCodec.doingParkour(args);
            case "hasEffect":
                return ConditionCodec.hasEffect(args);
            case "isSneaking":
                return ConditionCodec.isSneaking(args);
            case "isFlying":
                return ConditionCodec.isFlying(args);
            case "hasHealth":
                return ConditionCodec.hasHealth(args);
            case "hasMaxHealth":
                return ConditionCodec.hasMaxHealth(args);
            case "hasHunger":
                return ConditionCodec.hasHunger(args);
            case "hasGameMode":
                return ConditionCodec.hasGameMode(args);
            case "comparePlaceholder":
                return ConditionCodec.comparePlaceholder(args);
            case "hasTeam":
                return ConditionCodec.hasTeam(args);
            default:
                throw new UnsupportedOperationException(
                    String.format("Condition `%s` not implemented yet", call.name().value())
                );
        }
    }

    /**
     * Returns a string representation of the implementing class.
     *
     * @return the class debug information
     */
    @Override
    public @NotNull String print() {
        return (inverted ? "!" : "") + call.print();
    }
}
