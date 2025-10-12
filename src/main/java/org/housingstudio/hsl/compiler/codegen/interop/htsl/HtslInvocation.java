package org.housingstudio.hsl.compiler.codegen.interop.htsl;

import lombok.RequiredArgsConstructor;
import org.housingstudio.hsl.std.*;
import org.housingstudio.hsl.std.location.Location;
import org.housingstudio.hsl.std.location.impl.CustomLocation;
import org.housingstudio.hsl.std.slot.Slot;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class HtslInvocation {
    private final Map<String, Object> arguments = new HashMap<>();
    private final @NotNull String pattern;

    public @NotNull HtslInvocation set(@NotNull String key, @NotNull Object value) {
        if (arguments.containsKey(key))
            throw new IllegalStateException("HTSL key `" + key + "` already set for pattern `" + pattern + "`");
        arguments.put(key, value);
        return this;
    }

    public @NotNull HtslInvocation setString(@NotNull String key, @NotNull String value) {
        return set(key, "\"" + value + "\"");
    }

    public @NotNull HtslInvocation setWeather(@NotNull String key, @NotNull Weather weather) {
        return setString(key, weather.format());
    }

    public @NotNull HtslInvocation setEffect(@NotNull String key, @NotNull Effect effect) {
        return setString(key, effect.format());
    }

    public @NotNull HtslInvocation setTime(@NotNull String key, @NotNull Time time) {
        return set(key, time.format());
    }

    public @NotNull HtslInvocation setMode(@NotNull String key, @NotNull Mode mode) {
        return set(key, mode.format().toLowerCase());
    }

    public @NotNull HtslInvocation setGameMode(@NotNull String key, @NotNull GameMode gameMode) {
        return setString(key, gameMode.format());
    }

    public @NotNull HtslInvocation setComparator(@NotNull String key, @NotNull Comparator comparator) {
        String value;
        switch (comparator) {
            case LESS_THAN:
                value = "<";
                break;
            case LESS_THAN_OR_EQUAL:
                value = "<=";
                break;
            case EQUAL:
                value = "=";
                break;
            case GREATER_THAN:
                value = ">";
                break;
            case GREATER_THAN_OR_EQUAL:
                value = ">=";
                break;
            default:
                return this;
        }
        return set(key, value);
    }

    public @NotNull HtslInvocation setPermission(@NotNull String key, @NotNull Permission permission) {
        return set(key, permission.format());
    }

    public @NotNull HtslInvocation setLobby(@NotNull String key, @NotNull Lobby lobby) {
        String value;
        switch (lobby) {
            case MAIN:
                value = "Main Lobby";
                break;
            case TOURNAMENT_HALL:
                value = "Tournament Hall";
                break;
            case BLITZ_SG:
                value = "Blitz SG";
                break;
            case THE_TNT_GAMES:
                value = "The TNT Games";
                break;
            case MEGA_WALLS:
                value = "Mega Walls";
                break;
            case ARCADE_GAMES:
                value = "Arcade Games";
                break;
            case COPS_AND_CRIMS:
                value = "Cops and Crims";
                break;
            case UHC_CHAMPIONS:
                value = "UHC Champions";
                break;
            case WARLORDS:
                value = "Warlords";
                break;
            case SMASH_HEROES:
                value = "Smash Heroes";
                break;
            case HOUSING:
                value = "Housing";
                break;
            case SKY_WARS:
                value = "SkyWars";
                break;
            case SPEED_UHC:
                value = "Speed UHC";
                break;
            case CLASSIC_GAMES:
                value = "Classic Games";
                break;
            case PROTOTYPE:
                value = "Prototype";
                break;
            case BED_WARS:
                value = "Bed Wars";
                break;
            case MURDER_MYSTERY:
                value = "Murder Mystery";
                break;
            case BUILD_BATTLE:
                value = "Build Battle";
                break;
            case DUELS:
                value = "Duels";
                break;
            case WOOL_WARS:
                value = "Wood Wars";
                break;
            default:
                return this;
        }
        return setString(key, value);
    }

    public @NotNull HtslInvocation setEnchant(@NotNull String key, @NotNull Enchant enchant) {
        String value;
        switch (enchant) {
            case PROTECTION:
                value = "Protection";
                break;
            case FIRE_PROTECTION:
                value = "Fire Protection";
                break;
            case FEATHER_FALLING:
                value = "Feather Falling";
                break;
            case BLAST_PROTECTION:
                value = "Blast Protection";
                break;
            case PROJECTILE_PROTECTION:
                value = "Projectile Protection";
                break;
            case RESPIRATION:
                value = "Respiration";
                break;
            case AQUA_AFFINITY:
                value = "Aqua Affinity";
                break;
            case THORNS:
                value = "Thorns";
                break;
            case DEPTH_STRIDER:
                value = "Depth Strider";
                break;
            case SHARPNESS:
                value = "Sharpness";
                break;
            case SMITE:
                value = "Smite";
                break;
            case BANE_OF_ARTHROPODS:
                value = "Bane Of Arthropods";
                break;
            case KNOCKBACK:
                value = "Knockback";
                break;
            case FIRE_ASPECT:
                value = "Fire Aspect";
                break;
            case LOOTING:
                value = "Looting";
                break;
            case EFFICIENCY:
                value = "Efficiency";
                break;
            case SILK_TOUCH:
                value = "Silk Touch";
                break;
            case UNBREAKING:
                value = "Unbreaking";
                break;
            case FORTUNE:
                value = "Fortune";
                break;
            case POWER:
                value = "Power";
                break;
            case PUNCH:
                value = "Punch";
                break;
            case FLAME:
                value = "Flame";
                break;
            case INFINITY:
                value = "Infinity";
                break;
            default:
                return this;
        }

        return setString(key, value);
    }

    public @NotNull HtslInvocation setMaterial(@NotNull String key, @NotNull Material material) {
        return setString(key, material.format());
    }

    public @NotNull HtslInvocation setSlot(@NotNull String key, @NotNull Slot slot) {
        String value;
        switch (slot.type()) {
            case FIRST_AVAILABLE:
                value = "First Slot";
                break;
            default:
                return this;
        }
        return setString(key, value);
    }

    public @NotNull HtslInvocation setSound(@NotNull String key, @NotNull Sound sound) {
        String name = Stream.of(sound.name().split("_"))
            .map(part -> part.substring(0, 1).toUpperCase() + part.substring(1).toLowerCase())
            .collect(Collectors.joining(" "));

        return setString(key, name);
    }

    public @NotNull HtslInvocation setLocation(@NotNull String key, @NotNull Location location) {
        String value;
        switch (location.type()) {
            case SPAWN:
                value = "\"house_spawn\"";
                break;
            case CURRENT:
                value = "\"current_location\"";
                break;
            case INVOKER:
                value = "\"invokers_location\"";
                break;
            case CUSTOM:
                CustomLocation loc = (CustomLocation) location;
                value = String.format(
                    "\"custom_coordinates\" \"%s %s %s\"",
                    loc.x().load().asConstantValue(), loc.y().load().asConstantValue(), loc.z().load().asConstantValue()
                );
                break;
            default:
                return this;
        }
        return set(key, value);
    }

    public @NotNull String build() {
        String result = pattern;
        for (Map.Entry<String, Object> entry : arguments.entrySet()) {
            String key = "<" + entry.getKey() + ">";
            if (!pattern.contains(key))
                throw new IllegalStateException("HTSL key `" + key + "` does not match pattern `" + pattern + "`");
            result = result.replace(key, String.valueOf(entry.getValue()));
        }
        return result;
    }
}
