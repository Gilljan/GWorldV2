package de.gilljan.gworld;

import de.gilljan.gworld.commands.*;
import de.gilljan.gworld.data.DataHandler;
import de.gilljan.gworld.data.Database;
import de.gilljan.gworld.data.FileConfiguration;
import de.gilljan.gworld.data.MigrationManager;
import de.gilljan.gworld.data.mysql.MySQL;
import de.gilljan.gworld.data.world.WorldData;
import de.gilljan.gworld.listener.EntitySpawnListener;
import de.gilljan.gworld.listener.LoadWorldListener;
import de.gilljan.gworld.listener.PlayerJoinListener;
import de.gilljan.gworld.listener.WorldChangeListener;
import de.gilljan.gworld.utils.EntityUtil;
import de.gilljan.gworld.utils.GeneratorUtil;
import de.gilljan.gworld.utils.LanguageManager;
import de.gilljan.gworld.utils.SendMessageUtil;
import de.gilljan.gworld.world.ManageableWorld;
import de.gilljan.gworld.world.WorldManager;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class GWorld extends JavaPlugin {
    private static GWorld instance;
    public static final List<String> AVAILABLE_GENERATORS = new ArrayList<>();
    public static final List<String> ANIMALS = new ArrayList<>();
    public static final List<String> MONSTER = new ArrayList<>();
    public static String prefix;
    public static boolean autoImport;
    private DataHandler dataHandler;
    private final WorldManager worldManager = new WorldManager();
    private LanguageManager languageManager;

    //Debuggin: -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005

    /* todo
    try {
    // Ein Feature testen, das es erst ab Java 16/17 gibt
    Class.forName("java.lang.Record");
} catch (ClassNotFoundException e) {
    getLogger().severe("==============================================");
    getLogger().severe(" FEHLER: FALSCHE JAVA VERSION!");
    getLogger().severe(" Dieses Plugin benoetigt JAVA 17 oder JAVA 21.");
    getLogger().severe(" Dein Server laeuft auf: " + System.getProperty("java.version"));
    getLogger().severe(" Bitte update dein Start-Skript.");
    getLogger().severe("==============================================");
    Bukkit.getPluginManager().disablePlugin(this);
    return;
}
     */

    @Override
    public void onEnable() {
        init();
        loadSettings();

        //Migrations
        MigrationManager migrationManager = new MigrationManager();
        migrationManager.process();

        languageManager = new LanguageManager();
        languageManager.load();

        loadWorlds();
        registerListener();
        registerCommands();
        GeneratorUtil.getGenerators();

//        System.out.println(MONSTER);
//        System.out.println(ANIMALS);

        //bstats metrics
        new Metrics(this, 11160);

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            dataHandler.saveAllWorlds();
        }, 6000L, 6000L); // Save every 5 minutes

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getDataHandler().saveAllWorlds();
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

    public LanguageManager getLanguageManager() {
        return languageManager;
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
                    getConfig().getString("Storage.Host"),
                    getConfig().getString("Storage.Database"),
                    getConfig().getString("Storage.Username"),
                    getConfig().getString("Storage.Password"),
                    getConfig().getInt("Storage.Port")
                    ));
        }

        prefix = SendMessageUtil.sendRawMessage(Objects.requireNonNull(getConfig().getString("Prefix")));
        autoImport = getConfig().getBoolean("AutoImport");
    }

    private void loadWorlds() {
        dataHandler.loadAllWorlds();

        for (WorldData world : dataHandler.getWorlds().values()) {
            if(world.isLoadOnStartup()) {
                ManageableWorld manageableWorld = new ManageableWorld(world);
                Bukkit.getScheduler().runTask(this, manageableWorld::loadMap);
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
        getCommand("gworlds").setExecutor(new GWorldsCommand());
        getCommand("gcreate").setExecutor(new GCreateCommand());
        getCommand("gimport").setExecutor(new GImportCommand());
        getCommand("gclone").setExecutor(new GCloneCommand());
        getCommand("gdelete").setExecutor(new GDeleteCommand());
        getCommand("gload").setExecutor(new GLoadCommand());
        getCommand("gunload").setExecutor(new GUnloadCommand());
        getCommand("grecreate").setExecutor(new GRecreateCommand());
        getCommand("gwreload").setExecutor(new GWReloadCommand());
        getCommand("gset").setExecutor(new GSetCommand());
        getCommand("ghelp").setExecutor(new GHelpCommand());
    }

}
