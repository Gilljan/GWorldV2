package de.gilljan.gworld.world;

import de.gilljan.gworld.api.IGWorldApi;
import de.gilljan.gworld.data.world.WorldData;
import org.bukkit.WorldType;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ManageableWorld implements IGWorldApi {
    private WorldData worldData;

    public ManageableWorld(WorldData worldData) {
        this.worldData = worldData;
    }


    @Override
    public boolean loadMap() {
        return false;
    }

    @Override
    public boolean unloadMap() {
        return false;
    }

    @Override
    public boolean deleteMap(boolean saveMap) {
        return false;
    }

    @Override
    public boolean createMap(WorldType type, @Nullable long seed, @Nullable String generator) {
        return false;
    }

    @Override
    public boolean createMap(WorldType type) {
        return false;
    }

    @Override
    public boolean importExisting(WorldType type, @Nullable String generator) {
        return false;
    }

    @Override
    public boolean importExisting(WorldType type) {
        return false;
    }

    @Override
    public boolean isMapLoaded() {
        return false;
    }

    @Override
    public Optional<IGWorldApi> clone(String newWorldName) {
        return Optional.empty();
    }

    @Override
    public boolean delete() {
        return false;
    }

    @Override
    public boolean reCreate(boolean saveWorldToFile) {
        return false;
    }

    @Override
    public boolean saveWorld() {
        return false;
    }
}
