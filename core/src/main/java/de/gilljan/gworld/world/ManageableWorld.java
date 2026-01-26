package de.gilljan.gworld.world;

import de.gilljan.gworld.GWorld;
import de.gilljan.gworld.api.IGWorldApi;
import de.gilljan.gworld.api.gamerule.GGameRule;
import de.gilljan.gworld.api.gamerule.GameRuleAdapter;
import de.gilljan.gworld.api.gamerule.GameRuleAdapterFactory;
import de.gilljan.gworld.data.world.WorldData;
import de.gilljan.gworld.utils.DirectoryUtil;
import de.gilljan.gworld.utils.MainWorldUtil;
import de.gilljan.gworld.utils.SendMessageUtil;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

public class ManageableWorld implements IGWorldApi {
    private final WorldData worldData;
    private static final GameRuleAdapter GAME_RULE_ADAPTER = GameRuleAdapterFactory.createAdapter();

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
        if(MainWorldUtil.isMainWorld(this.getWorldName())) {
            System.out.println("Cannot unload main world: " + this.getWorldName());
            return false;
        }

        //potential teleport of players
        if(!isMapLoaded()) {
            return true;
        }

        World unloadWorld = Bukkit.getWorld(worldData.getGeneralInformation().worldName());

        if(unloadWorld == null) {
            worldData.setLoaded(false);
            return true;
        }

        if (!unloadWorld.getPlayers().isEmpty()) {
            String mainWorldName = GWorld.getInstance().getConfig().getString("MainWorld");

            if(mainWorldName == null || mainWorldName.isEmpty()) {
                mainWorldName = Bukkit.getWorlds().get(0).getName();
            }

            World mainWorld = Bukkit.getWorld(mainWorldName);

            if(mainWorld == null) {
                mainWorld = Bukkit.getWorlds().get(0);
            }

            for(Player player : unloadWorld.getPlayers()) {
                player.teleport(mainWorld.getSpawnLocation());
                player.sendMessage(SendMessageUtil.sendMessage("Unload.teleport_players").replace("%world%", this.getWorldName()));
            }
        }

        boolean success = Bukkit.unloadWorld(worldData.getGeneralInformation().worldName(), true);

        if(!success) {
            GWorld.getInstance().getLogger().warning("Could not unload world " + worldData.getGeneralInformation().worldName());
            return false;
        }

