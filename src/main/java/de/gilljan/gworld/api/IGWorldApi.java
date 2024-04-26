package de.gilljan.gworld.api;

import org.bukkit.WorldType;

import javax.annotation.Nullable;

public interface IGWorldApi {

    boolean loadMap();

    boolean unloadMap();

    boolean deleteMap(boolean saveMap);

    boolean createMap(WorldType type, @Nullable long seed, @Nullable String generator);

    boolean createMap(WorldType type);

    boolean importExisting(WorldType type, @Nullable String generator);

    boolean importExisting(WorldType type);

    boolean isMapLoaded();



}
