package org.housingstudio.hsl.importer;

import org.housingstudio.hsl.importer.impl.DefaultModuleBridge;
import org.jetbrains.annotations.NotNull;

public class ModuleBridgeProvider {
    private static ModuleBridge bridge;

    public static @NotNull ModuleBridge provide() {
        if (bridge == null)
            bridge = new DefaultModuleBridge();
        return bridge;
    }
}
