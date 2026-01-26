package de.gilljan.gworld.data.properties;

import de.gilljan.gworld.data.world.WorldData;

@FunctionalInterface
public interface PropertySetter<T> {
    void set(WorldData data, T value);
}
