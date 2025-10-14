package org.housingstudio.hsl.compiler.ast.impl.type;

import lombok.experimental.UtilityClass;
import org.housingstudio.hsl.compiler.ast.impl.value.ConstantLiteral;
import org.housingstudio.hsl.compiler.ast.impl.value.Coordinate;
import org.housingstudio.hsl.compiler.ast.impl.value.builtin.NullValue;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a predefined registry of builtin HSL types.
 */
@UtilityClass
public class Types {
    // primitive types
    public Type VOID   = new StaticType(BaseType.VOID, new NullValue());
    public Type INT    = new StaticType(BaseType.INT, ConstantLiteral.ofInt(0));
    public Type FLOAT  = new StaticType(BaseType.FLOAT, ConstantLiteral.ofFloat(0F));
    public Type STRING = new StaticType(BaseType.STRING, ConstantLiteral.ofString(""));
    public Type BOOL   = new StaticType(BaseType.BOOL, ConstantLiteral.ofBool(false));
    public Type ANY    = new StaticType(BaseType.ANY, new NullValue());
    public Type NIL    = new StaticType(BaseType.NIL, new NullValue());
    public Type COORD  = new StaticType(BaseType.COORD, new Coordinate(Coordinate.Kind.ABSOLUTE, ConstantLiteral.ofInt(0)));

    // compound types
    public Type SLOT              = new StaticType(BaseType.SLOT);
    public Type LOCATION          = new StaticType(BaseType.LOCATION);
    public Type GAME_MODE         = new StaticType(BaseType.GAME_MODE);
    public Type TARGET            = new StaticType(BaseType.TARGET);
    public Type WEATHER           = new StaticType(BaseType.WEATHER);
    public Type TIME              = new StaticType(BaseType.TIME);
    public Type NAMESPACE         = new StaticType(BaseType.NAMESPACE);
    public Type EFFECT            = new StaticType(BaseType.EFFECT);
    public Type ENCHANT           = new StaticType(BaseType.ENCHANT);
    public Type MODE              = new StaticType(BaseType.MODE);
    public Type LOBBY             = new StaticType(BaseType.LOBBY);
    public Type SOUND             = new StaticType(BaseType.SOUND);
    public Type FLAG              = new StaticType(BaseType.FLAG);
    public Type MATERIAL          = new StaticType(BaseType.MATERIAL);
    public Type VECTOR            = new StaticType(BaseType.VECTOR);
    public Type EXECUTOR          = new StaticType(BaseType.EXECUTOR);
    public Type COMPARATOR        = new StaticType(BaseType.COMPARATOR);
    public Type ITEM_COMPARATOR   = new StaticType(BaseType.ITEM_COMPARATOR);
    public Type COMPARATOR_TARGET = new StaticType(BaseType.COMPARATOR_TARGET);
    public Type COMPARATOR_AMOUNT = new StaticType(BaseType.COMPARATOR_AMOUNT);
    public Type PERMISSION        = new StaticType(BaseType.PERMISSION);

    public static @NotNull Type fromBase(@NotNull BaseType type) {
        switch (type) {
            case VOID:
                return VOID;
            case INT:
                return INT;
            case FLOAT:
                return FLOAT;
            case STRING:
                return STRING;
            case BOOL:
                return BOOL;
            case ANY:
                return ANY;
            case NIL:
                return NIL;
            case COORD:
                return COORD;
            case SLOT:
                return SLOT;
            case LOCATION:
                return LOCATION;
            case GAME_MODE:
                return GAME_MODE;
            case TARGET:
                return TARGET;
            case WEATHER:
                return WEATHER;
            case TIME:
                return TIME;
            case NAMESPACE:
                return NAMESPACE;
            case EFFECT:
                return EFFECT;
            case ENCHANT:
                return ENCHANT;
            case MODE:
                return MODE;
            case LOBBY:
                return LOBBY;
            case SOUND:
                return SOUND;
            case FLAG:
                return FLAG;
            case MATERIAL:
                return MATERIAL;
            case VECTOR:
                return VECTOR;
            case EXECUTOR:
                return EXECUTOR;
            case COMPARATOR:
                return COMPARATOR;
            case ITEM_COMPARATOR:
                return ITEM_COMPARATOR;
            case COMPARATOR_TARGET:
                return COMPARATOR_TARGET;
            case COMPARATOR_AMOUNT:
                return COMPARATOR_AMOUNT;
            case PERMISSION:
                return PERMISSION;
            default:
                throw new IllegalArgumentException("Unexpected value: " + type);
        }
    }
}
