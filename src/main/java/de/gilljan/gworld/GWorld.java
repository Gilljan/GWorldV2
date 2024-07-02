package de.gilljan.gworld;

import de.gilljan.gworld.data.DataHandler;
import de.gilljan.gworld.utils.EntityUtil;
import de.gilljan.gworld.world.WorldManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class GWorld extends JavaPlugin {
    private static GWorld instance;
    public static List<String> availableGenerators = new ArrayList<>();
    public static final List<String> ANIMALS = new ArrayList<>();
    public static final List<String> MONSTER = new ArrayList<>();
    private DataHandler dataHandler;
    private WorldManager worldManager;

    @Override
    public void onEnable() {
        instance = this;
        EntityUtil.getAllEntities();

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
}
