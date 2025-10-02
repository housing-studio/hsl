package org.housingstudio.hsl.runtime.natives;

import lombok.RequiredArgsConstructor;
import org.housingstudio.hsl.compiler.ast.impl.value.ConstantLiteral;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.housingstudio.hsl.runtime.vm.Frame;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class InvocationContext {
    private final @NotNull Frame parent;
    private final @NotNull Frame frame;

    public @NotNull String getString(@NotNull String parameter) {
        return get(parameter).asConstantValue();
    }

    public float getFloat(@NotNull String parameter) {
        String string = getString(parameter);
        return Float.parseFloat(string);
    }

    public void returnInt(int value) {
        frame.returnValue(new ConstantLiteral(Token.of(TokenType.INT, String.valueOf(value))));
    }

    public void returnFloat(float value) {
        frame.returnValue(new ConstantLiteral(Token.of(TokenType.FLOAT, String.valueOf(value))));
    }

    private @NotNull Value get(@NotNull String key) {
        Value value = parent.locals().get(key);
        if (value == null)
            throw new IllegalStateException("No value found for parameter " + key);

        // recursively load the visitable value ast, until there is something left to be loaded
        while (true) {
            Value loaded = value.load();
            // loading refers to itself, there is nothing left to load
            if (value == loaded)
                break;
            value = loaded;
        }
        return value;
    }
}
