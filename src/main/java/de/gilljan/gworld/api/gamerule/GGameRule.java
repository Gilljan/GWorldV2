package de.gilljan.gworld.api.gamerule;

public enum GGameRule {
    ADVANCE_WEATHER(Boolean.class),
    ADVANCE_TIME(Boolean.class),
    RANDOM_TICK_SPEED(Integer.class),
    SHOW_ADVANCEMENT_MESSAGES(Boolean.class);

    private final String gameRuleName;
    private final Class<?> valueType;

    GGameRule(Class<?> valueType) {
        this.gameRuleName = this.name().toLowerCase();
        this.valueType = valueType;
    }

    public String getGameRuleName() {
        return gameRuleName;
    }

    public Class<?> getValueType() {
        return valueType;
    }
}
