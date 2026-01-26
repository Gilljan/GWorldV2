package de.gilljan.gworld.api.gamerule;

public enum GGameRule {
    ADVANCE_WEATHER("doWeatherCycle", Boolean.class),
    ADVANCE_TIME("doDaylightCycle", Boolean.class),
    RANDOM_TICK_SPEED("randomTickSpeed", Integer.class),
    SHOW_ADVANCEMENT_MESSAGES("announceAdvancements", Boolean.class);

    private final String v21_11GameRuleName;
    private final String v20UpGameRuleName;
    private final Class<?> valueType;

    GGameRule(String v20UpGameRuleName, Class<?> valueType) {
        this.v21_11GameRuleName = this.name().toLowerCase();
        this.v20UpGameRuleName = v20UpGameRuleName;
        this.valueType = valueType;
    }

    public String getV21_11GameRuleName() {
        return v21_11GameRuleName;
    }

    public String getV20UpGameRuleName() {
        return v20UpGameRuleName;
    }

    public Class<?> getValueType() {
        return valueType;
    }
}
