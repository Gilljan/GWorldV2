package de.gilljan.gworld.api;

import de.gilljan.gworld.data.properties.WorldProperty;
import de.gilljan.gworld.enums.WorldTypeMapping;
import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class WorldCreationBuilder {
    private final String name;
    private final IWorldManager worldManager;

    private WorldTypeMapping type = WorldTypeMapping.NORMAL;
    private String generator = null;
    private long seed = new Random().nextLong();
    private final Map<WorldProperty<?>, Object> properties = new HashMap<>();

    public WorldCreationBuilder(String name, IWorldManager worldManager) {
        this.name = name;
        this.worldManager = worldManager;
    }

    public WorldCreationBuilder worldType(WorldTypeMapping type) {
        this.type = type;
        return this;
    }

    public WorldCreationBuilder generator(String generator) {
        this.generator = generator;
        return this;
    }

    public WorldCreationBuilder seed(long seed) {
        this.seed = seed;
        return this;
    }

    public <T> WorldCreationBuilder property(WorldProperty<T> property, T value) {
        properties.put(property, value);
        return this;
    }

    public Map<WorldProperty<?>, Object> getProperties() {
        return properties;
    }

    public long getSeed() {
        return seed;
    }

    public String getGenerator() {
        return generator;
    }

    public WorldTypeMapping getWorldType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public IManageableWorld build() {
        return worldManager.addWorldFromBuilder(this);
    }
}
