package org.housingstudio.hsl.compiler.ast.impl.placeholder.impl;

import lombok.experimental.UtilityClass;
import org.housingstudio.hsl.compiler.ast.impl.placeholder.*;
import org.housingstudio.hsl.compiler.ast.impl.type.Types;

@Struct("Server")
@UtilityClass
public class Server {
    @Fn
    public final Placeholder NAME = new PlaceholderBuilder()
        .name("name")
        .returnType(Types.STRING)
        .mapper(args -> new Result("%server.name%"))
        .build();

    @Fn
    public final Placeholder SHORT_NAME = new PlaceholderBuilder()
        .name("shortName")
        .returnType(Types.STRING)
        .mapper(args -> new Result("%server.shortname%"))
        .build();
}
