package de.gilljan.gworld.data;

import de.gilljan.gworld.data.mysql.MySQL;
import de.gilljan.gworld.data.world.WorldData;
import org.bukkit.WorldType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class Database implements DataHandler {
    private final MySQL mySQL;
    private final HashMap<String, WorldData> worlds;

    public Database(MySQL mySQL) {
        this.mySQL = mySQL;
        this.worlds = new HashMap<>();

        this.mySQL.connect();

        createTables();
    }

    private void createTables() {
        mySQL.update("CREATE TABLE if not exists maps (" +
                "  mapName varchar(32) NOT NULL," +
                "  environment enum('NORMAL','NETHER','THE_END','') NOT NULL DEFAULT 'NORMAL'," +
                "  type enum('NORMAL','FLAT','LARGE_BIOMES','AMPLIFIED') NOT NULL DEFAULT 'NORMAL'," +
                "  seed bigint NOT NULL," +
                "  worldGenerator varchar(64) DEFAULT NULL," +
                "  allowPvP tinyint(1) NOT NULL," +
                "  keepSpawnInMemory tinyint(1) NOT NULL," +
                "  animalSpawning tinyint(1) NOT NULL," +
                "  monsterSpawning tinyint(1) NOT NULL," +
                "  weatherCycle tinyint(1) NOT NULL," +
                "  weatherType enum('CLEAR','RAIN','THUNDER','') NOT NULL DEFAULT 'CLEAR'," +
                "  timeCycle tinyint(1) NOT NULL," +
                "  time int(11) NOT NULL," +
                "  defaultGamemode tinyint(1) NOT NULL," +
                "  gameMode enum('CREATIVE','SURVIVAL','ADVENTURE','SPECTATOR') NOT NULL DEFAULT 'SURVIVAL'," +
                "  difficulty enum('PEACEFUL','EASY','NORMAL','HARD') NOT NULL DEFAULT 'NORMAL'," +
                "  `load` tinyint(1) NOT NULL DEFAULT 1," +
                "  randomTickSpeed int(11) NOT NULL DEFAULT 3," +
                "  announceAdvancements tinyint(1) NOT NULL DEFAULT 1," +
                "PRIMARY KEY (mapName))");
        mySQL.update("CREATE TABLE if not exists disabledMonsters (" +
                "  mapName varchar(32) NOT NULL," +
                "  monster varchar(32) NOT NULL," +
                "  FOREIGN KEY (mapName) REFERENCES maps(mapName) ON DELETE CASCADE," +
                "PRIMARY KEY (mapName, monster))");
        mySQL.update("CREATE TABLE if not exists disabledAnimals (" +
                "  mapName varchar(32) NOT NULL," +
                "  animal varchar(32) NOT NULL," +
                "  FOREIGN KEY (mapName) REFERENCES maps(mapName) ON DELETE CASCADE," +
                "PRIMARY KEY (mapName, animal))");
    }

    @Override
    public Optional<WorldData> getWorld(String name) {
        if(!worlds.containsKey(name)) {
            Optional<WorldData> world = fetchWorld(name);
            world.ifPresent(worldData -> worlds.put(name, worldData));
        }
        return worlds.get(name) != null ? Optional.of(worlds.get(name)) : Optional.empty();
    }

    @Override
    public void saveWorld(WorldData world) {
        mySQL.update("INSERT INTO maps (mapName, environment, type, seed, worldGenerator, allowPvP, keepSpawnInMemory, animalSpawning, monsterSpawning, weatherCycle, weatherType, timeCycle, time, defaultGamemode, gameMode, difficulty, `load`, randomTickSpeed, announceAdvancements) " +
                "VALUES ('" + world.getGeneralInformation().worldName() + "', '" +
                world.getGeneralInformation().environment().name() + "', '" +
                world.getGeneralInformation().worldType().name() + "', " +
                world.getGeneralInformation().seed() + ", '" +
                world.getGeneralInformation().worldGenerator() + "', " +
                world.isAllowPvP() + ", " +
                world.isKeepSpawnInMemory() + ", " +
                world.isAnimalSpawning() + ", " +
                world.isMonsterSpawning() + ", " +
                world.isWeatherCycle() + ", '" +
                world.getWeatherType() + "', " +
                world.isTimeCycle() + ", " +
                world.getTime() + ", " +
                world.isDefaultGamemode() + ", '" +
                world.getGameMode().name() + "', '" +
                world.getDifficulty().name() + "', " +
                world.isLoadOnStartup() + ", " +
                world.getRandomTickSpeed() + ", " +
                world.isAnnounceAdvancements() +
                ") ON DUPLICATE KEY UPDATE " +
                "environment = VALUES(environment), " +
                "type = VALUES(type), " +
                "seed = VALUES(seed), " +
                "worldGenerator = VALUES(worldGenerator), " +
                "allowPvP = VALUES(allowPvP), " +
                "keepSpawnInMemory = VALUES(keepSpawnInMemory), " +
                "animalSpawning = VALUES(animalSpawning), " +
                "monsterSpawning = VALUES(monsterSpawning), " +
                "weatherCycle = VALUES(weatherCycle), " +
                "weatherType = VALUES(weatherType), " +
                "timeCycle = VALUES(timeCycle), " +
                "time = VALUES(time), " +
                "defaultGamemode = VALUES(defaultGamemode), " +
                "gameMode = VALUES(gameMode), " +
                "difficulty = VALUES(difficulty), " +
                "`load` = VALUES(`load`), " +
                "randomTickSpeed = VALUES(randomTickSpeed), " +
                "announceAdvancements = VALUES(announceAdvancements)");
    }

    @Override
    public void saveAllWorlds() {
        for(WorldData world : worlds.values()) {
            saveWorld(world);
        }
    }

    @Override
    public void loadAllWorlds() {
        worlds.clear();
        ResultSet rs = mySQL.getResult("SELECT * FROM maps;");
        try {
            while(rs.next()) {
                fetchWorld(rs.getString("mapName"))
                        .ifPresent(world -> worlds.put(world.getGeneralInformation().worldName(), world));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void refetchWorlds() {
        worlds.clear();
        loadAllWorlds();
    }

    @Override
    public Optional<WorldData> fetchWorld(String name) {
        try (PreparedStatement statement = mySQL.prepareStatement("SELECT * FROM maps WHERE mapName = ?")) {
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                ResultSet rsMonsters = mySQL.getResult("SELECT * FROM disabledMonsters WHERE mapName = '" + rs.getString("mapName") + "';");
                ResultSet rsAnimals = mySQL.getResult("SELECT * FROM disabledAnimals WHERE mapName = '" + rs.getString("mapName") + "';");
                List<String> disabledMonsters = new ArrayList<>();
                List<String> disabledAnimals = new ArrayList<>();
                while (rsMonsters.next()) {
                    disabledMonsters.add(rsMonsters.getString("monster"));
                }
                while (rsAnimals.next()) {
                    disabledAnimals.add(rsAnimals.getString("animal"));
                }
                rsAnimals.close();
                rsMonsters.close();
                return Optional.of(new WorldData(
                        new WorldData.GeneralInformation(
                                rs.getString("mapName"),
                                org.bukkit.World.Environment.valueOf(rs.getString("environment")),
                                WorldType.valueOf(rs.getString("type")),
                                rs.getLong("seed"),
                                rs.getString("worldGenerator")
                        ),
                        rs.getBoolean("allowPvP"),
                        rs.getBoolean("keepSpawnInMemory"),
                        rs.getBoolean("animalSpawning"),
                        disabledAnimals,
                        rs.getBoolean("monsterSpawning"),
                        disabledMonsters,
                        rs.getBoolean("weatherCycle"),
                        WorldData.WeatherType.valueOf(rs.getString("weatherType")),
                        rs.getBoolean("timeCycle"),
                        rs.getLong("time"),
                        rs.getBoolean("defaultGamemode"),
                        org.bukkit.GameMode.valueOf(rs.getString("gameMode")),
                        org.bukkit.Difficulty.valueOf(rs.getString("difficulty")),
                        rs.getBoolean("load"),
                        rs.getInt("randomTickSpeed"),
                        rs.getBoolean("announceAdvancements")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void addWorld(WorldData world) {
        worlds.put(world.getGeneralInformation().worldName(), world);
        saveWorld(world);
    }

    @Override
    public void removeWorld(WorldData worldData) {
        worlds.remove(worldData.getGeneralInformation().worldName());
        try (PreparedStatement statement = mySQL.prepareStatement("DELETE FROM maps WHERE mapName = ?;")) {
            statement.setString(1, worldData.getGeneralInformation().worldName());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean containsWorld(String name) {
        return worlds.containsKey(name);
    }

    @Override
    public HashMap<String, WorldData> getWorlds() {
        return worlds;
    }
}
