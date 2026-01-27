package de.gilljan.gworld.utils;

import de.gilljan.gworld.GWorld;
import org.bukkit.Bukkit;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;

import java.util.logging.Level;

public class GeneratorUtil {
    public static void getGenerators() {
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            try {
                if (plugin.isEnabled()) {
                    ChunkGenerator generator = plugin.getDefaultWorldGenerator("", "");
                    if (generator != null) {
                        GWorld.AVAILABLE_GENERATORS.add(plugin.getDescription().getName());
                    }
                }
            } catch (Throwable e) {
                GWorld.getInstance().getLogger().log(Level.WARNING, "Failed to get generator from plugin: " + plugin.getName(), e);
            }
        }
    }
}
