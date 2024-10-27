package de.gilljan.gworld.data.properties;

import de.gilljan.gworld.data.world.WorldData;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;

import java.util.ArrayList;
import java.util.List;

public record WorldProperty<T> (PropertyFunction<T> getFunction, T defaultValue, PropertySetter<T> setFunction) {

    public static final WorldProperty<Boolean> ALLOW_PVP = new WorldProperty<>(WorldData::isAllowPvP, true, WorldData::setAllowPvP);
    public static final WorldProperty<Boolean> KEEP_SPAWN_IN_MEMORY = new WorldProperty<>(WorldData::isKeepSpawnInMemory, true, WorldData::setKeepSpawnInMemory);
    public static final WorldProperty<Boolean> ANIMAL_SPAWNING = new WorldProperty<>(WorldData::isAnimalSpawning, true, WorldData::setAnimalSpawning);
    public static final WorldProperty<Boolean> MONSTER_SPAWNING = new WorldProperty<>(WorldData::isMonsterSpawning, true, WorldData::setMonsterSpawning);
    public static final WorldProperty<Boolean> WEATHER_CYCLE = new WorldProperty<>(WorldData::isWeatherCycle, true, WorldData::setWeatherCycle);
    public static final WorldProperty<Boolean> TIME_CYCLE = new WorldProperty<>(WorldData::isTimeCycle, true, WorldData::setTimeCycle);
    public static final WorldProperty<Boolean> DEFAULT_GAMEMODE = new WorldProperty<>(WorldData::isDefaultGamemode, false, WorldData::setDefaultGamemode);
    public static final WorldProperty<Long> TIME = new WorldProperty<>(WorldData::getTime, 0L, WorldData::setTime);
    public static final WorldProperty<WorldData.WeatherType> WEATHER_TYPE = new WorldProperty<>(WorldData::getWeatherType, WorldData.WeatherType.CLEAR, WorldData::setWeatherType);
    public static final WorldProperty<GameMode> GAMEMODE = new WorldProperty<>(WorldData::getGameMode, GameMode.SURVIVAL, WorldData::setGameMode);
    public static final WorldProperty<Difficulty> DIFFICULTY = new WorldProperty<>(WorldData::getDifficulty, Difficulty.NORMAL, WorldData::setDifficulty);
    public static final WorldProperty<String> GENERATOR = new WorldProperty<>(WorldData::getWorldGenerator, null, (data, value) -> {});
    public static final WorldProperty<List<String>> DISABLED_ANIMALS = new WorldProperty<>(WorldData::getDisabledAnimals, new ArrayList<>(), WorldData::setDisabledAnimals);
    public static final WorldProperty<List<String>> DISABLED_MONSTERS = new WorldProperty<>(WorldData::getDisabledMonsters, new ArrayList<>(), WorldData::setDisabledMonsters);
    public static final WorldProperty<Integer> RANDOM_TICK_SPEED = new WorldProperty<>(WorldData::getRandomTickSpeed, 3, WorldData::setRandomTickSpeed);
    public static final WorldProperty<Boolean> ANNOUNCE_ADVANCEMENTS = new WorldProperty<>(WorldData::isAnnounceAdvancements, true, WorldData::setAnnounceAdvancements);
    public static final WorldProperty<Boolean> LOAD_ON_STARTUP = new WorldProperty<>(WorldData::isLoadOnStartup, true, WorldData::setLoadOnStartup);


    public static <T> T getValue(WorldProperty<T> property, WorldData data) {
        return property.getFunction.get(data);
    }

    public static <T> T getDefaultValue(WorldProperty<T> property) {
        return property.defaultValue();
    }

    public static <T> void setValue(WorldProperty<T> property, WorldData data, T value) {
        property.setFunction.set(data, value);
    }



}
