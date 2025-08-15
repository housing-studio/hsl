package org.hsl.compiler.ast.impl.action;

import lombok.experimental.UtilityClass;
import org.hsl.compiler.ast.impl.declaration.Method;
import org.hsl.compiler.ast.impl.declaration.Parameter;
import org.hsl.compiler.ast.impl.scope.Scope;
import org.hsl.compiler.ast.impl.type.Type;
import org.hsl.compiler.ast.impl.value.ConstantLiteral;
import org.hsl.compiler.ast.impl.value.builtin.*;
import org.hsl.compiler.token.Token;
import org.hsl.compiler.token.TokenType;
import org.hsl.std.type.Comparator;
import org.hsl.std.type.ComparatorAmount;
import org.hsl.std.type.ItemComparator;
import org.hsl.std.type.ComparatorTarget;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@UtilityClass
public class BuiltinConditions {
    public final Map<String, Method> LOOKUP = new HashMap<>();

    public void init() {
        LOOKUP.put("hasGroup", HAS_GROUP);
        LOOKUP.put("compareVariable", COMPARE_VARIABLE);
        LOOKUP.put("hasPermission", HAS_PERMISSION);
        LOOKUP.put("withinRegion", WITHIN_REGION);
        LOOKUP.put("hasItem", HAS_ITEM);
        LOOKUP.put("doingParkour", DOING_PARKOUR);
        LOOKUP.put("hasEffect", HAS_EFFECT);
        LOOKUP.put("isSneaking", IS_SNEAKING);
        LOOKUP.put("isFlying", IS_FLYING);
        LOOKUP.put("hasHealth", HAS_HEALTH);
        LOOKUP.put("hasMaxHealth", HAS_MAX_HEALTH);
        LOOKUP.put("hasHunger", HAS_HUNGER);
        LOOKUP.put("hasGameMode", HAS_GAME_MODE);
        LOOKUP.put("comparePlaceholder", COMPARE_PLACEHOLDER);
        LOOKUP.put("hasTeam", HAS_TEAM);
    }

    public final Method HAS_GROUP = createCondition(
        "hasGroup",
        Parameter.required("group", Type.STRING),
        Parameter.optional("higher", Type.BOOL, ConstantLiteral.ofBool(false))
    );

    public final Method COMPARE_VARIABLE = createCondition(
        "compareVariable",
        Parameter.required("namespace", Type.NAMESPACE),
        Parameter.required("variable", Type.STRING),
        Parameter.required("value", Type.ANY),
        Parameter.optional("comparator", Type.COMPARATOR, new ComparatorValue(Comparator.EQUAL)),
        Parameter.optional("fallback", Type.ANY, new NullValue())
    );

    public final Method HAS_PERMISSION = createCondition(
        "hasPermission",
        Parameter.required("permission", Type.PERMISSION)
    );

    public final Method WITHIN_REGION = createCondition(
        "withinRegion",
        Parameter.required("region", Type.STRING)
    );

    public final Method HAS_ITEM = createCondition(
        "hasItem",
        Parameter.required("item", Type.MATERIAL),
        Parameter.optional("comparator", Type.ITEM_COMPARATOR, new ItemComparatorValue(ItemComparator.METADATA)),
        Parameter.optional("target", Type.COMPARATOR_TARGET, new ComparatorTargetValue(ComparatorTarget.ANYWHERE)),
        Parameter.optional("amount", Type.COMPARATOR_AMOUNT, new ComparatorAmountValue(ComparatorAmount.ANY))
    );

    public final Method DOING_PARKOUR = createCondition("doingParkour");

    public final Method HAS_EFFECT = createCondition("hasEffect", Parameter.required("effect", Type.EFFECT));

    public final Method IS_SNEAKING = createCondition("isSneaking");

    public final Method IS_FLYING = createCondition("isFlying");

    public final Method HAS_HEALTH = createCondition(
        "hasHealth",
        Parameter.required("health", Type.INT),
        Parameter.optional("comparator", Type.COMPARATOR, new ComparatorValue(Comparator.EQUAL))
    );

    private final Method HAS_MAX_HEALTH = createCondition(
        "hasMaxHealth",
        Parameter.required("maxHealth", Type.INT),
        Parameter.optional("comparator", Type.COMPARATOR, new ComparatorValue(Comparator.EQUAL))
    );

    public final Method HAS_HUNGER = createCondition(
        "hasHunger",
        Parameter.required("level", Type.INT),
        Parameter.optional("comparator", Type.COMPARATOR, new ComparatorValue(Comparator.EQUAL))
    );

    public final Method HAS_GAME_MODE = createCondition(
        "hasGameMode",
        Parameter.required("gameMode", Type.GAME_MODE)
    );

    public final Method COMPARE_PLACEHOLDER = createCondition(
        "comparePlaceholder",
        Parameter.required("placeholder", Type.STRING),
        Parameter.required("value", Type.ANY),
        Parameter.optional("comparator", Type.COMPARATOR, new ComparatorValue(Comparator.EQUAL))
    );

    public final Method HAS_TEAM = createCondition(
        "hasTeam",
        Parameter.required("team", Type.STRING)
    );

    public @NotNull Method createCondition(@NotNull String name, @NotNull Parameter @NotNull ... parameters) {
        List<Parameter> parameterList = new ArrayList<>(Arrays.asList(parameters));
        parameterList.add(Parameter.optional("invert", Type.BOOL, ConstantLiteral.ofBool(false)));
        return new Method(
            Collections.emptyList(),
            Token.of(TokenType.IDENTIFIER, name),
            Type.BOOL,
            parameterList,
            Scope.EMPTY
        );
    }
}
