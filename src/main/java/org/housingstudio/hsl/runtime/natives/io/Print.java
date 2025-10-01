package org.housingstudio.hsl.runtime.natives.io;

import lombok.experimental.UtilityClass;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Macro;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Parameter;
import org.housingstudio.hsl.compiler.ast.impl.type.Types;
import org.housingstudio.hsl.runtime.natives.MacroBuilder;

@UtilityClass
public class Print {
    public final Macro PRINTLN = new MacroBuilder()
        .name("println")
        .parameters(Parameter.required("o", Types.STRING))
        .callback(ctx -> {
            String o = ctx.getString("o");
            System.out.println(o);
        })
        .build();

    public final Macro PRINT = new MacroBuilder()
        .name("print")
        .parameters(Parameter.required("o", Types.STRING))
        .callback(ctx -> {
            String o = ctx.getString("o");
            System.out.print(o);
        })
        .build();
}
