package de.gilljan.gworld.data.properties;

import de.gilljan.gworld.world.ManageableWorld;

@FunctionalInterface
public interface PropertyFunction<T> {
    T get(ManageableWorld world);
}
