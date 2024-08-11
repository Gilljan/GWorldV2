package de.gilljan.gworld.data;

import de.gilljan.gworld.data.world.WorldData;

import java.util.HashMap;

public interface DataHandler {

    WorldData getWorld(String name);

    void saveWorld(WorldData world);

    void saveAllWorlds();

    void loadAllWorlds();

    void refetchWorlds();

    WorldData fetchWorld(String name);

    void addWorld(WorldData world);

    void removeWorld(WorldData worldData);

    boolean containsWorld(String name);

    HashMap<String, WorldData> getWorlds();

}
