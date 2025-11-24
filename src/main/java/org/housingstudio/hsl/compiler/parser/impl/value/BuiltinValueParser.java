package org.housingstudio.hsl.compiler.parser.impl.value;

import org.housingstudio.hsl.compiler.ast.impl.placeholder.Placeholder;
import org.housingstudio.hsl.compiler.ast.impl.placeholder.PlaceholderRegistry;
import org.housingstudio.hsl.compiler.ast.impl.placeholder.PlaceholderValue;
import org.housingstudio.hsl.compiler.ast.impl.value.Argument;
import org.housingstudio.hsl.compiler.ast.impl.value.EnumLookup;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.ast.impl.value.builtin.*;
import org.housingstudio.hsl.compiler.error.Notification;
import org.housingstudio.hsl.compiler.parser.AstParser;
import org.housingstudio.hsl.compiler.parser.ParserAlgorithm;
import org.housingstudio.hsl.compiler.parser.ParserContext;
import org.housingstudio.hsl.compiler.error.Errno;
import org.housingstudio.hsl.compiler.parser.impl.action.ArgAccess;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.housingstudio.hsl.std.*;
import org.housingstudio.hsl.std.location.Location;
import org.housingstudio.hsl.std.location.LocationType;
import org.housingstudio.hsl.std.location.impl.CustomLocation;
import org.housingstudio.hsl.std.location.impl.StaticLocation;
import org.housingstudio.hsl.std.ComparatorTarget;
import org.housingstudio.hsl.std.slot.Slot;
import org.housingstudio.hsl.std.slot.SlotType;
import org.housingstudio.hsl.std.slot.impl.CustomSlot;
import org.housingstudio.hsl.std.slot.impl.HotbarSlot;
import org.housingstudio.hsl.std.slot.impl.InventorySlot;
import org.housingstudio.hsl.std.slot.impl.StaticSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class BuiltinValueParser extends ParserAlgorithm<Value> {
    /**
     * Parse the next {@link Value} node from the token stream.
     *
     * @param parser  the AST node parser
     * @param context the token parser context
     * @return the next {@link Value} node from the token stream
     */
    @Override
    public @NotNull Value parse(@NotNull AstParser parser, @NotNull ParserContext context) {
        // parse the base type
        // Location::Spawn
        // ^^^^^^^^
        Token type = get(TokenType.IDENTIFIER);

        // skip the subtype separator
        // GameMode::Survival
        //         ^^
        get(TokenType.COLON);
        get(TokenType.COLON);

        switch (type.value()) {
            case "Location":
                return parseLocation(parser, context);
            case "Vector":
                return parseVector(parser, context);
            case "Slot":
                return parseSlot(parser, context);
            case "GameMode":
                return parseGameMode(context);
            case "Target":
                return parseTarget(context);
            case "Weather":
                return parseWeather(context);
            case "Time":
                return parseTime(context);
            case "Namespace":
                return parseNamespace(context);
            case "Effect":
                return parseEffect(context);
            case "Enchant":
                return parseEnchant(context);
            case "Mode":
                return parseMode(context);
            case "Lobby":
                return parseLobby(context);
            case "Sound":
                return parseSound(context);
            case "Flag":
                return parseFlag(context);
            case "Material":
                return parseMaterial(context);
            case "Executor":
                return parseExecutor(context);
            case "Comparator":
                return parseComparator(context);
            case "ItemComparator":
                return parseItemComparator(context);
            case "ComparatorTarget":
                return parseComparatorTarget(context);
            case "ComparatorAmount":
                return parseComparatorAmount(context);
            case "Permission":
                return parsePermission(context);
            case "Protocol":
                return parseProtocol(context);
            default:
                return parseEnumLookup(type, parser, context);
        }
    }

    private @NotNull Value parseEnumLookup(@NotNull Token type, @NotNull AstParser parser, @NotNull ParserContext context) {
        Token member = get(TokenType.IDENTIFIER);

        Value placeholder = parsePlaceholder(type, member, parser, context);
        if (placeholder != null)
            return placeholder;

        return new EnumLookup(type, member, Collections.emptyList());
    }

    private @Nullable Value parsePlaceholder(@NotNull Token type, @NotNull Token member, @NotNull AstParser parser, @NotNull ParserContext context) {
        Placeholder placeholder = PlaceholderRegistry.resolve(type.value(), member.value());
        if (placeholder == null)
            return null;

        List<Argument> arguments = parser.nextArgumentList();
        Map<String, Value> args = ArgumentParser.parseArguments(
            context, type, placeholder.method().parameters(), arguments
        );
        ArgAccess access = new ArgAccess(args);

        return new PlaceholderValue(placeholder, access);
    }

    private @NotNull Value parseSlot(@NotNull AstParser parser, @NotNull ParserContext context) {
        Token token = get(TokenType.IDENTIFIER);
        SlotType wrapped = Stream.of(SlotType.values())
            .filter(v -> v.format().equals(token.value()))
            .findFirst()
            .orElse(null);

        if (wrapped == null) {
            context.errorPrinter().print(
                Notification.error(Errno.INVALID_BUILTIN_TYPE, "invalid slot type")
                    .error("unrecognized slot type", token)
            );
            throw new UnsupportedOperationException("Invalid slot type: " + token);
        }

        Slot slot;
        switch (wrapped) {
            case HELMET:
            case CHESTPLATE:
            case LEGGINGS:
            case BOOTS:
            case FIRST_AVAILABLE:
            case HAND:
                slot = new StaticSlot(wrapped);
                break;
            case CUSTOM: {
                get(TokenType.LPAREN);
                Value value = parser.nextValue();
                get(TokenType.RPAREN);
                slot = new CustomSlot(value);
                break;
            }
            case INVENTORY: {
                get(TokenType.LPAREN);
                Value value = parser.nextValue();
                get(TokenType.RPAREN);
                slot = new InventorySlot(value);
                break;
            }
            case HOTBAR: {
                get(TokenType.LPAREN);
                Value value = parser.nextValue();
                get(TokenType.RPAREN);
                slot = new HotbarSlot(value);
                break;
            }
            default:
                context.errorPrinter().print(
                    Notification.error(Errno.INVALID_BUILTIN_TYPE, "invalid slot type")
                        .error("unrecognized slot type", token)
                );
                throw new UnsupportedOperationException("Invalid slot type: " + token);
        }

        return new SlotValue(slot);
    }

    private @NotNull Value parseLocation(@NotNull AstParser parser, @NotNull ParserContext context) {
        Token location = get(TokenType.IDENTIFIER);
        LocationType wrapped = Stream.of(LocationType.values())
            .filter(v -> v.format().equals(location.value()))
            .findFirst()
            .orElse(null);

        if (wrapped == null) {
            context.errorPrinter().print(
                Notification.error(Errno.INVALID_BUILTIN_TYPE, "invalid location type")
                    .error("unrecognized location type", location)
            );
            throw new UnsupportedOperationException("Invalid location type: " + location);
        }

        Location value;
        switch (wrapped) {
            case SPAWN:
            case INVOKER:
            case CURRENT:
                value = new StaticLocation(wrapped);
                break;
            case CUSTOM:
                get(TokenType.LPAREN);
                Value x = parser.nextValue();
                get(TokenType.COMMA);
                Value y = parser.nextValue();
                get(TokenType.COMMA);
                Value z = parser.nextValue();
                get(TokenType.RPAREN);
                value = new CustomLocation(x, y, z);
                break;
            default:
                context.errorPrinter().print(
                    Notification.error(Errno.INVALID_BUILTIN_TYPE, "invalid location type")
                        .error("unrecognized location type", location)
                );
                throw new UnsupportedOperationException("Invalid location type: " + location);
        };

        return new LocationValue(value);
    }

    public @NotNull Value parseVector(@NotNull AstParser parser, @NotNull ParserContext context) {
        get(TokenType.IDENTIFIER);
        get(TokenType.LPAREN);
        Value x = parser.nextValue();
        get(TokenType.COMMA);
        Value y = parser.nextValue();
        get(TokenType.COMMA);
        Value z = parser.nextValue();
        get(TokenType.RPAREN);
        return new VectorValue(new Vector(x, y, z));
    }

    private @NotNull Value parseGameMode(@NotNull ParserContext context) {
        Token mode = get(TokenType.IDENTIFIER);
        GameMode wrapped = Stream.of(GameMode.values())
            .filter(v -> v.format().equals(mode.value()))
            .findFirst()
            .orElse(null);

        if (wrapped == null) {
            context.errorPrinter().print(
                Notification.error(Errno.INVALID_BUILTIN_TYPE, "invalid game mode type")
                    .error("unrecognized game mode type", mode)
            );
            throw new UnsupportedOperationException("Invalid game mode type: " + mode);
        }

        return new GameModeValue(wrapped);
    }

    private @NotNull Value parseTarget(@NotNull ParserContext context) {
        Token target = get(TokenType.IDENTIFIER);
        Target wrapped = Stream.of(Target.values())
            .filter(v -> v.format().equals(target.value()))
            .findFirst()
            .orElse(null);

        if (wrapped == null) {
            context.errorPrinter().print(
                Notification.error(Errno.INVALID_BUILTIN_TYPE, "invalid target type")
                    .error("unrecognized target type", target)
            );
            throw new UnsupportedOperationException("Invalid target type: " + target);
        }

        return new TargetValue(wrapped);
    }

    private @NotNull Value parseWeather(@NotNull ParserContext context) {
        Token weather = get(TokenType.IDENTIFIER);
        Weather wrapped = Stream.of(Weather.values())
            .filter(v -> v.format().equals(weather.value()))
            .findFirst()
            .orElse(null);

        if (wrapped == null) {
            context.errorPrinter().print(
                Notification.error(Errno.INVALID_BUILTIN_TYPE, "invalid weather type")
                    .error("unrecognized weather type", weather)
            );
            throw new UnsupportedOperationException("Invalid weather type: " + weather);
        }

        return new WeatherValue(wrapped);
    }

    private @NotNull Value parseTime(@NotNull ParserContext context) {
        Token time = get(TokenType.IDENTIFIER);
        Time wrapped = Stream.of(Time.values())
            .filter(v -> v.format().equals(time.value()))
            .findFirst()
            .orElse(null);

        if (wrapped == null) {
            context.errorPrinter().print(
                Notification.error(Errno.INVALID_BUILTIN_TYPE, "invalid time type")
                    .error("unrecognized time type", time)
            );
            throw new UnsupportedOperationException("Invalid time type: " + time);
        }

        return new TimeValue(wrapped);
    }

    private @NotNull Value parseNamespace(@NotNull ParserContext context) {
        Token namespace = get(TokenType.IDENTIFIER);
        Namespace wrapped = Stream.of(Namespace.values())
            .filter(v -> v.format().equals(namespace.value()))
            .findFirst()
            .orElse(null);

        if (wrapped == null) {
            context.errorPrinter().print(
                Notification.error(Errno.INVALID_BUILTIN_TYPE, "invalid namespace type")
                    .error("unrecognized namespace type", namespace)
            );
            throw new UnsupportedOperationException("Invalid namespace type: " + namespace);
        }

        return new NamespaceValue(wrapped);
    }

    private @NotNull Value parseEffect(@NotNull ParserContext context) {
        Token effect = get(TokenType.IDENTIFIER);
        Effect wrapped = Stream.of(Effect.values())
            .filter(v -> v.format().equals(effect.value()))
            .findFirst()
            .orElse(null);

        if (wrapped == null) {
            context.errorPrinter().print(
                Notification.error(Errno.INVALID_BUILTIN_TYPE, "invalid effect type")
                    .error("unrecognized effect type", effect)
            );
            throw new UnsupportedOperationException("Invalid effect type: " + effect);
        }

        return new EffectValue(wrapped);
    }

    private @NotNull Value parseEnchant(@NotNull ParserContext context) {
        Token enchant = get(TokenType.IDENTIFIER);
        Enchant wrapped = Stream.of(Enchant.values())
            .filter(v -> v.format().equals(enchant.value()))
            .findFirst()
            .orElse(null);

        if (wrapped == null) {
            context.errorPrinter().print(
                Notification.error(Errno.INVALID_BUILTIN_TYPE, "invalid enchant type")
                    .error("unrecognized enchant type", enchant)
            );
            throw new UnsupportedOperationException("Invalid enchant type: " + enchant);
        }

        return new EnchantValue(wrapped);
    }

    private @NotNull Value parseMode(@NotNull ParserContext context) {
        Token mode = get(TokenType.IDENTIFIER);
        Mode wrapped = Stream.of(Mode.values())
            .filter(v -> v.format().equals(mode.value()))
            .findFirst()
            .orElse(null);

        if (wrapped == null) {
            context.errorPrinter().print(
                Notification.error(Errno.INVALID_BUILTIN_TYPE, "invalid mode type")
                    .error("unrecognized mode type", mode)
            );
            throw new UnsupportedOperationException("Invalid mode type: " + mode);
        }

        return new ModeValue(wrapped);
    }

    private @NotNull Value parseLobby(@NotNull ParserContext context) {
        Token lobby = get(TokenType.IDENTIFIER);
        Lobby wrapped = Stream.of(Lobby.values())
            .filter(v -> v.format().equals(lobby.value()))
            .findFirst()
            .orElse(null);

        if (wrapped == null) {
            context.errorPrinter().print(
                Notification.error(Errno.INVALID_BUILTIN_TYPE, "invalid lobby type")
                    .error("unrecognized lobby type", lobby)
            );
            throw new UnsupportedOperationException("Invalid lobby type: " + lobby);
        }

        return new LobbyValue(wrapped);
    }

    private @NotNull Value parseSound(@NotNull ParserContext context) {
        Token sound = get(TokenType.IDENTIFIER);
        Sound wrapped = Stream.of(Sound.values())
            .filter(v -> v.format().equals(sound.value()))
            .findFirst()
            .orElse(null);

        if (wrapped == null) {
            context.errorPrinter().print(
                Notification.error(Errno.INVALID_BUILTIN_TYPE, "invalid lobby type")
                    .error("unrecognized lobby type", sound)
            );
            throw new UnsupportedOperationException("Invalid sound type: " + sound);
        }

        return new SoundValue(wrapped);
    }

    private @NotNull Value parseFlag(@NotNull ParserContext context) {
        Token flag = get(TokenType.IDENTIFIER);
        Flag wrapped = Stream.of(Flag.values())
            .filter(v -> v.format().equals(flag.value()))
            .findFirst()
            .orElse(null);

        if (wrapped == null) {
            context.errorPrinter().print(
                Notification.error(Errno.INVALID_BUILTIN_TYPE, "invalid flag type")
                    .error("unrecognized flag type", flag)
            );
            throw new UnsupportedOperationException("Invalid flag type: " + flag);
        }

        return new FlagValue(wrapped);
    }

    private @NotNull Value parseMaterial(@NotNull ParserContext context) {
        Token material = get(TokenType.IDENTIFIER);
        Material wrapped = Stream.of(Material.values())
            .filter(v -> v.format().equals(material.value()))
            .findFirst()
            .orElse(null);

        if (wrapped == null) {
            context.errorPrinter().print(
                Notification.error(Errno.INVALID_BUILTIN_TYPE, "invalid material type")
                    .error("unrecognized material type", material)
            );
            throw new UnsupportedOperationException("Invalid material type: " + material);
        }

        return new MaterialValue(wrapped);
    }

    private @NotNull Value parseExecutor(@NotNull ParserContext context) {
        Token executor = get(TokenType.IDENTIFIER);
        Executor wrapped = Stream.of(Executor.values())
            .filter(v -> v.format().equals(executor.value()))
            .findFirst()
            .orElse(null);

        if (wrapped == null) {
            context.errorPrinter().print(
                Notification.error(Errno.INVALID_BUILTIN_TYPE, "invalid executor type")
                    .error("unrecognized executor type", executor)
            );
            throw new UnsupportedOperationException("Invalid executor type: " + executor);
        }

        return new ExecutorValue(wrapped);
    }

    private @NotNull Value parseComparator(@NotNull ParserContext context) {
        Token comparator = get(TokenType.IDENTIFIER);
        Comparator wrapped = Stream.of(Comparator.values())
            .filter(v -> v.format().equals(comparator.value()))
            .findFirst()
            .orElse(null);

        if (wrapped == null) {
            context.errorPrinter().print(
                Notification.error(Errno.INVALID_BUILTIN_TYPE, "invalid comparator type")
                    .error("unrecognized comparator type", comparator)
            );
            throw new UnsupportedOperationException("Invalid comparator type: " + comparator);
        }

        return new ComparatorValue(wrapped);
    }

    private @NotNull Value parseItemComparator(@NotNull ParserContext context) {
        Token item = get(TokenType.IDENTIFIER);
        ItemComparator wrapped = Stream.of(ItemComparator.values())
            .filter(v -> v.format().equals(item.value()))
            .findFirst()
            .orElse(null);

        if (wrapped == null) {
            context.errorPrinter().print(
                Notification.error(Errno.INVALID_BUILTIN_TYPE, "invalid item comparator type")
                    .error("unrecognized item comparator type", item)
            );
            throw new UnsupportedOperationException("Invalid item comparator type: " + item);
        }

        return new ItemComparatorValue(wrapped);
    }

    private @NotNull Value parseComparatorTarget(@NotNull ParserContext context) {
        Token comparator = get(TokenType.IDENTIFIER);
        ComparatorTarget wrapper = Stream.of(ComparatorTarget.values())
            .filter(v -> v.format().equals(comparator.value()))
            .findFirst()
            .orElse(null);

        if (wrapper == null) {
            context.errorPrinter().print(
                Notification.error(Errno.INVALID_BUILTIN_TYPE, "invalid comparator target type")
                    .error("unrecognized comparator target type", comparator)
            );
            throw new UnsupportedOperationException("Invalid comparator target type: " + comparator);
        }

        return new ComparatorTargetValue(wrapper);
    }

    private @NotNull Value parseComparatorAmount(@NotNull ParserContext context) {
        Token amount = get(TokenType.IDENTIFIER);
        ComparatorAmount wrapped = Stream.of(ComparatorAmount.values())
            .filter(v -> v.format().equals(amount.value()))
            .findFirst()
            .orElse(null);

        if (wrapped == null) {
            context.errorPrinter().print(
                Notification.error(Errno.INVALID_BUILTIN_TYPE, "invalid comparator amount type")
                    .error("unrecognized comparator amount type", amount)
            );
            throw new UnsupportedOperationException("Invalid comparator amount type: " + amount);
        }

        return new ComparatorAmountValue(wrapped);
    }

    private @NotNull Value parsePermission(@NotNull ParserContext context) {
        Token permission = get(TokenType.IDENTIFIER);
        Permission wrapped = Stream.of(Permission.values())
            .filter(v -> v.format().equals(permission.value()))
            .findFirst()
            .orElse(null);

        if (wrapped == null) {
            context.errorPrinter().print(
                Notification.error(Errno.INVALID_BUILTIN_TYPE, "invalid permission type")
                    .error("unrecognized permission type", permission)
            );
            throw new UnsupportedOperationException("Invalid permission type: " + permission);
        }

        return new PermissionValue(wrapped);
    }

    private @NotNull Value parseProtocol(@NotNull ParserContext context) {
        Token protocol = get(TokenType.IDENTIFIER);
        Protocol wrapped = Stream.of(Protocol.values())
            .filter(v -> v.format().equals(protocol.value()))
            .findFirst()
            .orElse(null);

        if (wrapped == null) {
            context.errorPrinter().print(
                Notification.error(Errno.INVALID_BUILTIN_TYPE, "invalid protocol type")
                    .error("unrecognized protocol type", protocol)
            );
            throw new UnsupportedOperationException("Invalid protocol type: " + protocol);
        }

        return new ProtocolValue(wrapped);
    }
}
