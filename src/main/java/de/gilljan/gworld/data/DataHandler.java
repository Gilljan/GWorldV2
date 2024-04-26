package de.gilljan.gworld.data;

import de.gilljan.gworld.data.world.WorldData;

public interface DataHandler {

    WorldData getWorld(String name);

    void saveWorld(WorldData world);

    void saveAllWorlds();

    void loadAllWorlds();

    void refetchWorlds();

}
