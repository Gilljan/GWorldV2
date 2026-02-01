package de.gilljan.gworld.api;

import de.gilljan.gworld.enums.WorldTypeMapping;
import de.gilljan.gworld.world.ManageableWorld;
import org.bukkit.World;

import java.util.List;
import java.util.Optional;

/**
 * Manages all worlds within the GWorld plugin.
 * This interface provides methods to get, create, and remove worlds.
 */
public interface IWorldManager {
    /**
     * Gets a manageable world by its name.
     *
     * @param name The name of the world.
     * @return An {@link Optional} containing the {@link IManageableWorld} if it exists, otherwise empty.
     */
    Optional<IManageableWorld> getWorld(String name);

    /**
     * Gets a list of all manageable worlds.
     *
     * @return A list of all {@link IManageableWorld}s.
     */
    List<IManageableWorld> getWorlds();

    /**
     * Creates and registers a new world from a {@link WorldCreationBuilder}.
     * This method only registers the world in the system. To load, create or import the world,
     * you need to use the corresponding methods on the returned {@link IManageableWorld} instance (e.g. {@link IManageableWorld#createMap()}).
     * This method is typically called by {@link WorldCreationBuilder#build()}.
     *
     * @param builder The builder containing the world's settings.
     * @return The newly created {@link IManageableWorld}.
     */
    IManageableWorld addWorldFromBuilder(WorldCreationBuilder builder);

    /**
     * Removes a manageable world.
     * This will unload and delete the world files.
     *
     * @param world The world to remove.
     * @return {@code true} if the world was successfully removed, otherwise {@code false}.
     */
    boolean removeWorld(IManageableWorld world);

    /**
     * Creates a new {@link WorldCreationBuilder} to define a new world.
     *
     * @param name The name of the world to be created.
     * @return A new {@link WorldCreationBuilder}.
     */
    default WorldCreationBuilder createBuilder(String name) {
        return new WorldCreationBuilder(name, this);
    }


}
