package de.gilljan.gworld.commands;

import de.gilljan.gworld.GWorld;
import de.gilljan.gworld.data.properties.WorldProperty;
import de.gilljan.gworld.data.world.WorldData;
import de.gilljan.gworld.enums.Settings;
import de.gilljan.gworld.utils.SendMessageUtil;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class GSetCommand extends ArgsCommand{
    public GSetCommand() {
        super("gset", 3, "Set.use", "gworld.commands.gset");
    }

    @Override
    public boolean executeCommandForPlayer(Player player, String[] args) {
        return true;
    }

    @Override
    public boolean executeConsoleCommand(ConsoleCommandSender console, String[] args) {
        return false;
    }

    private void setFlag(CommandSender sender, String[] args) {
        WorldData worldData = GWorld.getInstance().getDataHandler().getWorld(args[0]);

        if(worldData == null) {
            sender.sendMessage(SendMessageUtil.sendMessage("Set.failed").replaceAll("%world%", args[0]));
            return;
        }

        Settings setting = Settings.valueOf(args[1].toUpperCase());

        switch (setting) {
            case ANIMAL_SPECIFIC:
                handleSpecificEntities(sender, worldData, WorldProperty.DISABLED_ANIMALS, args);
                break;
            case MONSTER_SPECIFIC:
                handleSpecificEntities(sender, worldData, WorldProperty.DISABLED_MONSTERS, args);
                break;
            case TIME:
                handleLongFlags(sender, worldData, args);
                break;
            case RANDOM_TICK_SPEED:
                handleIntegerFlags(sender, worldData, WorldProperty.RANDOM_TICK_SPEED, args);
                break;
            case DEFAULT_GAMEMODE:
                handleGameModeFlags(sender, worldData, args);
                break;
            case DIFFICULTY:
                handleDifficultyFlags(sender, worldData, args);
                break;
            case WEATHER:
                handleWeatherFlags(sender, worldData, args);
                break;
            case WEATHER_CYCLE:
                handleBooleanFlags(sender, worldData, WorldProperty.WEATHER_CYCLE, args);
                break;
            case TIME_CYCLE:
                handleBooleanFlags(sender, worldData, WorldProperty.TIME_CYCLE, args);
                break;
            case PVP:
                handleBooleanFlags(sender, worldData, WorldProperty.ALLOW_PVP, args);
                break;
            case MONSTERS:
                handleBooleanFlags(sender, worldData, WorldProperty.MONSTER_SPAWNING, args);
                break;
            case ANIMALS:
                handleBooleanFlags(sender, worldData, WorldProperty.ANIMAL_SPAWNING, args);
                break;
            case FORCED_GAMEMODE:
                handleBooleanFlags(sender, worldData, WorldProperty.DEFAULT_GAMEMODE, args);
                break;
            case ANNOUNCE_ADVANCEMENTS:
                handleBooleanFlags(sender, worldData, WorldProperty.ANNOUNCE_ADVANCEMENTS, args);
                break;
            case LOAD_ON_STARTUP:
                handleBooleanFlags(sender, worldData, WorldProperty.LOAD_ON_STARTUP, args);
                break;
        }
    }

    private void handleSpecificEntities(CommandSender sender, WorldData worldData, WorldProperty<List<String>> property, String[] args) {
        List<String> disabledEntities = WorldProperty.getValue(property, worldData);
        String entity = args[2];

        if(args[2].equalsIgnoreCase("list")) {
            sender.sendMessage(SendMessageUtil.sendMessage("Set.list").replaceAll("%world%", worldData.getGeneralInformation().worldName()).replaceAll("%setting%", setting.toString()).replaceAll("%value%", disabledEntities.toString()));
            return;
        }

        if(disabledEntities.contains(entity)) {
            disabledEntities.remove(entity);
            sender.sendMessage(SendMessageUtil.sendMessage("Set.success").replaceAll("%world%", worldData.getGeneralInformation().worldName()).replaceAll("%setting%", setting.toString()).replaceAll("%value%", entity));
        } else {
            disabledEntities.add(entity);
            sender.sendMessage(SendMessageUtil.sendMessage("Set.success").replaceAll("%world%", worldData.getGeneralInformation().worldName()).replaceAll("%setting%", setting.toString()).replaceAll("%value%", entity));
        }

        WorldProperty.setValue(property, worldData, disabledEntities);
    }

    private void handleIntegerFlags(CommandSender sender, WorldData worldData, WorldProperty<Integer> property, String[] args) {
        try {
            int value = Integer.parseInt(args[2]);
            WorldProperty.setValue(property, worldData, value);
            sender.sendMessage(SendMessageUtil.sendMessage("Set.success").replaceAll("%world%", worldData.getGeneralInformation().worldName()).replaceAll("%setting%", setting.toString()).replaceAll("%value%", String.valueOf(value)));
        } catch (NumberFormatException e) {
            sender.sendMessage(SendMessageUtil.sendMessage("Set.failed").replaceAll("%world%", worldData.getGeneralInformation().worldName()));
        }
    }

    private void handleLongFlags(CommandSender sender, WorldData worldData, String[] args) {
        try {
            long value = Long.parseLong(args[2]);
            WorldProperty.setValue(WorldProperty.TIME, worldData, value);
            sender.sendMessage(SendMessageUtil.sendMessage("Set.success").replaceAll("%world%", worldData.getGeneralInformation().worldName()).replaceAll("%setting%", setting.toString()).replaceAll("%value%", String.valueOf(value)));
        } catch (NumberFormatException e) {
            sender.sendMessage(SendMessageUtil.sendMessage("Set.failed").replaceAll("%world%", worldData.getGeneralInformation().worldName()));
        }
    }

    private void handleBooleanFlags(CommandSender sender, WorldData worldData, WorldProperty<Boolean> property, String[] args) {
        boolean value = Boolean.parseBoolean(args[2]);
        WorldProperty.setValue(property, worldData, value);
        sender.sendMessage(SendMessageUtil.sendMessage("Set.success").replaceAll("%world%", worldData.getGeneralInformation().worldName()).replaceAll("%setting%", setting.toString()).replaceAll("%value%", String.valueOf(value)));
    }
    
    private void handleGameModeFlags(CommandSender sender, WorldData worldData, String[] args) {
        try {
            WorldProperty.setValue(WorldProperty.GAMEMODE, worldData, GameMode.valueOf(args[2].toUpperCase()));
            sender.sendMessage(SendMessageUtil.sendMessage("Set.success").replaceAll("%world%", worldData.getGeneralInformation().worldName()).replaceAll("%setting%", setting.toString()).replaceAll("%value%", value.toString()));
        } catch (IllegalArgumentException e) {
            sender.sendMessage(SendMessageUtil.sendMessage("Set.failed").replaceAll("%world%", worldData.getGeneralInformation().worldName()));
        }
    }
    
    private void handleDifficultyFlags(CommandSender sender, WorldData worldData, String[] args) {
        try {
            WorldProperty.setValue(WorldProperty.DIFFICULTY, worldData, Difficulty.valueOf(args[2].toUpperCase()));
            sender.sendMessage(SendMessageUtil.sendMessage("Set.success").replaceAll("%world%", worldData.getGeneralInformation().worldName()).replaceAll("%setting%", setting.toString()).replaceAll("%value%", value.toString()));
        } catch (IllegalArgumentException e) {
            sender.sendMessage(SendMessageUtil.sendMessage("Set.failed").replaceAll("%world%", worldData.getGeneralInformation().worldName()));
        }
    }
    
    private void handleWeatherFlags(CommandSender sender, WorldData worldData, String[] args) {
        try {
            WorldProperty.setValue(WorldProperty.WEATHER_TYPE, worldData, WorldData.WeatherType.valueOf(args[2].toUpperCase()));
            sender.sendMessage(SendMessageUtil.sendMessage("Set.success").replaceAll("%world%", worldData.getGeneralInformation().worldName()).replaceAll("%setting%", WorldProperty.WEATHER_TYPE.toString()).replaceAll("%value%", value.toString()));
        } catch (IllegalArgumentException e) {
            sender.sendMessage(SendMessageUtil.sendMessage("Set.failed").replaceAll("%world%", worldData.getGeneralInformation().worldName()));
        }
    }
}
