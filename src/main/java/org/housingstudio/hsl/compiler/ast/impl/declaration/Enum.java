package org.housingstudio.hsl.compiler.ast.impl.declaration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.impl.type.BaseType;
import org.housingstudio.hsl.compiler.ast.impl.type.Type;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.ast.impl.value.builtin.NullValue;
import org.housingstudio.hsl.compiler.codegen.hierarchy.Children;
import org.housingstudio.hsl.compiler.debug.Printable;
import org.housingstudio.hsl.compiler.error.Errno;
import org.housingstudio.hsl.compiler.error.Notification;
import org.housingstudio.hsl.compiler.token.Token;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.ENUM)
public class Enum extends Node implements Type, Printable {
    private final @NotNull Token name;
    private final @Nullable Type alias;
    private final @NotNull Kind kind;

    @Children
    private final List<Member> members;

    @Override
    public void init() {
        validateMemberValues();
    }

    public @NotNull Enum.Member lookupMember(@NotNull Token name) {
        for (Member member : members) {
            if (member.name.value().equals(name.value()))
                return member;
        }

        context.errorPrinter().print(
            Notification.error(Errno.MISSING_ENUM_MEMBER, "missing enum member")
                .error("enum `" + this.name.value() + " has no member `" + name.value() + "`", name)
        );
        throw new IllegalStateException("No such enum member '" + name.value() + "'");
    }

    private void validateMemberValues() {
        if (kind != Kind.VALUE)
            return;

        for (Member member : members) {
            Type type = member.value.getValueType();
            if (!type.matches(alias)) {
                context.errorPrinter().print(
                    Notification.error(Errno.ENUM_MEMBER_VALUE_TYPE_MISMATCH, "enum member value type mismatch")
                        .error("unexpected enum member value type: " + type.print(), member.name)
                        .note("member value types must match enum type alias")
                );
                throw new IllegalStateException("Enum member value type mismatch: " + name.value());
            }
        }
    }

    /**
     * Returns a string representation of the implementing class.
     *
     * @return the class debug information
     */
    @Override
    public @NotNull String print() {
        StringBuilder builder = new StringBuilder("enum ").append(name.value());
        if (kind == Kind.VALUE)
            builder.append(": ").append(alias.print());
        builder.append(" {\n");

        Iterator<Member> iterator = members.iterator();
        while (iterator.hasNext()) {
            Member member = iterator.next();
            builder.append(member.print());
            if (iterator.hasNext())
                builder.append(", ");
            builder.append("\n");
        }

        builder.append("}\n");
        return builder.toString();
    }

    @Override
    public @NotNull BaseType base() {
        return BaseType.ENUM;
    }

    @Override
    public @NotNull Value defaultValue() {
        return new NullValue();
    }

    /**
     * Indicate, whether the specified node matches the criteria of the matcher.
     *
     * @param other the node to compare to
     * @return {@code true} if the node matches the criteria, {@code false} otherwise
     */
    @Override
    public boolean matches(@NotNull Type other) {
        if (!(other instanceof Enum))
            return false;

        Enum o = (Enum) other;
        // should be a more precise check, but currently everything is one package level
        // and type names are unique
        return o.name.value().equals(name.value());
    }

    @Override
    public @NotNull List<Token> tokens() {
        return Collections.singletonList(name);
    }

    public enum Kind {
        SUM,
        VALUE
    }

    @RequiredArgsConstructor
    @Accessors(fluent = true)
    @Getter
    @NodeInfo(type = NodeType.ENUM_MEMBER)
    public static class Member extends Node implements Printable {
        private final @NotNull Token name;
        private final @NotNull Enum.Kind kind;
        private final @NotNull List<Type> fields;
        private final @Nullable Value value;

        /**
         * Returns a string representation of the implementing class.
         *
         * @return the class debug information
         */
        @Override
        public @NotNull String print() {
            StringBuilder builder = new StringBuilder("\t").append(name.value());
            if (kind == Enum.Kind.VALUE)
                builder.append(" = ").append(value.print());
            else {
                builder.append("(");
                Iterator<Type> iterator = fields.iterator();
                while (iterator.hasNext()) {
                    Type type = iterator.next();
                    builder.append(type.print());
                    if (iterator.hasNext())
                        builder.append(", ");
                }
                builder.append(")");
            }
            return builder.toString();
        }
    }
}
