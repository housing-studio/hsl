package org.housingstudio.hsl.compiler.ast.impl.action;

import lombok.experimental.UtilityClass;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Method;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Parameter;
import org.housingstudio.hsl.compiler.ast.impl.scope.Scope;
import org.housingstudio.hsl.compiler.ast.impl.type.Types;
import org.housingstudio.hsl.compiler.ast.impl.value.ConstantLiteral;
import org.housingstudio.hsl.compiler.ast.impl.value.builtin.*;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.housingstudio.hsl.std.Comparator;
import org.housingstudio.hsl.std.ComparatorAmount;
import org.housingstudio.hsl.std.ItemComparator;
import org.housingstudio.hsl.std.ComparatorTarget;
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
        Parameter.required("group", Types.STRING),
        Parameter.optional("higher", Types.BOOL, ConstantLiteral.ofBool(false))
    );

    public final Method COMPARE_VARIABLE = createCondition(
        "compareVariable",
        Parameter.required("namespace", Types.NAMESPACE),
        Parameter.required("variable", Types.STRING),
        Parameter.required("value", Types.ANY),
        Parameter.optional("comparator", Types.COMPARATOR, new ComparatorValue(Comparator.EQUAL)),
        Parameter.optional("fallback", Types.ANY, new NullValue())
    );

    public final Method HAS_PERMISSION = createCondition(
        "hasPermission",
        Parameter.required("permission", Types.PERMISSION)
    );

    public final Method WITHIN_REGION = createCondition(
        "withinRegion",
        Parameter.required("region", Types.STRING)
    );

    public final Method HAS_ITEM = createCondition(
        "hasItem",
        Parameter.required("item", Types.MATERIAL),
        Parameter.optional("comparator", Types.ITEM_COMPARATOR, new ItemComparatorValue(ItemComparator.METADATA)),
        Parameter.optional("target", Types.COMPARATOR_TARGET, new ComparatorTargetValue(ComparatorTarget.ANYWHERE)),
        Parameter.optional("amount", Types.COMPARATOR_AMOUNT, new ComparatorAmountValue(ComparatorAmount.ANY))
    );

    public final Method DOING_PARKOUR = createCondition("doingParkour");

    public final Method HAS_EFFECT = createCondition("hasEffect", Parameter.required("effect", Types.EFFECT));

    public final Method IS_SNEAKING = createCondition("isSneaking");

    public final Method IS_FLYING = createCondition("isFlying");

    public final Method HAS_HEALTH = createCondition(
        "hasHealth",
        Parameter.required("health", Types.INT),
        Parameter.optional("comparator", Types.COMPARATOR, new ComparatorValue(Comparator.EQUAL))
    );

    private final Method HAS_MAX_HEALTH = createCondition(
        "hasMaxHealth",
        Parameter.required("maxHealth", Types.INT),
        Parameter.optional("comparator", Types.COMPARATOR, new ComparatorValue(Comparator.EQUAL))
    );

    public final Method HAS_HUNGER = createCondition(
        "hasHunger",
        Parameter.required("level", Types.INT),
        Parameter.optional("comparator", Types.COMPARATOR, new ComparatorValue(Comparator.EQUAL))
    );

    public final Method HAS_GAME_MODE = createCondition(
        "hasGameMode",
        Parameter.required("gameMode", Types.GAME_MODE)
    );

    public final Method COMPARE_PLACEHOLDER = createCondition(
        "comparePlaceholder",
        Parameter.required("placeholder", Types.STRING),
        Parameter.required("value", Types.ANY),
        Parameter.optional("comparator", Types.COMPARATOR, new ComparatorValue(Comparator.EQUAL))
    );

    public final Method HAS_TEAM = createCondition(
        "hasTeam",
        Parameter.required("team", Types.STRING)
    );

    public @NotNull Method createCondition(@NotNull String name, @NotNull Parameter @NotNull ... parameters) {
        List<Parameter> parameterList = new ArrayList<>(Arrays.asList(parameters));
        parameterList.add(Parameter.optional("invert", Types.BOOL, ConstantLiteral.ofBool(false)));
        return new Method(
            Collections.emptyList(),
            Token.of(TokenType.IDENTIFIER, name),
            Types.BOOL,
            parameterList,
            Scope.EMPTY
        );
    }
}
