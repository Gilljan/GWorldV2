package de.gilljan.gworld.enums;

import de.gilljan.gworld.GWorld;

import java.util.*;
import java.util.stream.Collectors;

public enum Settings {
    WEATHER_CYCLE("true", "false"),
    WEATHER ("clear", "rain", "thunder"),
    TIME_CYCLE("true", "false"),
    TIME,
    DIFFICULTY("peaceful", "easy", "normal", "hard"),
    PVP("true", "false"),
    MONSTERS("true", "false"),
    DISABLED_MONSTERS(GWorld.MONSTER.toArray(new String[0])),
    ANIMALS("true", "false"),
    DISABLED_ANIMALS(GWorld.ANIMALS.toArray(new String[0])),
    FORCED_GAMEMODE("true", "false"),
    DEFAULT_GAMEMODE("survival", "creative", "adventure", "spectator"),
    RANDOM_TICK_SPEED,
    ANNOUNCE_ADVANCEMENTS("true", "false"),
    LOAD_ON_STARTUP("true", "false");

    private final List<String> values = new ArrayList<>();

    private static final Map<String, Settings> STRING_SETTINGS_MAP = Arrays.stream(values()).collect(Collectors.toMap(setting -> setting.name().toLowerCase(), setting -> setting));

    Settings(String... values) {
        this.values.addAll(Arrays.asList(values));
    }

    public List<String> getValues() {
        return values;
    }

    public static Optional<Settings> fromString(String name) {
        if(name == null) return Optional.empty();
        return Optional.ofNullable(STRING_SETTINGS_MAP.get(name.toLowerCase()));
    }

}
