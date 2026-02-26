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

import static de.gilljan.gworld.data.FileConfiguration.CURRENT_CONFIG_VERSION;

public class MigrationManager {
    private static final List<String> STANDARD_WORLDS = List.of("world", "world_nether", "world_the_end");

    // Cached legacy data between phases
    private List<WorldData> legacyData = new ArrayList<>();

    /**
     * Phase 1: Runs BEFORE the DataHandler is created.
     * Checks for legacy worlds.yml (1.x format) and converts it.
     * The converted data is stored in memory for Phase 2.
     * After this, the old worlds.yml is renamed to a backup,
     * so that FileConfiguration's constructor can safely create a fresh one.
     */
    public void migrateLegacy() {
        LegacyMigrator legacy = new LegacyMigrator();
        legacyData = legacy.checkAndMigrateLegacyWorlds();

        if (!legacyData.isEmpty()) {
            GWorld.getInstance().getLogger().info("Legacy migration: " + legacyData.size() + " worlds converted. Will import after DataHandler init.");
        }
    }

    /**
     * Phase 2: Runs AFTER the DataHandler is created.
     * Imports legacy data into the DataHandler and runs schema updates / DB transfers.
     */
    public void processPostInit() {
        DataHandler handler = GWorld.getInstance().getDataHandler();

        // 1. Import legacy data if present
        if (!legacyData.isEmpty()) {
            GWorld.getInstance().getLogger().info("Importing " + legacyData.size() + " legacy worlds into the new system...");

            if (handler instanceof FileConfiguration) {
                ((FileConfiguration) handler).clearAndReset();
            }

            importData(legacyData);
            legacyData.clear();
        }

        // 2. TRANSFER YAML -> DB (if user switched from YAML to DB)
        if (handler instanceof Database) {
            checkYamlToDatabaseTransfer();
        }

        // 3. SCHEMA UPDATES (2.0 -> 2.1 -> ...)
        if (handler instanceof FileConfiguration) {
            handleYamlUpdates();
        }

        // Database schema updates would be handled internally in the Database class when connecting, so no action needed here
    }

    @Deprecated
    public void process() {
        migrateLegacy();
        processPostInit();
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
        FileConfiguration fileConfig = (FileConfiguration) GWorld.getInstance().getDataHandler();
        int version = fileConfig.getConfigVersion();

        if (version < CURRENT_CONFIG_VERSION) {
            GWorld.getInstance().getLogger().info("Update Config von v" + version + " auf v" + CURRENT_CONFIG_VERSION);

            if (version < 2) {
                // In v2 wurde ein neues Feld "Alias" hinzugefügt, das in v1 nicht existierte
                // Da der Standard NULL ist, wird hier nichts getan
                version = 2;
            }

            fileConfig.setConfigVersion(version);
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
                config.getBoolean(path + ".AnnounceAdvancements"),
                config.getString(path + ".Alias")
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
