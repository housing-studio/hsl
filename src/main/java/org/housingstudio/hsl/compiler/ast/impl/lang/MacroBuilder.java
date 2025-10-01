package org.housingstudio.hsl.compiler.ast.impl.lang;

import org.housingstudio.hsl.compiler.ast.impl.declaration.Macro;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Parameter;
import org.housingstudio.hsl.compiler.ast.impl.type.Type;
import org.housingstudio.hsl.compiler.ast.impl.type.Types;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class MacroBuilder {
    private Token name;
    private Type returnType = Types.VOID;
    private List<Parameter> parameters;
    private Consumer<InvocationContext> callback;

    public @NotNull MacroBuilder name(@NotNull String name) {
        this.name = Token.of(TokenType.IDENTIFIER, name);
        return this;
    }

    public @NotNull MacroBuilder returnType(@NotNull Type returnType) {
        this.returnType = returnType;
        return this;
    }

    public @NotNull MacroBuilder parameters(@NotNull Parameter... parameters) {
        this.parameters = Arrays.asList(parameters);
        return this;
    }

    public @NotNull MacroBuilder callback(@NotNull Consumer<InvocationContext> callback) {
        this.callback = callback;
        return this;
    }

    public @NotNull Macro build() {
        return new NativeMacro(name, returnType, parameters, callback);
    }
}
