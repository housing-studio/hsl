package org.housingstudio.hsl.importer.platform;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.io.File;

@UtilityClass
public class FileLib {
    private final String MODULE_PATH = "./config/ChatTriggers/modules/HousingStudio";

    public @NotNull File getModuleFile(@NotNull String name) {
        return new File(MODULE_PATH, name);
    }
}
