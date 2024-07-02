package de.gilljan.gworld.world;

import de.gilljan.gworld.api.IGWorldApi;
import de.gilljan.gworld.data.world.WorldData;

import java.util.HashMap;

public class WorldManager {
    private HashMap<String, WorldData> worlds;

    public WorldManager() {
        this.worlds = new HashMap<>();
    }

    public WorldManager(HashMap<String, WorldData> worlds) {
        this.worlds = worlds;
    }

    public void addWorld(WorldData world) {
        this.worlds.put(world.getGeneralInformation().worldName(), world);
    }


}
