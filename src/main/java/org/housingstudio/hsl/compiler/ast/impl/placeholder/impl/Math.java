package org.housingstudio.hsl.compiler.ast.impl.placeholder.impl;

import lombok.experimental.UtilityClass;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Parameter;
import org.housingstudio.hsl.compiler.ast.impl.placeholder.*;
import org.housingstudio.hsl.compiler.ast.impl.type.Types;

@Struct("Math")
@UtilityClass
public class Math {
    @Fn
    public final Placeholder RAND_INT = new PlaceholderBuilder()
        .name("randInt")
        .parameters(
            Parameter.required("min", Types.INT),
            Parameter.required("max", Types.INT)
        )
        .returnType(Types.INT)
        .mapper(
            args -> new Result("%random.int/{min} {max}%")
                .set("min", args.getInt("min"))
                .set("max", args.getInt("max"))
        )
        .build();

    @Fn
    public final Placeholder RAND_FLOAT = new PlaceholderBuilder()
        .name("randFloat")
        .parameters(
            Parameter.required("min", Types.INT),
            Parameter.required("max", Types.INT)
        )
        .returnType(Types.INT)
        .mapper(
            args -> new Result("%random.decimal/{min} {max}%")
                .set("min", args.getFloat("min"))
                .set("max", args.getFloat("max"))
        )
        .build();
}
