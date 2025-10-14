package org.housingstudio.hsl.compiler.ast.impl.type;

import lombok.experimental.UtilityClass;
import org.housingstudio.hsl.compiler.ast.impl.value.ConstantLiteral;
import org.housingstudio.hsl.compiler.ast.impl.value.Coordinate;
import org.housingstudio.hsl.compiler.ast.impl.value.builtin.NullValue;
import org.housingstudio.hsl.compiler.ast.impl.value.builtin.SlotValue;
import org.housingstudio.hsl.std.slot.SlotType;
import org.housingstudio.hsl.std.slot.impl.StaticSlot;

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
}
