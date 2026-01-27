package de.gilljan.gworld.data.migration;

import de.gilljan.gworld.GWorld;
import de.gilljan.gworld.data.world.WorldData;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class LegacyMigrator {
    private final File dataFolder;
    private final File configFile;

    public LegacyMigrator() {
        this.dataFolder = GWorld.getInstance().getDataFolder();
        this.configFile = new File(dataFolder, "worlds.yml");
    }

    public List<WorldData> checkAndMigrateLegacyWorlds() {
        if (!shouldMigrate()) {
            return new ArrayList<>();
        }

        GWorld.getInstance().getLogger().info("--------------------------------------------------");
        GWorld.getInstance().getLogger().info(" Detected old version (Legacy 1.x).");
        GWorld.getInstance().getLogger().info(" Start automatic migration to Version 2.0...");
        GWorld.getInstance().getLogger().info("--------------------------------------------------");

        if (!createBackup()) {
            return new ArrayList<>(); // Secure legacy data and abort migration on failure
        }

        // Start migration, with backup file as source
        File backupFile = new File(dataFolder, "worlds_legacy_backup.yml");
        return performMigration(backupFile);
    }

    private boolean shouldMigrate() {
        if(!configFile.exists()) return false;

        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        //No migration needed for configs with version tag -> NO LEGACY data
        if(config.contains("ConfigVersion")) {
            return false;
        }

        // Distinguishing feature: LoadWorlds list has to exist along with Worlds section
        return config.contains("LoadWorlds") && config.contains("Worlds");
    }

    private boolean createBackup() {
        File backupFile = new File(dataFolder, "worlds_legacy_backup.yml");
        if (backupFile.exists()) backupFile.delete();

        if (configFile.renameTo(backupFile)) {
            GWorld.getInstance().getLogger().info("Backup created: worlds_legacy_backup.yml");
            return true;
        } else {
            GWorld.getInstance().getLogger().severe("ERROR: Could not rename worlds.yml. Migration aborted!");
            return false;
        }
    }

    private List<WorldData> performMigration(File sourceFile) {
        YamlConfiguration oldConfig = YamlConfiguration.loadConfiguration(sourceFile);
        ConfigurationSection worldsSection = oldConfig.getConfigurationSection("Worlds");
        List<String> loadWorldsList = oldConfig.getStringList("LoadWorlds");

        List<WorldData> migratedWorlds = new ArrayList<>();

        if(worldsSection == null) return migratedWorlds;

        for(String worldName : worldsSection.getKeys(false)) {
            try {
                ConfigurationSection sec = worldsSection.getConfigurationSection(worldName);
                if (sec != null) {
                    WorldData data = convertSingleWorld(worldName, sec, loadWorldsList.contains(worldName));
                    migratedWorlds.add(data);
                    GWorld.getInstance().getLogger().info(" -> converted world: " + worldName);
                }
            } catch (Exception e) {
                GWorld.getInstance().getLogger().log(Level.SEVERE, "Error converting world: " + worldName, e);
            }
        }

        return migratedWorlds;
    }

    private WorldData convertSingleWorld(String name, ConfigurationSection sec, boolean loadOnStartup) {
        // Generator Handling
        String generator = sec.getString("generator");
        if ("null".equalsIgnoreCase(generator) || "".equals(generator)) generator = null;

        // Environment & Type Mapping
        String oldType = sec.getString("type", "normal").toLowerCase();
        World.Environment env = World.Environment.NORMAL;
        WorldType worldType = WorldType.NORMAL;

        switch (oldType) {
            case "nether" -> env = World.Environment.NETHER;
            case "end", "the_end" -> env = World.Environment.THE_END;
            case "flat" -> worldType = WorldType.FLAT;
            case "amplified" -> worldType = WorldType.AMPLIFIED;
            case "large_biomes" -> worldType = WorldType.LARGE_BIOMES;
        }

        // Weather Mapping
        String oldWeather = sec.getString("weather", "sun");
        WorldData.WeatherType weatherType = WorldData.WeatherType.CLEAR;
        if (oldWeather.equalsIgnoreCase("rain") || oldWeather.equalsIgnoreCase("storm")) {
            weatherType = WorldData.WeatherType.RAIN;
        } else if (oldWeather.equalsIgnoreCase("thunder")) {
            weatherType = WorldData.WeatherType.THUNDER;
        }

        // Enum Parsing
        GameMode gameMode = parseEnum(GameMode.class, sec.getString("defaultGamemode"), GameMode.SURVIVAL);
        Difficulty difficulty = parseEnum(Difficulty.class, sec.getString("difficulty"), Difficulty.NORMAL);

        // Create new WorldData object
        WorldData.GeneralInformation genInfo = new WorldData.GeneralInformation(name, env, worldType, 0L, generator);

        return new WorldData.Builder()
                .setGeneralInformation(genInfo)
                .setAllowPvP(sec.getBoolean("pvp", true))
                .setMonsterSpawning(sec.getBoolean("mobs", true))     // Alt: mobs
                .setAnimalSpawning(sec.getBoolean("animals", true))   // Alt: animals
                .setWeatherCycle(sec.getBoolean("weatherCycle", true))
                .setTimeCycle(sec.getBoolean("timeCycle", true))
                .setTime(sec.getLong("time", 6000))
                .setWeatherType(weatherType)
                .setDefaultGamemode(sec.getBoolean("forcedGamemode", false)) // Alt: forcedGamemode
                .setGameMode(gameMode)
                .setDifficulty(difficulty)
                .setRandomTickSpeed(sec.getInt("randomTickSpeed", 3))
                .setAnnounceAdvancements(sec.getBoolean("announceAdvancements", true))
                .setLoadOnStartup(loadOnStartup)
                // Defaults für neue Features setzen
                .setKeepSpawnInMemory(true)
                .setDisabledAnimals(new ArrayList<>())
                .setDisabledMonsters(new ArrayList<>())
                .build();
    }

    private <T extends Enum<T>> T parseEnum(Class<T> enumType, String value, T defaultValue) {
        if (value == null) return defaultValue;
        try {
            return Enum.valueOf(enumType, value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return defaultValue;
        }
    }
}
