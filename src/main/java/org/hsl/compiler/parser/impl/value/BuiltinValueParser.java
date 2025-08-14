package org.hsl.compiler.parser.impl.value;

import org.hsl.compiler.ast.impl.value.Value;
import org.hsl.compiler.ast.impl.value.builtin.*;
import org.hsl.compiler.parser.AstParser;
import org.hsl.compiler.parser.ParserAlgorithm;
import org.hsl.compiler.parser.ParserContext;
import org.hsl.compiler.token.Token;
import org.hsl.compiler.token.TokenType;
import org.hsl.std.type.*;
import org.hsl.std.type.location.Location;
import org.hsl.std.type.location.LocationType;
import org.hsl.std.type.location.impl.CustomLocation;
import org.hsl.std.type.location.impl.StaticLocation;
import org.hsl.std.type.slot.Slot;
import org.hsl.std.type.slot.SlotType;
import org.hsl.std.type.slot.impl.CustomSlot;
import org.hsl.std.type.slot.impl.HotbarSlot;
import org.hsl.std.type.slot.impl.InventorySlot;
import org.hsl.std.type.slot.impl.StaticSlot;
import org.jetbrains.annotations.NotNull;

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
            case "Location" -> { return parseLocation(parser, context); }
            case "Vector" -> { return parseVector(parser, context); }
            case "Slot" -> { return parseSlot(parser, context); }
            case "GameMode" -> { return parseGameMode(context); }
            case "Target" -> { return parseTarget(context); }
            case "Weather" -> { return parseWeather(context); }
            case "Time" -> { return parseTime(context); }
            case "Namespace" -> { return parseNamespace(context); }
            case "Effect" -> { return parseEffect(context); }
            case "Enchant" -> { return parseEnchant(context); }
            case "Mode" -> { return parseMode(context); }
            case "Lobby" -> { return parseLobby(context); }
            case "Sound" -> { return parseSound(context); }
            case "Flag" -> { return parseFlag(context); }
            case "Material" -> { return parseMaterial(context); }
            case "Executor" -> { return parseExecutor(context); }
            default -> {
                context.syntaxError(type, "Invalid builtin type");
                throw new UnsupportedOperationException("Invalid builtin type: " + type);
            }
        }
    }

    private @NotNull Value parseSlot(@NotNull AstParser parser, @NotNull ParserContext context) {
        Token slot = get(TokenType.IDENTIFIER);
        SlotType wrapped = Stream.of(SlotType.values())
            .filter(v -> v.format().equals(slot.value()))
            .findFirst()
            .orElse(null);

        if (wrapped == null) {
            context.syntaxError(slot, "Invalid slot type");
            throw new UnsupportedOperationException("Invalid slot type: " + slot);
        }

        Slot value = switch (wrapped) {
            case HELMET, CHESTPLATE, LEGGINGS, BOOTS, FIRST_AVAILABLE, HAND_SLOT -> new StaticSlot(wrapped);
            case CUSTOM -> new CustomSlot(parser.nextValue());
            case INVENTORY -> new InventorySlot(parser.nextValue());
            case HOTBAR -> new HotbarSlot(parser.nextValue());
        };

        return new SlotValue(value);
    }

    private @NotNull Value parseLocation(@NotNull AstParser parser, @NotNull ParserContext context) {
        Token location = get(TokenType.IDENTIFIER);
        LocationType wrapped = Stream.of(LocationType.values())
            .filter(v -> v.format().equals(location.value()))
            .findFirst()
            .orElse(null);

        if (wrapped == null) {
            context.syntaxError(location, "Invalid location type");
            throw new UnsupportedOperationException("Invalid location type: " + location);
        }

        Location value = switch (wrapped) {
            case SPAWN, INVOKER, CURRENT -> new StaticLocation(wrapped);
            case CUSTOM -> {
                get(TokenType.LPAREN);
                Value x = parser.nextValue();
                get(TokenType.COMMA);
                Value y = parser.nextValue();
                get(TokenType.COMMA);
                Value z = parser.nextValue();
                get(TokenType.RPAREN);
                yield new CustomLocation(x, y, z);
            }
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
            context.syntaxError(mode, "Invalid game mode type");
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
            context.syntaxError(target, "Invalid target type");
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
            context.syntaxError(weather, "Invalid weather type");
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
            context.syntaxError(time, "Invalid time type");
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
            context.syntaxError(namespace, "Invalid namespace type");
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
            context.syntaxError(effect, "Invalid effect type");
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
            context.syntaxError(enchant, "Invalid enchant type");
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
            context.syntaxError(mode, "Invalid mode type");
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
            context.syntaxError(lobby, "Invalid lobby type");
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
            context.syntaxError(sound, "Invalid sound type");
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
            context.syntaxError(flag, "Invalid flag type");
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
            context.syntaxError(material, "Invalid material type");
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
            context.syntaxError(executor, "Invalid executor type");
            throw new UnsupportedOperationException("Invalid executor type: " + executor);
        }

        return new ExecutorValue(wrapped);
    }
}
