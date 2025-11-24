package de.gilljan.gworld.world;

import de.gilljan.gworld.GWorld;
import de.gilljan.gworld.api.IGWorldApi;
import de.gilljan.gworld.data.world.WorldData;
import de.gilljan.gworld.utils.DirectoryUtil;
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

        World created = Bukkit.createWorld(worldCreator);
        //Bukkit.getWorlds().add(Bukkit.getWorld(worldData.getGeneralInformation().worldName()));
        if(created == null) {
            return false;
        }

        worldData.setLoaded(true);

        setAllProperties();

        return true;
    }

    @Override
    public boolean unloadMap() {
        //potential teleport of players

        boolean success = Bukkit.unloadWorld(worldData.getGeneralInformation().worldName(), true);

        if(!success) {
            GWorld.getInstance().getLogger().warning("Could not unload world " + worldData.getGeneralInformation().worldName());
            return false;
        }

        //Bukkit.getWorlds().remove(Bukkit.getWorld(worldData.getGeneralInformation().worldName()));

        worldData.setLoaded(false);
        return true;
    }

    @Override
    public boolean deleteMap() {
        unloadMap();

        File world = new File(Bukkit.getWorldContainer(), worldData.getGeneralInformation().worldName());

        try {
            //delete directory
            FileUtils.deleteDirectory(world);
            return true;
        } catch (IOException ignored) {
            GWorld.getInstance().getLogger().severe("Could not delete world " + worldData.getGeneralInformation().worldName());
        }

        return false;
    }

    @Override
    public boolean createMap() {
        WorldCreator worldCreator = WorldCreator.name(worldData.getGeneralInformation().worldName());

        System.out.println(worldData.getGeneralInformation());

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

        World world = Bukkit.createWorld(worldCreator);
        //Bukkit.getWorlds().add(Bukkit.getWorld(worldData.getGeneralInformation().worldName()));

        if(world == null) {
            GWorld.getInstance().getLogger().warning("Could not create world " + worldData.getGeneralInformation().worldName());
            return false;
        }

        worldData.setLoaded(true);


        setAllProperties();

        return true;
    }

    @Override
    public boolean importExisting() {
        File target = new File(Bukkit.getWorldContainer(), worldData.getGeneralInformation().worldName());

        if(!target.exists()) { //todo maybe exception?
            return false;
        }

        try {
            new File(target, "uid.dat").delete();
        } catch (Exception ignored) {}

        boolean success = loadMap();
        if (!success) {
            GWorld.getInstance().getLogger().warning("Could not import existing world. Loading failed: " + worldData.getGeneralInformation().worldName());
            return false;
        }

        World world = Bukkit.getWorld(worldData.getGeneralInformation().worldName());

        if(world == null) {
            GWorld.getInstance().getLogger().warning("Could not import existing world. World is null: " + worldData.getGeneralInformation().worldName());
            return false;
        }

        worldData.updateGeneralInformation(new WorldData.GeneralInformation(world.getName(), world.getEnvironment(), world.getWorldType(), world.getSeed(), world.getGenerator() == null ? null : world.getGenerator().toString()));

        return true;
    }

    @Override
    public boolean isMapLoaded() {
        return worldData.isLoaded();
    }

    @Override
    public Optional<IGWorldApi> clone(String newWorldName) {
        this.saveWorld();

        if(!DirectoryUtil.copyMapDirectory(this.getWorldName(), newWorldName))
            return Optional.empty();


        ManageableWorld clonedWorld = new ManageableWorld(new WorldData(newWorldName));

        clonedWorld.worldData.updateGeneralInformation(new WorldData.GeneralInformation(newWorldName, worldData.getGeneralInformation().environment(), worldData.getGeneralInformation().worldType(), worldData.getGeneralInformation().seed(), worldData.getGeneralInformation().worldGenerator()));

        if (!clonedWorld.importExisting()) {
            return Optional.empty();
        }

        GWorld.getInstance().getWorldManager().addWorld(clonedWorld.worldData);
        return Optional.of(clonedWorld);
    }

    @Override
    public boolean reCreate(boolean saveWorldToFile) {
        if (!isMapLoaded()) {
            System.out.println("World " + worldData.getGeneralInformation().worldName() + " is not loaded, cannot recreate.");
            return false;
        }

        if(!unloadMap()) {
            System.out.println("Could not unload world " + worldData.getGeneralInformation().worldName() + " for recreation.");
            return false;
        }

        File world = new File(Bukkit.getWorldContainer(), worldData.getGeneralInformation().worldName());


        if (saveWorldToFile) {
            File destinationDir = new File(Bukkit.getWorldContainer(), "old_maps");
            File destination = new File(destinationDir, worldData.getGeneralInformation().worldName() + " - " + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(System.currentTimeMillis()));

            try {
                if (!destinationDir.exists()) {
                    destinationDir.mkdirs();
                }

                FileUtils.moveDirectory(world, destination);
            } catch (IOException ignored) {
                ignored.printStackTrace();
                return false;
            }
        } else deleteMap();

        System.out.println("#222");

        return createMap();
    }

    @Override
    public boolean saveWorld() {
        if (!isMapLoaded()) {
            return false;
        }

        Bukkit.getWorld(worldData.getGeneralInformation().worldName()).save();
        return true;
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
        worldData.setAnimalSpawning(worldData.isAnimalSpawning());
        worldData.setMonsterSpawning(worldData.isMonsterSpawning());
        worldData.setRandomTickSpeed(worldData.getRandomTickSpeed());
        worldData.setAnnounceAdvancements(worldData.isAnnounceAdvancements());
        worldData.setKeepSpawnInMemory(worldData.isKeepSpawnInMemory());
    }

    @Override
    public String getWorldName() {
        return worldData.getGeneralInformation().worldName();
    }
}
