package org.housingstudio.hsl.compiler.parser.impl.value;

import org.housingstudio.hsl.compiler.ast.impl.type.ArrayType;
import org.housingstudio.hsl.compiler.ast.impl.type.BaseType;
import org.housingstudio.hsl.compiler.ast.impl.type.StaticType;
import org.housingstudio.hsl.compiler.ast.impl.type.Type;
import org.housingstudio.hsl.compiler.parser.AstParser;
import org.housingstudio.hsl.compiler.parser.ParserAlgorithm;
import org.housingstudio.hsl.compiler.parser.ParserContext;
import org.housingstudio.hsl.compiler.error.Errno;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TypeParser extends ParserAlgorithm<Type> {
    /**
     * Parse the next {@link Type} node from the token stream.
     *
     * @param parser the AST node parser
     * @param context the token parser context
     *
     * @return the next {@link Type} node from the token stream
     */
    @Override
    public @NotNull Type parse(@NotNull AstParser parser, @NotNull ParserContext context) {
        int dimensions = 0;
        List<Token> dimensionTokens = new ArrayList<>();
        while (peek().is(TokenType.LBRACKET)) {
            dimensionTokens.add(get(TokenType.LBRACKET));
            dimensionTokens.add(get(TokenType.RBRACKET));
            dimensions++;
        }

        Token typeToken;
        BaseType type;
        if (peek().is(TokenType.TYPE)) {
            typeToken = get(TokenType.TYPE);
            switch (typeToken.value()) {
                case "int":
                    type = BaseType.INT;
                    break;
                case "float":
                    type = BaseType.FLOAT;
                    break;
                case "string":
                    type = BaseType.STRING;
                    break;
                case "bool":
                    type = BaseType.BOOL;
                    break;
                case "any":
                    type = BaseType.ANY;
                    break;
                default:
                    context.error(
                        Errno.UNEXPECTED_TYPE, "unexpected type value", typeToken, "invalid type name"
                    );
                    throw new UnsupportedOperationException("Invalid type name: " + typeToken.value());
            }
        } else if (peek().is(TokenType.IDENTIFIER)) {
            typeToken = get(TokenType.IDENTIFIER);
            switch (typeToken.value()) {
                case "Slot":
                    type = BaseType.SLOT;
                    break;
                case "Location":
                    type = BaseType.LOCATION;
                    break;
                case "GameMode":
                    type = BaseType.GAME_MODE;
                    break;
                case "Target":
                    type = BaseType.TARGET;
                    break;
                case "Weather":
                    type = BaseType.WEATHER;
                    break;
                case "Time":
                    type = BaseType.TIME;
                    break;
                case "Namespace":
                    type = BaseType.NAMESPACE;
                    break;
                case "Effect":
                    type = BaseType.EFFECT;
                    break;
                case "Enchant":
                    type = BaseType.ENCHANT;
                    break;
                case "Mode":
                    type = BaseType.MODE;
                    break;
                case "Lobby":
                    type = BaseType.LOBBY;
                    break;
                case "Sound":
                    type = BaseType.SOUND;
                    break;
                case "Flag":
                    type = BaseType.FLAG;
                    break;
                case "Material":
                    type = BaseType.MATERIAL;
                    break;
                case "Vector":
                    type = BaseType.VECTOR;
                    break;
                case "Executor":
                    type = BaseType.EXECUTOR;
                    break;
                case "Comparator":
                    type = BaseType.COMPARATOR;
                    break;
                case "ItemComparator":
                    type = BaseType.ITEM_COMPARATOR;
                    break;
                case "ComparatorTarget":
                    type = BaseType.COMPARATOR_TARGET;
                    break;
                case "ComparatorAmount":
                    type = BaseType.COMPARATOR_AMOUNT;
                    break;
                case "Permission":
                    type = BaseType.PERMISSION;
                    break;
                default:
                    context.error(
                        Errno.UNEXPECTED_TYPE, "expected type identifier", peek(), "invalid type name"
                    );
                    throw new UnsupportedOperationException("Invalid type name: " + peek().value());
            }
        } else {
            context.error(
                Errno.UNEXPECTED_TYPE, "expected type or identifier", peek(), "invalid type name"
            );
            throw new UnsupportedOperationException("Invalid type name: " + peek().value());
        }

        if (dimensions > 0)
            return new ArrayType(type, typeToken, dimensions, dimensionTokens);

        return new StaticType(type, typeToken);
    }

    public static boolean isTypeIdentifier(@NotNull String identifier) {
        switch (identifier) {
            case "Slot":
            case "Location":
            case "GameMode":
            case "Target":
            case "Weather":
            case "Time":
            case "Namespace":
            case "Effect":
            case "Enchant":
            case "Mode":
            case "Lobby":
            case "Sound":
            case "Flag":
            case "Material":
            case "Vector":
            case "Executor":
            case "Comparator":
            case "ItemComparator":
            case "ComparatorTarget":
            case "ComparatorAmount":
            case "Permission":
                return true;
            default:
                return false;
        }
    }
}
