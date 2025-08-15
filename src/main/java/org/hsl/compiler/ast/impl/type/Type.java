package org.hsl.compiler.ast.impl.type;

public enum Type {
    VOID,
    INT,
    FLOAT,
    STRING,
    BOOL,
    ANY,
    NIL,

    SLOT,
    LOCATION,
    GAME_MODE,
    TARGET,
    WEATHER,
    TIME,
    NAMESPACE,
    EFFECT,
    ENCHANT,
    MODE,
    LOBBY,
    SOUND,
    FLAG,
    MATERIAL,
    VECTOR,
    EXECUTOR,
    COMPARATOR,
    ITEM_COMPARATOR,
    COMPARATOR_TARGET,
    COMPARATOR_AMOUNT,
    PERMISSION;

    public boolean isNumber() {
        return this == INT || this == FLOAT;
    }
}
