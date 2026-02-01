package de.gilljan.gworld.data.properties;

import de.gilljan.gworld.api.IManageableWorld;

@FunctionalInterface
public interface PropertyFunction<T> {
    T get(IManageableWorld world);
}
