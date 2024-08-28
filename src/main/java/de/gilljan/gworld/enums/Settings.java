package de.gilljan.gworld.enums;

import de.gilljan.gworld.GWorld;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Settings {
    WEATHER_CYCLE("true", "false"),
    WEATHER ("clear", "rain", "thunder"),
    TIME_CYCLE("true", "false"),
    TIME,
    DIFFICULTY("peaceful", "easy", "normal", "hard"),
    PVP("true", "false"),
    MONSTERS("true", "false"),
    MONSTER_SPECIFIC(GWorld.MONSTER.toArray(new String[0])),
    ANIMALS("true", "false"),
    ANIMAL_SPECIFIC(GWorld.ANIMALS.toArray(new String[0])),
    FORCED_GAMEMODE("true", "false"),
    DEFAULT_GAMEMODE("survival", "creative", "adventure", "spectator"),
    RANDOM_TICK_SPEED,
    ANNOUNCE_ADVANCEMENTS("true", "false"),
    LOAD_ON_STARTUP("true", "false");

    private final List<String> values = new ArrayList<>();

    Settings(String... values) {
        this.values.addAll(Arrays.asList(values));
    }

    public List<String> getValues() {
        return values;
    }

}
