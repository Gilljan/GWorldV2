package de.gilljan.gworld.commands;

import de.gilljan.gworld.GWorld;
import de.gilljan.gworld.data.properties.WorldProperty;
import de.gilljan.gworld.data.world.WorldData;
import de.gilljan.gworld.utils.SendMessageUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class GInfoCommand extends ArgsCommand {

    public GInfoCommand() {
        super("ginfo", 0, "Info.use", "gworld.commands.ginfo");
    }

    @Override
    public boolean executeCommandForPlayer(Player player, String[] args) {
        if(args.length > 1) {
            player.sendMessage(SendMessageUtil.sendMessage("Info.use"));
            return false;
        }

        WorldData world;
        boolean arg = false;
        if(args.length == 0) {
            world = GWorld.getInstance().getDataHandler().getWorld(player.getWorld().getName());
        } else {
            world = GWorld.getInstance().getDataHandler().getWorld(args[0]);
            arg = true;
        }

        if(world == null) {
            if(arg) {
                player.sendMessage(SendMessageUtil.sendMessage("Info.failed").replaceAll("%world%", args[0]));
            } else {
                player.sendMessage(SendMessageUtil.sendMessage("Info.failed_world"));
            }

            return false;
        }

        //Send messages
        sendInfo(player, world);

        return true;
    }

    @Override
    public boolean executeConsoleCommand(ConsoleCommandSender console, String[] args) {
        if(args.length != 1) {
            console.sendMessage(SendMessageUtil.sendMessage("Info.use"));
            return false;
        }

        WorldData world = GWorld.getInstance().getDataHandler().getWorld(args[0]);

        if(world == null) {
            console.sendMessage(SendMessageUtil.sendMessage("Info.failed").replaceAll("%world%", args[0]));
            return false;
        }

        //Send messages
        sendInfo(console, world);
        return true;
    }

    private void sendInfo(CommandSender sender, WorldData world) {
        //Send messages
        sender.sendMessage(SendMessageUtil.sendMessage("Info.header"));
        sender.sendMessage(SendMessageUtil.sendMessage("Info.name") + SendMessageUtil.sendMessage("Info.flags.values").replaceAll("%value%", world.getGeneralInformation().worldName()));
        sender.sendMessage(SendMessageUtil.sendMessage("Info.generator") + SendMessageUtil.sendMessage("Info.flags.values").replaceAll("%value%", world.getGeneralInformation().worldGenerator() == null ? "Minecraft" : world.getGeneralInformation().worldGenerator()));
        sender.sendMessage(SendMessageUtil.sendMessage("Info.flags.environment") + SendMessageUtil.sendMessage("Info.flags.values").replaceAll("%value%", world.getGeneralInformation().environment().name()));
        sender.sendMessage(SendMessageUtil.sendMessage("Info.flags.type") + SendMessageUtil.sendMessage("Info.flags.values").replaceAll("%value%", world.getGeneralInformation().worldType().name()));
        sender.sendMessage(SendMessageUtil.sendMessage("Info.flags.timeCycle") + SendMessageUtil.sendMessage("Info.flags." + String.valueOf(WorldProperty.getValue(WorldProperty.TIME_CYCLE, world))));
        sender.sendMessage(SendMessageUtil.sendMessage("Info.flags.time") + SendMessageUtil.sendMessage("Info.flags.values").replaceAll("%value%", WorldProperty.getValue(WorldProperty.TIME, world).toString()));
        sender.sendMessage(SendMessageUtil.sendMessage("Info.flags.weatherCycle") + SendMessageUtil.sendMessage("Info.flags." + String.valueOf(WorldProperty.getValue(WorldProperty.WEATHER_CYCLE, world))));
        sender.sendMessage(SendMessageUtil.sendMessage("Info.flags.weather") + SendMessageUtil.sendMessage("Info.flags.values").replaceAll("%value%", WorldProperty.getValue(WorldProperty.WEATHER_TYPE, world).name()));
        sender.sendMessage(SendMessageUtil.sendMessage("Info.flags.pvp") + SendMessageUtil.sendMessage("Info.flags." + String.valueOf(WorldProperty.getValue(WorldProperty.ALLOW_PVP, world))));
        sender.sendMessage(SendMessageUtil.sendMessage("Info.flags.mobs") + SendMessageUtil.sendMessage("Info.flags." + String.valueOf(WorldProperty.getValue(WorldProperty.MONSTER_SPAWNING, world))));
        sender.sendMessage(SendMessageUtil.sendMessage("Info.flags.disabledMobs"));

        for(String mob : WorldProperty.getValue(WorldProperty.DISABLED_MONSTERS, world)) {
            sender.sendMessage(" §7- " + SendMessageUtil.sendMessage("Info.flags.values").replaceAll("%value%", mob));
        }

        sender.sendMessage(SendMessageUtil.sendMessage("Info.flags.animals") + SendMessageUtil.sendMessage("Info.flags." + String.valueOf(WorldProperty.getValue(WorldProperty.ANIMAL_SPAWNING, world))));
        sender.sendMessage(SendMessageUtil.sendMessage("Info.flags.disabledAnimals"));
        for(String animal : WorldProperty.getValue(WorldProperty.DISABLED_ANIMALS, world)) {
            sender.sendMessage(" §7- " + SendMessageUtil.sendMessage("Info.flags.values").replaceAll("%value%", animal));
        }
        //System.out.println(WorldProperty.getValue(WorldProperty.KEEP_SPAWN_IN_MEMORY, world));

        sender.sendMessage(SendMessageUtil.sendMessage("Info.flags.forcedGamemode") + SendMessageUtil.sendMessage("Info.flags." + String.valueOf(WorldProperty.getValue(WorldProperty.DEFAULT_GAMEMODE, world))));
        sender.sendMessage(SendMessageUtil.sendMessage("Info.flags.defaultGamemode") + SendMessageUtil.sendMessage("Info.flags.values").replaceAll("%value%", WorldProperty.getValue(WorldProperty.GAMEMODE, world).name()));
        sender.sendMessage(SendMessageUtil.sendMessage("Info.flags.difficulty") + SendMessageUtil.sendMessage("Info.flags.values").replaceAll("%value%", WorldProperty.getValue(WorldProperty.DIFFICULTY, world).name()));
        sender.sendMessage(SendMessageUtil.sendMessage("Info.flags.randomTickSpeed") + SendMessageUtil.sendMessage("Info.flags.values").replaceAll("%value%", WorldProperty.getValue(WorldProperty.RANDOM_TICK_SPEED, world).toString()));
        sender.sendMessage(SendMessageUtil.sendMessage("Info.flags.announceAdvancements") + SendMessageUtil.sendMessage("Info.flags." + String.valueOf(WorldProperty.getValue(WorldProperty.ANNOUNCE_ADVANCEMENTS, world))));
        sender.sendMessage(SendMessageUtil.sendMessage("Info.flags.keepSpawnInMemory") + SendMessageUtil.sendMessage("Info.flags." + String.valueOf(WorldProperty.getValue(WorldProperty.KEEP_SPAWN_IN_MEMORY, world))));
        sender.sendMessage(SendMessageUtil.sendMessage("Info.flags.loadOnStartup") + SendMessageUtil.sendMessage("Info.flags." + String.valueOf(WorldProperty.getValue(WorldProperty.LOAD_ON_STARTUP, world))));

        sender.sendMessage(SendMessageUtil.sendMessage("Info.footer"));


    }
}
