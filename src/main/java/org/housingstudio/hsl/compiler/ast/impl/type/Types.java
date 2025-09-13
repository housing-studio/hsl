package org.housingstudio.hsl.compiler.ast.impl.type;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a predefined registry of builtin HSL types.
 */
@UtilityClass
public class Types {
    // primitive types
    public @NotNull Type VOID   = new StaticType(BaseType.VOID);
    public @NotNull Type INT    = new StaticType(BaseType.INT);
    public @NotNull Type FLOAT  = new StaticType(BaseType.FLOAT);
    public @NotNull Type STRING = new StaticType(BaseType.STRING);
    public @NotNull Type BOOL   = new StaticType(BaseType.BOOL);
    public @NotNull Type ANY    = new StaticType(BaseType.ANY);
    public @NotNull Type NIL    = new StaticType(BaseType.NIL);

    // compound types
    public @NotNull Type SLOT              = new StaticType(BaseType.SLOT);
    public @NotNull Type LOCATION          = new StaticType(BaseType.LOCATION);
    public @NotNull Type GAME_MODE         = new StaticType(BaseType.GAME_MODE);
    public @NotNull Type TARGET            = new StaticType(BaseType.TARGET);
    public @NotNull Type WEATHER           = new StaticType(BaseType.WEATHER);
    public @NotNull Type TIME              = new StaticType(BaseType.TIME);
    public @NotNull Type NAMESPACE         = new StaticType(BaseType.NAMESPACE);
    public @NotNull Type EFFECT            = new StaticType(BaseType.EFFECT);
    public @NotNull Type ENCHANT           = new StaticType(BaseType.ENCHANT);
    public @NotNull Type MODE              = new StaticType(BaseType.MODE);
    public @NotNull Type LOBBY             = new StaticType(BaseType.LOBBY);
    public @NotNull Type SOUND             = new StaticType(BaseType.SOUND);
    public @NotNull Type FLAG              = new StaticType(BaseType.FLAG);
    public @NotNull Type MATERIAL          = new StaticType(BaseType.MATERIAL);
    public @NotNull Type VECTOR            = new StaticType(BaseType.VECTOR);
    public @NotNull Type EXECUTOR          = new StaticType(BaseType.EXECUTOR);
    public @NotNull Type COMPARATOR        = new StaticType(BaseType.COMPARATOR);
    public @NotNull Type ITEM_COMPARATOR   = new StaticType(BaseType.ITEM_COMPARATOR);
    public @NotNull Type COMPARATOR_TARGET = new StaticType(BaseType.COMPARATOR_TARGET);
    public @NotNull Type COMPARATOR_AMOUNT = new StaticType(BaseType.COMPARATOR_AMOUNT);
    public @NotNull Type PERMISSION        = new StaticType(BaseType.PERMISSION);
}
