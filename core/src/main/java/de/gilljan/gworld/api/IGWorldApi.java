package de.gilljan.gworld.api;

import de.gilljan.gworld.data.world.WorldData;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;

import java.util.List;
import java.util.Optional;

public interface IGWorldApi {

    boolean loadMap();

    boolean unloadMap();

    boolean deleteMap();

    boolean createMap();

    boolean importExisting();

    boolean isMapLoaded();

    Optional<IGWorldApi> clone(String newWorldName);

    boolean reCreate(boolean saveWorldToFile);

    boolean saveWorld();

    void setAllProperties();

    String getWorldName();

    boolean isAllowPvP();
    boolean isKeepSpawnInMemory();
    boolean isAnimalSpawning();
    List<String> getDisabledAnimals();
    boolean isMonsterSpawning();
    List<String> getDisabledMonsters();
    boolean isWeatherCycle();
    WorldData.WeatherType getWeatherType();
    boolean isTimeCycle();
    long getTime();
    boolean isDefaultGamemode();
    GameMode getGameMode();
    Difficulty getDifficulty();
    int getRandomTickSpeed();
    boolean isAnnounceAdvancements();

    void setAllowPvP(boolean allowPvP);
    void setKeepSpawnInMemory(boolean keepSpawnInMemory);
    void setAnimalSpawning(boolean animalSpawning);
    void setDisabledAnimals(List<String> disabledAnimals);
    void setMonsterSpawning(boolean monsterSpawning);
    void setDisabledMonsters(List<String> disabledMonsters);
    void setWeatherCycle(boolean weatherCycle);
    void setWeatherType(WorldData.WeatherType weatherType);
    void setTimeCycle(boolean timeCycle);
    void setTime(long time);
    void setDefaultGamemode(boolean defaultGamemode);
    void setGameMode(GameMode gameMode);
    void setDifficulty(Difficulty difficulty);
    void setRandomTickSpeed(int randomTickSpeed);
    void setAnnounceAdvancements(boolean announceAdvancements);
}
