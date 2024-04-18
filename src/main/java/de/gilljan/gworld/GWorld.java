package de.gilljan.gworld;

import de.gilljan.gworld.utils.EntityUtil;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class GWorld extends JavaPlugin {
    private static GWorld instance;
    public static List<String> availableGenerators = new ArrayList<>();
    public static final List<String> ANIMALS = new ArrayList<>();
    public static final List<String> MONSTER = new ArrayList<>();

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
}
