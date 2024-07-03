package de.gilljan.gworld.world;

import de.gilljan.gworld.GWorld;
import de.gilljan.gworld.api.IGWorldApi;
import de.gilljan.gworld.data.world.WorldData;

import java.util.HashMap;

public class WorldManager {

    public WorldManager() {
    }

    public ManageableWorld addWorld(WorldData world) {
        ManageableWorld manageableWorld = new ManageableWorld(world);
        GWorld.getInstance().getDataHandler().addWorld(world);

        return manageableWorld;
    }

    public ManageableWorld getWorld(String name) {
        return new ManageableWorld(GWorld.getInstance().getDataHandler().getWorld(name));
    }

    public void removeWorld(WorldData data) {
        getWorld(data.getGeneralInformation().worldName()).deleteMap();
        GWorld.getInstance().getDataHandler().removeWorld(data);
    }


}
