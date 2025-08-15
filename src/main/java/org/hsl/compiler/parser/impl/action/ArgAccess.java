package org.hsl.compiler.parser.impl.action;

import lombok.RequiredArgsConstructor;
import org.hsl.compiler.ast.impl.value.ConstantAccess;
import org.hsl.compiler.ast.impl.value.Value;
import org.hsl.compiler.ast.impl.value.builtin.*;
import org.hsl.std.type.*;
import org.hsl.std.type.location.Location;
import org.hsl.std.type.slot.Slot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@RequiredArgsConstructor
public class ArgAccess {
    private final @NotNull Map<String, Value> args;

    private @NotNull Value get(@NotNull String key) {
        Value value = args.get(key);
        if (value instanceof ConstantAccess access)
            return access.load();
        return value;
    }

    public @NotNull Namespace getNamespace(@NotNull String key) {
        return ((NamespaceValue) get(key)).namespace();
    }

    public @NotNull Effect getEffect(@NotNull String key) {
        return ((EffectValue) get(key)).effect();
    }

    public @NotNull String getString(@NotNull String key) {
        return get(key).asConstantValue();
    }

    public @Nullable String getNullableString(@NotNull String key) {
        Value value = get(key);
        if (value instanceof NullValue)
            return null;
        return value.asConstantValue();
    }

    public int getInt(@NotNull String key) {
        return Integer.parseInt(get(key).asConstantValue());
    }

    public @NotNull Mode getMode(@NotNull String key) {
        return ((ModeValue) get(key)).mode();
    }

    public @NotNull Lobby getLobby(@NotNull String key) {
        return ((LobbyValue) get(key)).lobby();
    }

    public @NotNull Location getLocation(@NotNull String key) {
        return ((LocationValue) get(key)).location();
    }

    public @NotNull Sound getSound(@NotNull String key) {
        return ((SoundValue) get(key)).sound();
    }

    public @NotNull GameMode getGameMode(@NotNull String key) {
        return ((GameModeValue) get(key)).gameMode();
    }

    public @NotNull Target getTarget(@NotNull String key) {
        return ((TargetValue) get(key)).target();
    }

    public @NotNull Enchant getEnchant(@NotNull String key) {
        return ((EnchantValue) get(key)).enchant();
    }

    public @NotNull Material getMaterial(@NotNull String key) {
        return ((MaterialValue) get(key)).material();
    }

    public @NotNull Vector getVector(@NotNull String key) {
        return ((VectorValue) get(key)).vector();
    }

    public @NotNull Weather getWeather(@NotNull String key) {
        return ((WeatherValue) get(key)).weather();
    }

    public @NotNull Time getTime(@NotNull String key) {
        return ((TimeValue) get(key)).time();
    }

    public @NotNull Slot getSlot(@NotNull String key) {
        return ((SlotValue) get(key)).slot();
    }

    public @NotNull Comparator getComparator(@NotNull String key) {
        return ((ComparatorValue) get(key)).comparator();
    }

    public @NotNull ItemComparator getItemComparator(@NotNull String key) {
        return ((ItemComparatorValue) get(key)).itemComparator();
    }

    public @NotNull ComparatorTarget getComparatorTarget(@NotNull String key) {
        return ((ComparatorTargetValue) get(key)).comparatorTarget();
    }

    public @NotNull ComparatorAmount getComparatorAmount(@NotNull String key) {
        return ((ComparatorAmountValue) get(key)).comparatorAmount();
    }

    public @NotNull Permission getPermission(@NotNull String key) {
        return ((PermissionValue) get(key)).permission();
    }

    public boolean getBoolean(@NotNull String key) {
        String value = get(key).asConstantValue();
        if (value.equals("true"))
            return true;
        else if (value.equals("false"))
            return false;
        throw new IllegalStateException("Unexpected boolean value: " + value);
    }

    public float getFloat(@NotNull String key) {
        String value = get(key).asConstantValue();
        return Float.parseFloat(value);
    }
}
