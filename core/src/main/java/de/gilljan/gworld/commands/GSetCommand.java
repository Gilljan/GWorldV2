package de.gilljan.gworld.commands;

import de.gilljan.gworld.GWorld;
import de.gilljan.gworld.commands.tabcompletion.CompletionNode;
import de.gilljan.gworld.data.properties.WorldProperty;
import de.gilljan.gworld.data.world.WorldData;
import de.gilljan.gworld.enums.Settings;
import de.gilljan.gworld.utils.SendMessageUtil;
import de.gilljan.gworld.world.ManageableWorld;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GSetCommand extends ArgsCommand {
    public GSetCommand() {
        super("gset", 2, "Set.use", "gworld.commands.gset");
    }

    @Override
    public boolean executeCommandForPlayer(Player player, String[] args) {
        if(args.length == 2) {
            World world = player.getWorld();
            GWorld.getInstance().getWorldManager().getWorld(world.getName()).ifPresentOrElse(manageableWorld -> {
                setFlag(player, manageableWorld, new String[]{world.getName(), args[0], args[1]});
                manageableWorld.saveProperties();
            }, () -> GWorld.getInstance().getLogger().warning("No WorldData found for world: " + world.getName()));
            return true;
        }

        GWorld.getInstance().getWorldManager().getWorld(args[0]).ifPresentOrElse(manageableWorld -> {
            setFlag(player, manageableWorld, args);
            manageableWorld.saveProperties();
        }, () -> GWorld.getInstance().getLogger().warning("No WorldData found for world: " + args[0]));

        return true;
    }

    @Override
    public boolean executeConsoleCommand(ConsoleCommandSender console, String[] args) {
        if(args.length < 3) {
            console.sendMessage(SendMessageUtil.sendMessage("Set.use"));
            return false;
        }

        GWorld.getInstance().getWorldManager().getWorld(args[0]).ifPresentOrElse(manageableWorld -> {
            setFlag(console, manageableWorld, args);
            manageableWorld.saveProperties();
        }, () -> GWorld.getInstance().getLogger().warning("No WorldData found for world: " + args[0]));

        return true;
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
        root.addChildren(settingsNodes);

        return root;
    }

    private void setFlag(CommandSender sender, ManageableWorld world, String[] args) {
        Optional<Settings> optionalSetting = Settings.fromString(args[1]);

        if(optionalSetting.isEmpty()) {
            sender.sendMessage(SendMessageUtil.sendMessage("Set.use").replace("%world%", world.getWorldName()));
            return;
        }

        Settings setting = optionalSetting.get();

        if (!validateArgument(setting, args[2])) {
            sender.sendMessage(SendMessageUtil.sendMessage("Set.use").replace("%world%", world.getWorldName()));
            return;
        }

        switch (setting) {
            case DISABLED_ANIMALS -> handleSpecificEntities(sender, world, WorldProperty.DISABLED_ANIMALS, args, setting);
            case DISABLED_MONSTERS -> handleSpecificEntities(sender, world, WorldProperty.DISABLED_MONSTERS, args, setting);
            case TIME -> handleLongFlags(sender, world, args, setting);
            case RANDOM_TICK_SPEED -> handleIntegerFlags(sender, world, WorldProperty.RANDOM_TICK_SPEED, args, setting);
            case DEFAULT_GAMEMODE -> handleGameModeFlags(sender, world, args, setting);
            case DIFFICULTY -> handleDifficultyFlags(sender, world, args, setting);
            case WEATHER -> handleWeatherFlags(sender, world, args, setting);
            case WEATHER_CYCLE -> handleBooleanFlags(sender, world, WorldProperty.WEATHER_CYCLE, args, setting);
            case TIME_CYCLE -> handleBooleanFlags(sender, world, WorldProperty.TIME_CYCLE, args, setting);
            case PVP -> handleBooleanFlags(sender, world, WorldProperty.ALLOW_PVP, args, setting);
            case MONSTERS -> handleBooleanFlags(sender, world, WorldProperty.MONSTER_SPAWNING, args, setting);
            case ANIMALS -> handleBooleanFlags(sender, world, WorldProperty.ANIMAL_SPAWNING, args, setting);
            case FORCED_GAMEMODE -> handleBooleanFlags(sender, world, WorldProperty.DEFAULT_GAMEMODE, args, setting);
            case ANNOUNCE_ADVANCEMENTS -> handleBooleanFlags(sender, world, WorldProperty.ANNOUNCE_ADVANCEMENTS, args, setting);
            case LOAD_ON_STARTUP -> handleBooleanFlags(sender, world, WorldProperty.LOAD_ON_STARTUP, args, setting);
        }
    }

    private void handleSpecificEntities(CommandSender sender, ManageableWorld world, WorldProperty<List<String>> property, String[] args, Settings setting) {
        List<String> disabledEntities = new ArrayList<>(WorldProperty.getValue(property, world));
        String entity = args[2];
        String flagName = SendMessageUtil.sendMessage("Set.flags." + toCamelCase(setting.name()));

        if (!GWorld.MONSTER.contains(entity) && !GWorld.ANIMALS.contains(entity)) {
            sender.sendMessage(SendMessageUtil.sendMessage("Set.invalidEntity").replace("%value%", entity));
            return;
        }

        if (disabledEntities.contains(entity)) {
            disabledEntities.remove(entity);
            sender.sendMessage(SendMessageUtil.sendMessage("Set.remove").replace("%flag%", flagName).replace("%value%", entity));
        } else {
            disabledEntities.add(entity);
            sender.sendMessage(SendMessageUtil.sendMessage("Set.add").replace("%flag%", flagName).replace("%value%", entity));
        }

        WorldProperty.setValue(property, world, disabledEntities);
    }

    private void handleIntegerFlags(CommandSender sender, ManageableWorld world, WorldProperty<Integer> property, String[] args, Settings setting) {
        try {
            int value = Integer.parseInt(args[2]);
            Integer oldValue = WorldProperty.getValue(property, world);
            WorldProperty.setValue(property, world, value);
            sendSuccessMessage(sender, world, setting, String.valueOf(oldValue), String.valueOf(value));
        } catch (NumberFormatException e) {
            sendFailureMessage(sender, world, setting, args[2]);
        }
    }

    private void handleLongFlags(CommandSender sender, ManageableWorld world, String[] args, Settings setting) {
        try {
            long value = Long.parseLong(args[2]);
            Long oldValue = WorldProperty.getValue(WorldProperty.TIME, world);
            WorldProperty.setValue(WorldProperty.TIME, world, value);
            sendSuccessMessage(sender, world, setting, String.valueOf(oldValue), String.valueOf(value));
        } catch (NumberFormatException e) {
            sendFailureMessage(sender, world, setting, args[2]);
        }
    }

    private void handleBooleanFlags(CommandSender sender, ManageableWorld world, WorldProperty<Boolean> property, String[] args, Settings setting) {
        boolean value = Boolean.parseBoolean(args[2]);
        Boolean oldValue = WorldProperty.getValue(property, world);
        WorldProperty.setValue(property, world, value);
        String oldState = SendMessageUtil.sendMessage(oldValue ? "Set.flags.enabled" : "Set.flags.disabled");
        String newState = SendMessageUtil.sendMessage(value ? "Set.flags.enabled" : "Set.flags.disabled");
        sendSuccessMessage(sender, world, setting, oldState, newState);
    }

    private void handleGameModeFlags(CommandSender sender, ManageableWorld world, String[] args, Settings setting) {
        try {
            GameMode value = GameMode.valueOf(args[2].toUpperCase());
            GameMode oldValue = WorldProperty.getValue(WorldProperty.GAMEMODE, world);
            WorldProperty.setValue(WorldProperty.GAMEMODE, world, value);
            sendSuccessMessage(sender, world, setting, oldValue.toString(), value.toString());
        } catch (IllegalArgumentException e) {
            sendFailureMessage(sender, world, setting, args[2]);
        }
    }

    private void handleDifficultyFlags(CommandSender sender, ManageableWorld world, String[] args, Settings setting) {
        try {
            Difficulty value = Difficulty.valueOf(args[2].toUpperCase());
            Difficulty oldValue = WorldProperty.getValue(WorldProperty.DIFFICULTY, world);
            WorldProperty.setValue(WorldProperty.DIFFICULTY, world, value);
            sendSuccessMessage(sender, world, setting, oldValue.toString(), value.toString());
        } catch (IllegalArgumentException e) {
            sendFailureMessage(sender, world, setting, args[2]);
        }
    }

    private void handleWeatherFlags(CommandSender sender, ManageableWorld world, String[] args, Settings setting) {
        try {
            WorldData.WeatherType value = WorldData.WeatherType.valueOf(args[2].toUpperCase());
            WorldData.WeatherType oldValue = WorldProperty.getValue(WorldProperty.WEATHER_TYPE, world);
            WorldProperty.setValue(WorldProperty.WEATHER_TYPE, world, value);
            sendSuccessMessage(sender, world, setting, oldValue.toString(), value.toString());
        } catch (IllegalArgumentException e) {
            sendFailureMessage(sender, world, setting, args[2]);
        }
    }

    private void sendSuccessMessage(CommandSender sender, ManageableWorld world, Settings setting, String oldValue, String newValue) {
        sender.sendMessage(SendMessageUtil.sendMessage("Set.success").replace("%world%", world.getWorldName()));
        sender.sendMessage(SendMessageUtil.sendMessage("Set.changes").replace("%flag%", SendMessageUtil.sendMessage("Set.flags." + toCamelCase(setting.name()))).replace("%oldValue%", oldValue).replace("%newValue%", newValue));
    }

    private void sendFailureMessage(CommandSender sender, ManageableWorld world, Settings setting, String attemptedValue) {
        sender.sendMessage(SendMessageUtil.sendMessage("Set.failed").replace("%world%", world.getWorldName()).replace("%flag%", SendMessageUtil.sendMessage("Set.flags." + toCamelCase(setting.name()))).replace("%value%", attemptedValue));
    }

    private boolean validateArgument(Settings settings, String arg) {
        if (settings.equals(Settings.TIME) || settings.equals(Settings.RANDOM_TICK_SPEED)) {
            return true; //Check in handler
        }
        return settings.getValues().stream()
                .map(String::toLowerCase)
                .anyMatch(arg::equalsIgnoreCase);
    }

    private String toCamelCase(String s) {
        String[] parts = s.split("_");
        StringBuilder camelCaseString = new StringBuilder(parts[0].toLowerCase());
        for (int i = 1; i < parts.length; i++) {
            camelCaseString.append(parts[i].substring(0, 1).toUpperCase()).append(parts[i].substring(1).toLowerCase());
        }
        return camelCaseString.toString();
    }
}