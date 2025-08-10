package org.hsl.compiler.ast.impl.type;

public enum Type {
    VOID,
    INT,
    FLOAT,
    STRING,
    BOOL,
    ANY,

    SLOT,
    LOCATION,
    GAME_MODE,
    TARGET,
    WEATHER,
    TIME,
    NAMESPACE,
    EFFECT,
    ENCHANT,
    MODE;

    public boolean isNumber() {
        return this == INT || this == FLOAT;
    }
}
