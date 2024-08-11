package de.gilljan.gworld;

import de.gilljan.gworld.commands.GInfoCommand;
import de.gilljan.gworld.commands.GTpCommand;
import de.gilljan.gworld.data.DataHandler;
import de.gilljan.gworld.data.Database;
import de.gilljan.gworld.data.FileConfiguration;
import de.gilljan.gworld.data.mysql.MySQL;
import de.gilljan.gworld.data.world.WorldData;
import de.gilljan.gworld.listener.EntitySpawnListener;
import de.gilljan.gworld.listener.LoadWorldListener;
import de.gilljan.gworld.listener.PlayerJoinListener;
import de.gilljan.gworld.listener.WorldChangeListener;
import de.gilljan.gworld.utils.EntityUtil;
import de.gilljan.gworld.utils.GeneratorUtil;
import de.gilljan.gworld.world.ManageableWorld;
import de.gilljan.gworld.world.WorldManager;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class GWorld extends JavaPlugin {
    private static GWorld instance;
    public static List<String> availableGenerators = new ArrayList<>();
    public static final List<String> ANIMALS = new ArrayList<>();
    public static final List<String> MONSTER = new ArrayList<>();
    private DataHandler dataHandler;
    private WorldManager worldManager = new WorldManager();

    private YamlConfiguration languageFile;

    @Override
    public void onEnable() {
        init();
        loadSettings();
        loadWorlds();
        registerListener();
        registerCommands();
        GeneratorUtil.getGenerators();

        for(String entity : ANIMALS) {
            getLogger().info("Animal: " + entity);
        }

        for(String entity : MONSTER) {
            getLogger().info("Monster: " + entity);
        }
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static GWorld getInstance() {
        return instance;
    }

    public WorldManager getWorldManager() {
        return worldManager;
    }

    public DataHandler getDataHandler() {
        return dataHandler;
    }

    public YamlConfiguration getLanguageFile() {
        return languageFile;
    }

    private void init() {
        instance = this;
        EntityUtil.getAllEntities();

        if (!getDataFolder().exists())
            getDataFolder().mkdir();
        saveDefaultConfig();
        if (!(new File(getDataFolder().getPath() + "//de_DE.yml").exists()))
            saveResource("de_DE.yml", false);
        if (!(new File(getDataFolder().getPath() + "//en_EN.yml").exists()))
            saveResource("en_EN.yml", false);
    }

    private void loadSettings() {
        if (getConfig().getString("Language").equalsIgnoreCase("de")) {
            languageFile = YamlConfiguration.loadConfiguration(new File(getDataFolder().getPath() + "//de_DE.yml"));
        } else {
            languageFile = YamlConfiguration.loadConfiguration(new File(getDataFolder().getPath() + "//en_EN.yml"));
        }

        if(getConfig().getString("Storage.Type").equalsIgnoreCase("YAML")) {
            dataHandler = new FileConfiguration();
        } else {
            dataHandler = new Database(new MySQL(
                    this,
                    getConfig().getString("Storage.MySQL.Host"),
                    getConfig().getString("Storage.MySQL.Database"),
                    getConfig().getString("Storage.MySQL.Username"),
                    getConfig().getString("Storage.MySQL.Password"),
                    getConfig().getInt("Storage.MySQL.Port")
                    ));
        }
    }

    private void loadWorlds() {
        dataHandler.loadAllWorlds();

        for (WorldData world : dataHandler.getWorlds().values()) {
            if(world.isLoadOnStartup()) {
                ManageableWorld manageableWorld = new ManageableWorld(world);
                manageableWorld.loadMap();
            }
        }
    }

    private void registerListener() {
        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new WorldChangeListener(), this);
        pluginManager.registerEvents(new EntitySpawnListener(), this);
        pluginManager.registerEvents(new LoadWorldListener(), this);
        pluginManager.registerEvents(new PlayerJoinListener(), this);

    }

    private void registerCommands() {
        getCommand("ginfo").setExecutor(new GInfoCommand());
        getCommand("gtp").setExecutor(new GTpCommand());
    }

}
