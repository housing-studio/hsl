package org.housingstudio.hsl.importer.impl;

import com.qibergames.futura.concurrent.future.Future;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.housingstudio.hsl.exporter.House;
import org.housingstudio.hsl.exporter.action.Action;
import org.housingstudio.hsl.exporter.generic.Command;
import org.housingstudio.hsl.importer.interaction.Interaction;
import org.housingstudio.hsl.importer.interaction.InteractionTarget;
import org.housingstudio.hsl.importer.interaction.InteractionType;
import org.housingstudio.hsl.importer.interaction.defaults.*;
import org.housingstudio.hsl.importer.platform.*;
import org.housingstudio.hsl.type.*;
import org.housingstudio.hsl.type.location.Location;
import org.housingstudio.hsl.type.location.LocationType;
import org.housingstudio.hsl.type.location.impl.CustomLocation;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.List;

@RequiredArgsConstructor
public class Importer {
    private Future<CommandCallback> commandCallback = new Future<>();
    private Future<Void> containerCallback = new Future<>();

    private final @NotNull House house;
    private boolean guiOpen;

    public void importHouse() {
        importCommands();
    }

    private void importCommands() {
        List<Command> commands = house.commands();
        if (commands.isEmpty())
            return;

        ChatLib.prefixChat("&7Importing total &f" + commands.size() + " &7commands");

        for (Command command : commands)
            importCommand(command);
    }

    @SneakyThrows
    private void importCommand(@NotNull Command command) {
        ChatLib.prefixChat("&7Importing command &f" + command.name());
        ChatLib.command("command create " + command.name(), false);

        commandCallback = new Future<>();
        containerCallback = new Future<>();

        CommandCallback cmd = commandCallback.await();

        switch (cmd) {
            case INVALID_FORMAT:
                ChatLib.prefixChat("&cCommand &f`" + command.name() + "` &chas invalid format");
                ChatLib.prefixChat("&cSkipping import for this command");
                return;
            case ALREADY_EXISTS: {
                ChatLib.prefixChat("&7Command &f" + command.name() + " &7already exists");
                ChatLib.prefixChat("&cSkipping import for this command");
                ChatLib.prefixChat("&7Use flag &f--override &7to enable overriding existing commands");
                return;
            }
            case CREATED:
                ChatLib.prefixChat("&aCommand &f" + command.name() + " &ahas been created");
        }

        containerCallback.await();

        Inventory container = Player.getContainer();
        if (container == null) {
            ChatLib.prefixChat("&cCommand container is not open");
            return;
        }

        if (!container.getName().equals("Actions: /" + command.name())) {
            ChatLib.prefixChat("&cInvalid command container: `" + container.getName() + "`");
            return;
        }

        ChatLib.chat("ready for actions");

        for (Action action : command.actions()) {
            if (!addAction(action)) {
                ChatLib.chat("could not add action " + action.type().name());
                return;
            }
            Thread.sleep(3000);
        }
    }

    @SneakyThrows
    private void waitAndClick(int slot) {
        while (true) {
            Thread.sleep(100);

            Inventory container = Player.getContainer();
            if (container == null)
                continue;

            for (Item item : container.getItems()) {
                if (item.getSlot() != slot)
                    continue;

                container.click(slot, false, "LEFT");
                return;
            }
        }
    }

    @SneakyThrows
    private boolean addAction(@NotNull Action action) {
        ChatLib.prefixChat("&7configuring action &f" + action.type().name());

        // click on "add action"
        waitAndClick(50);
        ChatLib.chat("clicked add action");
        Thread.sleep(3000);

        if (!selectAction(action)) {
            // retry on second page
            waitAndClick(53);
            if (!selectAction(action)) {
                ChatLib.chat("could not select action " + action.type().name());
                return false;
            }
        }

        if (!configureAction(action)) {
            ChatLib.chat("could not configure action " + action.type().name());
            return false;
        }

        // click on "go back"
        waitAndClick(31);
        ChatLib.chat("clicked go back");
        Thread.sleep(3000);

        return true;
    }

    @SneakyThrows
    private boolean selectAction(@NotNull Action action) {
        String name = action.type().itemName();

        for (
            long start = System.currentTimeMillis();
            System.currentTimeMillis() - start < 3000;
            Thread.sleep(100)
        ) {
            Inventory container = Player.getContainer();
            if (container == null)
                continue;

            for (Item item : container.getItems()) {
                if (!item.getName().contains(name))
                    continue;

                // click on action type from the action selector
                container.click(item.getSlot(), false, "LEFT");
                ChatLib.chat("selected action " + name + " at " + item.getSlot());
                Thread.sleep(3000);
                return true;
            }
        }

        return false;
    }

