package org.housingstudio.hsl.compiler.ast.impl.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public enum BaseType {
    VOID("void"),
    INT("int"),
    FLOAT("float"),
    STRING("string"),
    BOOL("bool"),
    ANY("any"),
    ARRAY("[]"),
    NIL("nil"),

    SLOT("Slot"),
    LOCATION("Location"),
    GAME_MODE("GameMode"),
    TARGET("Target"),
    WEATHER("Weather"),
    TIME("Time"),
    NAMESPACE("Namespace"),
    EFFECT("Effect"),
    ENCHANT("Enchant"),
    MODE("Mode"),
    LOBBY("Lobby"),
    SOUND("Sound"),
    FLAG("Flag"),
    MATERIAL("Material"),
    VECTOR("Vector"),
    EXECUTOR("Executor"),
    COMPARATOR("Comparator"),
    ITEM_COMPARATOR("ItemComparator"),
    COMPARATOR_TARGET("ComparatorTarget"),
    COMPARATOR_AMOUNT("ComparatorAmount"),
    PERMISSION("Permission");

    private final @NotNull String format;

    public boolean isNumber() {
        return this == INT || this == FLOAT;
    }
}
