package de.gilljan.gworld.api;

import de.gilljan.gworld.data.world.WorldData;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.WorldType;

import java.util.List;
import java.util.Optional;

/**
 * Represents a world that can be managed by the GWorld plugin.
 * This interface provides methods to control the world's lifecycle, properties, and settings.
 */
public interface IManageableWorld {

    /**
     * Loads the world map if it is not already loaded.
     *
     * @return {@code true} if the map was loaded successfully, otherwise {@code false}.
     */
    boolean loadMap();

    /**
     * Unloads the world map.
     *
     * @return {@code true} if the map was unloaded successfully, otherwise {@code false}.
     */
    boolean unloadMap();

    /**
     * Deletes the world map and its files. It also removes the world from GWorld management.
     *
     * @return {@code true} if the map was deleted successfully, otherwise {@code false}.
     */
    boolean deleteMap();

    /**
     * Creates the world map on the server.
     *
     * @return {@code true} if the map was created successfully, otherwise {@code false}.
     */
    boolean createMap();

    /**
     * Imports an existing world folder as a manageable world.
     *
     * @return {@code true} if the world was imported successfully, otherwise {@code false}.
     */
    boolean importExisting();

    /**
     * Checks if the world map is currently loaded.
     *
     * @return {@code true} if the map is loaded, otherwise {@code false}.
     */
    boolean isMapLoaded();

    /**
     * Clones this world with a new name.
     *
     * @param newWorldName The name for the cloned world.
     * @return An {@link Optional} containing the new {@link IManageableWorld} if the clone was successful, otherwise empty.
     */
    Optional<IManageableWorld> clone(String newWorldName);

    /**
     * Re-creates the world. This can optionally save the current world state to a file before deletion.
     *
     * @param saveWorldToFile {@code true} to save the world before re-creating it.
     * @return {@code true} if the world was re-created successfully, otherwise {@code false}.
     */
    boolean reCreate(boolean saveWorldToFile);

    /**
     * Saves the world's data to disk.
     *
     * @return {@code true} if the world was saved successfully, otherwise {@code false}.
     */
    boolean saveWorld();

    /**
     * Applies all stored properties to the loaded world.
     */
    void setAllProperties();

    /**
     * Gets the name of the world.
     *
     * @return The world's name.
     */
    String getWorldName();

    /**
     * Saves the current properties of the world to the configured data source.
     */
    void saveProperties();

    /**
     * Checks if PvP is allowed in this world.
     *
     * @return {@code true} if PvP is allowed, otherwise {@code false}.
     */
    boolean isAllowPvP();

    /**
     * Checks if the spawn chunks should be kept in memory.
     *
     * @return {@code true} if the spawn is kept in memory, otherwise {@code false}.
     */
    boolean isKeepSpawnInMemory();

    /**
     * Checks if animal spawning is enabled.
     *
     * @return {@code true} if animal spawning is enabled, otherwise {@code false}.
     */
    boolean isAnimalSpawning();

    /**
     * Gets a list of animal types that are not allowed to spawn.
     *
     * @return A list of disabled animal types.
     */
    List<String> getDisabledAnimals();

    /**
     * Checks if monster spawning is enabled.
     *
     * @return {@code true} if monster spawning is enabled, otherwise {@code false}.
     */
    boolean isMonsterSpawning();

    /**
     * Gets a list of monster types that are not allowed to spawn.
     *
     * @return A list of disabled monster types.
     */
    List<String> getDisabledMonsters();

    /**
     * Checks if the weather cycle is enabled.
     *
     * @return {@code true} if the weather cycle is enabled, otherwise {@code false}.
     */
    boolean isWeatherCycle();

    /**
     * Gets the current weather type of the world.
     *
     * @return The current {@link WorldData.WeatherType}.
     */
    WorldData.WeatherType getWeatherType();

    /**
     * Checks if the time cycle is enabled.
     *
     * @return {@code true} if the time cycle is enabled, otherwise {@code false}.
     */
    boolean isTimeCycle();

    /**
     * Gets the current time of the world.
     *
     * @return The current time.
     */
    long getTime();

    /**
     * Checks if a default gamemode is set for this world.
     *
     * @return {@code true} if a default gamemode is set, otherwise {@code false}.
     */
    boolean isDefaultGamemode();

    /**
     * Gets the default gamemode for this world.
     *
     * @return The default {@link GameMode}.
     */
    GameMode getGameMode();

    /**
     * Gets the difficulty of the world.
     *
     * @return The world's {@link Difficulty}.
     */
    Difficulty getDifficulty();

    /**
     * Gets the random tick speed of the world.
     *
     * @return The random tick speed.
     */
    int getRandomTickSpeed();

