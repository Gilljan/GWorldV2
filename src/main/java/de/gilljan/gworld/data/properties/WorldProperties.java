package de.gilljan.gworld.data.properties;

import de.gilljan.gworld.data.world.WorldData;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public enum WorldProperties {
    ALLOW_PVP(WorldData::isAllowPvP, Boolean.class, WorldData::setAllowPvP),
    KEEP_SPAWN_IN_MEMORY(WorldData::isKeepSpawnInMemory, Boolean.class),
    ANIMAL_SPAWNING(WorldData::isAnimalSpawning, Boolean.class),
    DISABLED_ANIMALS(WorldData::getDisabledAnimals, List.class),
    MONSTER_SPAWNING(WorldData::isMonsterSpawning, Boolean.class),
    DISABLED_MONSTERS(WorldData::getDisabledMonsters, List.class),
    WEATHER_CYCLE(WorldData::isWeatherCycle, Boolean.class),
    WEATHER_TYPE(WorldData::getWeatherType, Enum.class),
    TIME_CYCLE(WorldData::isTimeCycle, Boolean.class),
    TIME(WorldData::getTime, Long.class),
    DEFAULT_GAMEMODE(WorldData::isDefaultGamemode, Boolean.class),
    GAMEMODE(WorldData::getGameMode, Enum.class),
    DIFFICULTY(WorldData::getDifficulty, Enum.class),
    SPAWN_CHUNK_RADIUS(WorldData::getSpawnChunkRadius, Integer.class),;

    private final PropertyFunction<?> function;
    private final Class<?> type;
    private final Consumer<WorldData> saveOperation;

    <T> WorldProperties(PropertyFunction<T> function, Class<T> type, Consumer<WorldData> saveOperation) {
        this.function = function;
        this.type = type;
        this.saveOperation = saveOperation;
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(WorldData data) {
        return (T) type.cast(function.apply(data));
    }

    public void save(WorldData data) {
        saveOperation.accept(data);
    }
}
