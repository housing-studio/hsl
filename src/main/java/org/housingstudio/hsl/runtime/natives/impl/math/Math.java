package org.housingstudio.hsl.runtime.natives.impl.math;

import lombok.experimental.UtilityClass;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Constant;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Macro;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Parameter;
import org.housingstudio.hsl.compiler.ast.impl.type.Types;
import org.housingstudio.hsl.runtime.natives.RegisterNative;
import org.housingstudio.hsl.runtime.natives.builder.ConstantBuilder;
import org.housingstudio.hsl.runtime.natives.builder.MacroBuilder;

@UtilityClass
public class Math {
    @RegisterNative
    public final Constant PI = new ConstantBuilder()
        .name("PI")
        .floatValue(3.141592653589793F)
        .build();

    @RegisterNative
    public final Constant E = new ConstantBuilder()
        .name("E")
        .floatValue(2.718281828459045F)
        .build();

    @RegisterNative
    public final Macro SIN = new MacroBuilder()
        .name("sin")
        .parameters(Parameter.required("x", Types.FLOAT))
        .returnType(Types.FLOAT)
        .callback(ctx -> {
            float x = ctx.getFloat("x");
            float sin = (float) java.lang.Math.sin(x);
            ctx.returnFloat(sin);
        })
        .build();

    @RegisterNative
    public final Macro COS = new MacroBuilder()
        .name("cos")
        .parameters(Parameter.required("x", Types.FLOAT))
        .returnType(Types.FLOAT)
        .callback(ctx -> {
            float x = ctx.getFloat("x");
            float cos = (float) java.lang.Math.cos(x);
            ctx.returnFloat(cos);
        })
        .build();

    @RegisterNative
    public final Macro SQRT = new MacroBuilder()
        .name("sqrt")
        .parameters(Parameter.required("x", Types.FLOAT))
        .returnType(Types.FLOAT)
        .callback(ctx -> {
            float x = ctx.getFloat("x");
            float sqrt = (float) java.lang.Math.sqrt(x);
            ctx.returnFloat(sqrt);
        })
        .build();

    @RegisterNative
    public final Macro CEIL = new MacroBuilder()
        .name("ceil")
        .parameters(Parameter.required("x", Types.FLOAT))
        .returnType(Types.FLOAT)
        .callback(ctx -> {
            float x = ctx.getFloat("x");
            float ceil = (float) java.lang.Math.ceil(x);
            ctx.returnFloat(ceil);
        })
        .build();

    @RegisterNative
    public final Macro FLOOR = new MacroBuilder()
        .name("floor")
        .parameters(Parameter.required("x", Types.FLOAT))
        .returnType(Types.FLOAT)
        .callback(ctx -> {
            float x = ctx.getFloat("x");
            float floor = (float) java.lang.Math.floor(x);
            ctx.returnFloat(floor);
        })
        .build();

    @RegisterNative
    public final Macro POW = new MacroBuilder()
        .name("pow")
        .parameters(
            Parameter.required("base", Types.FLOAT),
            Parameter.required("exponent", Types.FLOAT)
        )
        .returnType(Types.FLOAT)
        .callback(ctx -> {
            float base = ctx.getFloat("base");
            float exponent = ctx.getFloat("exponent");
            float pow = (float) java.lang.Math.pow(base, exponent);
            ctx.returnFloat(pow);
        })
        .build();

    @RegisterNative
    public final Macro ROUND = new MacroBuilder()
        .name("round")
        .parameters(Parameter.required("x", Types.FLOAT))
        .returnType(Types.INT)
        .callback(ctx -> {
            float x = ctx.getFloat("x");
            int round = java.lang.Math.round(x);
            ctx.returnInt(round);
        })
        .build();

    @RegisterNative
    public final Macro RAD = new MacroBuilder()
        .name("rad")
        .parameters(Parameter.required("x", Types.FLOAT))
        .returnType(Types.INT)
        .callback(ctx -> {
            float x = ctx.getFloat("x");
            float rad = (float) java.lang.Math.toRadians(x);
            ctx.returnFloat(rad);
        })
        .build();

    @RegisterNative
    public final Macro DEG = new MacroBuilder()
        .name("deg")
        .parameters(Parameter.required("x", Types.FLOAT))
        .returnType(Types.INT)
        .callback(ctx -> {
            float x = ctx.getFloat("x");
            float deg = (float) java.lang.Math.toDegrees(x);
            ctx.returnFloat(deg);
        })
        .build();
}
