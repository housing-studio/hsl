package org.housingstudio.hsl.runtime.natives.impl.lang;

import lombok.experimental.UtilityClass;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Macro;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Parameter;
import org.housingstudio.hsl.compiler.ast.impl.type.Types;
import org.housingstudio.hsl.runtime.natives.RegisterNative;
import org.housingstudio.hsl.runtime.natives.builder.MacroBuilder;

@UtilityClass
public class Number {
    @RegisterNative
    public final Macro IS_NAN = new MacroBuilder()
        .name("isNaN")
        .parameters(Parameter.required("x", Types.FLOAT))
        .returnType(Types.BOOL)
        .callback(ctx -> {
            float x = ctx.getFloat("x");
            boolean nan = Float.isNaN(x);
            ctx.returnBoolean(nan);
        })
        .build();

    @RegisterNative
    public final Macro IS_INFINITE = new MacroBuilder()
        .name("isInfinite")
        .parameters(Parameter.required("x", Types.FLOAT))
        .returnType(Types.BOOL)
        .callback(ctx -> {
            float x = ctx.getFloat("x");
            boolean infinite = Float.isInfinite(x);
            ctx.returnBoolean(infinite);
        })
        .build();

    @RegisterNative
    public final Macro IS_FINITE = new MacroBuilder()
        .name("isFinite")
        .parameters(Parameter.required("x", Types.FLOAT))
        .returnType(Types.BOOL)
        .callback(ctx -> {
            float x = ctx.getFloat("x");
            boolean finite = Float.isFinite(x);
            ctx.returnBoolean(finite);
        })
        .build();
}
