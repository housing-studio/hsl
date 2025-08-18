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
import org.housingstudio.hsl.importer.interaction.defaults.DefaultBoolean;
import org.housingstudio.hsl.importer.interaction.defaults.DefaultInt;
import org.housingstudio.hsl.importer.interaction.defaults.DefaultString;
import org.housingstudio.hsl.importer.interaction.defaults.Required;
import org.housingstudio.hsl.importer.platform.*;
import org.housingstudio.hsl.type.location.Location;
import org.housingstudio.hsl.type.location.LocationType;
import org.housingstudio.hsl.type.location.impl.CustomLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

        for (Command command : commands) {
            importCommand(command);
        }
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
            ChatLib.chat("could not select action " + action.type().name());
            return false;
        }

        configureAction(action);

        // click on "go back"
        waitAndClick(31);
        ChatLib.chat("clicked go back");
        Thread.sleep(3000);

        return true;
    }

    @SneakyThrows
    private boolean selectAction(@NotNull Action action) {
        String name = action.type().itemName();
        while (true) {
            Thread.sleep(100);

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
    }

    @SneakyThrows
    private void configureAction(@NotNull Action action) {
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

            Object defaultValue = getDefaultValue(field);
            if (value.equals(defaultValue))
                continue;

            switch (type) {
                case CHAT:
                    waitAndClick(slot);
                    Thread.sleep(3000);
                    ChatLib.say((String) value);
                    Thread.sleep(3000);
                    break;
                case ANVIL:
                    waitAndClick(slot);
                    Thread.sleep(3000);
                    Anvil.input(String.valueOf(value));
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
                default:
                    ChatLib.prefixChat("Unimplemented interaction type: " + type.name());
            }
        }
    }

    private @Nullable Object getDefaultValue(@NotNull Field field) {
        if (field.isAnnotationPresent(DefaultInt.class))
            return field.getDeclaredAnnotation(DefaultInt.class).value();
        else if (field.isAnnotationPresent(DefaultString.class))
            return field.getDeclaredAnnotation(DefaultString.class).value();
        else if (field.isAnnotationPresent(DefaultBoolean.class))
            return field.getDeclaredAnnotation(DefaultBoolean.class).value();
        else if (field.isAnnotationPresent(Required.class))
            return null;
        else
            throw new IllegalStateException("Field " + field.getName() + " does not specify a default value");
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
        Exec.async(() -> {
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