    /**
     * Checks if advancements should be announced in chat.
     *
     * @return {@code true} if advancements are announced, otherwise {@code false}.
     */
    boolean isAnnounceAdvancements();

    /**
     * Checks if the world should be loaded on server startup.
     *
     * @return {@code true} if the world is loaded on startup, otherwise {@code false}.
     */
    boolean isLoadOnStartup();

    /**
     * Gets the name of the world's generator.
     *
     * @return The generator name.
     */
    String getGenerator();

    /**
     * Gets the type of the world (e.g., NORMAL, FLAT).
     *
     * @return The {@link WorldType}.
     */
    WorldType getWorldType();

    /**
     * Gets the environment of the world (e.g., NORMAL, NETHER, THE_END).
     *
     * @return The {@link World.Environment}.
     */
    World.Environment getEnvironment();


    /**
     * Gets the alias of the world, which is only used as displayname for PAPI.
     *
     * @return The world's alias.
     */
    String getAlias();

    /**
     * Gets the display name of the world, which is used for all messages and placeholders.
     *
     * @return The world's display name. If no display name (alias) is set, it should return the world name as a fallback.
     */
    String getDisplayName();

    /**
     * Gets the seed of the world.
     *
     * @return The world's seed.
     */
    long getSeed();

    /**
     * Sets whether PvP is allowed in this world.
     *
     * @param allowPvP {@code true} to allow PvP, {@code false} to disallow.
     */
    void setAllowPvP(boolean allowPvP);

    /**
     * Sets whether the spawn chunks should be kept in memory.
     *
     * @param keepSpawnInMemory {@code true} to keep the spawn in memory, {@code false} otherwise.
     */
    void setKeepSpawnInMemory(boolean keepSpawnInMemory);

    /**
     * Sets whether animal spawning is enabled.
     *
     * @param animalSpawning {@code true} to enable animal spawning, {@code false} to disable.
     */
    void setAnimalSpawning(boolean animalSpawning);

    /**
     * Sets the list of disabled animal types.
     *
     * @param disabledAnimals A list of animal types to disable.
     */
    void setDisabledAnimals(List<String> disabledAnimals);

    /**
     * Sets whether monster spawning is enabled.
     *
     * @param monsterSpawning {@code true} to enable monster spawning, {@code false} to disable.
     */
    void setMonsterSpawning(boolean monsterSpawning);

    /**
     * Sets the list of disabled monster types.
     *
     * @param disabledMonsters A list of monster types to disable.
     */
    void setDisabledMonsters(List<String> disabledMonsters);

    /**
     * Sets whether the weather cycle is enabled.
     *
     * @param weatherCycle {@code true} to enable the weather cycle, {@code false} to disable.
     */
    void setWeatherCycle(boolean weatherCycle);

    /**
     * Sets the weather type of the world.
     *
     * @param weatherType The {@link WorldData.WeatherType} to set.
     */
    void setWeatherType(WorldData.WeatherType weatherType);

    /**
     * Sets whether the time cycle is enabled.
     *
     * @param timeCycle {@code true} to enable the time cycle, {@code false} to disable.
     */
    void setTimeCycle(boolean timeCycle);

    /**
     * Sets the time of the world.
     *
     * @param time The time to set.
     */
    void setTime(long time);

    /**
     * Sets whether a default gamemode should be enforced.
     *
     * @param defaultGamemode {@code true} to enforce a default gamemode, {@code false} otherwise.
     */
    void setDefaultGamemode(boolean defaultGamemode);

    /**
     * Sets the default gamemode for this world.
     *
     * @param gameMode The {@link GameMode} to set as default.
     */
    void setGameMode(GameMode gameMode);

    /**
     * Sets the difficulty of the world.
     *
     * @param difficulty The {@link Difficulty} to set.
     */
    void setDifficulty(Difficulty difficulty);

    /**
     * Sets the random tick speed of the world.
     *
     * @param randomTickSpeed The random tick speed to set.
     */
    void setRandomTickSpeed(int randomTickSpeed);

    /**
     * Sets whether advancements should be announced.
     *
     * @param announceAdvancements {@code true} to announce advancements, {@code false} otherwise.
     */
    void setAnnounceAdvancements(boolean announceAdvancements);

    /**
     * Sets whether the world should be loaded on server startup.
     *
     * @param loadOnStartup {@code true} to load on startup, {@code false} otherwise.
     */
    void setLoadOnStartup(boolean loadOnStartup);

    /**
     * Sets the alias of the world, which is only used as displayname for PAPI.
     * This does not change the world name or the name used for commands/placeholders.
     *
     * @param alias The alias to set for the world.
     */
    void setAlias(String alias);

}
