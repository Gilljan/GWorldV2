package de.gilljan.gworld.data;

import de.gilljan.gworld.data.world.WorldData;

import java.util.HashMap;
import java.util.Optional;

public interface DataHandler {

    Optional<WorldData> getWorld(String name);

    void saveWorld(WorldData world);

    void saveAllWorlds();

    void loadAllWorlds();

    void refetchWorlds();

    Optional<WorldData> fetchWorld(String name);

    void addWorld(WorldData world);

    void removeWorld(WorldData worldData);

    boolean containsWorld(String name);

    HashMap<String, WorldData> getWorlds();

}
