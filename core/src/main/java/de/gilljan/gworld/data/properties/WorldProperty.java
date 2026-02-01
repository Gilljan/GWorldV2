package de.gilljan.gworld.data.properties;

import de.gilljan.gworld.api.IManageableWorld;
import de.gilljan.gworld.data.world.WorldData;
import de.gilljan.gworld.world.ManageableWorld;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;

import java.util.ArrayList;
import java.util.List;

public record WorldProperty<T> (PropertyFunction<T> getFunction, T defaultValue, PropertySetter<T> setFunction) {

    public static final WorldProperty<Boolean> ALLOW_PVP = new WorldProperty<>(IManageableWorld::isAllowPvP, true, IManageableWorld::setAllowPvP);
    public static final WorldProperty<Boolean> KEEP_SPAWN_IN_MEMORY = new WorldProperty<>(IManageableWorld::isKeepSpawnInMemory, true, IManageableWorld::setKeepSpawnInMemory);
    public static final WorldProperty<Boolean> ANIMAL_SPAWNING = new WorldProperty<>(IManageableWorld::isAnimalSpawning, true, IManageableWorld::setAnimalSpawning);
    public static final WorldProperty<Boolean> MONSTER_SPAWNING = new WorldProperty<>(IManageableWorld::isMonsterSpawning, true, IManageableWorld::setMonsterSpawning);
    public static final WorldProperty<Boolean> WEATHER_CYCLE = new WorldProperty<>(IManageableWorld::isWeatherCycle, true, IManageableWorld::setWeatherCycle);
    public static final WorldProperty<Boolean> TIME_CYCLE = new WorldProperty<>(IManageableWorld::isTimeCycle, true, IManageableWorld::setTimeCycle);
    public static final WorldProperty<Boolean> DEFAULT_GAMEMODE = new WorldProperty<>(IManageableWorld::isDefaultGamemode, false, IManageableWorld::setDefaultGamemode);
    public static final WorldProperty<Long> TIME = new WorldProperty<>(IManageableWorld::getTime, 0L, IManageableWorld::setTime);
    public static final WorldProperty<WorldData.WeatherType> WEATHER_TYPE = new WorldProperty<>(IManageableWorld::getWeatherType, WorldData.WeatherType.CLEAR, IManageableWorld::setWeatherType);
    public static final WorldProperty<GameMode> GAMEMODE = new WorldProperty<>(IManageableWorld::getGameMode, GameMode.SURVIVAL, IManageableWorld::setGameMode);
    public static final WorldProperty<Difficulty> DIFFICULTY = new WorldProperty<>(IManageableWorld::getDifficulty, Difficulty.NORMAL, IManageableWorld::setDifficulty);
    public static final WorldProperty<String> GENERATOR = new WorldProperty<>(IManageableWorld::getGenerator, null, (data, value) -> {});
    public static final WorldProperty<List<String>> DISABLED_ANIMALS = new WorldProperty<>(IManageableWorld::getDisabledAnimals, new ArrayList<>(), IManageableWorld::setDisabledAnimals);
    public static final WorldProperty<List<String>> DISABLED_MONSTERS = new WorldProperty<>(IManageableWorld::getDisabledMonsters, new ArrayList<>(), IManageableWorld::setDisabledMonsters);
    public static final WorldProperty<Integer> RANDOM_TICK_SPEED = new WorldProperty<>(IManageableWorld::getRandomTickSpeed, 3, IManageableWorld::setRandomTickSpeed);
    public static final WorldProperty<Boolean> ANNOUNCE_ADVANCEMENTS = new WorldProperty<>(IManageableWorld::isAnnounceAdvancements, true, IManageableWorld::setAnnounceAdvancements);
    public static final WorldProperty<Boolean> LOAD_ON_STARTUP = new WorldProperty<>(IManageableWorld::isLoadOnStartup, true, IManageableWorld::setLoadOnStartup);


    public static <T> T getValue(WorldProperty<T> property, IManageableWorld world) {
        return property.getFunction.get(world);
    }

    public static <T> T getDefaultValue(WorldProperty<T> property) {
        return property.defaultValue();
    }

    public static <T> void setValue(WorldProperty<T> property, IManageableWorld world, T value) {
        property.setFunction.set(world, value);
    }



}
