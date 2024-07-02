package de.gilljan.gworld.api;

import de.gilljan.gworld.data.world.WorldData;
import org.bukkit.WorldType;

import javax.annotation.Nullable;
import java.util.Optional;

public interface IGWorldApi {

    boolean loadMap();

    boolean unloadMap();

    boolean deleteMap(boolean saveMap);

    boolean createMap(WorldType type, @Nullable long seed, @Nullable String generator);

    boolean createMap(WorldType type);

    boolean importExisting(WorldType type, @Nullable String generator);

    boolean importExisting(WorldType type);

    boolean isMapLoaded();

    Optional<IGWorldApi> clone(String newWorldName);

    boolean delete();

    boolean reCreate(boolean saveWorldToFile);

    boolean saveWorld();

    //<T> T getProperty(WorldProperties property);



}
