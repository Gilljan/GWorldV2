package de.gilljan.gworld.world;

import de.gilljan.gworld.GWorld;
import de.gilljan.gworld.data.world.WorldData;

import java.util.Optional;

public class WorldManager {

    public WorldManager() {
    }

    public ManageableWorld addWorld(WorldData world) {
        ManageableWorld manageableWorld = new ManageableWorld(world);
        GWorld.getInstance().getDataHandler().addWorld(world);

        return manageableWorld;
    }

    //@Nullable
    public Optional<ManageableWorld> getWorld(String name) {
        return GWorld.getInstance().getDataHandler().getWorld(name).map(ManageableWorld::new);
    }

    public void removeWorld(WorldData data) {
        getWorld(data.getGeneralInformation().worldName()).ifPresent(ManageableWorld::deleteMap);
        GWorld.getInstance().getDataHandler().removeWorld(data);
    }


}
