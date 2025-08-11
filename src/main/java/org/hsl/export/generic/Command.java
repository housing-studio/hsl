package org.hsl.export.generic;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.compiler.ast.impl.value.Argument;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
public class Command {
    private @NotNull String name;
    private @NotNull Argument argument;

    @SerializedName("required-group-priority")
    private @Nullable Integer requiredGroupPriority;

    private boolean listed;
}
