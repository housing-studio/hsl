package org.housingstudio.hsl.compiler.ast.impl.placeholder.impl;

import lombok.experimental.UtilityClass;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Parameter;
import org.housingstudio.hsl.compiler.ast.impl.placeholder.*;
import org.housingstudio.hsl.compiler.ast.impl.type.Types;
import org.housingstudio.hsl.compiler.ast.impl.value.ConstantLiteral;

@Struct("Stat")
@UtilityClass
public class Stat {
    @Fn
    public final Placeholder GLOBAL = new PlaceholderBuilder()
        .name("global")
        .parameters(
            Parameter.required("key", Types.STRING),
            Parameter.optional("fallback", Types.ANY, ConstantLiteral.ofString(""))
        )
        .returnType(Types.ANY)
        .mapper(
            args -> new Result("%stat.global/{key} {fallback}%")
                .set("key", args.getString("key"))
                .set("fallback", args.getString("fallback"))
        )
        .build();

    @Fn
    public final Placeholder PLAYER = new PlaceholderBuilder()
        .name("player")
        .parameters(
            Parameter.required("key", Types.STRING),
            Parameter.optional("fallback", Types.ANY, ConstantLiteral.ofString(""))
        )
        .returnType(Types.ANY)
        .mapper(
            args -> new Result("%stat.player/{key} {fallback}%")
                .set("key", args.getString("key"))
                .set("fallback", args.getString("fallback"))
        )
        .build();

    @Fn
    public final Placeholder TEAM = new PlaceholderBuilder()
        .name("team")
        .parameters(
            Parameter.required("key", Types.STRING),
            Parameter.optional("team", Types.STRING, ConstantLiteral.ofString("")),
            Parameter.optional("fallback", Types.ANY, ConstantLiteral.ofString(""))
        )
        .returnType(Types.ANY)
        .mapper(
            args -> new Result("%stat.player/{key} {fallback}%")
                .set("key", args.getString("key"))
                .set("team", args.getString("team"))
                .set("fallback", args.getString("fallback"))
        )
        .build();
}
