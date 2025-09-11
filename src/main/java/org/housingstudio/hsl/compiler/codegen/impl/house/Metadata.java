package org.housingstudio.hsl.compiler.codegen.impl.house;

import com.moandjiezana.toml.Toml;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
@ToString
public class Metadata {
    private @NotNull String id;
    private @Nullable String name;
    private @Nullable String author;
    private @Nullable List<String> contributors;
    private @Nullable String description;
    private @NotNull String version;

    public static @NotNull Metadata read(@NotNull File file) {
        Toml toml = new Toml().read(file);

        String id = toml.getString("package.id");
        String name = toml.getString("package.name");
        String author = toml.getString("package.author");
        List<String> contributors = toml.getList("package.contributors");
        String description = toml.getString("package.description");
        String version = toml.getString("package.version");

        if (id == null || version == null)
            throw new IllegalArgumentException("Required fields (id, version) missing in TOML file.");

        return new Metadata(id, name, author, contributors, description, version);
    }
}
