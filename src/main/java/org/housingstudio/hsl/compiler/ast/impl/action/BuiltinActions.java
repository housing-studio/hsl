package org.housingstudio.hsl.compiler.ast.impl.action;

import lombok.experimental.UtilityClass;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Method;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Parameter;
import org.housingstudio.hsl.compiler.ast.impl.scope.Scope;
import org.housingstudio.hsl.compiler.ast.impl.type.Type;
import org.housingstudio.hsl.compiler.ast.impl.value.ConstantLiteral;
import org.housingstudio.hsl.compiler.ast.impl.value.builtin.*;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.housingstudio.hsl.type.Lobby;
import org.housingstudio.hsl.type.Mode;
import org.housingstudio.hsl.type.Vector;
import org.housingstudio.hsl.type.location.LocationType;
import org.housingstudio.hsl.type.location.impl.StaticLocation;
import org.housingstudio.hsl.type.slot.SlotType;
import org.housingstudio.hsl.type.slot.impl.StaticSlot;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class BuiltinActions {
    public final Map<String, Method> LOOKUP = new HashMap<>();

    public void init() {
        // TODO conditional
        LOOKUP.put("setGroup", SET_GROUP);
        LOOKUP.put("kill", KILL);
        LOOKUP.put("heal", HEAL);
        LOOKUP.put("title", TITLE);
        LOOKUP.put("actionbar", ACTIONBAR);
        LOOKUP.put("resetInventory", RESET_INVENTORY);
        LOOKUP.put("changeMaxHealth", CHANGE_MAX_HEALTH);
        LOOKUP.put("parkourCheckpoint", PARKOUR_CHECKPOINT);
        LOOKUP.put("giveItem", GIVE_ITEM);
        LOOKUP.put("removeItem", REMOVE_ITEM);
        LOOKUP.put("chat", CHAT);
        LOOKUP.put("addEffect", ADD_EFFECT);
        LOOKUP.put("clearEffects", CLEAR_EFFECTS);
        LOOKUP.put("addExperience", ADD_EXPERIENCE);
        LOOKUP.put("sendToLobby", SEND_TO_LOBBY);
        LOOKUP.put("changeVariable", CHANGE_VARIABLE);
        LOOKUP.put("teleport", TELEPORT);
        LOOKUP.put("failParkour", FAIL_PARKOUR);
        LOOKUP.put("playSound", PLAY_SOUND);
        LOOKUP.put("setCompassTarget", SET_COMPASS_TARGET);;
        LOOKUP.put("setGameMode", SET_GAME_MODE);
        LOOKUP.put("changeHealth", CHANGE_HEALTH);
        LOOKUP.put("changeHunger", CHANGE_HUNGER);
        // TODO random action
        LOOKUP.put("triggerFunction", TRIGGER_FUNCTION);
        LOOKUP.put("applyInventoryLayout", APPLY_INVENTORY_LAYOUT);
        LOOKUP.put("enchantHeldItem", ENCHANT_HELD_ITEM);
        LOOKUP.put("sleep", SLEEP);
        LOOKUP.put("setTeam", SET_TEAM);
        LOOKUP.put("dropItem", DROP_ITEM);
        LOOKUP.put("changeVelocity", CHANGE_VELOCITY);
        LOOKUP.put("launchToTarget", LAUNCH_TO_TARGET);
        LOOKUP.put("setPlayerWeather", SET_PLAYER_WEATHER);
        LOOKUP.put("setPlayerTime", SET_PLAYER_TIME);
        LOOKUP.put("toggleNameTagDisplay", TOGGLE_NAME_TAG_DISPLAY);
        LOOKUP.put("cancelEvent", CANCEL_EVENT);
        LOOKUP.put("openMenu", OPEN_MENU);
        LOOKUP.put("closeMenu", CLOSE_MENU);
    }

    // TODO conditional

    public final Method SET_GROUP = createAction(
        "setGroup",
        Parameter.required("group", Type.STRING),
        Parameter.optional("protection", Type.BOOL, ConstantLiteral.ofBool(true))
    );

    public final Method KILL = createAction("kill");

    public final Method HEAL = createAction("heal");

    public final Method TITLE = createAction(
        "title",
        Parameter.required("title", Type.STRING),
        Parameter.optional("subtitle", Type.STRING, ConstantLiteral.ofString("")),
        Parameter.optional("fadein", Type.INT, ConstantLiteral.ofInt(1)),
        Parameter.optional("stay", Type.INT, ConstantLiteral.ofInt(1)),
        Parameter.optional("fadeout", Type.INT, ConstantLiteral.ofInt(1))
    );

    public final Method ACTIONBAR = createAction("actionbar", Parameter.required("message", Type.STRING));

    public final Method RESET_INVENTORY = createAction("resetInventory");

    public final Method CHANGE_MAX_HEALTH = createAction(
        "changeMaxHealth",
        Parameter.optional("maxHealth", Type.INT, ConstantLiteral.ofInt(20)),
        Parameter.optional("mode", Type.MODE, new ModeValue(Mode.SET)),
        Parameter.optional("healOnChange", Type.BOOL, ConstantLiteral.ofBool(true))
    );

    public final Method PARKOUR_CHECKPOINT = createAction("parkourCheckpoint");

    public final Method GIVE_ITEM = createAction(
        "giveItem",
        Parameter.required("item", Type.MATERIAL),
        Parameter.optional("allowMultiple", Type.BOOL, ConstantLiteral.ofBool(false)),
        Parameter.optional("slot", Type.SLOT, new SlotValue(new StaticSlot(SlotType.FIRST_AVAILABLE))),
        Parameter.optional("replace", Type.BOOL, ConstantLiteral.ofBool(false))
    );

    public final Method REMOVE_ITEM = createAction("removeItem", Parameter.required("item", Type.MATERIAL));

    public final Method CHAT = createAction("chat", Parameter.required("message", Type.STRING));

    public final Method ADD_EFFECT = createAction(
        "addEffect",
        Parameter.required("effect", Type.EFFECT),
        Parameter.optional("duration", Type.INT, ConstantLiteral.ofInt(60)),
        Parameter.optional("level", Type.INT, ConstantLiteral.ofInt(1)),
        Parameter.optional("override", Type.BOOL, ConstantLiteral.ofBool(false)),
        Parameter.optional("showIcon", Type.BOOL, ConstantLiteral.ofBool(true))
    );

    public final Method CLEAR_EFFECTS = createAction("clearEffects");

    public final Method ADD_EXPERIENCE = createAction(
        "addExperience",
        Parameter.optional("levels", Type.INT, ConstantLiteral.ofInt(1))
    );

    public final Method SEND_TO_LOBBY = createAction(
        "sendToLobby",
        Parameter.optional("lobby", Type.LOBBY, new LobbyValue(Lobby.HOUSING))
    );

    public final Method CHANGE_VARIABLE = createAction(
        "changeVariable",
        Parameter.required("namespace", Type.NAMESPACE),
        Parameter.required("variable", Type.STRING),
        Parameter.optional("mode", Type.MODE, new ModeValue(Mode.SET)),
        Parameter.optional("value", Type.ANY, ConstantLiteral.ofInt(1)),
        Parameter.optional("autoUnset", Type.BOOL, ConstantLiteral.ofBool(false))
    );

    public final Method TELEPORT = createAction(
        "teleport",
        Parameter.required("location", Type.LOCATION),
        Parameter.optional("prevent", Type.BOOL, ConstantLiteral.ofBool(false))
    );

    public final Method FAIL_PARKOUR = createAction(
        "failParkour",
        Parameter.optional("reason", Type.STRING, ConstantLiteral.ofString("Failed!"))
    );

    public final Method PLAY_SOUND = createAction(
        "playSound",
        Parameter.required("sound", Type.SOUND),
        Parameter.optional("volume", Type.FLOAT, ConstantLiteral.ofFloat(0.7F)),
        Parameter.optional("pitch", Type.FLOAT, ConstantLiteral.ofFloat(1.0F)),
        Parameter.optional("location", Type.LOCATION, new LocationValue(new StaticLocation(LocationType.INVOKER)))
    );

    public final Method SET_COMPASS_TARGET = createAction(
        "setCompassTarget",
        Parameter.required("location", Type.LOCATION)
    );

    public final Method SET_GAME_MODE = createAction(
        "setGameMode",
        Parameter.required("gameMode", Type.GAME_MODE)
    );

    public final Method CHANGE_HEALTH = createAction(
        "changeHealth",
        Parameter.optional("health", Type.INT, ConstantLiteral.ofInt(20)),
        Parameter.optional("mode", Type.MODE, new ModeValue(Mode.SET))
    );

    public final Method CHANGE_HUNGER = createAction(
        "changeHunger",
        Parameter.optional("hunger", Type.INT, ConstantLiteral.ofInt(20)),
        Parameter.optional("mode", Type.MODE, new ModeValue(Mode.SET))
    );

    // TODO random action

    public final Method TRIGGER_FUNCTION = createAction(
        "triggerFunction",
        Parameter.required("function", Type.STRING),
        Parameter.required("target", Type.TARGET)
    );

    public final Method APPLY_INVENTORY_LAYOUT = createAction(
        "applyInventoryLayout",
        Parameter.required("layout", Type.STRING)
    );

    public final Method ENCHANT_HELD_ITEM = createAction(
        "enchantHeldItem",
        Parameter.required("enchant", Type.ENCHANT),
        Parameter.optional("level", Type.INT, ConstantLiteral.ofInt(1))
    );

    public final Method SLEEP = createAction(
        "sleep",
        Parameter.optional("ticks", Type.INT, ConstantLiteral.ofInt(20))
    );

    public final Method SET_TEAM = createAction(
        "setTeam",
        Parameter.required("team", Type.STRING)
    );

    public final Method DROP_ITEM = createAction(
        "dropItem",
        Parameter.required("item", Type.MATERIAL),
        Parameter.required("location", Type.LOCATION),
        Parameter.optional("naturally", Type.BOOL, ConstantLiteral.ofBool(true)),
        Parameter.optional("disableMerging", Type.BOOL, ConstantLiteral.ofBool(false)),
        Parameter.optional("prioritizePlayers", Type.BOOL, ConstantLiteral.ofBool(false)),
        Parameter.optional("fallbackToInventory", Type.BOOL, ConstantLiteral.ofBool(false))
    );

    public final Method CHANGE_VELOCITY = createAction(
        "changeVelocity",
        Parameter.optional("velocity", Type.VECTOR, new VectorValue(new Vector(
            ConstantLiteral.ofInt(10),
            ConstantLiteral.ofInt(10),
            ConstantLiteral.ofInt(10)
        )))
    );

    public final Method LAUNCH_TO_TARGET = createAction(
        "launchToTarget",
        Parameter.required("location", Type.LOCATION),
        Parameter.optional("strength", Type.INT, ConstantLiteral.ofInt(2))
    );

    public final Method SET_PLAYER_WEATHER = createAction(
        "setPlayerWeather",
        Parameter.required("weather", Type.WEATHER)
    );

    public final Method SET_PLAYER_TIME = createAction(
        "setPlayerTime",
        Parameter.required("time", Type.TIME)
    );

    public final Method TOGGLE_NAME_TAG_DISPLAY = createAction(
        "toggleNameTagDisplay",
        Parameter.required("shown", Type.BOOL)
    );

    public final Method CANCEL_EVENT = createAction("cancelEvent");

    public final Method OPEN_MENU = createAction(
        "openMenu",
        Parameter.required("menu", Type.STRING)
    );

    public final Method CLOSE_MENU = createAction("closeMenu");

    public @NotNull Method createAction(@NotNull String name, @NotNull Parameter @NotNull ... parameters) {
        return new Method(
            Collections.emptyList(),
            Token.of(TokenType.IDENTIFIER, name),
            Type.VOID,
            Arrays.asList(parameters),
            Scope.EMPTY
        );
    }
}
