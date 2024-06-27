package de.gilljan.gworld.data.world;

import de.gilljan.gworld.data.DataHandler;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.WorldType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class WorldData {
    private GeneralInformation generalInformation;
    private boolean loaded = false;

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
        this.allowPvP = true;
        this.keepSpawnInMemory = true;
        this.animalSpawning = true;
        this.disabledAnimals = new ArrayList<>();
        this.monsterSpawning = true;
        this.disabledMonsters = new ArrayList<>();
        this.weatherCycle = true;
        this.weatherType = WeatherType.CLEAR;
        this.timeCycle = true;
        this.time = 0;
        this.defaultGamemode = false;
        this.gameMode = GameMode.SURVIVAL;
        this.difficulty = Difficulty.NORMAL;
        this.spawnChunkRadius = 2;
    }

    public WorldData(GeneralInformation generalInformation, boolean allowPvP, boolean keepSpawnInMemory, boolean animalSpawning, List<String> disabledAnimals, boolean monsterSpawning, List<String> disabledMonsters, boolean weatherCycle, WeatherType weatherType, boolean timeCycle, long time, boolean defaultGamemode, GameMode gameMode, Difficulty difficulty, int spawnChunkRadius) {
        this.generalInformation = generalInformation;
        this.loaded = false;
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

    public boolean isAllowPvP() {
        return allowPvP;
    }

    public void setAllowPvP(boolean allowPvP) {
        this.allowPvP = allowPvP;
    }

    public boolean isKeepSpawnInMemory() {
        return keepSpawnInMemory;
    }

    public void setKeepSpawnInMemory(boolean keepSpawnInMemory) {
        this.keepSpawnInMemory = keepSpawnInMemory;
    }

    public boolean isAnimalSpawning() {
        return animalSpawning;
    }

    public void setAnimalSpawning(boolean animalSpawning) {
        this.animalSpawning = animalSpawning;
    }

    public List<String> getDisabledAnimals() {
        return disabledAnimals;
    }

    public void setDisabledAnimals(List<String> disabledAnimals) {
        this.disabledAnimals = disabledAnimals;
    }

    public boolean isMonsterSpawning() {
        return monsterSpawning;
    }

    public void setMonsterSpawning(boolean monsterSpawning) {
        this.monsterSpawning = monsterSpawning;
    }

    public List<String> getDisabledMonsters() {
        return disabledMonsters;
    }

    public void setDisabledMonsters(List<String> disabledMonsters) {
        this.disabledMonsters = disabledMonsters;
    }

    public boolean isWeatherCycle() {
        return weatherCycle;
    }

    public void setWeatherCycle(boolean weatherCycle) {
        this.weatherCycle = weatherCycle;
    }

    public WeatherType getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(WeatherType weatherType) {
        this.weatherType = weatherType;
    }

    public boolean isTimeCycle() {
        return timeCycle;
    }

    public void setTimeCycle(boolean timeCycle) {
        this.timeCycle = timeCycle;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isDefaultGamemode() {
        return defaultGamemode;
    }

    public void setDefaultGamemode(boolean defaultGamemode) {
        this.defaultGamemode = defaultGamemode;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public int getSpawnChunkRadius() {
        return spawnChunkRadius;
    }

    public void setSpawnChunkRadius(int spawnChunkRadius) {
        this.spawnChunkRadius = spawnChunkRadius;
    }

    public GeneralInformation getGeneralInformation() {
        return generalInformation;
    }

    public static WorldData loadWorldData(DataHandler dataHandler, String worldName) {
        return dataHandler.getWorld(worldName);
    }

    public boolean isLoaded() {
        return loaded;
    }

    public boolean isAllowPvP() {
        return allowPvP;
    }

    public boolean isKeepSpawnInMemory() {
        return keepSpawnInMemory;
    }

    public boolean isAnimalSpawning() {
        return animalSpawning;
    }

    public List<String> getDisabledAnimals() {
        return disabledAnimals;
    }

    public boolean isMonsterSpawning() {
        return monsterSpawning;
    }

    public List<String> getDisabledMonsters() {
        return disabledMonsters;
    }

    public boolean isWeatherCycle() {
        return weatherCycle;
    }

    public WeatherType getWeatherType() {
        return weatherType;
    }

    public boolean isTimeCycle() {
        return timeCycle;
    }

    public long getTime() {
        return time;
    }

    public boolean isDefaultGamemode() {
        return defaultGamemode;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public int getSpawnChunkRadius() {
        return spawnChunkRadius;
    }

    public record GeneralInformation(String worldName, org.bukkit.World.Environment environment, WorldType worldType, long seed, @Nullable String worldGenerator) {
    }

    public enum WeatherType {
        CLEAR,
        RAIN,
        THUNDER
    }
}
