package de.gilljan.gworld.data;

import de.gilljan.gworld.GWorld;
import de.gilljan.gworld.data.world.WorldData;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;

public class FileConfiguration implements DataHandler {
    public HashMap<String, WorldData> worlds = new HashMap<>();
    private final File worldFile = new File(GWorld.getInstance().getDataFolder().getPath() + "/worlds.yml");
    private final YamlConfiguration worldData = YamlConfiguration.loadConfiguration(worldFile);
    private static final String WORLD_PATH = "Worlds.";


    @Override
    public WorldData getWorld(String name) {
        if(worlds.containsKey(name)) {
            return worlds.get(name);
        }

        if(worldData.contains(WORLD_PATH + name)) {
            WorldData world = new WorldData(new WorldData.GeneralInformation(
                    name,
                    org.bukkit.World.Environment.valueOf(worldData.getString(WORLD_PATH + name + ".Environment")),
                    org.bukkit.WorldType.valueOf(worldData.getString(WORLD_PATH + name + ".Type")),
                    worldData.getLong(WORLD_PATH + name + ".Seed"),
                    worldData.getString(WORLD_PATH + name + ".Generator")
            ),
                    worldData.getBoolean(WORLD_PATH + name + ".AllowPvP"),
                    worldData.getBoolean(WORLD_PATH + name + ".KeepSpawnInMemory"),
                    worldData.getBoolean(WORLD_PATH + name + ".AnimalSpawning"),
                    worldData.getStringList(WORLD_PATH + name + ".DisabledAnimals"),
                    worldData.getBoolean(WORLD_PATH + name + ".MonsterSpawning"),
                    worldData.getStringList(WORLD_PATH + name + ".DisabledMonsters"),
                    worldData.getBoolean(WORLD_PATH + name + ".WeatherCycle"),
                    WorldData.WeatherType.valueOf(worldData.getString(WORLD_PATH + name + ".WeatherType")),
                    worldData.getBoolean(WORLD_PATH + name + ".TimeCycle"),
                    worldData.getLong(WORLD_PATH + name + ".Time"),
                    worldData.getBoolean(WORLD_PATH + name + ".DefaultGamemode"),
                    org.bukkit.GameMode.valueOf(worldData.getString(WORLD_PATH + name + ".GameMode")),
                    org.bukkit.Difficulty.valueOf(worldData.getString(WORLD_PATH + name + ".Difficulty")),
                    worldData.getInt(WORLD_PATH + name + ".SpawnChunkRadius"));
            worlds.put(name, world);
            return world;
        }

        return null;
    }

