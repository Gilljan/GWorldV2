package de.gilljan.gworld.commands;

import de.gilljan.gworld.GWorld;
import de.gilljan.gworld.commands.tabcompletion.CompletionNode;
import de.gilljan.gworld.data.properties.WorldProperty;
import de.gilljan.gworld.data.world.WorldData;
import de.gilljan.gworld.utils.SendMessageUtil;
import de.gilljan.gworld.world.ManageableWorld;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class GInfoCommand extends ArgsCommand {

    public GInfoCommand() {
        super("ginfo", 0, "Info.use", "gworld.commands.info");
    }

    @Override
    public boolean executeCommandForPlayer(Player player, String[] args) {
        if (args.length > 1) {
            player.sendMessage(SendMessageUtil.sendMessage("Info.use"));
            return false;
        }

        String worldName = (args.length > 0) ? args[0] : player.getWorld().getName();

        return GWorld.getInstance().getWorldManager().getWorld(worldName)
                .map(world -> {
                    sendInfo(player, world);

                    return true;
                }).orElseGet(() -> {
                    String messageKey = (args.length > 0) ? "Info.failed" : "Info.failed_world";
                    player.sendMessage(SendMessageUtil.sendMessage(messageKey).replace("%world%", worldName));

                    return false;
                });
    }

    @Override
    public boolean executeConsoleCommand(ConsoleCommandSender console, String[] args) {
        if (args.length != 1) {
            console.sendMessage(SendMessageUtil.sendMessage("Info.use"));
            return false;
        }

        Optional<ManageableWorld> world = GWorld.getInstance().getWorldManager().getWorld(args[0]);

        if (world.isEmpty()) {
            console.sendMessage(SendMessageUtil.sendMessage("Info.failed").replace("%world%", args[0]));
            return false;
        }

        //Send messages
        sendInfo(console, world.get());
        return true;
    }

    @Override
    protected CompletionNode createCompletions(CommandSender sender) {
        CompletionNode root = new CompletionNode("ginfo");

        for (WorldData world : GWorld.getInstance().getDataHandler().getWorlds().values()) {
            root.addChild(new CompletionNode(world.getGeneralInformation().worldName()));
        }

        return root;
    }

    private void sendInfo(CommandSender sender, ManageableWorld world) {
        //Send messages
        sender.sendMessage(SendMessageUtil.sendMessage("Info.header"));
        sender.sendMessage(SendMessageUtil.sendMessage("Info.name") + SendMessageUtil.sendMessage("Info.flags.values").replace("%value%", world.getWorldName()));
        sender.sendMessage(SendMessageUtil.sendMessage("Info.generator") + SendMessageUtil.sendMessage("Info.flags.values").replace("%value%", world.getGenerator() == null ? "Minecraft" : world.getGenerator()));
        sender.sendMessage(SendMessageUtil.sendMessage("Info.flags.environment") + SendMessageUtil.sendMessage("Info.flags.values").replace("%value%", world.getEnvironment().name()));
        sender.sendMessage(SendMessageUtil.sendMessage("Info.flags.type") + SendMessageUtil.sendMessage("Info.flags.values").replace("%value%", world.getWorldType().name()));
        sender.sendMessage(SendMessageUtil.sendMessage("Info.flags.timeCycle") + SendMessageUtil.sendMessage("Info.flags." + String.valueOf(WorldProperty.getValue(WorldProperty.TIME_CYCLE, world))));
        sender.sendMessage(SendMessageUtil.sendMessage("Info.flags.time") + SendMessageUtil.sendMessage("Info.flags.values").replace("%value%", WorldProperty.getValue(WorldProperty.TIME, world).toString()));
        sender.sendMessage(SendMessageUtil.sendMessage("Info.flags.weatherCycle") + SendMessageUtil.sendMessage("Info.flags." + String.valueOf(WorldProperty.getValue(WorldProperty.WEATHER_CYCLE, world))));
        sender.sendMessage(SendMessageUtil.sendMessage("Info.flags.weather") + SendMessageUtil.sendMessage("Info.flags.values").replace("%value%", WorldProperty.getValue(WorldProperty.WEATHER_TYPE, world).name()));
        sender.sendMessage(SendMessageUtil.sendMessage("Info.flags.pvp") + SendMessageUtil.sendMessage("Info.flags." + String.valueOf(WorldProperty.getValue(WorldProperty.ALLOW_PVP, world))));
        sender.sendMessage(SendMessageUtil.sendMessage("Info.flags.mobs") + SendMessageUtil.sendMessage("Info.flags." + String.valueOf(WorldProperty.getValue(WorldProperty.MONSTER_SPAWNING, world))));
        sender.sendMessage(SendMessageUtil.sendMessage("Info.flags.disabledMobs"));

        if(WorldProperty.getValue(WorldProperty.DISABLED_MONSTERS, world).isEmpty()) {
            sender.sendMessage(" §7- " + SendMessageUtil.sendMessage("Info.flags.none"));
        } else {
            for (String mob : WorldProperty.getValue(WorldProperty.DISABLED_MONSTERS, world)) {
                sender.sendMessage(" §7- " + SendMessageUtil.sendMessage("Info.flags.values").replace("%value%", mob));
            }
        }

        sender.sendMessage(SendMessageUtil.sendMessage("Info.flags.animals") + SendMessageUtil.sendMessage("Info.flags." + String.valueOf(WorldProperty.getValue(WorldProperty.ANIMAL_SPAWNING, world))));
        sender.sendMessage(SendMessageUtil.sendMessage("Info.flags.disabledAnimals"));

        if(WorldProperty.getValue(WorldProperty.DISABLED_ANIMALS, world).isEmpty()) {
            sender.sendMessage(" §7- " + SendMessageUtil.sendMessage("Info.flags.none"));
        } else {
            for (String animal : WorldProperty.getValue(WorldProperty.DISABLED_ANIMALS, world)) {
                sender.sendMessage(" §7- " + SendMessageUtil.sendMessage("Info.flags.values").replace("%value%", animal));
            }
        }
        //System.out.println(WorldProperty.getValue(WorldProperty.KEEP_SPAWN_IN_MEMORY, world));

        sender.sendMessage(SendMessageUtil.sendMessage("Info.flags.forcedGamemode") + SendMessageUtil.sendMessage("Info.flags." + String.valueOf(WorldProperty.getValue(WorldProperty.DEFAULT_GAMEMODE, world))));
        sender.sendMessage(SendMessageUtil.sendMessage("Info.flags.defaultGamemode") + SendMessageUtil.sendMessage("Info.flags.values").replace("%value%", WorldProperty.getValue(WorldProperty.GAMEMODE, world).name()));
        sender.sendMessage(SendMessageUtil.sendMessage("Info.flags.difficulty") + SendMessageUtil.sendMessage("Info.flags.values").replace("%value%", WorldProperty.getValue(WorldProperty.DIFFICULTY, world).name()));
        sender.sendMessage(SendMessageUtil.sendMessage("Info.flags.randomTickSpeed") + SendMessageUtil.sendMessage("Info.flags.values").replace("%value%", WorldProperty.getValue(WorldProperty.RANDOM_TICK_SPEED, world).toString()));
        sender.sendMessage(SendMessageUtil.sendMessage("Info.flags.announceAdvancements") + SendMessageUtil.sendMessage("Info.flags." + String.valueOf(WorldProperty.getValue(WorldProperty.ANNOUNCE_ADVANCEMENTS, world))));
        sender.sendMessage(SendMessageUtil.sendMessage("Info.flags.keepSpawnInMemory") + SendMessageUtil.sendMessage("Info.flags." + String.valueOf(WorldProperty.getValue(WorldProperty.KEEP_SPAWN_IN_MEMORY, world))));
        sender.sendMessage(SendMessageUtil.sendMessage("Info.flags.loadOnStartup") + SendMessageUtil.sendMessage("Info.flags." + String.valueOf(WorldProperty.getValue(WorldProperty.LOAD_ON_STARTUP, world))));

        sender.sendMessage(SendMessageUtil.sendMessage("Info.footer"));


    }
}
