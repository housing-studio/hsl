package org.housingstudio.hsl.importer.interaction.defaults;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Collections;

@UtilityClass
public class Defaults {
    /**
     * Retrieve the default value of the specified action property.
     * <p>
     * This value is compared against the arguments provided by the user. If the user specified the same
     * value as the default value, the interaction is skipped to save time.
     *
     * @param field the action property field
     * @return the default property value, or {@code null} if the property does not have a default value
     */
    public @Nullable Object getDefaultValue(@NotNull Field field) {
        if (field.isAnnotationPresent(DefaultInt.class))
            return field.getDeclaredAnnotation(DefaultInt.class).value();
        else if (field.isAnnotationPresent(DefaultFloat.class))
            return field.getDeclaredAnnotation(DefaultFloat.class).value();
        else if (field.isAnnotationPresent(DefaultString.class))
            return field.getDeclaredAnnotation(DefaultString.class).value();
        else if (field.isAnnotationPresent(DefaultBoolean.class))
            return field.getDeclaredAnnotation(DefaultBoolean.class).value();
        else if (field.isAnnotationPresent(DefaultMode.class))
            return field.getDeclaredAnnotation(DefaultMode.class).value();
        else if (field.isAnnotationPresent(DefaultNamespace.class))
            return field.getDeclaredAnnotation(DefaultNamespace.class).value();
        else if (field.isAnnotationPresent(DefaultTime.class))
            return field.getDeclaredAnnotation(DefaultTime.class).value();
        else if (field.isAnnotationPresent(Required.class))
            return null;
        else if (field.isAnnotationPresent(DefaultEmpty.class))
            return Collections.emptyList();
        else
            throw new IllegalStateException("Field " + field.getName() + " does not specify a default value");
    }
}