    @Override
    public void saveWorld(WorldData world) {
        /*worldData.set(WORLD_PATH + world.getGeneralInformation().worldName() + ".Environment", world.getGeneralInformation().environment().name());
        worldData.set(WORLD_PATH + world.getGeneralInformation().worldName() + ".Type", world.getGeneralInformation().worldType().name());
        worldData.set(WORLD_PATH + world.getGeneralInformation().worldName() + ".Seed", world.getGeneralInformation().seed());
        worldData.set(WORLD_PATH + world.getGeneralInformation().worldName() + ".Generator", world.getGeneralInformation().worldGenerator());
        worldData.set(WORLD_PATH + world.getGeneralInformation().worldName() + ".AllowPvP", world.isAllowPvP());
        worldData.set(WORLD_PATH + world.getGeneralInformation().worldName() + ".KeepSpawnInMemory", world.isKeepSpawnInMemory());
        worldData.set(WORLD_PATH + world.getGeneralInformation().worldName() + ".AnimalSpawning", world.isAnimalSpawning());
        worldData.set(WORLD_PATH + world.getGeneralInformation().worldName() + ".DisabledAnimals", world.getDisabledAnimals());
        worldData.set(WORLD_PATH + world.getGeneralInformation().worldName() + ".MonsterSpawning", world.isMonsterSpawning());
        worldData.set(WORLD_PATH + world.getGeneralInformation().worldName() + ".DisabledMonsters", world.getDisabledMonsters());
        worldData.set(WORLD_PATH + world.getGeneralInformation().worldName() + ".WeatherCycle", world.isWeatherCycle());
        worldData.set(WORLD_PATH + world.getGeneralInformation().worldName() + ".WeatherType", world.getWeatherType().name());
        worldData.set(WORLD_PATH + world.getGeneralInformation().worldName() + ".TimeCycle", world.isTimeCycle());
        worldData.set(WORLD_PATH + world.getGeneralInformation().worldName() + ".Time", world.getTime());
        worldData.set(WORLD_PATH + world.getGeneralInformation().worldName() + ".DefaultGamemode", world.isDefaultGamemode());
        worldData.set(WORLD_PATH + world.getGeneralInformation().worldName() + ".GameMode", world.getGameMode().name());
        worldData.set(WORLD_PATH + world.getGeneralInformation().worldName() + ".Difficulty", world.getDifficulty().name());
        worldData.set(WORLD_PATH + world.getGeneralInformation().worldName() + ".SpawnChunkRadius", world.getSpawnChunkRadius());*/
        setValueInConfig(WORLD_PATH + world.getGeneralInformation().worldName() + ".Environment", world.getGeneralInformation().environment().name());
        setValueInConfig(WORLD_PATH + world.getGeneralInformation().worldName() + ".Type", world.getGeneralInformation().worldType().name());
        setValueInConfig(WORLD_PATH + world.getGeneralInformation().worldName() + ".Seed", world.getGeneralInformation().seed());
        setValueInConfig(WORLD_PATH + world.getGeneralInformation().worldName() + ".Generator", world.getGeneralInformation().worldGenerator());
        setValueInConfig(WORLD_PATH + world.getGeneralInformation().worldName() + ".AllowPvP", world.isAllowPvP());
        setValueInConfig(WORLD_PATH + world.getGeneralInformation().worldName() + ".KeepSpawnInMemory", world.isKeepSpawnInMemory());
        setValueInConfig(WORLD_PATH + world.getGeneralInformation().worldName() + ".AnimalSpawning", world.isAnimalSpawning());
        setValueInConfig(WORLD_PATH + world.getGeneralInformation().worldName() + ".DisabledAnimals", world.getDisabledAnimals());
        setValueInConfig(WORLD_PATH + world.getGeneralInformation().worldName() + ".MonsterSpawning", world.isMonsterSpawning());
        setValueInConfig(WORLD_PATH + world.getGeneralInformation().worldName() + ".DisabledMonsters", world.getDisabledMonsters());
        setValueInConfig(WORLD_PATH + world.getGeneralInformation().worldName() + ".WeatherCycle", world.isWeatherCycle());
        setValueInConfig(WORLD_PATH + world.getGeneralInformation().worldName() + ".WeatherType", world.getWeatherType().name());
        setValueInConfig(WORLD_PATH + world.getGeneralInformation().worldName() + ".TimeCycle", world.isTimeCycle());
        setValueInConfig(WORLD_PATH + world.getGeneralInformation().worldName() + ".Time", world.getTime());
        setValueInConfig(WORLD_PATH + world.getGeneralInformation().worldName() + ".DefaultGamemode", world.isDefaultGamemode());
        setValueInConfig(WORLD_PATH + world.getGeneralInformation().worldName() + ".GameMode", world.getGameMode().name());
        setValueInConfig(WORLD_PATH + world.getGeneralInformation().worldName() + ".Difficulty", world.getDifficulty().name());
        setValueInConfig(WORLD_PATH + world.getGeneralInformation().worldName() + ".SpawnChunkRadius", world.getSpawnChunkRadius());


        synchronized (worldData) {
            try {
                worldData.save(worldFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setValueInConfig(String key, Object value) {
        worldData.set(key, value);
    }

    @Override
    public void saveAllWorlds() {
        for(WorldData world : worlds.values()) {
            saveWorld(world);
        }
    }

    @Override
    public void loadAllWorlds() {
        worlds.clear();
        for(String key : worldData.getConfigurationSection(WORLD_PATH).getKeys(false)) {
            WorldData world = getWorld(key);
            worlds.put(key, world);
        }
    }

    @Override
    public void refetchWorlds() {
        worlds.clear();
        loadAllWorlds();
    }

    @Override
    public WorldData fetchWorld(String name) {
        worlds.remove(name);
        return getWorld(name);
    }
}
