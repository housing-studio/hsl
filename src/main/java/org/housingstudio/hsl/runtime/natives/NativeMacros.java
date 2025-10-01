package org.housingstudio.hsl.runtime.natives;

import lombok.experimental.UtilityClass;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Macro;
import org.housingstudio.hsl.runtime.natives.io.Print;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class NativeMacros {
    public final Map<String, Macro> LOOKUP = new HashMap<>();

    public void init() {
        LOOKUP.put("println", Print.PRINTLN);
        LOOKUP.put("print", Print.PRINT);
    }
}
