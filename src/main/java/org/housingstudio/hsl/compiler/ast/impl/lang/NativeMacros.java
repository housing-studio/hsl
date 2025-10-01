package org.housingstudio.hsl.compiler.ast.impl.lang;

import lombok.experimental.UtilityClass;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Macro;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Parameter;
import org.housingstudio.hsl.compiler.ast.impl.type.Types;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class NativeMacros {
    public final Map<String, Macro> LOOKUP = new HashMap<>();

    public void init() {
        LOOKUP.put("println", PRINTLN);
        LOOKUP.put("print", PRINT);
    }

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
