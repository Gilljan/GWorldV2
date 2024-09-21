package de.gilljan.gworld.world;

import de.gilljan.gworld.api.IGWorldApi;
import de.gilljan.gworld.data.world.WorldData;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Optional;

public class ManageableWorld implements IGWorldApi {
    private final WorldData worldData;

    public ManageableWorld(WorldData worldData) {
        this.worldData = worldData;
    }


    @Override
    public boolean loadMap() {
        WorldCreator worldCreator = WorldCreator.name(worldData.getGeneralInformation().worldName());

        if (worldData.getGeneralInformation().environment() != null) {
            worldCreator.environment(worldData.getGeneralInformation().environment());
        }

        if (worldData.getGeneralInformation().worldType() != null) {
            worldCreator.type(worldData.getGeneralInformation().worldType());
        }

        if (worldData.getGeneralInformation().worldGenerator() != null) {
            worldCreator.generator(worldData.getGeneralInformation().worldGenerator());
        }

        Bukkit.createWorld(worldCreator);
        Bukkit.getWorlds().add(Bukkit.getWorld(worldData.getGeneralInformation().worldName()));

        worldData.setLoaded(true);

        setAllProperties();

        return true;
    }

    @Override
    public boolean unloadMap() {
        //potential teleport of players

        Bukkit.unloadWorld(worldData.getGeneralInformation().worldName(), true);

        Bukkit.getWorlds().remove(Bukkit.getWorld(worldData.getGeneralInformation().worldName()));

        worldData.setLoaded(false);
        return false;
    }

    @Override
    public boolean deleteMap() {
        unloadMap();

        File world = new File(Bukkit.getWorldContainer(), worldData.getGeneralInformation().worldName());

        try {
            //delete directory
            FileUtils.deleteDirectory(world);
        } catch (IOException ignored) {
        }

        return false;
    }

    @Override
    public boolean createMap() {
        WorldCreator worldCreator = WorldCreator.name(worldData.getGeneralInformation().worldName());

        if (worldData.getGeneralInformation().environment() != null) {
            worldCreator.environment(worldData.getGeneralInformation().environment());
        }

        if (worldData.getGeneralInformation().worldType() != null) {
            worldCreator.type(worldData.getGeneralInformation().worldType());
        }

        if (worldData.getGeneralInformation().seed() != 0) {
            worldCreator.seed(worldData.getGeneralInformation().seed());
        }

        if (worldData.getGeneralInformation().worldGenerator() != null) {
            worldCreator.generator(worldData.getGeneralInformation().worldGenerator());
        }

        Bukkit.createWorld(worldCreator);
        Bukkit.getWorlds().add(Bukkit.getWorld(worldData.getGeneralInformation().worldName()));

        worldData.setLoaded(true);


        setAllProperties();

        return true;
    }

    @Override
    public boolean importExisting() {
        File target = new File(Bukkit.getWorldContainer(), worldData.getGeneralInformation().worldName());
        try {
            new File(target, "uid.dat").delete();
        } catch (Exception ignored) {}

        boolean success = loadMap();

        World world = Bukkit.getWorld(worldData.getGeneralInformation().worldName());

        worldData.updateGeneralInformation(new WorldData.GeneralInformation(world.getName(), world.getEnvironment(), world.getWorldType(), world.getSeed(), world.getGenerator() == null ? null : world.getGenerator().toString()));

        return success;
    }

    @Override
    public boolean isMapLoaded() {
        return worldData.isLoaded();
    }

    @Override
    public Optional<IGWorldApi> clone(String newWorldName) {
        ManageableWorld manageableWorld = new ManageableWorld(new WorldData(newWorldName));

        manageableWorld.worldData.updateGeneralInformation(new WorldData.GeneralInformation(newWorldName, worldData.getGeneralInformation().environment(), worldData.getGeneralInformation().worldType(), worldData.getGeneralInformation().seed(), worldData.getGeneralInformation().worldGenerator()));

        if (manageableWorld.createMap()) {
            return Optional.of(manageableWorld);
        }

        return Optional.empty();
    }

    @Override
    public boolean reCreate(boolean saveWorldToFile) {
        if (!isMapLoaded()) {
            return false;
        }

        unloadMap();

        File world = new File(Bukkit.getWorldContainer(), worldData.getGeneralInformation().worldName());


        if (saveWorldToFile) {
            File destinationDir = new File("old_maps//");
            File destination = new File(destinationDir, worldData.getGeneralInformation().worldName() + " - " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));

            try {
                if (!destinationDir.exists()) {
                    destinationDir.mkdirs();
                }

                FileUtils.moveDirectory(world, destination);
            } catch (IOException ignored) {
                return false;
            }
        } else deleteMap();

        createMap();

        return true;
    }

    @Override
    public boolean saveWorld() {
        if (!isMapLoaded()) {
            return false;
        }

        Bukkit.getWorld(worldData.getGeneralInformation().worldName()).save();
        return false;
    }

    @Override
    public void setAllProperties() {
        worldData.setDisabledMonsters(worldData.getDisabledMonsters());
        worldData.setDisabledAnimals(worldData.getDisabledAnimals());
        worldData.setWeatherCycle(worldData.isWeatherCycle());
        worldData.setTimeCycle(worldData.isTimeCycle());
        worldData.setDifficulty(worldData.getDifficulty());
        worldData.setAllowPvP(worldData.isAllowPvP());
        worldData.setDefaultGamemode(worldData.isDefaultGamemode());
    }

    @Override
    public String getWorldName() {
        return worldData.getGeneralInformation().worldName();
    }
}
