package org.hsl.compiler.parser.impl.conditional;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.compiler.ast.Node;
import org.hsl.compiler.ast.NodeInfo;
import org.hsl.compiler.ast.NodeType;
import org.hsl.compiler.ast.builder.ConditionBuilder;
import org.hsl.compiler.ast.impl.action.BuiltinConditions;
import org.hsl.compiler.ast.impl.declaration.Method;
import org.hsl.compiler.ast.impl.value.ConstantLiteral;
import org.hsl.compiler.ast.impl.value.Value;
import org.hsl.compiler.parser.impl.action.ArgAccess;
import org.hsl.compiler.parser.impl.action.ConditionCodec;
import org.hsl.compiler.parser.impl.value.ArgumentParser;
import org.hsl.compiler.parser.impl.value.MethodCall;
import org.hsl.export.condition.Condition;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.METHOD_CALL)
public class ConditionMethodCall extends Node implements ConditionBuilder {
    private final @NotNull MethodCall call;
    private boolean inverted;

    @Override
    public @NotNull Condition buildCondition() {
        Method method = BuiltinConditions.LOOKUP.get(call.name().value());
        if (method == null) {
            context.syntaxError(call.name(), "Condition not found: `%s`".formatted(call.name().value()));
            throw new UnsupportedOperationException("Cannot find condition: " + call.name().value());
        }

        Map<String, Value> args = ArgumentParser.parseArguments(method.parameters(), call.arguments());
        call.validateArgumentTypes(method.parameters(), args);

        return buildBuiltinCondition(new ArgAccess(args));
    }

    @Override
    public void invert() {
        inverted = !inverted;
    }

    private @NotNull Condition buildBuiltinCondition(@NotNull ArgAccess args) {
        args.args().put("inverted", ConstantLiteral.ofBool(inverted));
        return switch (call.name().value()) {
            case "hasGroup" -> ConditionCodec.hasGroup(args);
            case "compareVariable" -> ConditionCodec.compareVariable(args);
            case "hasPermission" -> ConditionCodec.hasPermission(args);
            case "withinRegion" -> ConditionCodec.withinRegion(args);
            case "hasItem" -> ConditionCodec.hasItem(args);
            case "doingParkour" -> ConditionCodec.doingParkour(args);
            case "hasEffect" -> ConditionCodec.hasEffect(args);
            case "isSneaking" -> ConditionCodec.isSneaking(args);
            case "isFlying" -> ConditionCodec.isFlying(args);
            case "hasHealth" -> ConditionCodec.hasHealth(args);
            case "hasMaxHealth" -> ConditionCodec.hasMaxHealth(args);
            case "hasHunger" -> ConditionCodec.hasHunger(args);
            case "hasGameMode" -> ConditionCodec.hasGameMode(args);
            case "comparePlaceholder" -> ConditionCodec.comparePlaceholder(args);
            case "hasTeam" -> ConditionCodec.hasTeam(args);
            default -> throw new UnsupportedOperationException(
                "Condition `%s` not implemented yet".formatted(call.name().value())
            );
        };
    }
}
