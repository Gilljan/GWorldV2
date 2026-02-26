package de.gilljan.gworld.data;

import de.gilljan.gworld.GWorld;
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
import java.util.logging.Level;

public class Database implements DataHandler {
    private static final int CURRENT_DB_VERSION = 2;

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
                "  alias varchar(32) DEFAULT NULL," +
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

        //Versioning table
        mySQL.update("CREATE TABLE if not exists gworld_info (id INT PRIMARY KEY, version INT)");

        // Standard-Wert setzen, falls Tabelle neu ist
        mySQL.update("INSERT IGNORE INTO gworld_info (id, version) VALUES (1, " + CURRENT_DB_VERSION + ")");

        // Schema-Migration für bestehende Datenbanken
        checkSchemaUpdate();
    }

    @Override
    public Optional<WorldData> getWorld(String name) {
        if (!worlds.containsKey(name)) {
            Optional<WorldData> world = fetchWorld(name);
            world.ifPresent(worldData -> worlds.put(name, worldData));
        }
        return worlds.get(name) != null ? Optional.of(worlds.get(name)) : Optional.empty();
    }

    @Override
    public void saveWorld(WorldData world) {
        mySQL.update("INSERT INTO maps (mapName, alias, environment, type, seed, worldGenerator, allowPvP, keepSpawnInMemory, animalSpawning, monsterSpawning, weatherCycle, weatherType, timeCycle, time, defaultGamemode, gameMode, difficulty, `load`, randomTickSpeed, announceAdvancements) " +
                "VALUES ('" + world.getGeneralInformation().worldName() + "', '" +
                world.getAlias() + "', '" +
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
                "alias = VALUES(alias), " +
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

        // monsters
        try (PreparedStatement delMon = mySQL.prepareStatement("DELETE FROM disabledMonsters WHERE mapName = ?")) {
            delMon.setString(1, world.getGeneralInformation().worldName());
            delMon.executeUpdate();
        } catch (SQLException e) {
            GWorld.getInstance().getLogger().log(Level.SEVERE, "Failed to delete disabledMonsters for world: " + world.getGeneralInformation().worldName(), e);
        }

        List<String> disabledMonsters = world.getDisabledMonsters();
        if (!disabledMonsters.isEmpty()) {
            try (PreparedStatement insMon = mySQL.prepareStatement("INSERT INTO disabledMonsters (mapName, monster) VALUES (?, ?)")) {
                for (String monster : disabledMonsters) {
                    insMon.setString(1, world.getGeneralInformation().worldName());
                    insMon.setString(2, monster);
                    insMon.addBatch();
                }
                insMon.executeBatch();
            } catch (SQLException e) {
                GWorld.getInstance().getLogger().log(Level.SEVERE, "Failed to insert disabledMonsters for world: " + world.getGeneralInformation().worldName(), e);
            }
        }

        // animals
        try (PreparedStatement delAni = mySQL.prepareStatement("DELETE FROM disabledAnimals WHERE mapName = ?")) {
            delAni.setString(1, world.getGeneralInformation().worldName());
            delAni.executeUpdate();
        } catch (SQLException e) {
            GWorld.getInstance().getLogger().log(Level.SEVERE, "Failed to delete disabledAnimals for world: " + world.getGeneralInformation().worldName(), e);
        }

        List<String> disabledAnimals = world.getDisabledAnimals();
        if (!disabledAnimals.isEmpty()) {
            try (PreparedStatement insAni = mySQL.prepareStatement("INSERT INTO disabledAnimals (mapName, animal) VALUES (?, ?)")) {
                for (String animal : disabledAnimals) {
                    insAni.setString(1, world.getGeneralInformation().worldName());
                    insAni.setString(2, animal);
                    insAni.addBatch();
                }
                insAni.executeBatch();
            } catch (SQLException e) {
                GWorld.getInstance().getLogger().log(Level.SEVERE, "Failed to insert disabledAnimals for world: " + world.getGeneralInformation().worldName(), e);
            }
        }

    }

    @Override
    public void saveAllWorlds() {
        for (WorldData world : worlds.values()) {
            saveWorld(world);
        }
    }

    @Override
    public void loadAllWorlds() {
        worlds.clear();
        ResultSet rs = mySQL.getResult("SELECT * FROM maps;");
        try {
            while (rs.next()) {
                fetchWorld(rs.getString("mapName"))
                        .ifPresent(world -> worlds.put(world.getGeneralInformation().worldName(), world));
            }
        } catch (SQLException e) {
            GWorld.getInstance().getLogger().log(Level.SEVERE, "Failed to load worlds from database", e);
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
            if (rs.next()) {
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
                                rs.getString("worldGenerator") == null || rs.getString("worldGenerator").equalsIgnoreCase("null") ? null : rs.getString("worldGenerator")
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
                        rs.getBoolean("announceAdvancements"),
                        rs.getString("alias")
                ));
            }
        } catch (SQLException e) {
            GWorld.getInstance().getLogger().log(Level.SEVERE, "Failed to fetch world: " + name, e);
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
            GWorld.getInstance().getLogger().log(Level.SEVERE, "Failed to remove world: " + worldData.getGeneralInformation().worldName(), e);
        }

    }

    @Override
    public boolean containsWorld(String name) {
        return worlds.containsKey(name);
    }

    @Override
    public HashMap<String, WorldData> getWorlds() {
        return new HashMap<>(worlds);
    }

    private void checkSchemaUpdate() {
        int dbVersion = 0;
        try (ResultSet rs = mySQL.getResult("SELECT version FROM gworld_info WHERE id = 1")) {
            if (rs.next()) dbVersion = rs.getInt("version");
        } catch (SQLException e) {
            GWorld.getInstance().getLogger().log(Level.SEVERE, "Failed to check schema version", e);
        }

        if (dbVersion < CURRENT_DB_VERSION) {
            GWorld.getInstance().getLogger().info("Database schema outdated (v" + dbVersion + "). Starting migration...");

            if (dbVersion < 2) {
                GWorld.getInstance().getLogger().info("Migrating to schema version 2: Adding 'alias' column to 'maps' table...");
                mySQL.update("ALTER TABLE maps ADD COLUMN alias VARCHAR(32) DEFAULT NULL");
                GWorld.getInstance().getLogger().info("Migration to schema version 2 completed.");
                dbVersion = 2;
            }

            // Neue Version speichern
            mySQL.update("UPDATE gworld_info SET version = " + dbVersion + " WHERE id = 1");
            GWorld.getInstance().getLogger().info("Database schema updated to version " + dbVersion + ".");
        }
    }
}
