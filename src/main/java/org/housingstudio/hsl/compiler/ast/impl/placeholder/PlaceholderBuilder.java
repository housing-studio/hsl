package org.housingstudio.hsl.compiler.ast.impl.placeholder;

import org.housingstudio.hsl.compiler.ast.impl.declaration.Method;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Parameter;
import org.housingstudio.hsl.compiler.ast.impl.scope.Scope;
import org.housingstudio.hsl.compiler.ast.impl.type.Type;
import org.housingstudio.hsl.compiler.ast.impl.type.Types;
import org.housingstudio.hsl.compiler.parser.impl.action.ArgAccess;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class PlaceholderBuilder {
    private Token name;
    private Type returnType = Types.VOID;
    private List<Parameter> parameters = new ArrayList<>();
    private Function<ArgAccess, Result> mapper;

    public @NotNull PlaceholderBuilder name(@NotNull String name) {
        this.name = Token.of(TokenType.IDENTIFIER, name);
        return this;
    }

    public @NotNull PlaceholderBuilder returnType(@NotNull Type returnType) {
        this.returnType = returnType;
        return this;
    }

    public @NotNull PlaceholderBuilder parameters(@NotNull Parameter... parameters) {
        this.parameters = Arrays.asList(parameters);
        return this;
    }

    public @NotNull PlaceholderBuilder mapper(@NotNull Function<ArgAccess, Result> mapper) {
        this.mapper = mapper;
        return this;
    }

    public @NotNull Placeholder build() {
        Method method = new Method(Collections.emptyList(), name, returnType, parameters, Scope.EMPTY);
        return new Placeholder(name.value(), method, mapper);
    }
}
