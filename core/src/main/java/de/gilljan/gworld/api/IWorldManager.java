package de.gilljan.gworld.api;

import de.gilljan.gworld.enums.WorldTypeMapping;
import de.gilljan.gworld.world.ManageableWorld;
import org.bukkit.World;

import java.util.List;
import java.util.Optional;

public interface IWorldManager {
    // Getter
    Optional<IManageableWorld> getWorld(String name);
    List<ManageableWorld> getWorlds();

    // Builder
    IManageableWorld addWorldFromBuilder(WorldCreationBuilder builder);

    boolean removeWorld(IManageableWorld world);

    default WorldCreationBuilder createBuilder(String name) {
        return new WorldCreationBuilder(name, this);
    }


}
