package org.housingstudio.hsl.importer;

import org.housingstudio.hsl.importer.impl.DefaultModuleBridge;
import org.jetbrains.annotations.NotNull;

public class ModuleBridgeProvider {
    private static @NotNull ModuleBridge provide() {
        return new DefaultModuleBridge();
    }
}
