package de.gilljan.gworld.data.world;

import de.gilljan.gworld.GWorld;
import de.gilljan.gworld.data.DataHandler;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.WorldType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    //private int spawnChunkRadius; //todo entfernen und dafür loaded in DB!
    private boolean loadOnStartup;
    private int randomTickSpeed;
    private boolean announceAdvancements;


    public WorldData(String worldName) {
        this(worldName, org.bukkit.World.Environment.NORMAL, WorldType.NORMAL, 0, null);
    }

    //todo use presets of configuration (user)
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
        this.loadOnStartup = true;
        this.randomTickSpeed = 3;
        this.announceAdvancements = true;
        //this.spawnChunkRadius = 2;
    }

    public WorldData(GeneralInformation generalInformation, boolean allowPvP, boolean keepSpawnInMemory, boolean animalSpawning, List<String> disabledAnimals, boolean monsterSpawning, List<String> disabledMonsters, boolean weatherCycle, WeatherType weatherType, boolean timeCycle, long time, boolean defaultGamemode, GameMode gameMode, Difficulty difficulty, boolean loadOnStartup, int randomTickSpeed, boolean announceAdvancements) {
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
        this.loadOnStartup = loadOnStartup;
        this.randomTickSpeed = randomTickSpeed;
        this.announceAdvancements = announceAdvancements;
        //this.spawnChunkRadius = spawnChunkRadius;
    }

    public boolean isAllowPvP() {
        return allowPvP;
    }

    public void setAllowPvP(boolean allowPvP) {
        this.allowPvP = allowPvP;

        if (isLoaded())
            Bukkit.getWorld(generalInformation.worldName()).setPVP(allowPvP);
    }

    public boolean isKeepSpawnInMemory() {
        return keepSpawnInMemory;
    }

    public void setKeepSpawnInMemory(boolean keepSpawnInMemory) {
        this.keepSpawnInMemory = keepSpawnInMemory;

        if (isLoaded())
            Bukkit.getWorld(generalInformation.worldName()).setKeepSpawnInMemory(keepSpawnInMemory);
    }

    public boolean isAnimalSpawning() {
        return animalSpawning;
    }

    public void setAnimalSpawning(boolean animalSpawning) {
        this.animalSpawning = animalSpawning;

        //Remove all animals
        if (!isLoaded())
            return;

        for (Entity entity : Bukkit.getWorld(generalInformation.worldName()).getEntities()) {
            String spawnedTypeName = entity.getType().name().toLowerCase();
            if (GWorld.ANIMALS.contains(spawnedTypeName)) {
                entity.remove();
            }
        }
    }

    public List<String> getDisabledAnimals() {
        return disabledAnimals;
    }

    public void setDisabledAnimals(List<String> disabledAnimals) {
        this.disabledAnimals = disabledAnimals;

        if (!isLoaded())
            return;

        for (Entity entity : Bukkit.getWorld(generalInformation.worldName()).getEntities()) {
            String spawnedTypeName = entity.getType().name().toLowerCase();
            if (disabledAnimals.contains(spawnedTypeName)) {
                entity.remove();
            }
        }
    }

    public boolean isMonsterSpawning() {
        return monsterSpawning;
    }

    public void setMonsterSpawning(boolean monsterSpawning) {
        this.monsterSpawning = monsterSpawning;

        if (!isLoaded())
            return;

        //Remove all monsters
        for (Entity entity : Bukkit.getWorld(generalInformation.worldName()).getEntities()) {
            String spawnedTypeName = entity.getType().name().toLowerCase();
            if (GWorld.MONSTER.contains(spawnedTypeName)) {
                entity.remove();
            }
        }
    }

    public List<String> getDisabledMonsters() {
        return disabledMonsters;
    }

    public void setDisabledMonsters(List<String> disabledMonsters) {
        this.disabledMonsters = disabledMonsters;

        if (!isLoaded())
            return;

        for (Entity entity : Bukkit.getWorld(generalInformation.worldName()).getEntities()) {
            String spawnedTypeName = entity.getType().name().toLowerCase();
            if (disabledMonsters.contains(spawnedTypeName)) {
                entity.remove();
            }
        }

    }

    public boolean isWeatherCycle() {
        return weatherCycle;
    }

    public void setWeatherCycle(boolean weatherCycle) {
        this.weatherCycle = weatherCycle;

        if (isLoaded())
            Bukkit.getWorld(generalInformation.worldName()).setGameRuleValue("doWeatherCycle", String.valueOf(weatherCycle));

        setWeatherType(weatherType);
    }

    public WeatherType getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(WeatherType weatherType) {
        this.weatherType = weatherType;

        if (!isLoaded()) {
            return;
        }

        if (weatherCycle) {
            return;
        }

        Bukkit.getWorld(generalInformation.worldName()).setStorm(weatherType == WeatherType.RAIN || weatherType == WeatherType.THUNDER);
        Bukkit.getWorld(generalInformation.worldName()).setThundering(weatherType == WeatherType.THUNDER);

    }

    public boolean isTimeCycle() {
        return timeCycle;
    }

    public void setTimeCycle(boolean timeCycle) {
        this.timeCycle = timeCycle;

        if (isLoaded())
            Bukkit.getWorld(generalInformation.worldName()).setGameRuleValue("doDaylightCycle", String.valueOf(timeCycle));

        setTime(time);
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;

        if (isLoaded() && !timeCycle)
            Bukkit.getWorld(generalInformation.worldName()).setTime(time);
    }

    public boolean isDefaultGamemode() {
        return defaultGamemode;
    }

    public void setDefaultGamemode(boolean defaultGamemode) {
        this.defaultGamemode = defaultGamemode;

        setGameMode(gameMode);
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;

        if (isLoaded() && defaultGamemode) {
            for (Player player : Bukkit.getWorld(generalInformation.worldName()).getPlayers()) {
                player.setGameMode(gameMode);
            }
        }
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;

        if (isLoaded())
            Bukkit.getWorld(generalInformation.worldName()).setDifficulty(difficulty);
    }

    public boolean isLoadOnStartup() {
        return loadOnStartup;
    }

    public void setLoadOnStartup(boolean loadOnStartup) {
        this.loadOnStartup = loadOnStartup;
    }

    public int getRandomTickSpeed() {
        return randomTickSpeed;
    }

    public void setRandomTickSpeed(int randomTickSpeed) {
        this.randomTickSpeed = randomTickSpeed;

        if (isLoaded())
            Bukkit.getWorld(generalInformation.worldName()).setGameRuleValue("randomTickSpeed", String.valueOf(randomTickSpeed));
    }

    public boolean isAnnounceAdvancements() {
        return announceAdvancements;
    }

    public void setAnnounceAdvancements(boolean announceAdvancements) {
        this.announceAdvancements = announceAdvancements;

        if (isLoaded())
            Bukkit.getWorld(generalInformation.worldName()).setGameRuleValue("announceAdvancements", String.valueOf(announceAdvancements));
    }

    public GeneralInformation getGeneralInformation() {
        return generalInformation;
    }

    public static Optional<WorldData> loadWorldData(DataHandler dataHandler, String worldName) {
        return dataHandler.getWorld(worldName);
    }

    public String getWorldGenerator() {
        return generalInformation.worldGenerator();
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public void updateGeneralInformation(GeneralInformation generalInformation) {
        this.generalInformation = generalInformation;
    }

    public record GeneralInformation(String worldName, org.bukkit.World.Environment environment, WorldType worldType,
                                     long seed, @Nullable String worldGenerator) {
    }

    public enum WeatherType {
        CLEAR,
        RAIN,
        THUNDER
    }

    public static class Builder {
        private GeneralInformation generalInformation;
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
        private boolean loadOnStartup;
        private int randomTickSpeed;
        private boolean announceAdvancements;

        public Builder setGeneralInformation(GeneralInformation generalInformation) {
            this.generalInformation = generalInformation;
            return this;
        }

        public Builder setAllowPvP(boolean allowPvP) {
            this.allowPvP = allowPvP;
            return this;
        }

        public Builder setKeepSpawnInMemory(boolean keepSpawnInMemory) {
            this.keepSpawnInMemory = keepSpawnInMemory;
            return this;
        }

        public Builder setAnimalSpawning(boolean animalSpawning) {
            this.animalSpawning = animalSpawning;
            return this;
        }

        public Builder setDisabledAnimals(List<String> disabledAnimals) {
            this.disabledAnimals = disabledAnimals;
            return this;
        }

        public Builder setMonsterSpawning(boolean monsterSpawning) {
            this.monsterSpawning = monsterSpawning;
            return this;
        }

        public Builder setDisabledMonsters(List<String> disabledMonsters) {
            this.disabledMonsters = disabledMonsters;
            return this;
        }

        public Builder setWeatherCycle(boolean weatherCycle) {
            this.weatherCycle = weatherCycle;
            return this;
        }

        public Builder setWeatherType(WeatherType weatherType) {
            this.weatherType = weatherType;
            return this;
        }

        public Builder setTimeCycle(boolean timeCycle) {
            this.timeCycle = timeCycle;
            return this;
        }

        public Builder setTime(long time) {
            this.time = time;
            return this;
        }

        public Builder setDefaultGamemode(boolean defaultGamemode) {
            this.defaultGamemode = defaultGamemode;
            return this;
        }

        public Builder setGameMode(GameMode gameMode) {
            this.gameMode = gameMode;
            return this;
        }

        public Builder setDifficulty(Difficulty difficulty) {
            this.difficulty = difficulty;
            return this;
        }

        public Builder setLoadOnStartup(boolean loadOnStartup) {
            this.loadOnStartup = loadOnStartup;
            return this;
        }

        public Builder setRandomTickSpeed(int randomTickSpeed) {
            this.randomTickSpeed = randomTickSpeed;
            return this;
        }

        public Builder setAnnounceAdvancements(boolean announceAdvancements) {
            this.announceAdvancements = announceAdvancements;
            return this;
        }

        public WorldData build() {
            return new WorldData(generalInformation, allowPvP, keepSpawnInMemory, animalSpawning, disabledAnimals, monsterSpawning, disabledMonsters, weatherCycle, weatherType, timeCycle, time, defaultGamemode, gameMode, difficulty, loadOnStartup, randomTickSpeed, announceAdvancements);
        }
    }
}
