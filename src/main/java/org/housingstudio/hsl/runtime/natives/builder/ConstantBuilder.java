package org.housingstudio.hsl.runtime.natives.builder;

import org.housingstudio.hsl.compiler.ast.impl.declaration.Constant;
import org.housingstudio.hsl.compiler.ast.impl.value.ConstantLiteral;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

public class ConstantBuilder {
    private Token name;
    private Value value;

    public @NotNull ConstantBuilder name(@NotNull String name) {
        this.name = Token.of(TokenType.IDENTIFIER, name);
        return this;
    }

    public @NotNull ConstantBuilder floatValue(double value) {
        return value(new ConstantLiteral(Token.of(TokenType.FLOAT, String.valueOf(value))));
    }

    public @NotNull ConstantBuilder value(@NotNull Value value) {
        this.value = value;
        return this;
    }

    public @NotNull Constant build() {
        return new Constant(name, value);
    }
}
