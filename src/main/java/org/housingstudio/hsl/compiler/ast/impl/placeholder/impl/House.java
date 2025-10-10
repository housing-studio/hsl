package org.housingstudio.hsl.compiler.ast.impl.placeholder.impl;

import lombok.experimental.UtilityClass;
import org.housingstudio.hsl.compiler.ast.impl.placeholder.*;
import org.housingstudio.hsl.compiler.ast.impl.type.Types;

@Struct("House")
@UtilityClass
public class House {
    @Fn
    public final Placeholder NAME = new PlaceholderBuilder()
        .name("name")
        .returnType(Types.STRING)
        .mapper(args -> new Result("%house.name%"))
        .build();

    @Fn
    public final Placeholder GUESTS = new PlaceholderBuilder()
        .name("guests")
        .returnType(Types.INT)
        .mapper(args -> new Result("%house.guests%"))
        .build();

    @Fn
    public final Placeholder PLAYERS = new PlaceholderBuilder()
        .name("players")
        .returnType(Types.INT)
        .mapper(args -> new Result("%house.players%"))
        .build();

    @Fn
    public final Placeholder COOKIES = new PlaceholderBuilder()
        .name("cookies")
        .returnType(Types.INT)
        .mapper(args -> new Result("%house.cookies%"))
        .build();

    @Fn
    public final Placeholder VISITING_RULES = new PlaceholderBuilder()
        .name("visitingRules")
        .returnType(Types.STRING)
        .mapper(args -> new Result("%house.visitingrules%"))
        .build();
}
