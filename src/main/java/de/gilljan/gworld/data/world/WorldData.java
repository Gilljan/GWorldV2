package de.gilljan.gworld.data.world;

import de.gilljan.gworld.data.DataHandler;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.WorldType;

import javax.annotation.Nullable;
import java.util.List;

public class WorldData {
    private GeneralInformation generalInformation;
    private boolean loaded;

    //World properties
    private boolean allowPvP;
    private boolean keepSpawnInMemory;
    private boolean animalSpawning;
    private List<String> disabledAnimals;
    private boolean monsterSpawning;
    private List<String> disabledMonsters;
    private boolean weatherCycle;
    private WeatherType weatherType;
    private boolean timeCycle;
    private long time;
    private boolean defaultGamemode;
    private GameMode gameMode;
    private Difficulty difficulty;
    private int spawnChunkRadius;



    public WorldData(String worldName) {

    }

    public WorldData(String worldName, org.bukkit.World.Environment environment, WorldType worldType, long seed, String worldGenerator) {
        this.generalInformation = new GeneralInformation(worldName, environment, worldType, seed, worldGenerator);
    }

    public WorldData(GeneralInformation generalInformation, boolean loaded, boolean allowPvP, boolean keepSpawnInMemory, boolean animalSpawning, List<String> disabledAnimals, boolean monsterSpawning, List<String> disabledMonsters, boolean weatherCycle, WeatherType weatherType, boolean timeCycle, long time, boolean defaultGamemode, GameMode gameMode, Difficulty difficulty, int spawnChunkRadius) {
        this.generalInformation = generalInformation;
        this.loaded = loaded;
        this.allowPvP = allowPvP;
        this.keepSpawnInMemory = keepSpawnInMemory;
        this.animalSpawning = animalSpawning;
        this.disabledAnimals = disabledAnimals;
        this.monsterSpawning = monsterSpawning;
        this.disabledMonsters = disabledMonsters;
        this.weatherCycle = weatherCycle;
        this.weatherType = weatherType;
        this.timeCycle = timeCycle;
        this.time = time;
        this.defaultGamemode = defaultGamemode;
        this.gameMode = gameMode;
        this.difficulty = difficulty;
        this.spawnChunkRadius = spawnChunkRadius;
    }

    public GeneralInformation getGeneralInformation() {
        return generalInformation;
    }

    public static WorldData loadWorldData(DataHandler dataHandler, String worldName) {
        return dataHandler.getWorld(worldName);
    }

    public record GeneralInformation(String worldName, org.bukkit.World.Environment environment, WorldType worldType, long seed, @Nullable String worldGenerator) {
    }

    enum WeatherType {
        CLEAR,
        RAIN,
        THUNDER
    }
}
