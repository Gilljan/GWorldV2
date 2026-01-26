package de.gilljan.gworld.enums;

import org.bukkit.World;
import org.bukkit.WorldType;

public enum WorldTypeMapping {
    NORMAL(WorldType.NORMAL, World.Environment.NORMAL),
    NETHER(WorldType.NORMAL, World.Environment.NETHER),
    END(WorldType.NORMAL, World.Environment.THE_END),
    FLAT(WorldType.FLAT, World.Environment.NORMAL),
    LARGE_BIOMES(WorldType.LARGE_BIOMES, World.Environment.NORMAL),
    AMPLIFIED(WorldType.AMPLIFIED, World.Environment.NORMAL);

    private final WorldType worldType;
    private final World.Environment environment;

    WorldTypeMapping(WorldType worldType, World.Environment environment) {
        this.worldType = worldType;
        this.environment = environment;
    }

    public WorldType getWorldType() {
        return worldType;
    }

    public World.Environment getEnvironment() {
        return environment;
    }

    public static WorldTypeMapping fromString(String type) {
        try {
            return WorldTypeMapping.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
