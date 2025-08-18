package org.housingstudio.hsl.importer.interaction.defaults;

import org.housingstudio.hsl.type.Time;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DefaultTime {
    @NotNull Time value();
}
