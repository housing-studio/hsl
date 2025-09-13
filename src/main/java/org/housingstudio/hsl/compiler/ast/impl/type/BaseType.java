package org.housingstudio.hsl.compiler.ast.impl.type;

public enum BaseType {
    VOID,
    INT,
    FLOAT,
    STRING,
    BOOL,
    ANY,
    ARRAY,
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
