package de.gilljan.gworld.data.properties;

import de.gilljan.gworld.world.ManageableWorld;

@FunctionalInterface
public interface PropertySetter<T> {
    void set(ManageableWorld world, T value);
}
