package de.gilljan.gworld.commands;

import de.gilljan.gworld.GWorld;
import de.gilljan.gworld.commands.tabcompletion.CompletionNode;
import de.gilljan.gworld.data.properties.WorldProperty;
import de.gilljan.gworld.data.world.WorldData;
import de.gilljan.gworld.enums.Settings;
import de.gilljan.gworld.utils.SendMessageUtil;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GSetCommand extends ArgsCommand {
    public GSetCommand() {
        super("gset", 3, "Set.use", "gworld.commands.gset");
    }

    @Override
    public boolean executeCommandForPlayer(Player player, String[] args) {
        GWorld.getInstance().getDataHandler().getWorld(args[0]).ifPresentOrElse(worldData -> {
            setFlag(player, worldData, args);
            GWorld.getInstance().getDataHandler().saveWorld(worldData);
        }, () -> System.out.println("No WorldData found for world: " + args[0]));

        return true;
    }

    @Override
    public boolean executeConsoleCommand(ConsoleCommandSender console, String[] args) {
        return false;
    }

    @Override
    protected CompletionNode createCompletions() {
        CompletionNode root = new CompletionNode("gset");

        for(World world : GWorld.getInstance().getServer().getWorlds()) {
            CompletionNode worldNode = new CompletionNode(world.getName());

            root.addChild(worldNode);
        }

        List<CompletionNode> settingsNodes = new ArrayList<>();

        for (Settings setting : Settings.values()) {
            CompletionNode settingNode = new CompletionNode(setting.toString());

            setting.getValues().forEach((item) -> settingNode.addChild(new CompletionNode(item.toLowerCase())));

            settingsNodes.add(settingNode);
        }

        root.addForAllChildren(settingsNodes);

        return root;
    }

    private void setFlag(CommandSender sender, WorldData worldData, String[] args) {
        Settings setting = Settings.mapFromString(args[1]);

        if (setting == null || !validateArgument(setting, args[2])) {
            sender.sendMessage(SendMessageUtil.sendMessage("Set.use").replace("%world%", worldData.getGeneralInformation().worldName()));
            return;
        }

        switch (setting) {
            case ANIMAL_SPECIFIC -> handleSpecificEntities(sender, worldData, WorldProperty.DISABLED_ANIMALS, args, setting);
            case MONSTER_SPECIFIC -> handleSpecificEntities(sender, worldData, WorldProperty.DISABLED_MONSTERS, args, setting);
            case TIME -> handleLongFlags(sender, worldData, args, setting);
            case RANDOM_TICK_SPEED -> handleIntegerFlags(sender, worldData, WorldProperty.RANDOM_TICK_SPEED, args, setting);
            case DEFAULT_GAMEMODE -> handleGameModeFlags(sender, worldData, args, setting);
            case DIFFICULTY -> handleDifficultyFlags(sender, worldData, args, setting);
            case WEATHER -> handleWeatherFlags(sender, worldData, args, setting);
            case WEATHER_CYCLE -> handleBooleanFlags(sender, worldData, WorldProperty.WEATHER_CYCLE, args, setting);
            case TIME_CYCLE -> handleBooleanFlags(sender, worldData, WorldProperty.TIME_CYCLE, args, setting);
            case PVP -> handleBooleanFlags(sender, worldData, WorldProperty.ALLOW_PVP, args, setting);
            case MONSTERS -> handleBooleanFlags(sender, worldData, WorldProperty.MONSTER_SPAWNING, args, setting);
            case ANIMALS -> handleBooleanFlags(sender, worldData, WorldProperty.ANIMAL_SPAWNING, args, setting);
            case FORCED_GAMEMODE -> handleBooleanFlags(sender, worldData, WorldProperty.DEFAULT_GAMEMODE, args, setting);
            case ANNOUNCE_ADVANCEMENTS -> handleBooleanFlags(sender, worldData, WorldProperty.ANNOUNCE_ADVANCEMENTS, args, setting);
            case LOAD_ON_STARTUP -> handleBooleanFlags(sender, worldData, WorldProperty.LOAD_ON_STARTUP, args, setting);
        }
    }

    private void handleSpecificEntities(CommandSender sender, WorldData worldData, WorldProperty<List<String>> property, String[] args, Settings setting) {
        List<String> disabledEntities = WorldProperty.getValue(property, worldData);
        String entity = args[2];

        if(!GWorld.MONSTER.contains(entity) && !GWorld.ANIMALS.contains(entity)) {
            sender.sendMessage(SendMessageUtil.sendMessage("Set.failed").replace("%world%", worldData.getGeneralInformation().worldName()));
            return;
        }
/*
        if(!setting.getValues().contains(entity)) {
            sender.sendMessage(SendMessageUtil.sendMessage("Set.failed").replaceAll("%world%", worldData.getGeneralInformation().worldName()));
            return;
        }*/

        if (args[2].equalsIgnoreCase("list")) {
            sender.sendMessage(SendMessageUtil.sendMessage("Set.list").replace("%world%", worldData.getGeneralInformation().worldName()).replace("%setting%", setting.toString()).replace("%value%", disabledEntities.toString()));
            return;
        }

        if (disabledEntities.contains(entity)) {
            disabledEntities.remove(entity);
            sender.sendMessage(SendMessageUtil.sendMessage("Set.success").replace("%world%", worldData.getGeneralInformation().worldName()).replace("%setting%", setting.toString()).replace("%value%", entity));
        } else {
            disabledEntities.add(entity);
            sender.sendMessage(SendMessageUtil.sendMessage("Set.success").replace("%world%", worldData.getGeneralInformation().worldName()).replace("%setting%", setting.toString()).replace("%value%", entity));
        }

        WorldProperty.setValue(property, worldData, disabledEntities);
    }

    private void handleIntegerFlags(CommandSender sender, WorldData worldData, WorldProperty<Integer> property, String[] args, Settings setting) {
        try {
            int value = Integer.parseInt(args[2]);
            WorldProperty.setValue(property, worldData, value);
            sender.sendMessage(SendMessageUtil.sendMessage("Set.success").replace("%world%", worldData.getGeneralInformation().worldName()).replace("%setting%", setting.toString()).replace("%value%", String.valueOf(value)));
        } catch (NumberFormatException e) {
            sender.sendMessage(SendMessageUtil.sendMessage("Set.failed").replace("%world%", worldData.getGeneralInformation().worldName()));
        }
    }

    private void handleLongFlags(CommandSender sender, WorldData worldData, String[] args, Settings setting) {
        try {
            long value = Long.parseLong(args[2]);
            WorldProperty.setValue(WorldProperty.TIME, worldData, value);
            sender.sendMessage(SendMessageUtil.sendMessage("Set.success").replace("%world%", worldData.getGeneralInformation().worldName()).replace("%setting%", setting.toString()).replace("%value%", String.valueOf(value)));
        } catch (NumberFormatException e) {
            sender.sendMessage(SendMessageUtil.sendMessage("Set.failed").replace("%world%", worldData.getGeneralInformation().worldName()));
        }
    }

    private void handleBooleanFlags(CommandSender sender, WorldData worldData, WorldProperty<Boolean> property, String[] args, Settings setting) {
        boolean value = Boolean.parseBoolean(args[2]);
        WorldProperty.setValue(property, worldData, value);
        sender.sendMessage(SendMessageUtil.sendMessage("Set.success").replace("%world%", worldData.getGeneralInformation().worldName()).replace("%setting%", setting.toString()).replace("%value%", String.valueOf(value)));
    }

    private void handleGameModeFlags(CommandSender sender, WorldData worldData, String[] args, Settings setting) {
        try {
            GameMode value = GameMode.valueOf(args[2].toUpperCase());
            WorldProperty.setValue(WorldProperty.GAMEMODE, worldData, value);
            sender.sendMessage(SendMessageUtil.sendMessage("Set.success").replace("%world%", worldData.getGeneralInformation().worldName()).replace("%setting%", setting.toString()).replace("%value%", value.toString()));
        } catch (IllegalArgumentException e) {
            sender.sendMessage(SendMessageUtil.sendMessage("Set.failed").replace("%world%", worldData.getGeneralInformation().worldName()));
        }
    }

    private void handleDifficultyFlags(CommandSender sender, WorldData worldData, String[] args, Settings setting) {
        try {
            Difficulty value = Difficulty.valueOf(args[2].toUpperCase());
            WorldProperty.setValue(WorldProperty.DIFFICULTY, worldData, value);
            sender.sendMessage(SendMessageUtil.sendMessage("Set.success").replace("%world%", worldData.getGeneralInformation().worldName()).replace("%setting%", setting.toString()).replace("%value%", value.toString()));
        } catch (IllegalArgumentException e) {
            sender.sendMessage(SendMessageUtil.sendMessage("Set.failed").replace("%world%", worldData.getGeneralInformation().worldName()));
        }
    }

    private void handleWeatherFlags(CommandSender sender, WorldData worldData, String[] args, Settings setting) {
        try {
            WorldData.WeatherType value = WorldData.WeatherType.valueOf(args[2].toUpperCase());
            WorldProperty.setValue(WorldProperty.WEATHER_TYPE, worldData, value);
            sender.sendMessage(SendMessageUtil.sendMessage("Set.success").replace("%world%", worldData.getGeneralInformation().worldName()).replace("%setting%", setting.toString()).replace("%value%", value.toString()));
        } catch (IllegalArgumentException e) {
            sender.sendMessage(SendMessageUtil.sendMessage("Set.failed").replace("%world%", worldData.getGeneralInformation().worldName()));
        }
    }

    private boolean validateArgument(Settings settings, String arg) {
        return settings.getValues().stream()
                .map(String::toLowerCase)
                .anyMatch(arg::equalsIgnoreCase);
    }
}