        worldData.setLoaded(false);
        return true;
    }

    @Override
    public boolean deleteMap() {
        if(!unloadMap()) {
            return false;
        }

        File world = new File(Bukkit.getWorldContainer(), worldData.getGeneralInformation().worldName());

        try {
            //delete directory
            FileUtils.deleteDirectory(world);
            GWorld.getInstance().getWorldManager().removeWorld(this.worldData);
            return true;
        } catch (IOException ignored) {
            GWorld.getInstance().getLogger().severe("Could not delete world " + worldData.getGeneralInformation().worldName());
        }

        return false;
    }

    public boolean deleteMap(boolean removeFromManager) {
        if(!unloadMap()) {
            return false;
        }

        File world = new File(Bukkit.getWorldContainer(), worldData.getGeneralInformation().worldName());

        try {
            //delete directory
            FileUtils.deleteDirectory(world);
            if(removeFromManager) GWorld.getInstance().getWorldManager().removeWorld(this.worldData);
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

        updateWorldSpawnLocation(this);

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

        clonedWorld.worldData.setImporting(true);
        GWorld.getInstance().getWorldManager().addWorld(clonedWorld.worldData);

        System.out.println(clonedWorld.getWorldName());
        System.out.println(clonedWorld.worldData);

        if (!clonedWorld.importExisting()) {
            GWorld.getInstance().getLogger().warning("Could not clone world " + newWorldName);
            GWorld.getInstance().getWorldManager().removeWorld(clonedWorld.worldData);
            return Optional.empty();
        }

        clonedWorld.worldData.setImporting(false);

        updateWorldSpawnLocation(clonedWorld);

        return Optional.of(clonedWorld);
    }

    @Override
    public boolean reCreate(boolean saveWorldToFile) {
        if (!isMapLoaded()) {
            //System.out.println("World " + worldData.getGeneralInformation().worldName() + " is not loaded, cannot recreate.");
            return false;
        }

        if(!unloadMap()) {
            //System.out.println("Could not unload world " + worldData.getGeneralInformation().worldName() + " for recreation.");
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
        } else deleteMap(false);

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

    @Override
    public void setAllowPvP(boolean allowPvP) {
        worldData.setAllowPvP(allowPvP);
        if (isMapLoaded()) {
            Bukkit.getWorld(getWorldName()).setPVP(allowPvP);
        }
    }

    @Override
    public void setKeepSpawnInMemory(boolean keepSpawnInMemory) {
        worldData.setKeepSpawnInMemory(keepSpawnInMemory);
        if (isMapLoaded()) {
            Bukkit.getWorld(getWorldName()).setKeepSpawnInMemory(keepSpawnInMemory);
        }
    }

    @Override
    public void setAnimalSpawning(boolean animalSpawning) {//genauer nachschauen
        worldData.setAnimalSpawning(animalSpawning);
        if (!isMapLoaded() || animalSpawning) return;
        for (Entity entity : Bukkit.getWorld(getWorldName()).getEntities()) {
            String spawnedTypeName = entity.getType().name().toLowerCase();
            if (GWorld.ANIMALS.contains(spawnedTypeName)) {
                entity.remove();
            }
        }
    }

    @Override
    public void setDisabledAnimals(List<String> disabledAnimals) {
        worldData.setDisabledAnimals(disabledAnimals);
        if (!isMapLoaded()) return;
        for (Entity entity : Bukkit.getWorld(getWorldName()).getEntities()) {
            String spawnedTypeName = entity.getType().name().toLowerCase();
            if (disabledAnimals.contains(spawnedTypeName)) {
                entity.remove();
            }
        }
    }

    @Override
    public void setMonsterSpawning(boolean monsterSpawning) {
        worldData.setMonsterSpawning(monsterSpawning);
        if (!isMapLoaded() || monsterSpawning) return;
        for (Entity entity : Bukkit.getWorld(getWorldName()).getEntities()) {
            String spawnedTypeName = entity.getType().name().toLowerCase();
            if (GWorld.MONSTER.contains(spawnedTypeName)) {
                entity.remove();
            }
        }
    }

    @Override
    public void setDisabledMonsters(List<String> disabledMonsters) {
        worldData.setDisabledMonsters(disabledMonsters);
        if (!isMapLoaded()) return;
        for (Entity entity : Bukkit.getWorld(getWorldName()).getEntities()) {
            String spawnedTypeName = entity.getType().name().toLowerCase();
            if (disabledMonsters.contains(spawnedTypeName)) {
                entity.remove();
            }
        }
    }

    @Override
    public void setWeatherCycle(boolean weatherCycle) {
        worldData.setWeatherCycle(weatherCycle);
        if (isMapLoaded()) {
            GAME_RULE_ADAPTER.setGameRule(Bukkit.getWorld(getWorldName()), GGameRule.ADVANCE_WEATHER, weatherCycle);
        }
        setWeatherType(worldData.getWeatherType());
    }

    @Override
    public void setWeatherType(WorldData.WeatherType weatherType) {
        worldData.setWeatherType(weatherType);
        if (!isMapLoaded() || worldData.isWeatherCycle()) return;
        Bukkit.getWorld(getWorldName()).setStorm(weatherType == WorldData.WeatherType.RAIN || weatherType == WorldData.WeatherType.THUNDER);
        Bukkit.getWorld(getWorldName()).setThundering(weatherType == WorldData.WeatherType.THUNDER);
    }

    @Override
    public void setTimeCycle(boolean timeCycle) {
        worldData.setTimeCycle(timeCycle);
        if (isMapLoaded()) {
            GAME_RULE_ADAPTER.setGameRule(Bukkit.getWorld(getWorldName()), GGameRule.ADVANCE_TIME, timeCycle);
        }
        setTime(worldData.getTime());
    }

    @Override
    public void setTime(long time) {
        worldData.setTime(time);
        if (isMapLoaded() && !worldData.isTimeCycle()) {
            Bukkit.getWorld(getWorldName()).setTime(time);
        }
    }

    @Override
    public void setDefaultGamemode(boolean defaultGamemode) {
        worldData.setDefaultGamemode(defaultGamemode);
        setGameMode(worldData.getGameMode());
    }

    @Override
    public void setGameMode(GameMode gameMode) {
        worldData.setGameMode(gameMode);
        if (isMapLoaded() && worldData.isDefaultGamemode()) {
            for (Player player : Bukkit.getWorld(getWorldName()).getPlayers()) {
                player.setGameMode(gameMode);
            }
        }
    }

    @Override
    public void setDifficulty(Difficulty difficulty) {
        worldData.setDifficulty(difficulty);
        if (isMapLoaded()) {
            Bukkit.getWorld(getWorldName()).setDifficulty(difficulty);
        }
    }

    @Override
    public void setRandomTickSpeed(int randomTickSpeed) {
        worldData.setRandomTickSpeed(randomTickSpeed);
        if (isMapLoaded()) {
            GAME_RULE_ADAPTER.setGameRule(Bukkit.getWorld(getWorldName()), GGameRule.RANDOM_TICK_SPEED, randomTickSpeed);
        }
    }

    @Override
    public void setAnnounceAdvancements(boolean announceAdvancements) {
        worldData.setAnnounceAdvancements(announceAdvancements);
        if (isMapLoaded()) {
            GAME_RULE_ADAPTER.setGameRule(Bukkit.getWorld(getWorldName()), GGameRule.SHOW_ADVANCEMENT_MESSAGES, announceAdvancements);
        }
    }

    @Override
    public void setLoadOnStartup(boolean loadOnStartup) {
        if(MainWorldUtil.isMainWorld(this.getWorldName())) {
            worldData.setLoadOnStartup(true);
            return;
        }

        worldData.setLoadOnStartup(loadOnStartup);
    }

    @Override
    public boolean isAllowPvP() {
        return worldData.isAllowPvP();
    }

    @Override
    public boolean isKeepSpawnInMemory() {
        return worldData.isKeepSpawnInMemory();
    }

    @Override
    public boolean isAnimalSpawning() {
        return worldData.isAnimalSpawning();
    }

    @Override
    public List<String> getDisabledAnimals() {
        return worldData.getDisabledAnimals();
    }

    @Override
    public boolean isMonsterSpawning() {
        return worldData.isMonsterSpawning();
    }

    @Override
    public List<String> getDisabledMonsters() {
        return worldData.getDisabledMonsters();
    }

    @Override
    public boolean isWeatherCycle() {
        return worldData.isWeatherCycle();
    }

    @Override
    public WorldData.WeatherType getWeatherType() {
        return worldData.getWeatherType();
    }

    @Override
    public boolean isTimeCycle() {
        return worldData.isTimeCycle();
    }

    @Override
    public long getTime() {
        return worldData.getTime();
    }

    @Override
    public boolean isDefaultGamemode() {
        return worldData.isDefaultGamemode();
    }

    @Override
    public GameMode getGameMode() {
        return worldData.getGameMode();
    }

    @Override
    public Difficulty getDifficulty() {
        return worldData.getDifficulty();
    }

    @Override
    public int getRandomTickSpeed() {
        return worldData.getRandomTickSpeed();
    }

    @Override
    public boolean isAnnounceAdvancements() {
        return worldData.isAnnounceAdvancements();
    }

    @Override
    public boolean isLoadOnStartup() {
        return worldData.isLoadOnStartup();
    }

    @Override
    public String getGenerator() {
        return this.worldData.getWorldGenerator();
    }

    @Override
    public WorldType getWorldType() {
        return worldData.getGeneralInformation().worldType();
    }

    @Override
    public long getSeed() {
        return worldData.getGeneralInformation().seed();
    }

    @Override
    public World.Environment getEnvironment() {
        return worldData.getGeneralInformation().environment();
    }

    @Override
    public void saveProperties() {
        GWorld.getInstance().getDataHandler().saveWorld(this.worldData);
    }

    private void updateWorldSpawnLocation(ManageableWorld newWorld) {
        if(newWorld== null) {
            return;
        }

        if (!newWorld.isMapLoaded()) {
            //System.out.println("World " + newWorld.worldData.getGeneralInformation().worldName() + " is not loaded, cannot update spawn location.");
            return;
        }

        World world = Bukkit.getWorld(newWorld.worldData.getGeneralInformation().worldName());
        if (world == null) {
            //System.out.println("World " + newWorld.worldData.getGeneralInformation().worldName() + " is null, cannot update spawn location.");
            return;
        }

        Location location = world.getSpawnLocation();
        //System.out.println("spawn location: " + location);
        world.setSpawnLocation(new Location(world, location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()));
        //System.out.println("To: " + world.getSpawnLocation());
    }
}
