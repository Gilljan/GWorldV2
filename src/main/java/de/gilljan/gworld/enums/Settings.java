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

    public static Settings mapFromString(String setting) {
        return switch (setting.toLowerCase()) {
            case "weathercycle" -> WEATHER_CYCLE;
            case "weather" -> WEATHER;
            case "timecycle" -> TIME_CYCLE;
            case "time" -> TIME;
            case "difficulty" -> DIFFICULTY;
            case "pvp" -> PVP;
            case "monsterspawning" -> MONSTERS;
            case "disabledmonsters" -> MONSTER_SPECIFIC;
            case "animalspawning" -> ANIMALS;
            case "disabledanimals" -> ANIMAL_SPECIFIC;
            case "forcedgamemode" -> FORCED_GAMEMODE;
            case "defaultgamemode" -> DEFAULT_GAMEMODE;
            case "randomtickspeed" -> RANDOM_TICK_SPEED;
            case "announceadvancements" -> ANNOUNCE_ADVANCEMENTS;
            case "loadonstartup" -> LOAD_ON_STARTUP;
            default -> null;
        };
    }

}
