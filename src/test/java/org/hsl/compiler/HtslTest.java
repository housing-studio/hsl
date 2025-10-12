package org.hsl.compiler;

import org.housingstudio.hsl.compiler.ast.impl.value.ConstantLiteral;
import org.housingstudio.hsl.compiler.codegen.impl.action.Action;
import org.housingstudio.hsl.compiler.codegen.impl.action.impl.*;
import org.housingstudio.hsl.std.*;
import org.housingstudio.hsl.std.location.LocationType;
import org.housingstudio.hsl.std.location.impl.CustomLocation;
import org.housingstudio.hsl.std.location.impl.StaticLocation;
import org.housingstudio.hsl.std.slot.SlotType;
import org.housingstudio.hsl.std.slot.impl.StaticSlot;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class HtslTest {
    @Test
    public void verifySyntax() {
        Map<Action, String> expect = new HashMap<>();
        expect.put(new ApplyInventoryLayout("Spawn Layout"), "applyLayout \"Spawn Layout\"");
        expect.put(
            new ApplyPotionEffect(Effect.ABSORPTION, 100, 3, true, true),
            "applyPotion \"Absorption\" 100 3 true true"
        );
        // TODO balance team
        expect.put(new CancelEvent(), "cancelEvent");
        expect.put(
            new ChangeVariable(Namespace.GLOBAL, "gold", Mode.DECREMENT, "1", true),
            "globalvar \"gold\" decrement 1 true"
        );
        expect.put(new ChangeHealth(20, Mode.INCREMENT), "changeHealth increment 20");
        expect.put(new ChangeHungerLevel(5, Mode.INCREMENT), "hungerLevel increment 5");
        expect.put(new ChangeMaxHealth(5, Mode.INCREMENT, true), "maxHealth increment 5 true");
        expect.put(new ChangePlayerGroup("Visitor", true), "changePlayerGroup \"Visitor\" true");
        expect.put(
            new ChangeVariable(Namespace.PLAYER, "Kills", Mode.INCREMENT, "1", false),
            "var \"Kills\" increment 1 false"
        );
        // TODO team var
        expect.put(new ClearAllPotionEffects(), "clearEffects");
        // TODO close menu
        expect.put(new DisplayActionBar("Hello, World!"), "actionBar \"Hello, World!\"");
        // TODO display menu
        expect.put(
            new DisplayTitle("Hello", "World", 2, 5, 3),
            "title \"Hello\" \"World\" 2 5 3"
        );
        expect.put(new EnchantHeldItem(Enchant.SHARPNESS, 5), "enchant \"Sharpness\" 5");
        expect.put(new Exit(), "exit");
        expect.put(new FailParkour("Failed!"), "failParkour \"Failed!\"");
        expect.put(new FullHealth(), "fullHeal");
        expect.put(new GiveExperienceLevels(10), "xpLevel 10");
        expect.put(
            new GiveItem(Material.STONE, true, new StaticSlot(SlotType.FIRST_AVAILABLE), false),
            "giveItem \"Stone\" true \"First Slot\" false"
        );
        expect.put(new KillPlayer(), "kill");
        expect.put(new ParkourCheckpoint(), "parkCheck");
        expect.put(new PauseExecution(10), "pause 10");
        expect.put(
            new PlaySound(Sound.ANVIL_LAND, 0.7F, 1.0F, new StaticLocation(LocationType.INVOKER)),
            "sound \"Anvil Land\" 0.7 1.0 \"invokers_location\""
        );
        expect.put(new RemoveItem(Material.STONE), "removeItem \"Stone\"");
        expect.put(new ResetInventory(), "resetInventory");
        expect.put(new SendChatMessage("Hello, World!"), "chat \"Hello, World!\"");
        expect.put(new SendToLobby(Lobby.MAIN), "lobby \"Main Lobby\"");
        expect.put(
            new SetCompassTarget(
                new CustomLocation(ConstantLiteral.ofInt(0), ConstantLiteral.ofInt(0), ConstantLiteral.ofInt(0))
            ),
            "compassTarget \"custom_coordinates\" \"0 0 0\""
        );
        expect.put(new SetGameMode(GameMode.CREATIVE), "gamemode \"Creative\"");
        expect.put(new SetPlayerTeam("Blue"), "setTeam \"Blue\"");
        expect.put(
            new TeleportPlayer(
                new CustomLocation(
                    ConstantLiteral.ofInt(0), ConstantLiteral.ofInt(0), ConstantLiteral.ofInt(0)
                ),
                true
            ),
            "tp \"custom_coordinates\" \"0 0 0\" true"
        );
        expect.put(new TriggerFunction("Announce", true), "function \"Announce\" true");
        //expect.put( TODO add missing args from new update
        //    new DropItem(Material.STONE, new StaticLocation(LocationType.INVOKER), true, true, true, true),
        //    "dropItem \"Stone\" \"invokers_location\" true true true true"
        //);
        expect.put(new ChangeVelocity(1F, 2F, 3F), "changeVelocity 1.0 2.0 3.0");
        expect.put(
            new LaunchToTarget(
                new CustomLocation(
                    ConstantLiteral.ofInt(0), ConstantLiteral.ofInt(0), ConstantLiteral.ofInt(0)
                ),
                3
            ),
            "launchTarget \"custom_coordinates\" \"0 0 0\" 3"
        );
        expect.put(new SetPlayerWeather(Weather.SUNNY), "playerWeather \"Sunny\"");
        // TODO does HTSL support enum player time or must convert everything to world tick
        expect.put(new ToggleNameTagDisplay(false), "displayNametag false"); // TODO could rename this action to HTSL's name

        for (Map.Entry<Action, String> entry : expect.entrySet()) {
            String export = entry.getKey().asHTSL().build();
            Assertions.assertEquals(export, entry.getValue());
        }
    }
}
