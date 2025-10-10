package org.housingstudio.hsl.compiler.ast.impl.placeholder.impl;

import lombok.experimental.UtilityClass;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Parameter;
import org.housingstudio.hsl.compiler.ast.impl.placeholder.*;
import org.housingstudio.hsl.compiler.ast.impl.type.Types;
import org.housingstudio.hsl.compiler.ast.impl.value.ConstantLiteral;

@Struct("Date")
@UtilityClass
public class Date {
    @Fn
    public final Placeholder UNIX_MILLIS = new PlaceholderBuilder()
        .name("unixMillis")
        .returnType(Types.INT)
        .mapper(args -> new Result("%date.unix.ms%"))
        .build();

    @Fn
    public final Placeholder UNIX_SECONDS = new PlaceholderBuilder()
        .name("unixSeconds")
        .returnType(Types.INT)
        .mapper(args -> new Result("%date.unix%"))
        .build();

    @Fn
    public final Placeholder DAY = new PlaceholderBuilder()
        .name("day")
        .parameters(Parameter.optional("timezone", Types.STRING, ConstantLiteral.ofString("UTC")))
        .returnType(Types.INT)
        .mapper(
            args -> new Result("%date.day/{timezone}%")
                .set("timezone", args.getString("timezone"))
        )
        .build();

    @Fn
    public final Placeholder MONTH = new PlaceholderBuilder()
        .name("month")
        .parameters(Parameter.optional("timezone", Types.STRING, ConstantLiteral.ofString("UTC")))
        .returnType(Types.INT)
        .mapper(
            args -> new Result("%date.month/{timezone}%")
                .set("timezone", args.getString("timezone"))
        )
        .build();

    @Fn
    public final Placeholder YEAR = new PlaceholderBuilder()
        .name("year")
        .parameters(Parameter.optional("timezone", Types.STRING, ConstantLiteral.ofString("UTC")))
        .returnType(Types.INT)
        .mapper(
            args -> new Result("%date.year/{timezone}%")
                .set("timezone", args.getString("timezone"))
        )
        .build();

    @Fn
    public final Placeholder HOUR = new PlaceholderBuilder()
        .name("hour")
        .parameters(Parameter.optional("timezone", Types.STRING, ConstantLiteral.ofString("UTC")))
        .returnType(Types.INT)
        .mapper(
            args -> new Result("%date.hour/{timezone}%")
                .set("timezone", args.getString("timezone"))
        )
        .build();

    @Fn
    public final Placeholder MINUTE = new PlaceholderBuilder()
        .name("minute")
        .parameters(Parameter.optional("timezone", Types.STRING, ConstantLiteral.ofString("UTC")))
        .returnType(Types.INT)
        .mapper(
            args -> new Result("%date.minute/{timezone}%")
                .set("timezone", args.getString("timezone"))
        )
        .build();

    @Fn
    public final Placeholder SECONDS = new PlaceholderBuilder()
        .name("seconds")
        .parameters(Parameter.optional("timezone", Types.STRING, ConstantLiteral.ofString("UTC")))
        .returnType(Types.INT)
        .mapper(
            args -> new Result("%date.seconds/{timezone}%")
                .set("timezone", args.getString("timezone"))
        )
        .build();
}
