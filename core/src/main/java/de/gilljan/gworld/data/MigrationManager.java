package de.gilljan.gworld.data;

import de.gilljan.gworld.GWorld;
import de.gilljan.gworld.data.migration.LegacyMigrator;
import de.gilljan.gworld.data.world.WorldData;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class MigrationManager {
    // Die Version, die das Plugin aktuell erwartet
    private static final int CURRENT_CONFIG_VERSION = 1;
    private static final List<String> STANDARD_WORLDS = List.of("world", "world_nether", "world_the_end");

    public void process() {
        // 1. LEGACY MIGRATION (1.x -> 2.0)
        LegacyMigrator legacy = new LegacyMigrator();
        List<WorldData> oldData = legacy.checkAndMigrateLegacyWorlds();

        if (!oldData.isEmpty()) {
            GWorld.getInstance().getLogger().info("Integrate " + oldData.size() + " Legacy-worlds into the new system...");

            DataHandler handler = GWorld.getInstance().getDataHandler();
            if (handler instanceof FileConfiguration) {
                ((FileConfiguration) handler).clearAndReset();
            }

            importData(oldData);
        }

        DataHandler handler = GWorld.getInstance().getDataHandler();

        // 2. TRANSFER YAML -> DB (if user switched from YAML to DB)
        if (GWorld.getInstance().getDataHandler() instanceof Database) {
            checkYamlToDatabaseTransfer();
        }

        // 3. SCHEMA UPDATES (2.0 -> 2.1 -> ...)
        if (handler instanceof FileConfiguration) {
            handleYamlUpdates();
        }

        // Datenbank-Updates in DB-Class at startup
    }

    private void importData(List<WorldData> dataList) {
        if(dataList.isEmpty()) return;

        GWorld.getInstance().getLogger().info("Integrate " + dataList.size() + " worlds...");

        for (WorldData wd : dataList) {
            String name = wd.getGeneralInformation().worldName();
            boolean existsInDb = GWorld.getInstance().getDataHandler().fetchWorld(name).isPresent();

            // Write standard worlds always, others only if not existing
            if (STANDARD_WORLDS.contains(name) || !existsInDb) {
                GWorld.getInstance().getDataHandler().addWorld(wd);
            } else {
                GWorld.getInstance().getLogger().info("Skip " + name + " (already exists in DB).");
            }
        }
    }

    private void checkYamlToDatabaseTransfer() {
        File yamlFile = new File(GWorld.getInstance().getDataFolder(), "worlds.yml");
        if (!yamlFile.exists()) return;

        YamlConfiguration config = YamlConfiguration.loadConfiguration(yamlFile);
        if (!config.contains("ConfigVersion") || !config.contains("Worlds")) return; // Only import V2 structure

        GWorld.getInstance().getLogger().info("Found local worlds.yml. Transfer to DB...");

        List<WorldData> worlds = new ArrayList<>();

        int count = 0;
        // Process each world from YAML file
        for (String worldName : config.getConfigurationSection("Worlds").getKeys(false)) {
            try {
                // Parse world data
                WorldData wd = parseWorldFromYaml(worldName, config);

                worlds.add(wd);
                count++;
            } catch (Exception e) {
                GWorld.getInstance().getLogger().log(Level.WARNING, "Error while transfering world: " + worldName, e);
            }
        }

        importData(worlds);

        if (count > 0) {
            GWorld.getInstance().getLogger().info("Transfer finished (" + count + " worlds). Rename local conf...");
            yamlFile.renameTo(new File(GWorld.getInstance().getDataFolder(), "worlds.yml.imported"));
        }
    }

    private void handleYamlUpdates() {
        File configFile = new File(GWorld.getInstance().getDataFolder(), "worlds.yml");
        if (!configFile.exists()) return;

        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        // Wenn keine Version vorhanden, aber es keine Legacy-Datei, Version 1
        if (!config.contains("ConfigVersion")) {
            config.set("ConfigVersion", 1);
            save(config, configFile);
            return;
        }

        int version = config.getInt("ConfigVersion");

        if (version < CURRENT_CONFIG_VERSION) {
            GWorld.getInstance().getLogger().info("Update Config von v" + version + " auf v" + CURRENT_CONFIG_VERSION);

            // Beispiel für zukünftiges Update:
            /*
            if (version < 2) {
                 // updateLogicForV2(config);
                 version = 2;
            }
            */

            config.set("ConfigVersion", version);
            save(config, configFile);
        }
    }

    private void save(YamlConfiguration config, File file) {
        try {
            config.save(file);
        } catch (IOException e) {
            GWorld.getInstance().getLogger().log(Level.SEVERE, "Could not save migration config: " + file.getName(), e);
        }
    }

    private WorldData parseWorldFromYaml(String worldName, YamlConfiguration config) {
        String path = "Worlds." + worldName;

        // General Information
        WorldData.GeneralInformation genInfo = new WorldData.GeneralInformation(
                worldName,
                parseEnum(org.bukkit.World.Environment.class, config.getString(path + ".Environment"), org.bukkit.World.Environment.NORMAL),
                parseEnum(org.bukkit.WorldType.class, config.getString(path + ".Type"), org.bukkit.WorldType.NORMAL),
                config.getLong(path + ".Seed"),
                config.getString(path + ".Generator")
        );

        // WorldData Builder nutzen oder Konstruktor (hier Konstruktor wie in FileConfiguration)
        return new WorldData(
                genInfo,
                config.getBoolean(path + ".AllowPvP"),
                config.getBoolean(path + ".KeepSpawnInMemory"),
                config.getBoolean(path + ".AnimalSpawning"),
                config.getStringList(path + ".DisabledAnimals"),
                config.getBoolean(path + ".MonsterSpawning"),
                config.getStringList(path + ".DisabledMonsters"),
                config.getBoolean(path + ".WeatherCycle"),
                parseEnum(WorldData.WeatherType.class, config.getString(path + ".WeatherType"), WorldData.WeatherType.CLEAR),
                config.getBoolean(path + ".TimeCycle"),
                config.getLong(path + ".Time"),
                config.getBoolean(path + ".DefaultGamemode"),
                parseEnum(org.bukkit.GameMode.class, config.getString(path + ".GameMode"), org.bukkit.GameMode.SURVIVAL),
                parseEnum(org.bukkit.Difficulty.class, config.getString(path + ".Difficulty"), org.bukkit.Difficulty.NORMAL),
                config.getBoolean(path + ".LoadOnStartup"),
                config.getInt(path + ".RandomTickSpeed"),
                config.getBoolean(path + ".AnnounceAdvancements")
        );
    }

    private <T extends Enum<T>> T parseEnum(Class<T> enumType, String value, T defaultValue) {
        if (value == null || value.isEmpty()) return defaultValue;
        try {
            return Enum.valueOf(enumType, value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return defaultValue;
        }
    }
}
