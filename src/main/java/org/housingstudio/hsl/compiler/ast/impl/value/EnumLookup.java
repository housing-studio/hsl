package org.housingstudio.hsl.compiler.ast.impl.value;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Enum;
import org.housingstudio.hsl.compiler.ast.impl.type.BaseType;
import org.housingstudio.hsl.compiler.ast.impl.type.Type;
import org.housingstudio.hsl.compiler.error.Errno;
import org.housingstudio.hsl.compiler.error.Notification;
import org.housingstudio.hsl.compiler.token.Token;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.ENUM_LOOKUP)
public class EnumLookup extends Value {
    private final @NotNull Token typeName;
    private final @NotNull Token memberName;
    private final @NotNull List<Value> arguments;

    @Override
    public void init() {
        Enum lookup = lookup();
        lookup.lookupMember(memberName);
    }

    private @NotNull Enum lookup() {
        Type type = resolveType(typeName.value());
        if (type == null) {
            context.errorPrinter().print(
                Notification.error(Errno.UNKNOWN_TYPE, "cannot resolve name from scope")
                    .error("cannot find enum in this scope", typeName)
                    .note("did you misspell the name, or forgot to declare the enum?")
            );
            throw new IllegalStateException("Cannot resolve type " + typeName.value());
        }

        if (type.base() != BaseType.ENUM) {
            context.errorPrinter().print(
                Notification.error(Errno.UNEXPECTED_TYPE, "unexpected type")
                    .error("expected enum, but found: " + type.base(), typeName)
            );
            throw new IllegalStateException("Not an enum type " + typeName.value());
        }

        return (Enum) type;
    }

    /**
     * Retrieve the type of the held value. This result will be used to inter types for untyped variables.
     *
     * @return the resolved value of the type
     */
    @Override
    public @NotNull Type getValueType() {
        Enum lookup = lookup();
        if (lookup.kind() == Enum.Kind.VALUE)
            return lookup.alias();
        return lookup;
    }

    /**
     * Get the constant string representation of the value.
     * <p>
     * Housing variables are handled as string by default, this format is the input for housing variables.
     *
     * @return the final string value
     */
    @Override
    public @NotNull String asConstantValue() {
        return load().asConstantValue();
    }

    @Override
    public @NotNull Value load() {
        Enum lookup = lookup();
        if (lookup.kind() != Enum.Kind.VALUE)
            throw new IllegalStateException("Sum enum types are not supported yet");

        Enum.Member member = lookup.lookupMember(memberName);
        return member.value().load();
    }

    /**
     * Returns a string representation of the implementing class.
     *
     * @return the class debug information
     */
    @Override
    public @NotNull String print() {
        return typeName + "::" + memberName;
    }
}
