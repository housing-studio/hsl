package org.housingstudio.hsl.compiler.ast.impl.type;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a predefined registry of builtin HSL types.
 */
@UtilityClass
public class Types {
    // primitive types
    public Type VOID   = new StaticType(BaseType.VOID);
    public Type INT    = new StaticType(BaseType.INT);
    public Type FLOAT  = new StaticType(BaseType.FLOAT);
    public Type STRING = new StaticType(BaseType.STRING);
    public Type BOOL   = new StaticType(BaseType.BOOL);
    public Type ANY    = new StaticType(BaseType.ANY);
    public Type NIL    = new StaticType(BaseType.NIL);
    public Type COORD = new StaticType(BaseType.COORD);

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
}
