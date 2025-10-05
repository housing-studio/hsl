package org.housingstudio.hsl.compiler.codegen.impl.htsl;

import lombok.experimental.UtilityClass;

/**
 * @see <a href="https://github.com/BusterBrown1218/HTSL/blob/main/actions/syntax.js">HTSL Syntax</a>
 */
@UtilityClass
public class HTSL {
    @UtilityClass
    public class Action {
        public final HtslCommand APPLY_LAYOUT = new HtslCommand("applyLayout <layout>");
        public final HtslCommand APPLY_POTION = new HtslCommand("applyPotion <effect> <duration> <level> <override_existing_effects> <show_potion_icon>");
        // TODO BALANCE_PLAYER_TEAM
        public final HtslCommand CANCEL_EVENT = new HtslCommand("cancelEvent");
        public final HtslCommand GLOBAL_STAT = new HtslCommand("globalstat <variable> <operation> <value>");
        public final HtslCommand GLOBAL_VAR = new HtslCommand("globalvar <variable> <operation> <value> <automatic_unset>");
        public final HtslCommand CHANGE_HEALTH = new HtslCommand("changeHealth <mode> <health>");
        public final HtslCommand HUNGER_LEVEL = new HtslCommand("hungerLevel <mode> <level>");
        public final HtslCommand MAX_HEALTH = new HtslCommand("maxHealth <mode> <max_health> <heal_on_change>");
        public final HtslCommand CHANGE_PLAYER_GROUP = new HtslCommand("changePlayerGroup <group> <demotion_protection>");
        public final HtslCommand CHANGE_GROUP = new HtslCommand("changeGroup <group> <demotion_protection>"); // TODO why is this duplicate?
        public final HtslCommand STAT = new HtslCommand("stat <variable> <operation> <value>");
        public final HtslCommand VAR = new HtslCommand("var <variable> <operation> <value> <automatic_unset>");
        public final HtslCommand TEAM_STAT = new HtslCommand("teamstat <variable> <team> <operation> <value>");
        public final HtslCommand TEAM_VAR = new HtslCommand("teamvar <variable> <team> <operation> <value> <automatic_unset>");
        public final HtslCommand CLEAR_EFFECTS = new HtslCommand("clearEffects");
        public final HtslCommand CLOSE_MENU = new HtslCommand("closeMenu");
        public final HtslCommand ACTION_BAR = new HtslCommand("actionBar <message>");
        public final HtslCommand DISPLAY_MENU = new HtslCommand("displayMenu <menu>");
        public final HtslCommand TITLE = new HtslCommand("title <title> <subtitle> <fadein> <stay> <fadeout>");
        public final HtslCommand ENCHANT = new HtslCommand("enchant <enchantment> <level>");
        public final HtslCommand EXIT = new HtslCommand("exit");
        public final HtslCommand FAIL_PARKOUR = new HtslCommand("failParkour <reason>");
        public final HtslCommand FULL_HEAL = new HtslCommand("fullHeal");
        public final HtslCommand XP_LEVEL = new HtslCommand("xpLevel <levels>");
        public final HtslCommand GIVE_ITEM = new HtslCommand("giveItem <item> <allow_multiple> <inventory_slot> <replace_existing_item>");
        public final HtslCommand HOUSE_SPAWN = new HtslCommand("houseSpawn");
        public final HtslCommand KILL = new HtslCommand("kill");
        public final HtslCommand PARK_CHECK = new HtslCommand("parkCheck");
        public final HtslCommand PAUSE = new HtslCommand("pause <ticks_to_wait>");
        public final HtslCommand SOUND = new HtslCommand("sound <sound> <volume> <pitch> <location>");
        public final HtslCommand REMOVE_ITEM = new HtslCommand("removeItem <item>");
        public final HtslCommand RESET_INVENTORY = new HtslCommand("resetInventory");
        public final HtslCommand CHAT = new HtslCommand("chat <message>");
        public final HtslCommand LOBBY = new HtslCommand("lobby <location>");
        public final HtslCommand COMPASS_TARGET = new HtslCommand("compassTarget <location>");
        public final HtslCommand GAMEMODE = new HtslCommand("gamemode <gamemode>");
        public final HtslCommand SET_TEAM = new HtslCommand("setTeam <team>");
        public final HtslCommand TP = new HtslCommand("tp <location> <prevent_teleport_inside_blocks>");
        public final HtslCommand FUNCTION = new HtslCommand("function <function> <trigger_for_all_players>");
        public final HtslCommand CONSUME_ITEM = new HtslCommand("consumeItem");
        public final HtslCommand DROP_ITEM = new HtslCommand("dropItem <item> <location> <drop_naturally> <disable_item_merging> <prioritize_player> <fallback_to_inventory> <despawn_duration_ticks> <pickup_delay_ticks>");
        public final HtslCommand CHANGE_VELOCITY = new HtslCommand("changeVelocity <x_direction> <y_direction> <z_direction>");
        public final HtslCommand LAUNCH_TARGET = new HtslCommand("launchTarget <target_location> <launch_strength>");
        public final HtslCommand IF = new HtslCommand("if <match_any_condition> (<conditions>) {\n<if_actions>\n} else {\n<else_actions>\n}");
        public final HtslCommand RANDOM = new HtslCommand("random {\n<actions>\n}");
        public final HtslCommand PLAYER_WEATHER = new HtslCommand("playerWeather <weather>");
        public final HtslCommand PLAYER_TIME = new HtslCommand("playerTime <time>");
        public final HtslCommand DISPLAY_NAMETAG = new HtslCommand("displayNametag <display_nametag>");
    }
}
