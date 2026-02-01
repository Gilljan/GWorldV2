package de.gilljan.gworld.data.properties;

import de.gilljan.gworld.api.IManageableWorld;

@FunctionalInterface
public interface PropertySetter<T> {
    void set(IManageableWorld world, T value);
}
