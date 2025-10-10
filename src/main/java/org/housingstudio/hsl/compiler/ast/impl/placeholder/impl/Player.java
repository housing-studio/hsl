package org.housingstudio.hsl.compiler.ast.impl.placeholder.impl;

import lombok.experimental.UtilityClass;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Parameter;
import org.housingstudio.hsl.compiler.ast.impl.placeholder.*;
import org.housingstudio.hsl.compiler.ast.impl.type.Types;

@Struct("Player")
@UtilityClass
public class Player {
    @Fn
    public final Placeholder NAME = new PlaceholderBuilder()
        .name("name")
        .returnType(Types.STRING)
        .mapper(args -> new Result("%player.name%"))
        .build();

    @Fn
    public final Placeholder PING = new PlaceholderBuilder()
        .name("ping")
        .returnType(Types.STRING)
        .mapper(args -> new Result("%player.ping%"))
        .build();

    @Fn
    public final Placeholder HEALTH = new PlaceholderBuilder()
        .name("health")
        .returnType(Types.INT)
        .mapper(args -> new Result("%player.health%"))
        .build();

    @Fn
    public final Placeholder MAX_HEALTH = new PlaceholderBuilder()
        .name("maxHealth")
        .returnType(Types.INT)
        .mapper(args -> new Result("%player.maxhealth%"))
        .build();

    @Fn
    public final Placeholder HUNGER = new PlaceholderBuilder()
        .name("hunger")
        .returnType(Types.INT)
        .mapper(args -> new Result("%player.hunger%"))
        .build();

    @Fn
    public final Placeholder EXPERIENCE = new PlaceholderBuilder()
        .name("experience")
        .returnType(Types.INT)
        .mapper(args -> new Result("%player.experience%"))
        .build();

    @Fn
    public final Placeholder LEVEL = new PlaceholderBuilder()
        .name("level")
        .returnType(Types.INT)
        .mapper(args -> new Result("%player.level%"))
        .build();

    @Fn
    public final Placeholder POS_X = new PlaceholderBuilder()
        .name("posX")
        .returnType(Types.FLOAT)
        .mapper(args -> new Result("%player.pos.x%"))
        .build();

    @Fn
    public final Placeholder POS_Y = new PlaceholderBuilder()
        .name("posY")
        .returnType(Types.FLOAT)
        .mapper(args -> new Result("%player.pos.y%"))
        .build();

    @Fn
    public final Placeholder POS_Z = new PlaceholderBuilder()
        .name("posZ")
        .returnType(Types.FLOAT)
        .mapper(args -> new Result("%player.pos.z%"))
        .build();

    @Fn
    public final Placeholder YAW = new PlaceholderBuilder()
        .name("yaw")
        .returnType(Types.FLOAT)
        .mapper(args -> new Result("%player.pos.yaw"))
        .build();

    @Fn
    public final Placeholder PITCH = new PlaceholderBuilder()
        .name("pitch")
        .returnType(Types.FLOAT)
        .mapper(args -> new Result("%player.pos.pitch%"))
        .build();

    @Fn
    public final Placeholder BLOCK_X = new PlaceholderBuilder()
        .name("blockX")
        .returnType(Types.FLOAT)
        .mapper(args -> new Result("%player.block.x%"))
        .build();

    @Fn
    public final Placeholder BLOCK_Y = new PlaceholderBuilder()
        .name("blockY")
        .returnType(Types.FLOAT)
        .mapper(args -> new Result("%player.block.y%"))
        .build();

    @Fn
    public final Placeholder BLOCK_Z = new PlaceholderBuilder()
        .name("blockZ")
        .returnType(Types.FLOAT)
        .mapper(args -> new Result("%player.block.z%"))
        .build();

    @Fn
    public final Placeholder VERSION = new PlaceholderBuilder()
        .name("version")
        .returnType(Types.STRING)
        .mapper(args -> new Result("%player.version%"))
        .build();

    @Fn
    public final Placeholder PROTOCOL = new PlaceholderBuilder()
        .name("protocol")
        .returnType(Types.INT)
        .mapper(args -> new Result("%player.protocol%"))
        .build();

    @Fn
    public final Placeholder GAME_MODE = new PlaceholderBuilder()
        .name("gameMode")
        .returnType(Types.GAME_MODE)
        .mapper(args -> new Result("%player.gamemode%"))
        .build();

    @Fn
    public final Placeholder REGION_NAME = new PlaceholderBuilder()
        .name("regionName")
        .returnType(Types.STRING)
        .mapper(args -> new Result("%player.region.name%"))
        .build();

    @Fn
    public final Placeholder GROUP_NAME = new PlaceholderBuilder()
        .name("groupName")
        .returnType(Types.STRING)
        .mapper(args -> new Result("%player.group.name%"))
        .build();

    @Fn
    public final Placeholder GROUP_TAG = new PlaceholderBuilder()
        .name("groupTag")
        .returnType(Types.STRING)
        .mapper(args -> new Result("%player.group.tag%"))
        .build();

    @Fn
    public final Placeholder GROUP_PRIORITY = new PlaceholderBuilder()
        .name("groupPriority")
        .returnType(Types.INT)
        .mapper(args -> new Result("%player.group.priority%"))
        .build();

    @Fn
    public final Placeholder GROUP_COLOR = new PlaceholderBuilder()
        .name("groupColor")
        .returnType(Types.STRING)
        .mapper(args -> new Result("%player.group.color%"))
        .build();

    @Fn
    public final Placeholder TEAM_NAME = new PlaceholderBuilder()
        .name("teamName")
        .returnType(Types.STRING)
        .mapper(args -> new Result("%player.team.name%"))
        .build();

    @Fn
    public final Placeholder TEAM_TAG = new PlaceholderBuilder()
        .name("teamTag")
        .returnType(Types.STRING)
        .mapper(args -> new Result("%player.team.tag%"))
        .build();

    @Fn
    public final Placeholder TEAM_COLOR = new PlaceholderBuilder()
        .name("teamColor")
        .returnType(Types.STRING)
        .mapper(args -> new Result("%player.team.color%"))
        .build();

    @Fn
    public final Placeholder TEAM_PLAYERS = new PlaceholderBuilder()
        .name("teamPlayers")
        .parameters(Parameter.required("team", Types.STRING))
        .returnType(Types.STRING)
        .mapper(
            args -> new Result("%player.team.players/{team}%")
                .set("team", args.getString("team"))
        )
        .build();

    @Fn
    public final Placeholder PARKOUR_TICKS = new PlaceholderBuilder()
        .name("parkourTicks")
        .returnType(Types.INT)
        .mapper(args -> new Result("%player.parkour.ticks%"))
        .build();

    @Fn
    public final Placeholder PARKOUR_FORMATTED = new PlaceholderBuilder()
        .name("parkourFormatted")
        .returnType(Types.STRING)
        .mapper(args -> new Result("%player.parkour.formatted%"))
        .build();
}
