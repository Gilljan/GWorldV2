package de.gilljan.gworld.world;

import de.gilljan.gworld.GWorld;
import de.gilljan.gworld.data.world.WorldData;

import java.util.Optional;

public class WorldManager {

    public ManageableWorld addWorld(WorldData world) {
        ManageableWorld manageableWorld = new ManageableWorld(world);
        GWorld.getInstance().getDataHandler().addWorld(world);

        return manageableWorld;
    }

    //@Nullable
    public Optional<ManageableWorld> getWorld(String name) {
        return GWorld.getInstance().getDataHandler().getWorld(name).map(ManageableWorld::new);
    }


    /**
     * Remove a world from the data handler. <br>
     *
     * Important: This does NOT delete the world files!
     * Use for that: GWorld.getInstance().getWorldManager().getWorld(worldName).ifPresent(world -> world.deleteMap());
     * @param data the world data to remove
     * @see ManageableWorld#deleteMap()
     */
    public void removeWorld(WorldData data) {
        GWorld.getInstance().getDataHandler().removeWorld(data);
    }


}