    @SneakyThrows
    private boolean configureAction(@NotNull Action action) {
        ChatLib.chat("configuring action " + action.type().name());

        Field[] fields = action.getClass().getDeclaredFields();
        for (Field field : fields) {
            InteractionTarget target = field.getDeclaredAnnotation(InteractionTarget.class);
            if (target == null)
                continue;

            field.setAccessible(true);
            Object value = field.get(action);

            InteractionType type = target.type();
            int offset = target.offset();
            int slot = Interaction.START_INDEX + offset;

            Object defaultValue = Defaults.getDefaultValue(field);
            if (value.equals(defaultValue))
                continue;

            switch (type) {
                case CHAT:
                    waitAndClick(slot);
                    Thread.sleep(3000);
                    ChatLib.say(String.valueOf(value));
                    Thread.sleep(3000);
                    break;
                case ANVIL:
                    waitAndClick(slot);
                    Thread.sleep(3000);
                    Anvil.input(String.valueOf(value));
                    Thread.sleep(3000);
                    break;
                case TOGGLE:
                    waitAndClick(slot);
                    Thread.sleep(3000);
                    break;
                case LOCATION:
                    waitAndClick(slot);
                    Thread.sleep(3000);
                    // select location type
                    Location location = (Location) value;
                    waitAndClick(Interaction.START_INDEX + location.type().offset());
                    Thread.sleep(3000);
                    // enter values for custom location
                    if (location.type() == LocationType.CUSTOM) {
                        CustomLocation loc = (CustomLocation) value;
                        String format = String.format(
                            "%s %s %s", loc.x().asConstantValue(), loc.y().asConstantValue(), loc.z().asConstantValue()
                        );
                        Anvil.input(format);
                    }
                    break;
                case MODE:
                case MODE_WITH_UNSET:
                    waitAndClick(slot);
                    Thread.sleep(3000);
                    // select mode type
                    Mode mode = (Mode) value;
                    int modeSlot = Interaction.START_INDEX + mode.offset();
                    // some guis have an "Unset" mode, so all the modes are shifted right
                    // this is kinda strange, as afaik no other enum types have an unset
                    // field in the option selector
                    if (type == InteractionType.MODE_WITH_UNSET)
                        modeSlot++;
                    waitAndClick(modeSlot);
                    Thread.sleep(3000);
                    break;
                case LOBBY:
                    waitAndClick(slot);
                    Thread.sleep(3000);
                    // select lobby type
                    Lobby lobby = (Lobby) value;
                    waitAndClick(Interaction.START_INDEX + lobby.offset());
                    Thread.sleep(3000);
                    break;
                case GAME_MODE:
                    waitAndClick(slot);
                    Thread.sleep(3000);
                    // select game mode
                    GameMode gameMode = (GameMode) value;
                    waitAndClick(Interaction.START_INDEX + gameMode.offset());
                    Thread.sleep(3000);
                    break;
                case WEATHER:
                    waitAndClick(slot);
                    Thread.sleep(3000);
                    // select weather type
                    Weather weather = (Weather) value;
                    waitAndClick(Interaction.START_INDEX + weather.offset());
                    Thread.sleep(3000);
                    break;
                case TIME:
                    waitAndClick(slot);
                    Thread.sleep(3000);
                    // select time type
                    Time time = (Time) value;
                    waitAndClick(Interaction.START_INDEX + time.offset());
                    Thread.sleep(3000);
                    break;
                case EFFECT:
                    waitAndClick(slot);
                    Thread.sleep(3000);
                    // move to next page if needed
                    Effect effect = (Effect) value;
                    if (effect.page() > 1) {
                        waitAndClick(53);
                        Thread.sleep(3000);
                    }
                    // select effect type
                    waitAndClick(Interaction.START_INDEX + effect.offset());
                    Thread.sleep(3000);
                    break;
                case DYNAMIC_OPTION:
                    waitAndClick(slot);
                    Thread.sleep(3000);
                    // select item dynamically from the container that matches the expected name
                    if (!findAndClick((String) value)) {
                        ChatLib.chat(
                            "could not find dynamic option `" + value + "` for property " + field.getName() +
                            " for action " + action.type().name()
                        );
                        return false;
                    }
                    Thread.sleep(3000);
                    break;
                default:
                    ChatLib.prefixChat("Unimplemented interaction type: " + type.name());
            }
        }

        return true;
    }

    @SneakyThrows
    private boolean findAndClick(@NotNull String name) {
        for (
            long start = System.currentTimeMillis();
            System.currentTimeMillis() - start < 10_000;
            Thread.sleep(100)
        ) {
            Inventory container = Player.getContainer();
            if (container == null)
                continue;

            for (Item item : container.getItems()) {
                if (!item.getName().contains(name))
                    continue;

                container.click(item.getSlot(), false, "LEFT");
                return true;
            }
        }

        return false;
    }

    public void onMessage(@NotNull Object event, @NotNull String message) {
        CommandCallback cmd = null;
        if (message.contains("Created command"))
            cmd = CommandCallback.CREATED;
        else if (message.contains("This command is not valid")) {
            if (message.contains("Commands must be at least 3 characters long"))
                cmd = CommandCallback.INVALID_FORMAT;
            else
                cmd = CommandCallback.ALREADY_EXISTS;
        }

        if (cmd != null) {
            // TODO cancel event
            commandCallback.complete(cmd);
        }
    }

    public void onContainerOpen(@NotNull Object event) {
        Future.tryInvokeAsync(() -> {
            Thread.sleep(500);
            Inventory container = Player.getContainer();
            if (container == null)
                return;

            guiOpen = true;
            containerCallback.complete(null);
        });
    }

    public void onContainerRender(@NotNull Object event) {
    }

    public void onContainerClose(@NotNull Object event) {
        guiOpen = false;
    }
}
