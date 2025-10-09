package org.housingstudio.hsl.compiler.ast.impl.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public enum BaseType {
    VOID("void", true, true),
    INT("int", true, true),
    FLOAT("float", true, true),
    STRING("string", true, true),
    BOOL("bool", true, true),
    ANY("any", true, true),
    ARRAY("[]", true, false),
    NIL("nil", true, true),
    COORD("coord", true, true),

    ENUM("enum", false, true),

    SLOT("Slot", false, true),
    LOCATION("Location", false, true),
    GAME_MODE("GameMode", false, true),
    TARGET("Target", false, true),
    WEATHER("Weather", false, true),
    TIME("Time", false, true),
    NAMESPACE("Namespace", false, true),
    EFFECT("Effect", false, true),
    ENCHANT("Enchant", false, true),
    MODE("Mode", false, true),
    LOBBY("Lobby", false, true),
    SOUND("Sound", false, true),
    FLAG("Flag", false, true),
    MATERIAL("Material", false, true),
    VECTOR("Vector", false, true),
    EXECUTOR("Executor", false, true),
    COMPARATOR("Comparator", false, true),
    ITEM_COMPARATOR("ItemComparator", false, true),
    COMPARATOR_TARGET("ComparatorTarget", false, true),
    COMPARATOR_AMOUNT("ComparatorAmount", false, true),
    PERMISSION("Permission", false, true);

    private final @NotNull String format;
    private final boolean primitive;
    private final boolean constant;

    public boolean isNumber() {
        return this == INT || this == FLOAT;
    }
}
