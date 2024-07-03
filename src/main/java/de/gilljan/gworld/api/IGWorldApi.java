package de.gilljan.gworld.api;

import de.gilljan.gworld.data.world.WorldData;
import org.bukkit.WorldType;

import javax.annotation.Nullable;
import java.util.Optional;

public interface IGWorldApi {

    boolean loadMap();

    boolean unloadMap();

    boolean deleteMap();

    boolean createMap();

    boolean importExisting();

    boolean isMapLoaded();

    Optional<IGWorldApi> clone(String newWorldName);

    boolean reCreate(boolean saveWorldToFile);

    boolean saveWorld();

    //<T> T getProperty(WorldProperties property);
    void setAllProperties();



}
