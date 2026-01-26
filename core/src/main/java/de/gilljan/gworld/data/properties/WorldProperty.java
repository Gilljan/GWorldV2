package de.gilljan.gworld.data.properties;

import de.gilljan.gworld.data.world.WorldData;
import de.gilljan.gworld.world.ManageableWorld;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;

import java.util.ArrayList;
import java.util.List;

public record WorldProperty<T> (PropertyFunction<T> getFunction, T defaultValue, PropertySetter<T> setFunction) {

    public static final WorldProperty<Boolean> ALLOW_PVP = new WorldProperty<>(ManageableWorld::isAllowPvP, true, ManageableWorld::setAllowPvP);
    public static final WorldProperty<Boolean> KEEP_SPAWN_IN_MEMORY = new WorldProperty<>(ManageableWorld::isKeepSpawnInMemory, true, ManageableWorld::setKeepSpawnInMemory);
    public static final WorldProperty<Boolean> ANIMAL_SPAWNING = new WorldProperty<>(ManageableWorld::isAnimalSpawning, true, ManageableWorld::setAnimalSpawning);
    public static final WorldProperty<Boolean> MONSTER_SPAWNING = new WorldProperty<>(ManageableWorld::isMonsterSpawning, true, ManageableWorld::setMonsterSpawning);
    public static final WorldProperty<Boolean> WEATHER_CYCLE = new WorldProperty<>(ManageableWorld::isWeatherCycle, true, ManageableWorld::setWeatherCycle);
    public static final WorldProperty<Boolean> TIME_CYCLE = new WorldProperty<>(ManageableWorld::isTimeCycle, true, ManageableWorld::setTimeCycle);
    public static final WorldProperty<Boolean> DEFAULT_GAMEMODE = new WorldProperty<>(ManageableWorld::isDefaultGamemode, false, ManageableWorld::setDefaultGamemode);
    public static final WorldProperty<Long> TIME = new WorldProperty<>(ManageableWorld::getTime, 0L, ManageableWorld::setTime);
    public static final WorldProperty<WorldData.WeatherType> WEATHER_TYPE = new WorldProperty<>(ManageableWorld::getWeatherType, WorldData.WeatherType.CLEAR, ManageableWorld::setWeatherType);
    public static final WorldProperty<GameMode> GAMEMODE = new WorldProperty<>(ManageableWorld::getGameMode, GameMode.SURVIVAL, ManageableWorld::setGameMode);
    public static final WorldProperty<Difficulty> DIFFICULTY = new WorldProperty<>(ManageableWorld::getDifficulty, Difficulty.NORMAL, ManageableWorld::setDifficulty);
    public static final WorldProperty<String> GENERATOR = new WorldProperty<>(ManageableWorld::getGenerator, null, (data, value) -> {});
    public static final WorldProperty<List<String>> DISABLED_ANIMALS = new WorldProperty<>(ManageableWorld::getDisabledAnimals, new ArrayList<>(), ManageableWorld::setDisabledAnimals);
    public static final WorldProperty<List<String>> DISABLED_MONSTERS = new WorldProperty<>(ManageableWorld::getDisabledMonsters, new ArrayList<>(), ManageableWorld::setDisabledMonsters);
    public static final WorldProperty<Integer> RANDOM_TICK_SPEED = new WorldProperty<>(ManageableWorld::getRandomTickSpeed, 3, ManageableWorld::setRandomTickSpeed);
    public static final WorldProperty<Boolean> ANNOUNCE_ADVANCEMENTS = new WorldProperty<>(ManageableWorld::isAnnounceAdvancements, true, ManageableWorld::setAnnounceAdvancements);
    public static final WorldProperty<Boolean> LOAD_ON_STARTUP = new WorldProperty<>(ManageableWorld::isLoadOnStartup, true, ManageableWorld::setLoadOnStartup);


    public static <T> T getValue(WorldProperty<T> property, ManageableWorld world) {
        return property.getFunction.get(world);
    }

    public static <T> T getDefaultValue(WorldProperty<T> property) {
        return property.defaultValue();
    }

    public static <T> void setValue(WorldProperty<T> property, ManageableWorld world, T value) {
        property.setFunction.set(world, value);
    }



}
