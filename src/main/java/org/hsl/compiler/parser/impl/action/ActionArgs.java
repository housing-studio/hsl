package org.hsl.compiler.parser.impl.action;

import lombok.RequiredArgsConstructor;
import org.hsl.compiler.ast.impl.value.Value;
import org.hsl.compiler.ast.impl.value.builtin.*;
import org.hsl.std.type.*;
import org.hsl.std.type.location.Location;
import org.hsl.std.type.slot.Slot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@RequiredArgsConstructor
public class ActionArgs {
    private final @NotNull Map<String, Value> args;

    public @NotNull Namespace getNamespace(@NotNull String key) {
        return ((NamespaceValue) args.get(key)).namespace();
    }

    public @NotNull Effect getEffect(@NotNull String key) {
        return ((EffectValue) args.get(key)).effect();
    }

    public @NotNull String getString(@NotNull String key) {
        return args.get(key).asConstantValue();
    }

    public @Nullable String getNullableString(@NotNull String key) {
        Value value = args.get(key);
        if (value instanceof NullValue)
            return null;
        return value.asConstantValue();
    }

    public int getInt(@NotNull String key) {
        return Integer.parseInt(args.get(key).asConstantValue());
    }

    public @NotNull Mode getMode(@NotNull String key) {
        return ((ModeValue) args.get(key)).mode();
    }

    public @NotNull Lobby getLobby(@NotNull String key) {
        return ((LobbyValue) args.get(key)).lobby();
    }

    public @NotNull Location getLocation(@NotNull String key) {
        return ((LocationValue) args.get(key)).location();
    }

    public @NotNull Sound getSound(@NotNull String key) {
        return ((SoundValue) args.get(key)).sound();
    }

    public @NotNull GameMode getGameMode(@NotNull String key) {
        return ((GameModeValue) args.get(key)).gameMode();
    }

    public @NotNull Target getTarget(@NotNull String key) {
        return ((TargetValue) args.get(key)).target();
    }

    public @NotNull Enchant getEnchant(@NotNull String key) {
        return ((EnchantValue) args.get(key)).enchant();
    }

    public @NotNull Material getMaterial(@NotNull String key) {
        return ((MaterialValue) args.get(key)).material();
    }

    public @NotNull Vector getVector(@NotNull String key) {
        return ((VectorValue) args.get(key)).vector();
    }

    public @NotNull Weather getWeather(@NotNull String key) {
        return ((WeatherValue) args.get(key)).weather();
    }

    public @NotNull Time getTime(@NotNull String key) {
        return ((TimeValue) args.get(key)).time();
    }

    public @NotNull Slot getSlot(@NotNull String key) {
        return ((SlotValue) args.get(key)).slot();
    }

    public @NotNull Comparator getComparator(@NotNull String key) {
        return ((ComparatorValue) args.get(key)).comparator();
    }

    public @NotNull ItemComparator getItemComparator(@NotNull String key) {
        return ((ItemComparatorValue) args.get(key)).itemComparator();
    }

    public @NotNull ComparatorTarget getComparatorTarget(@NotNull String key) {
        return ((ComparatorTargetValue) args.get(key)).comparatorTarget();
    }

    public @NotNull ComparatorAmount getComparatorAmount(@NotNull String key) {
        return ((ComparatorAmountValue) args.get(key)).comparatorAmount();
    }

    public @NotNull Permission getPermission(@NotNull String key) {
        return ((PermissionValue) args.get(key)).permission();
    }

    public boolean getBoolean(@NotNull String key) {
        String value = args.get(key).asConstantValue();
        if (value.equals("true"))
            return true;
        else if (value.equals("false"))
            return false;
        throw new IllegalStateException("Unexpected boolean value: " + value);
    }

    public float getFloat(@NotNull String key) {
        String value = args.get(key).asConstantValue();
        return Float.parseFloat(value);
    }
}
