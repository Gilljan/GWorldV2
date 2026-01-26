package de.gilljan.gworld.api;

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

    void setAllProperties();

    String getWorldName();
}
